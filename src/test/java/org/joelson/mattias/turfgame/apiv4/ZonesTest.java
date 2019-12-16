package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ZonesTest {
    @Test
    public void parseAllZones() throws IOException {
        List<Zone> zones = getAllZones();
        assertEquals(64065, zones.size());
    }
    
    @Test
    public void regionNameLengthTest() throws IOException {
        List<Zone> zones = getAllZones();
        Set<String> regionNames = new HashSet<>();
        zones.forEach(zone -> regionNames.add(zone.getRegion().getName()));
        Set<String> countryNames = new HashSet<>();
        zones.forEach(zone -> countryNames.add(zone.getRegion().getCountry()));
        System.out.println("Max length: " + regionNames.stream().map(String::length).max(Integer::compareTo));
        System.out.println("Max length: " + regionNames.stream().map(String::length).min(Integer::compareTo));
        countryNames.forEach(System.out::println);
        //System.out.println("Max length: " + countryNames.stream().map(String::length).max(Integer::compareTo));
        //System.out.println("Max length: " + countryNames.stream().map(String::length).min(Integer::compareTo));
    }

    @Test
    public void zonenameLengthTest() throws IOException {
        List<Zone> zones = getAllZones();
        System.out.println("Max length: " + zones.stream().map(Zone::getName).map(String::length).max(Integer::compareTo));
        System.out.println("Max length: " + zones.stream().map(Zone::getName).map(String::length).min(Integer::compareTo));
    }
    
    @Test
    public void zonePointsTest() throws IOException {
        List<Zone> zones = getAllZones();
        Set<Integer> pph = new HashSet<>();
        Set<Integer> tp = new HashSet<>();
        zones.stream().map(Zone::getTakeoverPoints).forEach(tp::add);
        zones.stream().map(Zone::getPointsPerHour).forEach(pph::add);
        System.out.println("TP");
        tp.stream().sorted().forEach(System.out::println);
        System.out.println("PPH");
        pph.stream().sorted().forEach(System.out::println);
    }
    
    @Test
    public void zonePointsVerifyTest() throws  IOException {
        List<Zone> zones = getAllZones();
        Map<Integer, Integer> points = Map.of(0, 250,
                1, 185,
                2, 170,
                3, 155,
                4, 140,
                5, 125,
                6, 110,
                7, 95,
                8, 80,
                9, 65);
        zones.stream().forEach(zone -> assertEquals(zone.getName(), (Integer) points.get(zone.getPointsPerHour()), (Integer) zone.getTakeoverPoints()));
    }

    public static List<Zone> getAllZones() throws IOException {
        return URLReaderTest.readProperties("/zones-all.json", Zones::fromHTML);
    }
    
    @Test
    public void testName() throws IOException {
        List<Zone> zones = getAllZones();
        Set<String> zoneNames = new HashSet<>(zones.size());
        zones.forEach(zone -> zoneNames.add(zone.getName()));
        System.out.println("zones: " + zones.size());
        System.out.println("zoneNames: " + zoneNames.size());
        zones.stream().filter(zone -> zone.getName().contains("FäbodaSwim")).forEach(zone -> System.out.println(zone.getName() + " - " + zone.getId() + " - " + zone.getDateCreated() + " - " + zone.getRegion().getName() + " @ " + zone.getLatitude() + ", " + zone.getLongitude()));
    }
}
