package org.joelson.turf.turfgame.apiv5;

import org.joelson.turf.util.JacksonUtil;
import org.joelson.turf.util.URLReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Zones {
    private static final String ALL_ZONES_REQUEST = "https://api.turfgame.com/unstable/zones/all"; //NON-NLS
    private static final String DEFAULT_ZONES_FILENAME = "zones-all.v5.json"; //NON-NLS

    private Zones() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }

    public static List<Zone> readAllZones() throws IOException {
        return fromJSON(getAllZonesJSON());
    }

    private static String getAllZonesJSON() throws IOException {
        return URLReader.getRequest(ALL_ZONES_REQUEST);
    }

    public static void main(String[] args) throws IOException {
        Files.writeString(Path.of(DEFAULT_ZONES_FILENAME), getAllZonesJSON(), StandardCharsets.UTF_8);
    }

    public static void mainNew(String[] args) {
        LocalDateTime startDate = LocalDateTime.of(2021, 10, 3, 12, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 10, 3, 12, 35, 0);
        Instant start = toInstant(startDate);
        Instant end = toInstant(endDate);

        System.out.println("Start time: " + start + " (" + startDate + ')');
        System.out.println("End time: " + end + " (" + endDate + ')');
        while (Instant.now().isBefore(start)) {
            System.out.println("Sleeping at " + Instant.now());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        while (Instant.now().isBefore(end)) {
            try {
                Instant instant = Instant.now();
                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                String time = formatter.format(ldt);
                String name = String.format("zones_%s.v5.json", time);
                Path file = Path.of(".", name);
                Files.writeString(file, getAllZonesJSON(), StandardCharsets.UTF_8);
                System.out.println("Downloaded " + file + " at " + Instant.now());
            } catch (IOException e) {
                System.out.println(Instant.now() + ": " + e);
            }
            Instant until = Instant.now().plusSeconds(30 * 60);
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

    private static Instant toInstant(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return Instant.from(zonedDateTime);
    }

    public static List<Zone> fromJSON(String s) {
        return Arrays.asList(JacksonUtil.readValue(s, Zone[].class));
    }
}
