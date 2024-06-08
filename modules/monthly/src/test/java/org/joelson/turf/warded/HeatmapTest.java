package org.joelson.turf.warded;

import org.joelson.turf.lundkvist.MunicipalityTest;
import org.joelson.turf.turfgame.apiv4.Zone;
import org.joelson.turf.turfgame.apiv4.ZoneUtil;
import org.joelson.turf.turfgame.apiv4.ZonesTest;
import org.joelson.turf.util.KMLWriter;
import org.joelson.turf.util.URLReaderTest;
import org.joelson.turf.zundin.Monthly;
import org.joelson.turf.zundin.MonthlyTest;
import org.joelson.turf.zundin.MonthlyZone;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HeatmapTest {

    private static final int TAKES_ENTRIES = HeatmapCategories.VIOLET.getTakes() + 1;

    public static Set<String> getDSSZones() throws Exception {
        Set<String> combinedZones = new HashSet<>();
        combinedZones.addAll(MunicipalityTest.getSolnaZones().keySet());
        combinedZones.addAll(MunicipalityTest.getDanderydZones().keySet());
        combinedZones.addAll(MunicipalityTest.getSundbybergZones().keySet());
        return combinedZones;
    }

    public static Set<String> getCircleZones() throws Exception {
        Set<String> combinedZones = getDSSZones();

        Map<String, Zone> zoneMap = ZoneUtil.toNameMap(ZonesTest.getAllZones());
        Zone krausTorgZone = zoneMap.get("KrausTorg");
//        double possibleDistance = ZoneUtil.calcDistance(59.441020, 18.004288, krausTorgZone);
//        System.out.println("possibleDistance: " + possibleDistance);

        double maxDistance = getMaxDistance(zoneMap, krausTorgZone);
        System.out.println("Max distance: " + maxDistance);

        Set<String> stockholmZones = MunicipalityTest.getStockholmZones().keySet().stream().filter(
                zoneName -> inDistance(zoneMap, krausTorgZone, maxDistance, zoneName)).collect(Collectors.toSet());
        List<Entry<String, Double>> stockholmDistances = getSortedZoneDistances(zoneMap, krausTorgZone, stockholmZones);
        combinedZones.addAll(stockholmZones);
        Set<String> sollentunaZones = MunicipalityTest.getSollentunaZones().keySet().stream().filter(
                zoneName -> inDistance(zoneMap, krausTorgZone, maxDistance, zoneName)).collect(Collectors.toSet());
        combinedZones.addAll(sollentunaZones);
        Set<String> tabyZones = MunicipalityTest.getTabyZones().keySet().stream().filter(
                zoneName -> inDistance(zoneMap, krausTorgZone, maxDistance, zoneName)).collect(Collectors.toSet());
        combinedZones.addAll(tabyZones);
        Map<Integer, Zone> zoneIdMap = ZoneUtil.toIdMap(ZonesTest.getAllZones());
        Set<String> extraZones = new HashSet<>();
        extraZones.add(zoneIdMap.get(120).getName());
        extraZones.add(zoneIdMap.get(299).getName());
        extraZones.add(zoneIdMap.get(64100).getName());
        extraZones.add(zoneIdMap.get(94113).getName());
        extraZones.add(zoneIdMap.get(131839).getName());
        extraZones.add(zoneIdMap.get(223822).getName());
        extraZones.add(zoneIdMap.get(680062).getName());
        //        extraZones.add("MillHillField");
        //        extraZones.add("RinkebyAllé");
        //        extraZones.add("Gliaskogen");
        List<Entry<String, Double>> extraDistances = getSortedZoneDistances(zoneMap, krausTorgZone, extraZones);
        combinedZones.addAll(extraZones);
        List<Entry<String, Double>> combinedDistances = getSortedZoneDistances(zoneMap, krausTorgZone, combinedZones);
//        combinedDistances.stream().forEach(stringDoubleEntry -> {
//            if (stringDoubleEntry.getValue() >= 6600d) {
//                System.out.println(stringDoubleEntry.getKey() + "("+ zoneMap.get(stringDoubleEntry.getKey()).getId
//                () +"): " + stringDoubleEntry.getValue());
//            }
//        });
        //        Set<String> restOfStockholmZones = MunicipalityTest.getStockholmZones().keySet();
        //        restOfStockholmZones.removeAll(combinedZones);
        //        List<Entry<String, Double>> restOfStockholmDistances = getSortedZoneDistances(zoneMap,
        //        krausTorgZone, restOfStockholmZones);
        System.out.println("Zones: " + combinedZones.size());
        return combinedZones;
    }

    public static Set<String> getAdjustedCircleZones() throws Exception {
        Set<String> combinedZones = getDSSZones();

        Map<String, Zone> zoneMap = ZoneUtil.toNameMap(ZonesTest.getAllZones());
        Zone krausTorgZone = zoneMap.get("DeSomBlevKvar");
//        double possibleDistance = ZoneUtil.calcDistance(59.441020, 18.004288, krausTorgZone);
//        System.out.println("possibleDistance: " + possibleDistance);

        double maxDistance = getMaxDistance(zoneMap, krausTorgZone);
        System.out.println("Max distance: " + maxDistance);

        Set<String> stockholmZones = MunicipalityTest.getStockholmZones().keySet().stream().filter(
                zoneName -> inDistance(zoneMap, krausTorgZone, maxDistance, zoneName)).collect(Collectors.toSet());
        List<Entry<String, Double>> stockholmDistances = getSortedZoneDistances(zoneMap, krausTorgZone, stockholmZones);
        combinedZones.addAll(stockholmZones);
        Set<String> sollentunaZones = MunicipalityTest.getSollentunaZones().keySet().stream().filter(
                zoneName -> inDistance(zoneMap, krausTorgZone, maxDistance, zoneName)).collect(Collectors.toSet());
        combinedZones.addAll(sollentunaZones);
        Set<String> tabyZones = MunicipalityTest.getTabyZones().keySet().stream().filter(
                zoneName -> inDistance(zoneMap, krausTorgZone, maxDistance, zoneName)).collect(Collectors.toSet());
        combinedZones.addAll(tabyZones);
        Map<Integer, Zone> zoneIdMap = ZoneUtil.toIdMap(ZonesTest.getAllZones());
        Set<String> extraZones = new HashSet<>();
        extraZones.add(zoneIdMap.get(120).getName());
        extraZones.add(zoneIdMap.get(299).getName());
        extraZones.add(zoneIdMap.get(64100).getName());
        extraZones.add(zoneIdMap.get(94113).getName());
        extraZones.add(zoneIdMap.get(131839).getName());
        extraZones.add(zoneIdMap.get(223822).getName());
        extraZones.add(zoneIdMap.get(680062).getName());
        //        extraZones.add("MillHillField");
        //        extraZones.add("RinkebyAllé");
        //        extraZones.add("Gliaskogen");
        List<Entry<String, Double>> extraDistances = getSortedZoneDistances(zoneMap, krausTorgZone, extraZones);
        combinedZones.addAll(extraZones);
        List<Entry<String, Double>> combinedDistances = getSortedZoneDistances(zoneMap, krausTorgZone, combinedZones);
//        combinedDistances.stream().forEach(stringDoubleEntry -> {
//            if (stringDoubleEntry.getValue() >= 6600d) {
//                System.out.println(stringDoubleEntry.getKey() + "("+ zoneMap.get(stringDoubleEntry.getKey()).getId
//                () +"): " + stringDoubleEntry.getValue());
//            }
//        });
        //        Set<String> restOfStockholmZones = MunicipalityTest.getStockholmZones().keySet();
        //        restOfStockholmZones.removeAll(combinedZones);
        //        List<Entry<String, Double>> restOfStockholmDistances = getSortedZoneDistances(zoneMap,
        //        krausTorgZone, restOfStockholmZones);
        System.out.println("Zones: " + combinedZones.size());
        return combinedZones;
    }

    private static double getMaxDistance(Map<String, Zone> zoneMap, Zone origoZone) throws Exception {
        double solnaDistance = getMaxDistance(zoneMap, origoZone, MunicipalityTest.getSolnaZones().keySet());
        List<Entry<String, Double>> solnaZones = getSortedZoneDistances(zoneMap, origoZone,
                MunicipalityTest.getSolnaZones().keySet());
//        System.out.println("solnaDistance: " + solnaDistance);

        double danderydDistance = getMaxDistance(zoneMap, origoZone, MunicipalityTest.getDanderydZones().keySet());
        List<Entry<String, Double>> danderydZones = getSortedZoneDistances(zoneMap, origoZone,
                MunicipalityTest.getDanderydZones().keySet());
//        System.out.println("danderydDistance: " + danderydDistance);

        double sundbybergDistance = getMaxDistance(zoneMap, origoZone, MunicipalityTest.getSundbybergZones().keySet());
        List<Entry<String, Double>> sundbybergZones = getSortedZoneDistances(zoneMap, origoZone,
                MunicipalityTest.getSundbybergZones().keySet());
//        System.out.println("sundbybergDistance: " + sundbybergDistance);

        return Math.max(Math.max(solnaDistance, danderydDistance), Math.max(sundbybergDistance, 6600.0));
    }

    private static double getMaxDistance(Map<String, Zone> zoneMap, Zone origoZone, Set<String> zoneNames) {
        return zoneNames.stream().mapToDouble(name -> ZoneUtil.calcDistance(origoZone, zoneMap.get(name))).max().orElse(
                0.0);
    }

    private static List<Entry<String, Double>> getSortedZoneDistances(
            Map<String, Zone> zoneMap, Zone origoZone, Set<String> zoneNames) {
        return zoneNames.stream().map(
                name -> new SimpleImmutableEntry<>(name, ZoneUtil.calcDistance(origoZone, zoneMap.get(name)))).sorted(
                Comparator.comparing(Entry::getValue)).collect(Collectors.toList());
    }

    private static boolean inDistance(Map<String, Zone> zoneMap, Zone origoZone, double maxDistance, String zoneName) {
        if (zoneMap.get(zoneName) == null) {
            throw new NullPointerException("Missing zone \"" + zoneName + "\"!");
        }
        return ZoneUtil.calcDistance(origoZone, zoneMap.get(zoneName)) <= maxDistance;
    }

    public static Set<String> getTrueCircleZones() throws Exception {
        Map<String, Zone> zoneMap = ZoneUtil.toNameMap(ZonesTest.getAllZones());
        Zone krausTorgZone = zoneMap.get("KrausTorg");
        double maxDistance = getMaxDistance(zoneMap, krausTorgZone);
        System.out.println("Max distance: " + maxDistance);

        Set<String> circleZones = ZonesTest.getAllZones().stream().filter(
                zone -> zone.getRegion() != null && zone.getRegion().getId() == 141).filter(
                zone -> inDistance(zoneMap, krausTorgZone, maxDistance, zone.getName())).map(Zone::getName).collect(
                Collectors.toSet());
        System.out.println("Zones: " + circleZones.size());
        return circleZones;
    }

    private static void initZoneMaps(List<Map<Zone, Integer>> zoneMaps, HeatmapCategories category) {
        initZoneMaps(zoneMaps, category.takes);
    }

    private static void initZoneMaps(
            List<Map<Zone, Integer>> zoneMaps, HeatmapCategories category, HeatmapCategories nextCategory) {
        initZoneMaps(zoneMaps, IntStream.range(category.getTakes(), nextCategory.getTakes()).toArray());
    }

    private static void initZoneMaps(List<Map<Zone, Integer>> zoneMaps, int... takes) {
        Map<Zone, Integer> map = new HashMap<>();
        for (int take : takes) {
            zoneMaps.add(map);
        }
    }

    //    private void writeHeatmapFolder(Map<Zone, Integer> zoneCounts, String fileName) {
//        if (zoneCounts.isEmpty()) {
//            return;
//        }
//        try (CSVWriter out = new CSVWriter(fileName + ".csv")) {
//            zoneCounts.entrySet().stream()
//                    .sorted(HeatmapTest::compareEntries)
//                    .forEach(zoneCountEntry -> out.writePlacemark(String.format("%d - %s", zoneCountEntry.getValue
//                    (), zoneCountEntry.getKey().getName()),
//                            zoneCountEntry.getKey().getLongitude(), zoneCountEntry.getKey().getLatitude()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
    private static int compareEntries(Entry<Zone, Integer> o1, Entry<Zone, Integer> o2) {
        int countDiff = o1.getValue() - o2.getValue();
        if (countDiff != 0) {
            return countDiff;
        }
        return o1.getKey().getName().compareTo(o2.getKey().getName());
    }

    public static Map<String, Integer> readTakenZones() throws Exception {
        return URLReaderTest.readProperties("warded.unique.php.html", TakenZones::fromHTML);
    }

    public static Map<String, Integer> readLeifonsTakenZones() throws Exception {
        return URLReaderTest.readProperties("Leifons-sthlm-unique.php.html", TakenZones::fromHTML);
    }

    @Test
    public void danderydHeatmap() throws Exception {
        municipalityHeatmap("danderyd_heatmap.kml", readTakenZones(), MunicipalityTest.getDanderydZones().keySet(),
                true);
    }

    @Test
    public void tabyHeatmap() throws Exception {
        municipalityHeatmap("taby_heatmap.kml", readTakenZones(), MunicipalityTest.getTabyZones().keySet(), false);
    }

    @Test
    public void solnaHeatmap() throws Exception {
        municipalityHeatmap("solna_heatmap.kml", readTakenZones(), MunicipalityTest.getSolnaZones().keySet(), true);
    }

    @Test
    public void sundbybergHeatmap() throws Exception {
        municipalityHeatmap("sundbyberg_heatmap.kml", readTakenZones(), MunicipalityTest.getSundbybergZones().keySet(),
                true);
    }

    @Test
    public void dssHeatmap() throws Exception {
        municipalityHeatmap("dss_heatmap.kml", readTakenZones(), getDSSZones(), true);
    }

    @Test
    public void circleHeatmap() throws Exception {
        municipalityHeatmap("circle_heatmap.kml", readTakenZones(), getCircleZones(), true);
    }

    @Test
    public void adjustedCircleHeatmap() throws Exception {
        municipalityHeatmap("adjusted_circle_heatmap.kml", readTakenZones(), getAdjustedCircleZones(), true);
    }

    @Test
    public void trueCircleHeatmap() throws Exception {
        municipalityHeatmap("true_circle_heatmap.kml", readTakenZones(), getTrueCircleZones(), true);
    }

    @Test
    public void combinedMonthlyHeatmap() throws Exception {
        Set<String> combinedZones = getCircleZones();
        Monthly monthly = MonthlyTest.getMonthly();
        Map<String, Integer> monthlyTakenZones = monthly.getZones().stream().filter(
                monthlyZone -> combinedZones.contains(monthlyZone.getName())).collect(
                Collectors.toMap(MonthlyZone::getName, MonthlyZone::getVisits));
        Map<String, Integer> notTakenZones = combinedZones.stream().filter(name -> !monthlyTakenZones.containsKey(name))
                .collect(Collectors.toMap(Function.identity(), name -> 0));
        monthlyTakenZones.putAll(notTakenZones);
        Map<String, Integer> takenZones = readTakenZones().entrySet().stream().filter(
                entry -> combinedZones.contains(entry.getKey())).collect(
                Collectors.toMap(Entry::getKey, Entry::getValue));
        Set<String> filteredZones = combinedZones.stream().filter(
                        entry -> takenZones.get(entry) == null || takenZones.get(entry) - monthlyTakenZones.get(entry) <= 50)
                .collect(Collectors.toSet());
        municipalityHeatmap("monthlyCombinedHeatmap.kml", monthlyTakenZones, filteredZones, false);
    }

    @Test
    public void leifonsSolnaHeatmap() throws Exception {
        municipalityHeatmap("leifons_solna_heatmap.kml", readLeifonsTakenZones(),
                MunicipalityTest.getLeifonsSolnaZones().keySet(), false);
    }

    @Test
    public void leifonsSundbybergHeatmap() throws Exception {
        municipalityHeatmap("leifons_sundbyberg_heatmap.kml", readLeifonsTakenZones(),
                MunicipalityTest.getLeifonsSundbybergZones().keySet(), false);
    }

    @Test
    public void monthlyHeatmap() throws Exception {
        Monthly monthly = MonthlyTest.getMonthly();
        Map<String, Integer> takenZones = monthly.getZones().stream().collect(Collectors.toMap(MonthlyZone::getName,
                monthlyZone -> monthlyZone.getTakes() + monthlyZone.getAssists()));
        municipalityHeatmap("monthlyHeatmap.kml", takenZones, takenZones.keySet(), false);
    }

    @Test
    public void monthlySolnaHeatmap() throws Exception {
        Monthly monthly = MonthlyTest.getMonthly();
        Map<String, Integer> takenZones = monthly.getZones().stream().collect(Collectors.toMap(MonthlyZone::getName,
                monthlyZone -> monthlyZone.getTakes() + monthlyZone.getAssists()));
        municipalityHeatmap("monthlySolnaHeatmap.kml", takenZones, MunicipalityTest.getSolnaZones().keySet(), false);
    }

    private void municipalityHeatmap(
            String filename, Map<String, Integer> takenZones, Set<String> zoneNames, boolean printZones)
            throws Exception {
        List<Zone> allZones = ZonesTest.getAllZones();

        List<Map<Zone, Integer>> zoneMaps = new ArrayList<>(TAKES_ENTRIES);
        Map<String, Integer> zoneMap = new HashMap<>();
        initZoneMaps(zoneMaps, HeatmapCategories.UNTAKEN);
        initZoneMaps(zoneMaps, HeatmapCategories.GREEN);
        initZoneMaps(zoneMaps, HeatmapCategories.YELLOW, HeatmapCategories.ORANGE);
        initZoneMaps(zoneMaps, HeatmapCategories.ORANGE, HeatmapCategories.RED_21);
        initZoneMaps(zoneMaps, HeatmapCategories.RED_21, HeatmapCategories.RED_27);
        initZoneMaps(zoneMaps, HeatmapCategories.RED_27, HeatmapCategories.RED_33);
        initZoneMaps(zoneMaps, HeatmapCategories.RED_33, HeatmapCategories.RED_39);
        initZoneMaps(zoneMaps, HeatmapCategories.RED_39, HeatmapCategories.RED_45);
        initZoneMaps(zoneMaps, HeatmapCategories.RED_45, HeatmapCategories.VIOLET);
        initZoneMaps(zoneMaps, HeatmapCategories.VIOLET);
        int[] zoneTakes = new int[TAKES_ENTRIES];
        int municipalityTakes = 0;

        for (String zoneName : zoneNames) {
            int takes = 0;
            if (takenZones.containsKey(zoneName)) {
                takes = takenZones.get(zoneName);
                municipalityTakes += takes;
            }
            for (Zone zone : allZones) {
                if (zone.getName().equals(zoneName)) {
                    int cappedTakes = Math.min(takes, 51);
                    zoneMaps.get(cappedTakes).put(zone, takes);
                    zoneTakes[cappedTakes] += 1;
                    zoneMap.put(zone.getName(), takes);
                    break;
                }
            }
        }

        int toOrange = countTakes(zoneTakes, WardedCategories.ORANGE);
        int toOrangeZones = countZones(zoneTakes, WardedCategories.ORANGE);
        int toRed = countTakes(zoneTakes, WardedCategories.RED);
        int toRedZones = countZones(zoneTakes, WardedCategories.RED);
        int toViolet = countTakes(zoneTakes, WardedCategories.VIOLET);
        int toVioletZones = countZones(zoneTakes, WardedCategories.VIOLET);

        Map<Zone, Integer> nextZones = new HashMap<>();
        for (int i = 0; i < zoneTakes.length - 1; i += 1) {
            if (zoneTakes[i] >= 10) {
                int next = i;
                int nextMax = Math.min(next + 4, 50);
                for (int j = next; j <= nextMax; j += 1) {
                    Map<Zone, Integer> map = zoneMaps.get(j);

                }
            }
        }

        KMLWriter out = new KMLWriter(filename);
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.UNTAKEN.getTakes()), "untaken");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.GREEN.getTakes()), "green");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.YELLOW.getTakes()), "yellow");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.ORANGE.getTakes()), "orange");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.RED_21.getTakes()), "red 21-26");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.RED_27.getTakes()), "red 27-32");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.RED_33.getTakes()), "red 33-38");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.RED_39.getTakes()), "red 39-44");
        writeHeatmapFolder(out, zoneMaps.get(HeatmapCategories.RED_45.getTakes()), "red 45-50");
        writeHeatmapFolder(out, zoneMaps.get(WardedCategories.VIOLET.getTakes()), "violet");
        out.close();

