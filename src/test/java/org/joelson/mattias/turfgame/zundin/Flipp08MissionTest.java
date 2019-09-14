package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONString;
import org.joelson.mattias.turfgame.util.JSONValue;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Flipp08MissionTest {
    
    @Test
    public void flipp08Test() throws IOException {
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Zone> zoneMap = new HashMap<>();
        for (Zone zone : zones) {
            zoneMap.put(zone.getName(), zone);
        }
        
        File jsonFile = new File(Flipp08MissionTest.class.getResource("/flipp08zones.json").getFile());
        List<JSONObject> flips = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF8"))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Parsing " + line);
                JSONObject flip = (JSONObject) new JSONParser().parse(line);
                flips.add(flip);
            }
        }
        
        KMLWriter outAll = new KMLWriter("flipp08-all.kml");
        outAll.writeFolder("Nollåttaflippen - alla zoner");
                
        KMLWriter outRemaining = new KMLWriter("flipp08-remaining.kml");

        int totalZoneCount = 0;
        int takenZoneCount = 0;
        Set<String> uniqueZoneNames = new HashSet<>();
        Map<String,String> allTakenZones = new HashMap<>();
        Map<String,String> allRemainingZones = new HashMap<>();
        for (JSONObject flip : flips) {
            String flipName = ((JSONString) flip.getValue("name")).stringValue();
            System.out.println("Processing " + flipName);
            int flipZoneCount = ((JSONNumber) flip.getValue("zoneCount")).intValue();
            boolean flipZoneTaken = ((JSONString) flip.getValue("taken")).stringValue().equals("true");
            System.out.println("  Taken " + flipZoneTaken);
            JSONArray array = (JSONArray) flip.getValue("zones");
            int count = 0;
            Set<String> flipZoneNames = new HashSet<>();
            for (JSONValue value : array.getElements()) {
                String zoneName = ((JSONString) value).stringValue();
                assertFalse(uniqueZoneNames.contains(zoneName));
                uniqueZoneNames.add(zoneName);
                Zone zone = zoneMap.get(zoneName);
                if (zone == null) {
                    System.err.println("Zone '" + zoneName + "' not found!");
                    assertTrue(zone != null);
                }
                System.out.println("  " + zoneName);
                flipZoneNames.add(zoneName);
                count += 1;
            }
            assertEquals(flipZoneCount, count);
            System.out.println("  zones: " + count + ", unique: " + uniqueZoneNames.size() + ", same: " + (count == uniqueZoneNames.size()));

            totalZoneCount += count;
            if (flipZoneTaken) {
                flipZoneNames.stream()
                    .forEach(name -> allTakenZones.put(name, flipName + ": " + name));
                takenZoneCount += count;
            } else if (count > 0) {
                flipZoneNames.stream()
                    .forEach(name -> allRemainingZones.put(name, flipName + ": " + name));
                outRemaining.writeFolder(flipName);
                for (String zoneName : flipZoneNames.stream()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())) {
                    Zone zone = zoneMap.get(zoneName);
                    outRemaining.writePlacemark(zoneName, flipName + ": " + zoneName, zone.getLongitude(), zone.getLatitude());
                }
            }
        }
        outAll.writeFolder("Taken zones");
        for (Entry<String, String> entry : allTakenZones.entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList())) {
            Zone zone = zoneMap.get(entry.getKey());
            outAll.writePlacemark(entry.getKey(), entry.getValue(), zone.getLongitude(), zone.getLatitude());
        }
        outAll.writeFolder("Remaining zones");
        for (Entry<String, String> entry : allRemainingZones.entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList())) {
            Zone zone = zoneMap.get(entry.getKey());
            outAll.writePlacemark(entry.getKey(), entry.getValue(), zone.getLongitude(), zone.getLatitude());
        }
        outRemaining.close();
        outAll.close();
        System.out.println("Total zones: " + totalZoneCount);
        System.out.println("Taken zones: " + takenZoneCount);
    }
}
