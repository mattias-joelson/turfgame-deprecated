package org.joelson.mattias.turfgame.apiv4;

import com.fasterxml.jackson.databind.JsonNode;
import org.h2.api.Interval;
import org.joelson.mattias.turfgame.util.FilesUtil;
import org.joelson.mattias.turfgame.util.JacksonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FeedsIntervalReader {

    private static enum FeedType {
        CHAT_MEDAL, TAKEOVER, ZONE
    }

    private static class FeedInterval {

        private final FeedType type;
        private final String start;
        private final String end;

        private FeedInterval(FeedType type, String start, String end) {
            if (start.compareTo(end) >= 0) {
                throw new IllegalArgumentException("Wrong order " + start + " and " + end);
            }
            this.type = type;
            this.start = start;
            this.end = end;
        }

        public boolean intersects(FeedInterval that) {
            if (type != that.type) {
                return false;
            }
            if (end.compareTo(that.start) < 0 || that.end.compareTo(start) < 0) {
                return false;
            }
            if (end.compareTo(that.start) >= 0 || that.end.compareTo(start) >= 0) {
                return true;
            }
            throw new IllegalStateException("Should not come here...");
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println(String.format("Usage:\n\t%s feed_file1.json ...", FeedsIntervalReader.class.getName()));
            return;
        }
        SortedSet<FeedInterval> feedNodes = new TreeSet<>(new FeedIntervalComparator());
        for (String filename : args) {
            System.out.println("*** Reading " + filename);
            Path feedPath = Path.of(filename);
            if (FilesUtil.isZipFile(feedPath)) {
                ZipFile zipFile = new ZipFile(feedPath.toFile());
                zipFile.stream().forEach(zipEntry -> readFeedNodes(feedNodes, readZipEntry(feedPath, zipFile, zipEntry)));
            } else {
                readFeedNodes(feedNodes, readFeedFile(feedPath));
            }
        }
        feedNodes.stream().forEach(feedInterval -> System.out.println(String.format("%s: %s - %s",
                feedInterval.type, feedInterval.start, feedInterval.end)));
    }

    private static void readFeedNodes(SortedSet<FeedInterval> feedIntervals, List<JsonNode> fileNodes) {
        if (fileNodes.size() <= 1) {
            return;
        }
        FeedType type = FeedType.CHAT_MEDAL;
        String nodeType = fileNodes.get(0).get("type").asText();
        if (nodeType.equals("takeover")) {
            type = FeedType.TAKEOVER;
        } else if (nodeType.equals("zone")) {
            type = FeedType.ZONE;
        }
        String start = fileNodes.get(fileNodes.size() - 1).get("time").asText();
        String end = fileNodes.get(0).get("time").asText();
        if (start.equals(end)) {
            return;
        }
        FeedInterval newInterval = new FeedInterval(type, start, end);
        outer:
        while (true) {
            for (FeedInterval feedInterval : feedIntervals) {
                if (feedInterval.intersects(newInterval)) {
                    newInterval = joinIntervals(feedInterval, newInterval);
                    feedIntervals.remove(feedInterval);
                    continue outer;
                }
            }
            break;
        }
        feedIntervals.add(newInterval);
    }

    private static FeedInterval joinIntervals(FeedInterval interval1, FeedInterval interval2) {
        String start = interval1.start.compareTo(interval2.start) <= 0 ? interval1.start : interval2.start;
        String end = interval1.end.compareTo(interval2.end) > 0 ? interval1.end : interval2.end;
        return new FeedInterval(interval1.type, start, end);
    }

    private static List<JsonNode> readFeedFile(Path feedPath) throws IOException {
        return readJsonNodes(feedPath.toString(), Files.readString(feedPath));
    }

    private static List<JsonNode> readZipEntry(Path feedPath, ZipFile zipFile, ZipEntry zipEntry) {
//        System.out.println("  * Reading " + zipEntry.getName());
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)));
            List<String> rows = new ArrayList<>();
            String row;
            while ((row = reader.readLine()) != null) {
                rows.add(row);
            }
            String content = String.join("\n", rows);
            return readJsonNodes(feedPath + " -> " + zipEntry.getName(), content);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
            return Collections.emptyList();
        }
    }

    private static List<JsonNode> readJsonNodes(String path, String content) {
        if (content.isEmpty() || content.startsWith("<html>")) {
//            System.err.println("--- File " + path + " contains no data!");
            return Collections.emptyList();
        }
        return Arrays.asList(JacksonUtil.readValue(content, JsonNode[].class));
    }

    private static class FeedIntervalComparator implements Comparator<FeedInterval> {
        @Override
        public int compare(FeedInterval fi1, FeedInterval fi2) {
            if (fi1.type != fi2.type) {
                return fi1.type.ordinal() - fi2.type.ordinal();
            }
            int startCompare = fi1.start.compareTo(fi2.start);
            if (startCompare != 0) {
                return startCompare;
            }
            return fi1.end.compareTo(fi2.end);
        }
    }
}
