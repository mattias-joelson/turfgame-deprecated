package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.Municipality;
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
import static org.junit.Assert.assertTrue;

public class MissionTest {

    private static String MAP_KEY = "";
    
    @Test
    public void jarfallaSnurrTest() throws IOException {
        List<Integer> zones = readJarfallaSnurrZones();
        assertEquals(302, zones.size());
    }
    
    @Test
    public void oberoffJarfallaSnurrTest() throws IOException {
        List<Integer> zones = readOberoffJarfallaSnurrZones();
        assertEquals(197, zones.size());
    }
    
    @Test
    public void soderSnurrTest() throws IOException {
        List<Integer> zones = readSoderSnurrZones();
        assertEquals(517, zones.size());
    }
    
    @Test
    public void oberoffSoderSnurrTest() throws IOException {
        List<Integer> zones = readOberoffSoderSnurrZones();
        assertEquals(291, zones.size());
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
    
    public static List<Integer> readOberoffSolnaSnurrZones() throws IOException {
        return URLReaderTest.readProperties("/mission_33_solna_oberoff.html", Mission::fromHTML);
    }

    private static class ZoneStat implements Comparable<ZoneStat> {
        private final String name;
        private final int id;
        private final String municipality;
        private final double longitude;
        private final double latitude;
        private ZoneStat next;
        private boolean taken;
    
        private ZoneStat(final String name, final int id, String municipality, double longitude, double latitude) {
            this.name = name;
            this.id = id;
            this.municipality = municipality;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    
        @Override
        public int compareTo(ZoneStat o) {
            return name.compareTo(o.name);
        }
    }
    
    @Test
    public void combineStockholm() throws IOException {
        List<Zone> allZones = ZonesTest.getAllZones();
        List<Integer> soderSnurrZoneIds = MissionTest.readOberoffSoderSnurrZones();
        List<Integer> jarfallaSnurrZoneIds = MissionTest.readOberoffJarfallaSnurrZones();
        Map<String, Boolean> stockholmZoneNames = MunicipalityTest.getStockholmZones();
        Map<String, Boolean> huddingZoneNames = MunicipalityTest.getHuddingeZones();
        //Map<String, Boolean> nackaZoneNames = MunicipalityTest.getNackaZones();
        Map<String, Boolean> jarfallaZoneNames = MunicipalityTest.getJarfallaZones();
        //Map<String, Boolean> sollentunaZoneNames = MunicipalityTest.getSollentunaZones();
        //Map<String, Boolean> upplandsBroZoneNames = MunicipalityTest.getUpplandsBroZones();
        
        Map<String, Zone> namedZones = new HashMap<>(allZones.size());
        Map<Integer, Zone> numberedZones = new HashMap<>(allZones.size());
        for (Zone zone : allZones) {
            namedZones.put(zone.getName(), zone);
            numberedZones.put(zone.getId(), zone);
        }
    
        Map<String, ZoneStat> zones = new HashMap<>(soderSnurrZoneIds.size() + stockholmZoneNames.size());
        addMunicipalityZones(stockholmZoneNames, "Stockholm", namedZones, zones);
        addMunicipalityZones(huddingZoneNames, "Huddinge", namedZones, zones);
        //addMunicipalityZones(nackaZoneNames, "Nacka", namedZones, zones);
        addMunicipalityZones(jarfallaZoneNames, "Järfälla", namedZones, zones);
        //addMunicipalityZones(sollentunaZoneNames, "Sollentuna", namedZones, zones);
        //addMunicipalityZones(upplandsBroZoneNames, "Upplands-Bro", namedZones, zones);
    
        addMissionZones(soderSnurrZoneIds, numberedZones, zones);
        addMissionZones(jarfallaSnurrZoneIds, numberedZones, zones);

        List<ZoneStat> includedZones = zones.values().stream().sorted().collect(Collectors.toList());
        ZoneStat zoneStat = includedZones.get(0);

        PrintStream out = new PrintStream(new FileOutputStream("testStockholm.html"));
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
            String icon = getIcon(zone);
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
    
    private String getIcon(ZoneStat zone) {
        String[] zoneIcons = null;
        switch (zone.municipality) {
            case "Stockholm":
            case "":
                zoneIcons = new String[] { "http://maps.google.com/mapfiles/ms/icons/red-dot.png", "http://maps.google.com/mapfiles/ms/icons/orange.png", "http://maps.google.com/mapfiles/ms/icons/green.png"};
                break;
            case "Huddinge":
            case "Järfälla":
            case "Nacka":
                zoneIcons = new String[] { "http://maps.google.com/mapfiles/ms/icons/purple-dot.png", "http://maps.google.com/mapfiles/ms/icons/lightblue.png", "http://maps.google.com/mapfiles/ms/icons/pink.png"};
                break;
            case "Sollentuna":
                zoneIcons = new String[] { "http://maps.google.com/mapfiles/ms/icons/blue-dot.png", "http://maps.google.com/mapfiles/ms/icons/yellow.png", "http://maps.google.com/mapfiles/ms/icons/lightblue.png"};
                break;
            case "Upplands-Bro":
                zoneIcons = new String[] { "http://maps.google.com/mapfiles/ms/icons/red-dot.png", "http://maps.google.com/mapfiles/ms/icons/orange.png", "http://maps.google.com/mapfiles/ms/icons/green.png"};
                break;
            default:
                assertTrue("Missing " + zone.municipality, false);
        }
        String icon = zoneIcons[0];
        if (zone.next != null) {
            icon = zoneIcons[1];
        } else if (zone.taken) {
            icon = zoneIcons[2];
        }
        return icon;
    }
    
    private void addMunicipalityZones(Map<String, Boolean> municipalityZones, String municipalityName, Map<String, Zone> namedZones, Map<String, ZoneStat> zones) {
        for (Map.Entry<String, Boolean> entry : municipalityZones.entrySet()) {
            ZoneStat zoneStat = zones.get(entry.getKey());
            assertTrue(zoneStat == null);
            if (zoneStat == null) {
                Zone zone = namedZones.get(entry.getKey());
                assertTrue("Missing zone " + entry.getKey(), zone != null);
                zoneStat = new ZoneStat(zone.getName(), zone.getId(), municipalityName, zone.getLongitude(), zone.getLatitude());
                zones.put(zoneStat.name, zoneStat);
            }
            zoneStat.taken = entry.getValue();
        }
    }
    
    private void addMissionZones(List<Integer> missionZoneIds, Map<Integer, Zone> numberedZones, Map<String, ZoneStat> zones) {
        ZoneStat prev = null;
        for (int id : missionZoneIds) {
            Zone zone = numberedZones.get(id);
            ZoneStat zoneStat = zones.get(zone.getName());
            assertTrue("Missing zone " + zone.getName(), zoneStat != null);
            if (prev != null) {
                prev.next = zoneStat;
            }
            prev = zoneStat;
        }
        if (prev != null) {
            prev.next = prev;
        }
    }

    @Test
    public void combineSolnaSundbyberg() throws IOException {
        List<Zone> allZones = ZonesTest.getAllZones();
        List<Integer> solnaSnurrZoneIds = readSolnaSnurrZones();
        Map<String, Boolean> solnaZoneNames = MunicipalityTest.getSolnaZones();
        Map<String, Boolean> sundbybergZoneNames = MunicipalityTest.getSundbybergZones();
        Map<String, Boolean> zoneNames = new HashMap<>(solnaZoneNames.size() + sundbybergZoneNames.size());
        
        zoneNames.putAll(solnaZoneNames);
        zoneNames.putAll(sundbybergZoneNames);
        
        Map<String, Zone> namedZones = new HashMap<>(allZones.size());
        Map<Integer, Zone> numberedZones = new HashMap<>(allZones.size());
        for (Zone zone : allZones) {
            namedZones.put(zone.getName(), zone);
            numberedZones.put(zone.getId(), zone);
        }
    
        Map<String, ZoneStat> zones = new HashMap<>(solnaSnurrZoneIds.size() + zoneNames.size());
        for (Map.Entry<String, Boolean> entry : zoneNames.entrySet()) {
            ZoneStat zoneStat = zones.get(entry.getKey());
            if (zoneStat == null) {
                Zone zone = namedZones.get(entry.getKey());
                zoneStat = new ZoneStat(zone.getName(), zone.getId(), "", zone.getLongitude(), zone.getLatitude());
                zones.put(zoneStat.name, zoneStat);
            }
            zoneStat.taken = entry.getValue();
        }
        addMissionZones(solnaSnurrZoneIds, numberedZones, zones);
        
        
        List<ZoneStat> includedZones = zones.values().stream().sorted().collect(Collectors.toList());
        ZoneStat zoneStat = includedZones.get(0);
        
        PrintStream out = new PrintStream(new FileOutputStream("testSolna.html"));
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
            String icon = getIcon(zone);
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
        out.println("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=" + MAP_KEY + "&callback=initMap\"></script></head>");
        out.println("<body><div id='table'><table><tr><th>Name</th><th>Mission</th><th>Taken</th></tr>");
        for (ZoneStat zone : includedZones) {
            out.println("  <tr><td>" + zone.name + "</td><td>" + (zone.next != null) + "</td><td>" + zone.taken + "</td></tr>");
            if (zone.next == null) {
                System.out.println(zone.name);
            }
        }
        out.println("</table></div><div id='map'>");
        //out.println("Hi!");
        out.println("</div></body></html>");
        out.close();
    }
}
