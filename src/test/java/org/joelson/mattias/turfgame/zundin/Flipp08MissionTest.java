package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONNumber;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONString;
import org.joelson.mattias.turfgame.util.JSONValue;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
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
        
        PrintStream out = new PrintStream(new FileOutputStream("flipp08.kml"));
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        out.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">");
        out.println("  <Document>");

        int totalZoneCount = 0;
        int takenZoneCount = 0;
        for (JSONObject flip : flips) {
            String flipName = ((JSONString) flip.getValue("name")).stringValue();
            System.out.println("Processing " + flipName);
            int flipZoneCount = ((JSONNumber) flip.getValue("zoneCount")).intValue();
            boolean flipZoneTaken = ((JSONString) flip.getValue("taken")).stringValue().equals("true");
            System.out.println("  Taken " + flipZoneTaken);
            JSONArray array = (JSONArray) flip.getValue("zones");
            int count = 0;
            Set<String> uniqueZoneNames = new HashSet<>();
            for (JSONValue value : array.getElements()) {
                String zoneName = ((JSONString) value).stringValue();
                uniqueZoneNames.add(zoneName);
                Zone zone = zoneMap.get(zoneName);
                if (zone == null) {
                    System.err.println("Zone '" + zoneName + "' not found!");
                    assertTrue(zone != null);
                }
                System.out.println("  " + zoneName);
                count += 1;
            }
            assertEquals(count, uniqueZoneNames.size());
            assertEquals(flipZoneCount, count);
            System.out.println("  zones: " + count + ", unique: " + uniqueZoneNames.size() + ", same: " + (count == uniqueZoneNames.size()));

            out.println("    <Folder>");
            out.println("      <name>" + flipName + "</name>");
            for (String zoneName : uniqueZoneNames.stream().sorted(String::compareTo).collect(Collectors.toList())) {
                Zone zone = zoneMap.get(zoneName);
                out.println("        <Placemark>");
                out.println("          <name>" + zoneName + "</name>");
                out.println("          <description>" + flipName + " - " + zoneName + "</description>");
                out.println("          <Point>");
                out.println("            <coordinates>" + zone.getLongitude() + ',' + zone.getLatitude() + "</coordinates>");
                out.println("          </Point>");
                out.println("        </Placemark>");
            }
            out.println("    </Folder>");
            totalZoneCount += count;
            if (flipZoneTaken) {
                takenZoneCount += count;
            }
        }
        out.println("  </Document>");
        out.println("</kml>");
        out.close();
        System.out.println("Total zones: " + totalZoneCount);
        System.out.println("Taken zones: " + takenZoneCount);
    }
}
