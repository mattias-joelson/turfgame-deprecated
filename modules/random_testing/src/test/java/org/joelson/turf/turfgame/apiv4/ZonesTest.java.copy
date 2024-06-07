package org.joelson.mattias.turfgame.apiv4;

import com.fasterxml.jackson.databind.JsonNode;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.JacksonUtil;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZonesTest {
    @Test
    public void parseAllZones() throws Exception {
        List<Zone> zones = getAllZones();
        assertTrue(zones.size() >= 113388);
    }

    @Test
    public void regionNameLengthTest() throws Exception {
        List<Zone> zones = getAllZones();
        zones = zones.stream().filter(zone -> zone.getRegion() != null).collect(Collectors.toList());
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
        zones.forEach(zone -> assertEquals((long) points.get(zone.getPointsPerHour()), (long) zone.getTakeoverPoints(), zone.getName()));
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

    @Test
    public void testDanderyd() throws Exception {
        List<Zone> zones = getAllZones();
        Set<String> danderydZoneNames = MunicipalityTest.getDanderydZones().keySet();
        Map<Integer, Zone> zoneIdMap = zones.stream()
                .filter(zone -> danderydZoneNames.contains(zone.getName()))
                .collect(Collectors.toMap(Zone::getId, Function.identity()));
        String foo ="[20736,94211,20738,256523,64012,94221,7436,206860,179986,180755,92688,92691,92695,224021,44567,92694,152344,8220,44573,44574,97823,141607,141604,116009,97837,139308,304,201778,97840,8498,96309,86070,86072,161858,34112,25154,34114,91722,37708,25170,25172,278610,30807,179295,13665,96612,33127,221291,208746,295284,233584,61045,2425,1661,12421,109447,92294,109449,114057,107913,78984,109448,109451,109450,92302,92309,119700,321682,92310,8343,8344,92313,216475,216474,96667,216473,204451,324260,324261,178336,138148,79018,22188,178351,213165,13491,154293,32192,8385,32193,114886,113864,97994,22220,12241,28370,12242,12245,12246,109529,109528,114908,164831,147170,20705,147169,1253,142570,1258,142574,15092,7156,15093,63990,15095,226804,9464,24058,94204,7167]";
        JsonNode array = JacksonUtil.readValue(foo, JsonNode.class);
        Set<Integer> owned = new HashSet<>();
        for (int i = 0; i < array.size(); i += 1) {
            owned.add(array.get(i).intValue());
        }
        System.out.println(owned);
        int[] scores = new int[] { 0, 0, 0, 0, 0};
        zoneIdMap.values().forEach(zone -> {
            scores[0] += zone.getTakeoverPoints();
            scores[2] += zone.getPointsPerHour();
            if (owned.contains(zone.getId())) {
                scores[1] += zone.getTakeoverPoints() / 2;
            } else {
                scores[1] += zone.getTakeoverPoints();
                scores[3] += zone.getPointsPerHour();
                scores[4] += 1;
            }
        });
        System.out.println("Zones:   " + danderydZoneNames.size());
        System.out.println("Takes:   " + scores[4]);
        System.out.println("Max TP:  " + scores[0]);
        System.out.println("TP:      " + scores[1]);
        System.out.println("Max PPH: " + scores[2]);
        System.out.println("PPH:     " + scores[3]);
    }

    @Test
    public void testSundbyberg() throws Exception {
        List<Zone> zones = getAllZones();
        Set<String> sundbybergZoneNames = MunicipalityTest.getSundbybergZones().keySet();
        Map<Integer, Zone> zoneIdMap = zones.stream()
                .filter(zone -> sundbybergZoneNames.contains(zone.getName()))
                .collect(Collectors.toMap(Zone::getId, Function.identity()));
        String foo ="[20736,94211,20738,256523,64012,94221,7436,206860,179986,180755,92688,92691,92695,224021,44567,92694,152344,8220,44573,44574,97823,141607,141604,116009,97837,139308,304,201778,97840,8498,96309,86070,86072,161858,34112,25154,34114,91722,37708,25170,25172,278610,30807,179295,13665,96612,33127,221291,208746,295284,233584,61045,2425,1661,12421,109447,92294,109449,114057,107913,78984,109448,109451,109450,92302,92309,119700,321682,92310,8343,8344,92313,216475,216474,96667,216473,204451,324260,324261,178336,138148,79018,22188,178351,213165,13491,154293,32192,8385,32193,114886,113864,97994,22220,12241,28370,12242,12245,12246,109529,109528,114908,164831,147170,20705,147169,1253,142570,1258,142574,15092,7156,15093,63990,15095,226804,9464,24058,94204,7167]";
        JsonNode array = JacksonUtil.readValue(foo, JsonNode.class);
        Set<Integer> owned = new HashSet<>();
        for (int i = 0; i < array.size(); i += 1) {
            owned.add(array.get(i).intValue());
        }
        int[] scores = new int[] { 0, 0, 0, 0, 0};
        zoneIdMap.values().forEach(zone -> {
            scores[0] += zone.getTakeoverPoints();
            scores[2] += zone.getPointsPerHour();
            if (owned.contains(zone.getId())) {
                scores[1] += zone.getTakeoverPoints() / 2;
            } else {
                scores[1] += zone.getTakeoverPoints();
                scores[3] += zone.getPointsPerHour();
                scores[4] += 1;
            }
        });
        System.out.println("Zones:   " + sundbybergZoneNames.size());
        System.out.println("Takes:   " + scores[4]);
        System.out.println("Max TP:  " + scores[0]);
        System.out.println("TP:      " + scores[1]);
        System.out.println("Max PPH: " + scores[2]);
        System.out.println("PPH:     " + scores[3]);
    }
}
