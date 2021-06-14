package org.joelson.mattias.turfgame.apiv5;

import com.fasterxml.jackson.databind.JsonNode;
import org.joelson.mattias.turfgame.util.JacksonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FeedsReader {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println(String.format("Usage:\n\t%s feed_file1.json ...", FeedsReader.class.getName()));
            return;
        }
        for (String filename : args) {
            System.out.println("*** Reading " + filename);
            readFeedFile(Path.of(filename));
        }
    }

    private static void readFeedFile(Path feedPath) throws IOException {
        for (JsonNode node : JacksonUtil.readValue(Files.readString(feedPath), JsonNode[].class)) {
            System.out.println("    " + node.get("time") + " - " + node.get("type"));
        }
    }
}
