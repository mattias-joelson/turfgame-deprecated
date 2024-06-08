package org.joelson.turf;

import org.joelson.turf.lundkvist.MunicipalityTest;
import org.joelson.turf.turfgame.apiv4.Zone;
import org.joelson.turf.turfgame.apiv4.ZoneUtil;
import org.joelson.turf.turfgame.apiv4.ZonesTest;
import org.joelson.turf.util.KMLWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TurfersRunTest {

    private Map<String, Zone> zones;

    public static Stream<String> getDandebetZones() throws IOException {
        Set<String> omitZones = Set.of("BlötaEneby", "Edsviken", "Tranpiren");
        return MunicipalityTest.getDanderydZones().keySet().stream().filter(s -> !omitZones.contains(s)).sorted();
    }

    private static Zone zoneIfExists(String zoneName, Zone zone) {
        if (zone == null) {
            throw new NullPointerException("Zone for name \"" + zoneName + "\" not found!");
        }
        return zone;
    }

    @BeforeEach
    public void before() throws Exception {
        zones = ZoneUtil.toNameMap(ZonesTest.getAllZones());
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
    public void writeDanderyd() throws IOException {
        try (KMLWriter writer = new KMLWriter("dandebet.kml")) {
            writeRun(writer, "Dandebet", getDandebetZones());
        }
    }

    private void writeRun(KMLWriter writer, String shortFilename) throws IOException {
        String filename = TurfersRunTest.class.getResource("/" + shortFilename).getFile();
        try (Stream<String> lines = Files.lines(new File(filename).toPath())) {
            writeRun(writer, shortFilename, lines);
        }
    }

    private void writeRun(KMLWriter writer, String runName, Stream<String> lines) {
        List<Zone> run = lines.filter(s -> !s.isEmpty()).filter(s -> !s.startsWith("//"))
                .map(zoneName -> zoneIfExists(zoneName, zones.get(zoneName))).toList();
        writer.writeFolder(runName);
        IntStream.range(0, run.size()).forEach(i -> {
            Zone zone = run.get(i);
            System.out.println(zone.getName());
            writer.writePlacemark(String.format("%d - %s", i + 1, zone.getName()), "", zone.getLongitude(),
                    zone.getLatitude());
        });
    }
}
