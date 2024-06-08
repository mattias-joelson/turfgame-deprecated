package org.joelson.turf.turfgame.apiv4;

import com.fasterxml.jackson.databind.JsonNode;
import org.joelson.turf.lundkvist.MunicipalityTest;
import org.joelson.turf.util.JacksonUtil;
import org.joelson.turf.util.URLReaderTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ZonesTest {

    public static List<Zone> getAllZones() throws IOException {
        return URLReaderTest.readProperties("zones-all.json", Zones::fromJSON);
    }

    @Test
    public void testDanderyd() throws Exception {
        List<Zone> zones = getAllZones();
        Set<String> danderydZoneNames = MunicipalityTest.getDanderydZones().keySet();
        Map<Integer, Zone> zoneIdMap = zones.stream().filter(zone -> danderydZoneNames.contains(zone.getName()))
                .collect(Collectors.toMap(Zone::getId, Function.identity()));
        String foo = "[20736,94211,20738,256523,64012,94221,7436,206860,179986,180755,92688,92691,92695,224021,44567,"
                + "92694,152344,8220,44573,44574,97823,141607,141604,116009,97837,139308,304,201778,97840,"
                + "8498,96309,86070,86072,161858,34112,25154,34114,91722,37708,25170,25172,278610,30807,"
                + "179295,13665,96612,33127,221291,208746,295284,233584,61045,2425,1661,12421,109447,92294,"
                + "109449,114057,107913,78984,109448,109451,109450,92302,92309,119700,321682,92310,8343,8344,"
                + "92313,216475,216474,96667,216473,204451,324260,324261,178336,138148,79018,22188,178351,"
                + "213165,13491,154293,32192,8385,32193,114886,113864,97994,22220,12241,28370,12242,12245,"
                + "12246,109529,109528,114908,164831,147170,20705,147169,1253,142570,1258,142574,15092,7156,"
                + "15093,63990,15095,226804,9464,24058,94204,7167]";
        JsonNode array = JacksonUtil.readValue(foo, JsonNode.class);
        Set<Integer> owned = new HashSet<>();
        for (int i = 0; i < array.size(); i += 1) {
            owned.add(array.get(i).intValue());
        }
        System.out.println(owned);
        int[] scores = new int[]{ 0, 0, 0, 0, 0 };
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
        Map<Integer, Zone> zoneIdMap = zones.stream().filter(zone -> sundbybergZoneNames.contains(zone.getName()))
                .collect(Collectors.toMap(Zone::getId, Function.identity()));
        String foo = "[20736,94211,20738,256523,64012,94221,7436,206860,179986,180755,92688,92691,92695,224021,44567,"
                + "92694,152344,8220,44573,44574,97823,141607,141604,116009,97837,139308,304,201778,97840,"
                + "8498,96309,86070,86072,161858,34112,25154,34114,91722,37708,25170,25172,278610,30807,"
                + "179295,13665,96612,33127,221291,208746,295284,233584,61045,2425,1661,12421,109447,92294,"
                + "109449,114057,107913,78984,109448,109451,109450,92302,92309,119700,321682,92310,8343,8344,"
                + "92313,216475,216474,96667,216473,204451,324260,324261,178336,138148,79018,22188,178351,"
                + "213165,13491,154293,32192,8385,32193,114886,113864,97994,22220,12241,28370,12242,12245,"
                + "12246,109529,109528,114908,164831,147170,20705,147169,1253,142570,1258,142574,15092,7156,"
                + "15093,63990,15095,226804,9464,24058,94204,7167]";
        JsonNode array = JacksonUtil.readValue(foo, JsonNode.class);
        Set<Integer> owned = new HashSet<>();
        for (int i = 0; i < array.size(); i += 1) {
            owned.add(array.get(i).intValue());
        }
        int[] scores = new int[]{ 0, 0, 0, 0, 0 };
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
