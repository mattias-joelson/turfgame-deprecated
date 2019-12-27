package org.joelson.mattias.turfgame;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TurfersRunTest {

    private Map<String, Zone> zones;
    
    @Before
    public void before() throws Exception {
        zones = new HashMap<>();
        ZonesTest.getAllZones().forEach(zone -> zones.put(zone.getName(), zone));
    }
    
    @Test
    public void writeRuns() throws IOException {
        try (KMLWriter writer = new KMLWriter("runs.kml")) {
            writeRun(writer, "zonerx_2019-11-15.txt");
            writeRun(writer, "oberoff_2019-11-21.txt");
            writeRun(writer, "oberoff_2019-11-26.txt");
        }
    }
    
    private void writeRun(KMLWriter writer, String shortFilename) throws IOException {
        String filename = TurfersRunTest.class.getResource(File.separatorChar + shortFilename).getFile();
        Path path = Paths.get(filename);
        try (Stream<String> lines = Files.lines(path)) {
            List<Zone> run = lines.map(zoneName -> zoneIfExists(zoneName, zones.get(zoneName)))
                    .collect(Collectors.toList());
            writer.writeFolder(shortFilename);
            IntStream.range(0, run.size()).forEach(i -> {
                Zone zone = run.get(i);
                writer.writePlacemark(String.format("%d - %s", i + 1, zone.getName()), "", zone.getLongitude(), zone.getLatitude());
            });
        }
    }
    
    private static Zone zoneIfExists(String zoneName, Zone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone for name \"" + zoneName + "\" not found!");
        }
        return zone;
    }
}
