package org.joelson.mattias.turfgame.apiv4;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ZonesTest {
    @Test
    public void parseAllZones() throws Exception {
        List<Zone> zones = getAllZones();
        assertEquals(93308, zones.size());
    }

    @Test
    public void regionNameLengthTest() throws Exception {
        List<Zone> zones = getAllZones();
        Set<String> regionNames = new HashSet<>();
        zones.forEach(zone -> regionNames.add(zone.getRegion().getName()));
        Set<String> countryNames = new HashSet<>();
        zones.forEach(zone -> countryNames.add(zone.getRegion().getCountry()));
        System.out.println("Max length: " + regionNames.stream().map(String::length).max(Integer::compareTo));
        System.out.println("Min length: " + regionNames.stream().map(String::length).min(Integer::compareTo));
        countryNames.forEach(System.out::println);
        //System.out.println("Max length: " + countryNames.stream().map(String::length).max(Integer::compareTo));
        //System.out.println("Max length: " + countryNames.stream().map(String::length).min(Integer::compareTo));
    }

    @Test
    public void zonenameLengthTest() throws Exception {
        List<Zone> zones = getAllZones();
        System.out.println("Max length: " + zones.stream().map(Zone::getName).map(String::length).max(Integer::compareTo));
        System.out.println("Min length: " + zones.stream().map(Zone::getName).map(String::length).min(Integer::compareTo));
    }

    @Test
    public void zonenameCompositionTest() throws Exception {
        List<Zone> zones = getAllZones();
        int[] characters = new int[256];
        Map<Integer, Integer> other = new HashMap<>();
        int numeric = 0;
        int alphabetic = 0;
        int non = 0;
        List<String> nonAlphabetic = new ArrayList<>();
        List<String> nonZones = new ArrayList<>();
        List<Character> numerics = new ArrayList<>();
        List<Character> alphabetics = new ArrayList<>();
        List<Character> nons = new ArrayList<>();
        List<String> lowercaseZones = new ArrayList<>();
        for (String name : zones.stream()
                .map(Zone::getName)
                .sorted()
                .collect(Collectors.toList())) {
            name.chars().distinct().forEach(ch -> {
                if (ch < 256) {
                    characters[ch] += 1;
                } else {
                    Integer count = other.get(ch);
                    count = (count == null) ? 1 : count + 1;
                    other.put(ch, count);
                }
            });
            boolean hasNumeric = false;
            boolean hasAlphabetic = false;
            boolean hasNon = false;
            for (int i = 0; i < name.length(); i += 1) {
                char ch = name.charAt(i);
                if (Character.isDigit(ch)) {
                    hasNumeric = true;
                } else if (Character.isAlphabetic(ch)) {
                    hasAlphabetic = true;
                } else {
                    hasNon = true;
                }
            }
            numeric += (hasNumeric) ? 1 : 0;
            alphabetic += (hasAlphabetic) ? 1 : 0;
            if (!hasAlphabetic) {
                nonAlphabetic.add(name);
            }
            non += (hasNon) ? 1 : 0;
            if (hasNon) {
                nonZones.add(name);
            }
            if (Character.isAlphabetic(name.charAt(0)) && Character.isLowerCase(name.charAt(0))) {
                lowercaseZones.add(name);
            }
        }
        for (int i = 0; i < characters.length; i += 1) {
            if (characters[i] > 0) {
                System.out.println(String.format("%5d - %c - %5d - %b %b", i, (i >= 32) ? i : ' ', characters[i], Character.isDigit(i), Character.isAlphabetic(i)));
                if (Character.isDigit(i)) {
                    numerics.add(Character.valueOf((char) i));
                } else if (Character.isAlphabetic(i)) {
                    alphabetics.add(Character.valueOf((char) i));
                } else {
                    nons.add(Character.valueOf((char) i));
                }
            }
        }
        other.keySet().stream().sorted().forEach(ch -> {
            System.out.println(String.format("%5d - %c - %5d - %b %b", ch, ch, other.get(ch), Character.isDigit(ch), Character.isAlphabetic(ch)));
            if (Character.isDigit(ch)) {
                numerics.add(Character.valueOf((char) ch.intValue()));
            } else if (Character.isAlphabetic(ch)) {
                alphabetics.add(Character.valueOf((char) ch.intValue()));
            } else {
                nons.add(Character.valueOf((char) ch.intValue()));
            }
        });
        System.out.println("numeric:    " + numeric);
        System.out.println("alphabetic: " + alphabetic);
        System.out.println("non:        " + non);
        System.out.println("zones:      " + zones.size());
        System.out.println();
        System.out.println("nonAlphabetic: " + nonAlphabetic.size());
        for (String name : nonAlphabetic) {
            System.out.println(name);
        }
        System.out.println();
        System.out.println("nonZones: " + nonZones.size());
        for (String name : nonZones) {
            System.out.println(name);
        }
        System.out.println();
        System.out.println("lowercaseZones: " + lowercaseZones.size());
        for (String name : lowercaseZones) {
            System.out.println(name);
        }
        System.out.println();
        System.out.println(numerics);
        System.out.println();
        System.out.println(alphabetics);
        System.out.println();
        System.out.println(nons);
    }

    @Test
    public void zonePointsTest() throws Exception {
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
    public void zonePointsVerifyTest() throws Exception {
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
        zones.forEach(zone -> assertEquals(zone.getName(), (long) points.get(zone.getPointsPerHour()), (long) zone.getTakeoverPoints()));
    }

    public static List<Zone> getAllZones() throws IOException {
        return URLReaderTest.readProperties("zones-all.json", Zones::fromJSON);
    }
    
    @Test
    public void testName() throws Exception {
        List<Zone> zones = getAllZones();
        Set<String> zoneNames = new HashSet<>(zones.size());
        zones.forEach(zone -> zoneNames.add(zone.getName()));
        System.out.println("zones: " + zones.size());
        System.out.println("zoneNames: " + zoneNames.size());
        zones.stream()
                .filter(zone -> zone.getName().contains("FäbodaSwim") || zone.getName().contains("CarlislePlaza"))
                .forEach(zone -> System.out.println(zone.getName() + " - " + zone.getId() + " - " + zone.getDateCreated() + " - " + zone.getRegion().getName()
                        + " @ " + zone.getLatitude() + ", " + zone.getLongitude()));
    }
}
