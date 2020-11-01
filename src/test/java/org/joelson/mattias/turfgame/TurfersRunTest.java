package org.joelson.mattias.turfgame;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            writeRun(writer, "oberoff_2020-05-09.txt");
        }
    }

    @Test
    public void writeRoadtrip() throws IOException {
        try (KMLWriter writer = new KMLWriter("roadtrip.kml")) {
            writeRun(writer, "oberoff_2020-07-25.txt");
        }
    }

    @Test
    public void writeStaminatrix() throws IOException {
        try (KMLWriter writer = new KMLWriter("staminatrix.kml")) {
            writeRun(writer, "staminatrix_north.txt");
            writeRun(writer, "staminatrix_south.txt");
        }
    }

    @Test
    public void writeDanderyd() throws IOException, ParseException {
        try (KMLWriter writer = new KMLWriter("dandebet.kml")) {
            writeRun(writer, "Dandebet", getDandebetZones());
        }
    }

    public static Stream<String> getDandebetZones() throws IOException, ParseException {
        Set<String> omitZones = Set.of("BlötaEneby", "Edsviken", "Tranpiren");
        return MunicipalityTest.getDanderydZones().keySet().stream().filter(s -> !omitZones.contains(s)).sorted();
    }

    private void writeRun(KMLWriter writer, String shortFilename) throws IOException {
        String filename = TurfersRunTest.class.getResource(File.separatorChar + shortFilename).getFile();
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            writeRun(writer, shortFilename, lines);
        }
    }

    private void writeRun(KMLWriter writer, String runName, Stream<String> lines) {
        List<Zone> run = lines.filter(s -> !s.isEmpty())
                .filter(s -> !s.startsWith("//"))
                .map(zoneName -> zoneIfExists(zoneName, zones.get(zoneName)))
                .collect(Collectors.toList());
        writer.writeFolder(runName);
        IntStream.range(0, run.size()).forEach(i -> {
            Zone zone = run.get(i);
            System.out.println(zone.getName());
            writer.writePlacemark(String.format("%d - %s", i + 1, zone.getName()), "", zone.getLongitude(), zone.getLatitude());
        });
    }

    private static Zone zoneIfExists(String zoneName, Zone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone for name \"" + zoneName + "\" not found!");
        }
        return zone;
    }
}
