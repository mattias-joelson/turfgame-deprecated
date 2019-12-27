package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ZonesComparisonTest {
    
    @Test
    public void zoneComparisonSolna() throws Exception {
        zoneComparisonTest("Solna", MunicipalityTest.getSolnaZones().keySet());
    }
    
    @Test
    public void zoneComparisonSundbyberg() throws Exception {
        zoneComparisonTest("Sundbyberg", MunicipalityTest.getSundbybergZones().keySet());
    }
    
    @Test
    public void zoneComparisonDanderyd() throws Exception {
        zoneComparisonTest("Danderyd", MunicipalityTest.getDanderydZones().keySet());
    }

    private static void zoneComparisonTest(String name, Set<String> zonesToTest) throws Exception {
        System.out.println("Comparing " + name);
        List<Zone> oldScores = getZones("zones-all.pre-tomtesv.json", zonesToTest);
        List<Zone> newScores = getZones("zones-all.2019-11-18.json", zonesToTest);
        int zd = 0;
        List<Zone> upZones = new ArrayList<>();
        List<Zone> downZones = new ArrayList<>();

        for (int i = 0; i < oldScores.size(); i += 1) {
            Zone oldZone = oldScores.get(i);
            Zone newZone = newScores.get(i + zd);
            if (!oldZone.getName().equals(newZone.getName())) {
                if (newZone.getName().equals("TomtensSenväg")) {
                    zd += 1;
                    System.out.println(String.format("%16s - new zone -> %3d +%1d", newZone.getName(), newZone.getTakeoverPoints(),
                            newZone.getPointsPerHour()));
                    continue;
                } else {
                    throw new NullPointerException(oldZone.getName() + " != " + newZone.getName() + " (" + i + ')');
                }
            }
            int diff = oldZone.getTakeoverPoints() - newZone.getTakeoverPoints();
            if (diff < 0) {
                System.out.println(String.format("%16s - %3d +%1d -> %3d +%1d up", oldZone.getName(), oldZone.getTakeoverPoints(), oldZone.getPointsPerHour(),
                        newZone.getTakeoverPoints(), newZone.getPointsPerHour()));
                upZones.add(newZone);
            } else if (diff > 0) {
                System.out.println(String.format("%16s - %3d +%1d -> %3d +%1d down", oldZone.getName(), oldZone.getTakeoverPoints(), oldZone.getPointsPerHour(),
                        newZone.getTakeoverPoints(), newZone.getPointsPerHour()));
                downZones.add(newZone);
            } else {
                System.out.println(String.format("%16s - %3d +%1d", oldZone.getName(), oldZone.getTakeoverPoints(), oldZone.getPointsPerHour()));
            }
        }
        System.out.println("Total up:   " + upZones.size());
        System.out.println("Total down: " + downZones.size());
    
        try (KMLWriter writer = new KMLWriter("comparing" + name + ".kml")) {
            writer.writeFolder(name + " up");
            upZones.forEach(zone -> writer
                    .writePlacemark(zone.getName(), zone.getTakeoverPoints() + " +" + zone.getPointsPerHour(),
                            zone.getLongitude(), zone.getLatitude()));
            writer.writeFolder(name + " down");
            downZones.forEach(zone -> writer
                    .writePlacemark(zone.getName(), zone.getTakeoverPoints() + " +" + zone.getPointsPerHour(),
                            zone.getLongitude(), zone.getLatitude()));
        }
    }

    private static List<Zone> getZones(String filename, Set<String> zonesToTest) throws Exception {
        return URLReaderTest.readProperties(filename, Zones::fromJSON).stream()
                .filter(zone -> zonesToTest.contains(zone.getName()))
                .sorted(Comparator.comparing(Zone::getName))
                .collect(Collectors.toList());
    }
    
    @Test
    public void zoneCompareAllTest() throws Exception {
//        Map<String, Zone> oldScores = getZones("zones-all.pre-tomtesv.json");
//        Map<String, Zone> oldScores = getZones("zones-all.2019-11-18.json");
//        Map<String, Zone> oldScores = getZones("zones-all.2019-11-19.json");
//        Map<String, Zone> oldScores = getZones("zones-all.2019-11-20.json");
//        Map<String, Zone> oldScores = getZones("zones-all.2019-11-21.json");
        Map<String, Zone> oldScores = getZones("zones-all.2019-11-22.json");
        Map<String, Zone> newScores = getZones("zones-all.2019-11-23.json");
        
        int[] changed = { 0, 0 };
        
        newScores.values().stream()
                .sorted(Comparator.comparing(zone -> zone.getRegion().getName() + ':' + zone.getName()))
                .forEach(zone -> compareZoneToOldZone(zone, oldScores.get(zone.getName()), changed));
        System.out.println("Changed total: " + changed[0]);
        System.out.println("Changed Stockholm: " + changed[1]);
    }
    
    private static void compareZoneToOldZone(Zone zone, Zone oldZone, int[] changed) {
        if (oldZone != null && oldZone.getTakeoverPoints() != zone.getTakeoverPoints()) {
            if (zone.getRegion().getName().equals("Stockholm")) {
                System.out.println(String.format("%16s - %3d +%1d -> %3d +%1d  (%s)", oldZone.getName(), oldZone.getTakeoverPoints(),
                        oldZone.getPointsPerHour(), zone.getTakeoverPoints(), zone.getPointsPerHour(), zone.getRegion().getName()));
                changed[1] += 1;
            }
            changed[0] += 1;
        }
    }
    
    private static Map<String, Zone> getZones(String filename) throws Exception {
        // Fäbodaswim exists as two zones - prevents collect to map
        Map<String, Zone> zones = new HashMap<>();
        URLReaderTest.readProperties(filename, Zones::fromJSON).forEach(zone -> zones.put(zone.getName(), zone));
        return zones;
    }
}
