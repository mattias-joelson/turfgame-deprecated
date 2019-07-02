package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class MissionTest {
    
    @Test
    public void jarfallaSnurrTest() throws IOException {
        List<Integer> zones = readJarfallaSnurrZones();
        assertEquals(302, zones.size());
    }
    
    @Test
    public void oberoffJarfallaSnurrTest() throws IOException {
        List<Integer> zones = readOberoffJarfallaSnurrZones();
        assertEquals(279, zones.size());
    }
    
    @Test
    public void soderSnurrTest() throws IOException {
        List<Integer> zones = readSoderSnurrZones();
        assertEquals(517, zones.size());
    }
    
    @Test
    public void oberoffSoderSnurrTest() throws IOException {
        List<Integer> zones = readOberoffSoderSnurrZones();
        assertEquals(477, zones.size());
    }

    @Test
    public void solnaSnurrTest() throws IOException {
        List<Integer> zones = readSolnaSnurrZones();
        assertEquals(256, zones.size());
    }
    
    public static List<Integer> readJarfallaSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_37_jarfalla.html", Mission::fromHTML);
    
    }
    
    public static List<Integer> readOberoffJarfallaSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_37_jarfalla_oberoff.html", Mission::fromHTML);
        
    }
    
    public static List<Integer> readSoderSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_34_sodersnurr.html", Mission::fromHTML);
        
    }
    
    public static List<Integer> readOberoffSoderSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_34_sodersnurr_oberoff.html", Mission::fromHTML);
        
    }
    
    public static List<Integer> readSolnaSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_33_solna.html", Mission::fromHTML);
        
    }
    
    private static class ZoneStat implements Comparable<ZoneStat> {
        private final String name;
        private final int id;
        private final double longitude;
        private final double latitude;
        private ZoneStat next;
        private boolean taken;
    
        private ZoneStat(final String name, final int id, double longitude, double latitude) {
            this.name = name;
            this.id = id;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    
        @Override
        public int compareTo(ZoneStat o) {
            return name.compareTo(o.name);
        }
    }
    
    @Test
    public void combine() throws IOException {
        List<Zone> allZones = ZonesTest.getAllZones();
        List<Integer> soderSnurrZoneIds = MissionTest.readOberoffSoderSnurrZones();
        List<Integer> jarfallaSnurrZoneIds = MissionTest.readOberoffJarfallaSnurrZones();
        Map<String, Boolean> stockholmZoneNames = MunicipalityTest.getStockholmZones();
        
        Map<String, Zone> namedZones = new HashMap<>(allZones.size());
        Map<Integer, Zone> numberedZones = new HashMap<>(allZones.size());
        for (Zone zone : allZones) {
            namedZones.put(zone.getName(), zone);
            numberedZones.put(zone.getId(), zone);
        }
    
        Map<String, ZoneStat> zones = new HashMap<>(soderSnurrZoneIds.size() + stockholmZoneNames.size());
        addMissionZones(soderSnurrZoneIds, numberedZones, zones);
        addMissionZones(jarfallaSnurrZoneIds, numberedZones, zones);
    
        for (Map.Entry<String, Boolean> entry : stockholmZoneNames.entrySet()) {
            ZoneStat zoneStat = zones.get(entry.getKey());
            if (zoneStat == null) {
                Zone zone = namedZones.get(entry.getKey());
                zoneStat = new ZoneStat(zone.getName(), zone.getId(), zone.getLongitude(), zone.getLatitude());
                zones.put(zoneStat.name, zoneStat);
            }
            zoneStat.taken = entry.getValue();
        }
        
        List<ZoneStat> includedZones = zones.values().stream().sorted().collect(Collectors.toList());
        ZoneStat zoneStat = includedZones.get(0);

        PrintStream out = new PrintStream(new FileOutputStream("test.html"));
        out.println("<html><head><style>");
        out.println("#map { height: 75%; }");
        out.println("#table { height: 200px; }");
        out.println("</style><script>");
        out.println("function initMap() {");
        out.println("var myLatLng = {lat: " + zoneStat.latitude + ", lng: " + zoneStat.longitude + "};");
        out.println("var map = new google.maps.Map(document.getElementById('map'), { zoom: 10, center: myLatLng });");
        int n = 0;
        int l = 0;
        for (ZoneStat zone : includedZones) {
            String name = "marker_" + n++;
            String icon = "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
            if (zone.next != null) {
                icon = "http://maps.google.com/mapfiles/ms/icons/orange.png";
            } else if (zone.taken) {
                icon = "http://maps.google.com/mapfiles/ms/icons/green.png";
            }
            out.println("var " + name + " = new google.maps.Marker({ position: { lat: " + zone.latitude + ", lng: " + zone.longitude
                    + " }, map: map, icon: { url: '" + icon + "' } });");
            if (zone.next != null && zone.next != zone) {
                String line = "line_" + l++;
                out.println("var " + line + " = new google.maps.Polyline({ path: [{lat: " + zone.latitude + ", lng: " + zone.longitude
                        + "},{lat: " + zone.next.latitude + ", lng: " + zone.next.longitude
                        + "}], geodesic: true, strokeColor: '#ff8800', strokeOpacity: 1.0, strokeWeight: 2 });");
                out.println(line + ".setMap(map);");
            }
        }
        out.println("}");
        out.println("</script>");
        String MAP_KEY = "";
        out.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=" + MAP_KEY + "&callback=initMap\"></script></head>");
        out.println("<body><div id='table'><table><tr><th>Name</th><th>Mission</th><th>Taken</th></tr>");
        for (ZoneStat zone : includedZones) {
            out.println("  <tr><td>" + zone.name + "</td><td>" + (zone.next != null) + "</td><td>" + zone.taken + "</td></tr>");
        }
        out.println("</table></div><div id='map'>");
        //out.println("Hi!");
        out.println("</div></body></html>");
        out.close();
    }
    
    private void addMissionZones(List<Integer> missionZoneIds, Map<Integer, Zone> numberedZones, Map<String, ZoneStat> zones) {
        ZoneStat prev = null;
        for (int id : missionZoneIds) {
            Zone zone = numberedZones.get(id);
            ZoneStat zoneStat = new ZoneStat(zone.getName(), zone.getId(), zone.getLongitude(), zone.getLatitude());
            zones.put(zoneStat.name, zoneStat);
            if (prev != null) {
                prev.next = zoneStat;
            }
            prev = zoneStat;
        }
        if (prev != null) {
            prev.next = prev;
        }
    }
}
