package org.joelson.mattias.turfgame.apiv5;

import com.fasterxml.jackson.databind.JsonNode;
import org.joelson.mattias.turfgame.util.JacksonUtil;
import org.joelson.mattias.turfgame.util.TimeUtil;
import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Feeds {

    private static final String FEEDS_REQUEST = "https://api.turfgame.com/unstable/feeds"; //NON-NLS

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) {
        new Thread(() -> handleFeed("takeover", "feeds_takeover_%s.json")).start();
        new Thread(() -> handleFeed("medal+chat", "feeds_medal_chat_%s.json")).start();
        new Thread(() -> handleFeed("zone", "feeds_zone_%s.json")).start();
    }

    private static void handleFeed(String feed, String filenamePattern) {
        Instant lastEntry = null;
        for (;;) {
            lastEntry = getFeed(feed, filenamePattern, lastEntry);
            waitUntilNext();
        }
    }

    private static Instant getFeed(String feed, String filenamePattern, Instant since) {
        try {
            String json = getFeedsJSON(feed, since);
            if (json.equals("[]")) {
                log("No data for " + feed + " since " + since);
                return since;
            }
            Instant lastEntryTime = getLastEntryTime(json);
            if (lastEntryTime == null) {
                log("JSON: " + json);
                log("lastEntryTime is null");
            }
            Path file = getFilePath(filenamePattern, lastEntryTime);
            Files.writeString(file, json, StandardCharsets.UTF_8);
            log("Downloaded " + file + " at " + Instant.now());
            return Instant.from(lastEntryTime).minusSeconds(1);
        } catch (Throwable e) {
            log(Instant.now() + ": " + e);
            return null;
        }
    }
    private static String getFeedsJSON(String feed, Instant since) throws IOException {
        String afterDate = "";
        if (since != null) {
            afterDate = "?afterDate=" + TimeUtil.turfAPITimestampFormatter(since);
        }
        return URLReader.getRequest(FEEDS_REQUEST + '/' + feed + afterDate);
    }

    private static Instant getLastEntryTime(String json) {
        Instant latest = null;
        for (JsonNode node : JacksonUtil.readValue(json, JsonNode[].class)) {
            String timeStamp = node.get("time").asText();
            Instant instant = TimeUtil.turfAPITimestampToInstant(timeStamp);
            if (latest == null || instant.isAfter(latest)) {
                latest = instant;
            }
        }
        return latest;
    }

    private static Path getFilePath(String filenamePattern, Instant lastEntryTime) throws IOException {
        String timeString = toTimeString(lastEntryTime);
        String name = String.format(filenamePattern, timeString);
        Path filePath = Path.of(".", name);
        if (Files.exists(filePath)) {
            filePath = Files.createTempFile(Path.of("."), name.substring(0, name.indexOf(".json") + 1), ".json");
        }
        return filePath;
    }

    private static String toTimeString(Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    private static void waitUntilNext() {
        Instant until = Instant.now().plusSeconds(5 * 60);
        log("Sleeping until " + until);
        while (Instant.now().isBefore(until)) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private static void log(String msg) {
        System.out.println(String.format("[%s] %s", Thread.currentThread().getName(), msg));
    }
}
