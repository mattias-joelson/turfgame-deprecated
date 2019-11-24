package org.joelson.mattias.turfgame;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TurfersRunTest {

    private Map<String, Zone> zones;
    
    @Before
    public void before() throws IOException {
        zones = new HashMap<>();
        ZonesTest.getAllZones().forEach(zone -> zones.put(zone.getName(), zone));
    }
    
    @Test
    public void writeRuns() throws IOException {
        KMLWriter writer = new KMLWriter("runs.kml");

        writeRun(writer, "zonerx_2019-11-15");
        writeRun(writer, "oberoff_2019-11-21");

        writer.close();
    }
    
    private void writeRun(KMLWriter writer, String shortFilename) throws IOException {
        String filename = TurfersRunTest.class.getResource(String.format("/%s.txt", shortFilename)).getFile();
        Path path = Paths.get(filename);
        List<Zone> run = Files.lines(path).map(zoneName -> {
            Zone zone = zones.get(zoneName);
            if (zone == null) {
                throw new NullPointerException("Zone for name \"" + zoneName + "\" not found!");
            }
            return zone;
        }).collect(Collectors.toList());
        writer.writeFolder(shortFilename);
        IntStream.range(0, run.size()).forEach(i ->  {
            Zone zone = run.get(i);
            writer.writePlacemark(String.format("%d - %s", i + 1, zone.getName()), "", zone.getLongitude(), zone.getLatitude());
        });
    }
}
