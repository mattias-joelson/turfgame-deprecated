package org.joelson.mattias.turfgame.zundin;

import com.fasterxml.jackson.databind.JsonNode;
import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.JacksonUtil;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.ZoneUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Flipp08MissionTest {

    public static Set<String> getFlippZones() throws Exception {
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Zone> zoneMap = ZoneUtil.toNameMap(zones);
        Map<Integer, Zone> zoneIdMap = ZoneUtil.toIdMap(zones);

        File jsonFile = new File(Flipp08MissionTest.class.getResource("/flipp08zones.json").getFile());
        List<JsonNode> flips = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                JsonNode flip = JacksonUtil.readValue(line, JsonNode.class);
                flips.add(flip);
            }
        }

        Set<String> flippZoneNames = new HashSet<>();
        for (JsonNode flip : flips) {
            JsonNode array = flip.get("zones");
            for (int i = 0; i < array.size(); i += 1) {
                JsonNode value = array.get(i);
                Zone zone;
                if (value.canConvertToInt()) {
                    int zoneId = value.asInt();
                    zone = zoneIdMap.get(zoneId);
                    assertNotNull(zone, "Zone '" + zoneId + "' not found!");
                } else {
                    String zoneName = value.asText();
                    zone = zoneMap.get(zoneName);
                    assertNotNull(zone, "Zone '" + zoneName + "' not found!");
                }
                flippZoneNames.add(zone.getName());
            }
        }

        return flippZoneNames;
    }

    @Test
    public void flipp08MonthProgressTest() throws Exception {
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Zone> zoneMap = ZoneUtil.toNameMap(zones);
        Map<Integer, Zone> zoneIdMap = ZoneUtil.toIdMap(zones);

        File jsonFile = new File(Flipp08MissionTest.class.getResource("/flipp08zones.json").getFile());
        List<JsonNode> flips = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println("Parsing " + line);
                JsonNode flip = JacksonUtil.readValue(line, JsonNode.class);
                flips.add(flip);
            }
        }

        Monthly monthly = MonthlyTest.getMonthly();
        int totalZoneCount = 0;
        int totalTakenCount = 0;
        for (JsonNode flip : flips) {
            String flipName = flip.get("name").asText();
            int flipZoneCount = flip.get("zoneCount").asInt();
            JsonNode array = flip.get("zones");
            int count = 0;
            Set<String> flipZoneNames = new HashSet<>();
            boolean zoneNameInput = false;
            for (int i = 0; i < array.size(); i += 1) {
                JsonNode value = array.get(i);
                Zone zone;
                if (value.canConvertToInt()) {
                    int zoneId = value.asInt();
                    zone = zoneIdMap.get(zoneId);
                    assertNotNull(zone, "Zone '" + zoneId + "' not found!");
                } else {
                    String zoneName = value.asText();
                    zone = zoneMap.get(zoneName);
                    assertNotNull(zone, "Zone '" + zoneName + "' not found!");
                    zoneNameInput = true;
                }
                flipZoneNames.add(zone.getName());
                count += 1;
            }
            assertEquals(flipZoneCount, count);
            List<MonthlyZone> takeZones = monthly.getZones().stream()
                    .filter(monthlyZone -> flipZoneNames.contains(monthlyZone.getName()))
                    .collect(Collectors.toList());
            System.out.println(String.format("%s - %d / %d", flipName, takeZones.size(), flipZoneNames.size()));
            totalZoneCount += flipZoneNames.size();
            totalTakenCount += takeZones.size();
        }

        System.out.println(String.format("Total - %d / %d", totalTakenCount, totalZoneCount));
    }
    
    @Test
    public void flipp08Test() throws Exception {
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Zone> zoneMap = ZoneUtil.toNameMap(zones);
        Map<Integer, Zone> zoneIdMap = ZoneUtil.toIdMap(zones);

        File jsonFile = new File(Flipp08MissionTest.class.getResource("/flipp08zones.json").getFile());
        List<JsonNode> flips = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println("Parsing " + line);
                JsonNode flip = JacksonUtil.readValue(line, JsonNode.class);
                flips.add(flip);
            }
        }

        try (KMLWriter outAll = new KMLWriter("flipp08-all.kml");
                KMLWriter outRemaining = new KMLWriter("flipp08-remaining.kml")) {

            outAll.writeFolder("Nollåttaflippen - alla zoner");

            int roundRobin = flips.size() / 10 + ((flips.size() % 10 > 0) ? 1 : 0);
            List<KMLWriter> roundRobinMaps = new ArrayList<>(roundRobin);
            for (int i = 0; i < roundRobin; i += 1) {
                roundRobinMaps.add(new KMLWriter("flipp-08-round-" + i + ".kml"));
            }

            int totalZoneCount = 0;
            int takenZoneCount = 0;
            Set<String> uniqueZoneNames = new HashSet<>();
            Map<String, String> allTakenZones = new HashMap<>();
            Map<String, String> allRemainingZones = new HashMap<>();
            for (JsonNode flip : flips) {
                String flipName = flip.get("name").asText();
                //System.out.println("Processing " + flipName);
                int flipZoneCount = flip.get("zoneCount").asInt();
                boolean flipZoneTaken = flip.get("taken").asBoolean();
                //System.out.println("  Taken " + flipZoneTaken);
                JsonNode array = flip.get("zones");
                int count = 0;
                Set<String> flipZoneNames = new HashSet<>();
                boolean zoneNameInput = false;
                for (int i = 0; i < array.size(); i += 1) {
                    JsonNode value = array.get(i);
                    Zone zone;
                    if (value.canConvertToInt()) {
                        int zoneId = value.asInt();
                        zone = zoneIdMap.get(zoneId);
                        assertNotNull(zone, "Zone '" + zoneId + "' not found!");
                    } else {
                        String zoneName = value.asText();
                        zone = zoneMap.get(zoneName);
                        assertNotNull(zone, "Zone '" + zoneName + "' not found!");
                        zoneNameInput = true;
                    }
                    assertFalse(uniqueZoneNames.contains(zone.getName()));
                    uniqueZoneNames.add(zone.getName());
                    //System.out.println("  " + zoneName);
                    flipZoneNames.add(zone.getName());
                    count += 1;
                }
                if (zoneNameInput) {
                    String flipZoneNumbers = flipZoneNames.stream()
                            .map(zoneMap::get)
                            .map(Zone::getId)
                            .map(integer -> Integer.toString(integer))
                            .collect(Collectors.joining(","));
                    String outFlip = String.format("{\"name\":\"%s\",\"taken\":\"%s\",\"zoneCount\":%d,\"zones\":[%s]}",
                            flipName, Boolean.toString(flipZoneTaken), flipZoneCount, flipZoneNumbers);
                    System.out.println(outFlip);
                }
                assertEquals(flipZoneCount, count);
                //System.out.println("  zones: " + count + ", unique: " + uniqueZoneNames.size() + ", same: " + (count == uniqueZoneNames.size()));

                totalZoneCount += count;
                if (flipZoneTaken) {
                    flipZoneNames.forEach(name -> allTakenZones.put(name, flipName + ": " + name));
                    takenZoneCount += count;
                } else if (count > 0) {
                    flipZoneNames.forEach(name -> allRemainingZones.put(name, flipName + ": " + name));
                    outRemaining.writeFolder(flipName);
                    for (String zoneName : flipZoneNames.stream()
                            .sorted(String::compareTo)
                            .collect(Collectors.toList())) {
                        Zone zone = zoneMap.get(zoneName);
                        outRemaining.writePlacemark(zoneName, flipName + ": " + zoneName, zone.getLongitude(), zone.getLatitude());
                    }
                }
                int flipNumber = Integer.valueOf(flipName.substring(5, flipName.indexOf(' ', 5)));
                KMLWriter roundMap = roundRobinMaps.get(flipNumber % roundRobin);
                roundMap.writeFolder(flipName);
                for (String zoneName : flipZoneNames.stream()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())) {
                    Zone zone = zoneMap.get(zoneName);
                    roundMap.writePlacemark(zoneName, flipName + ": " + zoneName, zone.getLongitude(), zone.getLatitude());
                }
            }
            outAll.writeFolder("Taken zones");
            for (Entry<String, String> entry : allTakenZones.entrySet().stream()
                    .sorted(Comparator.comparing(Entry::getValue))
                    .collect(Collectors.toList())) {
                Zone zone = zoneMap.get(entry.getKey());
                outAll.writePlacemark(entry.getKey(), entry.getValue(), zone.getLongitude(), zone.getLatitude());
            }
            outAll.writeFolder("Remaining zones");
            for (Entry<String, String> entry : allRemainingZones.entrySet().stream()
                    .sorted(Comparator.comparing(Entry::getValue))
                    .collect(Collectors.toList())) {
                Zone zone = zoneMap.get(entry.getKey());
                outAll.writePlacemark(entry.getKey(), entry.getValue(), zone.getLongitude(), zone.getLatitude());
            }
            System.out.println("Total zones: " + totalZoneCount);
            System.out.println("Taken zones: " + takenZoneCount);
            for (KMLWriter roundMap : roundRobinMaps) {
                roundMap.close();
            }
        }
    }
}
