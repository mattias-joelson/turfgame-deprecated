package org.joelson.mattias.turfgame.apiv5;

import org.joelson.mattias.turfgame.util.URLReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Feeds {

    private static final String FEEDS_REQUEST = "https://api.turfgame.com/unstable/feeds"; //NON-NLS

    private static String getFeedsJSON() throws IOException {
        return URLReader.getRequest(FEEDS_REQUEST);
    }

    private static String getFeedsJSON(String feed) throws IOException {
        return URLReader.getRequest(FEEDS_REQUEST + '/' + feed);
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) {

        for (;;) {
            getFeed("takeover", "feeds_v5_takeover_%s.json");
            getFeed("medal+chat", "feeds_v5_medal_chat_%s.json");
            getFeed("zone", "feeds_v5_zone_%s.json");
            Instant until = Instant.now().plusSeconds(5 * 60);
            while (Instant.now().isBefore(until)) {
                System.out.println("Sleeping at " + Instant.now());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    private static void getFeed(String feed, String filename) {
        try {
            Instant instant = Instant.now();
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            String time = formatter.format(ldt);
            String name = String.format(filename, time);
            Path file = Path.of(".", name);
            Files.writeString(file, getFeedsJSON(feed), StandardCharsets.UTF_8);
            System.out.println("Downloaded " + file + " at " + Instant.now());
        } catch (IOException e) {
            System.out.println(Instant.now() + ": " + e);
        }
    }
}
