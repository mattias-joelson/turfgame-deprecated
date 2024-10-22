package org.joelson.mattias.turfgame;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.ZoneUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ZoneTypeTest {
    
    @Test
    public void zoneTypeTest() throws Exception {
//        Map<String, Zone> zones = new HashMap<>();
//        ZonesTest.getAllZones().forEach(zone -> zones.put(zone.getName(), zone));
    
        Set<String> municipalityZones = List.of(MunicipalityTest.getDanderydZones().keySet(),
                MunicipalityTest.getSolnaZones().keySet(),
                MunicipalityTest.getSundbybergZones().keySet(),
                MunicipalityTest.getSollentunaZones().keySet(),
                MunicipalityTest.getStockholmZones().keySet(),
                MunicipalityTest.getJarfallaZones().keySet()).stream().flatMap(Set::stream).collect(Collectors.toSet());
        System.out.println("municipalityZones " + municipalityZones.size());
        
        Set<Zone> zones = ZonesTest.getAllZones().stream()
                .filter(zone -> municipalityZones.contains(zone.getName()))
                .filter(zone -> zone.getTakeoverPoints() > 95)
                .collect(Collectors.toSet());
        System.out.println("zones " + zones.size());
    
        try (KMLWriter zoneTypeWriter = new KMLWriter("zone_tp.kml")) {
            zones.stream()
                    .collect(Collectors.groupingBy(Zone::getTakeoverPoints))
                    .entrySet().stream()
                    .sorted(Entry.comparingByKey())
                    .forEach(integerListEntry -> writeZoneTpFolder(zoneTypeWriter, integerListEntry.getKey(), integerListEntry.getValue()));
        }
    }
    
    private static void writeZoneTpFolder(KMLWriter writer, int tp, List<Zone> zones) {
        writer.writeFolder("TP " + tp);
        zones.stream().sorted(Comparator.comparing(Zone::getName))
                .forEach(zone -> writer.writePlacemark(zone.getName(), zone.getTakeoverPoints() + "+" + zone.getPointsPerHour(),
                        zone.getLongitude(), zone.getLatitude()));
    }
    
    @Test
    public void zoneTripTest() throws Exception {
        List<String> zoneNames = getZoneNames();
        Map<String, Zone> zones = ZoneUtil.toNameMap(ZonesTest.getAllZones());
    
        IntStream.range(0, zoneNames.size()).forEach(i -> {
            if (!zones.containsKey(zoneNames.get(i))) {
                System.out.println(String.format("%3d - %s missing", i, zoneNames.get(i)));
            }
        });
        IntStream.range(0, zoneNames.size() - 1).forEach(i -> calcDistance(zones, zoneNames.get(i), zoneNames.get(i + 1)));
        double dist = IntStream.range(0, zoneNames.size() - 1)
                .mapToDouble(i -> ZoneUtil.calcDistance(zones.get(zoneNames.get(i)), zones.get(zoneNames.get(i + 1))))
                .sum();
        System.out.println("Distance: " + dist);
        System.out.println("Zones:    " + zoneNames.size());
        System.out.println("TP:       " + zoneNames.stream().map(zones::get).mapToInt(Zone::getTakeoverPoints).sum());
    }
    
    private static List<String> getZoneNamesOld() throws IOException {
        String filename = TurfersRunTest.class.getResource("/oberoff_2019-11-26.txt").getFile();
        Path path = Paths.get(filename);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.collect(Collectors.toList());
        }
    }

    private static List<String> getZoneNames() throws IOException {
        return TurfersRunTest.getDandebetZones().collect(Collectors.toList());
    }
    
    private static void calcDistance(Map<String, Zone> zones, String zn1, String zn2) {
        System.out.println("Distance from " + zn1 + " to " + zn2 + " is " + ZoneUtil.calcDistance(zones.get(zn1), zones.get(zn2)));
    }
}
