package org.joelson.mattias.turfgame.zundin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.JSONArray;
import org.joelson.mattias.turfgame.util.JSONObject;
import org.joelson.mattias.turfgame.util.JSONParser;
import org.joelson.mattias.turfgame.util.JSONString;
import org.joelson.mattias.turfgame.util.JSONValue;
import org.joelson.mattias.turfgame.util.URLReader;
import org.junit.Test;

public class Flipp08MissionTest {
    
    @Test
    public void flipp08Test() throws IOException {
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Zone> zoneMap = new HashMap<>();
        for (Zone zone : zones) {
            zoneMap.put(zone.getName(), zone);
        }
        
        File jsonFile = new File(Flipp08MissionTest.class.getResource("/flipp08zones.json").getFile());
        //StringBuffer jsonBuffer = new StringBuffer();
        List<JSONObject> flips = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                JSONObject flip = (JSONObject) new JSONParser().parse(line);
                System.out.println(flip);
                flips.add(flip);
            }
        }
        
        PrintStream out = new PrintStream(new FileOutputStream("flipp08.kml"));
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        out.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">");
        out.println("  <Document>");
        out.println("    <Folder>");
        out.println("      <name>Flip08</name");

        for (JSONObject flip : flips) {
            String name = ((JSONString) flip.getValue("name")).stringValue();
            System.out.println(name);
            JSONArray array = (JSONArray) flip.getValue("zones");
            int count = 0;
            for (JSONValue value : array.getElements()) {
                String zoneName = ((JSONString) value).stringValue();
                System.out.println(zoneName);
                out.println("        <Placemark>");
                out.println("          <name>" + zoneName + "</zone>");
                out.println("          <description>" + name + " - " + zoneName + "</description>");
                Zone zone = zoneMap.get(zoneName);
                out.println("          <Point>");
                out.println("            <coordinates>" + zone.getLongitude() + ',' + zone.getLatitude() + "</coordinates>");
                out.println("          </Point>");
                out.println("        </Placemark>");
                count += 1;
            }
            System.out.println("  zones: " + count);
            break;
        }
        out.println("    </Folder>");
        out.println("  </Document>");
        out.println("</kml>");
        out.close();
    }
}