//        String filenamePrefix = filename.substring(0, filename.indexOf(".kml"));
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.UNTAKEN.getTakes()), filenamePrefix + "_untaken");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.GREEN.getTakes()), filenamePrefix + "_green");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.YELLOW.getTakes()), filenamePrefix + "_yellow");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.ORANGE.getTakes()), filenamePrefix + "_orange");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.RED_21.getTakes()), filenamePrefix + "_red_21-26");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.RED_27.getTakes()), filenamePrefix + "_red_27-32");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.RED_33.getTakes()), filenamePrefix + "_red_33-38");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.RED_39.getTakes()), filenamePrefix + "_red_39-44");
//        writeHeatmapFolder(zoneMaps.get(HeatmapCategories.RED_45.getTakes()), filenamePrefix + "_red_45-50");
//        writeHeatmapFolder(zoneMaps.get(WardedCategories.VIOLET.getTakes()), filenamePrefix + "_violet");
//
        int[][] zoneCountArray = IntStream.range(0, zoneTakes.length).mapToObj(i -> new int[]{ i, zoneTakes[i] })
                .sorted(Comparator.comparingInt(a -> a[1])).toArray(int[][]::new);
        int max = zoneCountArray[51][1];
        if (max % 5 != 0) {
            max = ((max / 5) + 1) * 5;
        }
        int nextToMax = zoneCountArray[50][1];
        if (nextToMax % 5 != 0) {
            nextToMax = ((nextToMax / 5) + 1) * 5;
        }
        for (int i = max; i > 0; i -= 1) {
            if (i % 5 == 0) {
                if (i + 5 == max && i > nextToMax + 5) {
                    System.out.printf("     : %" + (zoneCountArray[51][0] + 1) + "s%n", ":");
                    i = nextToMax;
                    if (i == 0) {
                        continue;
                    }
                }
                System.out.printf("%4d + ", i);
            } else {
                System.out.print("     | ");
            }
            for (int zoneTake : zoneTakes) {
                System.out.print((zoneTake >= i) ? "*" : " ");
            }
            System.out.println();
        }
        System.out.println("     +-+----+----+----+----+----+----+----+----+----+----+-");
        System.out.println("       0    5   10   15   20   25   30   35   40   45   50");

        System.out.println("File:            " + filename);
        System.out.println("Takes to orange: " + toOrange + " (" + toOrangeZones + " zones)");
        System.out.println("Takes to red:    " + toRed + " (" + toRedZones + " zones)");
        System.out.println("Takes to violet: " + toViolet + " (" + toVioletZones + " zones)");
        System.out.println("Total takes:     " + municipalityTakes);

        if (!printZones) {
            return;
        }
        List<Entry<String, Integer>> sortedZones = zoneMap.entrySet().stream().sorted(
                Entry.<String, Integer>comparingByValue().thenComparing(Entry.comparingByKey())).collect(
                Collectors.toList());
        int takes = 0;
        int zones = 0;
        for (Entry<String, Integer> entry : sortedZones) {
            if (entry.getValue() >= WardedCategories.VIOLET.getTakes()) {
                return;
            }
            if (entry.getValue() == takes) {
                System.out.println(entry.getValue() + " - " + entry.getKey());
                zones += 1;
            } else if (zones < 20 || entry.getValue() < WardedCategories.RED.getTakes()) {
                System.out.println(entry.getValue() + " - " + entry.getKey());
                takes = entry.getValue();
                zones += 1;
            } else {
                return;
            }
        }
    }

    private void writeHeatmapFolder(KMLWriter out, Map<Zone, Integer> zoneCounts, String folderName) {
        if (zoneCounts.isEmpty()) {
            return;
        }
        out.writeFolder(folderName);
        zoneCounts.entrySet().stream().sorted(HeatmapTest::compareEntries).forEach(zoneCountEntry -> out.writePlacemark(
                String.format("%d - %s", zoneCountEntry.getValue(), zoneCountEntry.getKey().getName()), "",
                zoneCountEntry.getKey().getLongitude(), zoneCountEntry.getKey().getLatitude()));
    }

    private int countZones(int[] zoneTakes, WardedCategories limit) {
        return IntStream.range(0, limit.getTakes()).map(i -> zoneTakes[i]).sum();
    }

    private int countTakes(int[] zoneTakes, WardedCategories limit) {
        int limitTakes = limit.getTakes();
        return IntStream.range(0, limitTakes).map(i -> (limitTakes - i) * zoneTakes[i]).sum();
    }

    private enum WardedCategories {
        UNTAKEN(0), GREEN(1), YELLOW(2), ORANGE(11), RED(21), VIOLET(51);

        private final int takes;

        WardedCategories(int takes) {
            this.takes = takes;
        }

        public int getTakes() {
            return takes;
        }
    }

    private enum HeatmapCategories {
        UNTAKEN(WardedCategories.UNTAKEN),
        GREEN(WardedCategories.GREEN),
        YELLOW(WardedCategories.YELLOW),
        ORANGE(WardedCategories.ORANGE),
        RED_21(WardedCategories.RED),
        RED_27(WardedCategories.RED, 27),
        RED_33(WardedCategories.RED, 33),
        RED_39(WardedCategories.RED, 39),
        RED_45(WardedCategories.RED, 45),
        VIOLET(WardedCategories.VIOLET);

        private final WardedCategories category;
        private final int takes;

        HeatmapCategories(WardedCategories category) {
            this(category, category.getTakes());
        }

        HeatmapCategories(WardedCategories category, int takes) {
            this.category = category;
            this.takes = takes;
        }

        public WardedCategories getCategory() {
            return category;
        }

        public int getTakes() {
            return takes;
        }
    }
}
