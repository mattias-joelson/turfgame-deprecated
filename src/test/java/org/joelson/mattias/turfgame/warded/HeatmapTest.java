package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.joelson.mattias.turfgame.zundin.Monthly;
import org.joelson.mattias.turfgame.zundin.MonthlyTest;
import org.joelson.mattias.turfgame.zundin.MonthlyZone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HeatmapTest {

    private static final int TAKES_ENTRIES = HeatmapCategories.VIOLET.getTakes() + 1;
    
    private static Boolean alwaysTrue(String s) {
        return Boolean.TRUE;
    }
    
    private enum WardedCategories {
        UNTAKEN(0),
        GREEN(1),
        YELLOW(2),
        ORANGE(11),
        RED(21),
        VIOLET(51);
    
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

    @Test
    public void danderydHeatmap() throws Exception {
        municipalityHeatmap("danderyd_heatmap.kml", readTakenZones(), MunicipalityTest.getDanderydZones(), true);
    }

    @Test
    public void tabyHeatmap() throws Exception {
        municipalityHeatmap("taby_heatmap.kml", readTakenZones(), MunicipalityTest.getTabyZones(), false);
    }

    @Test
    public void solnaHeatmap() throws Exception {
        municipalityHeatmap("solna_heatmap.kml", readTakenZones(), MunicipalityTest.getSolnaZones(), true);
    }

    @Test
    public void sundbybergHeatmap() throws Exception {
        municipalityHeatmap("sundbyberg_heatmap.kml", readTakenZones(), MunicipalityTest.getSundbybergZones(), true);
    }

    @Test
    public void combinedHeatmap() throws Exception {
        Map<String, Boolean> combinedZones = MunicipalityTest.getSolnaZones();
        combinedZones.putAll(MunicipalityTest.getDanderydZones());
        combinedZones.putAll(MunicipalityTest.getSundbybergZones());
        municipalityHeatmap("dss_heatmap.kml", readTakenZones(), combinedZones, true);
    }

    @Test
    public void combinedMonthlyHeatmap() throws Exception {
        Map<String, Boolean> combinedZones = MunicipalityTest.getSolnaZones();
        combinedZones.putAll(MunicipalityTest.getDanderydZones());
        combinedZones.putAll(MunicipalityTest.getSundbybergZones());
        Monthly monthly = MonthlyTest.getMonthly();
        Map<String, Integer> monthlyTakenZones = monthly.getZones().stream()
                .filter(monthlyZone -> combinedZones.containsKey(monthlyZone.getName()))
                .collect(Collectors.toMap(MonthlyZone::getName, MonthlyZone::getVisits));
        Map<String, Integer> notTakenZones = combinedZones.keySet().stream()
                .filter(name -> !monthlyTakenZones.containsKey(name))
                .collect(Collectors.toMap(Function.identity(), name -> 0));
        monthlyTakenZones.putAll(notTakenZones);
        Map<String, Integer> takenZones = readTakenZones().entrySet().stream()
                .filter(entry -> combinedZones.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        Map<String, Boolean> filteredZones = combinedZones.entrySet().stream()
                .filter(entry -> takenZones.get(entry.getKey()) == null || takenZones.get(entry.getKey()) - monthlyTakenZones.get(entry.getKey()) <= 50)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        municipalityHeatmap("monthlyCombinedHeatmap.kml", monthlyTakenZones, filteredZones, false);
    }
    
    @Test
    public void leifonsSolnaHeatmap() throws Exception {
        municipalityHeatmap("leifons_solna_heatmap.kml", readLeifonsTakenZones(), MunicipalityTest.getLeifonsSolnaZones(), false);
    }

    @Test
    public void leifonsSundbybergHeatmap() throws Exception {
        municipalityHeatmap("leifons_sundbyberg_heatmap.kml", readLeifonsTakenZones(), MunicipalityTest.getLeifonsSundbybergZones(), false);
    }
    
    @Test
    public void monthlyHeatmap() throws Exception {
        Monthly monthly = MonthlyTest.getMonthly();
        Map<String, Integer> takenZones = monthly.getZones().stream()
                .collect(Collectors.toMap(MonthlyZone::getName, monthlyZone -> monthlyZone.getTakes() + monthlyZone.getAssists()));
        Map<String, Boolean> municipalityZones = takenZones.keySet().stream().collect(Collectors.toMap(Function.identity(), HeatmapTest::alwaysTrue));
        municipalityHeatmap("monthlyHeatmap.kml", takenZones, municipalityZones, false);
    }
    
    @Test
    public void monthlySolnaHeatmap() throws Exception {
        Monthly monthly = MonthlyTest.getMonthly();
        Map<String, Integer> takenZones = monthly.getZones().stream()
                .collect(Collectors.toMap(MonthlyZone::getName, monthlyZone -> monthlyZone.getTakes() + monthlyZone.getAssists()));
        Map<String, Boolean> municipalityZones = MunicipalityTest.getSolnaZones();
        municipalityHeatmap("monthlySolnaHeatmap.kml", takenZones, municipalityZones, false);
    }

    private void municipalityHeatmap(String filename, Map<String, Integer> takenZones, Map<String, Boolean> municipalityZones, boolean printZones)
            throws Exception {
        List<Zone> allZones = ZonesTest.getAllZones();
        //Map<String, Zone> allZones = ZonesTest.getAllZones().stream().collect(Collectors.toMap(Zone::getName, Function.identity()));

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

        for (String zoneName : municipalityZones.keySet()) {
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
//            Zone zone = allZones.get(zoneName);
//            if (zone != null) {
//                int cappedTakes = Math.min(takes, 51);
//                zoneMaps.get(cappedTakes).put(zone, takes);
//                zoneTakes[cappedTakes] += 1;
//                zoneMap.put(zone.getName(), takes);
//            } else {
//                System.out.println("Error");
//            }
        }
    
        int toOrange = countTakes(zoneTakes, WardedCategories.ORANGE);
        int toOrangeZones = countZones(zoneTakes, WardedCategories.ORANGE);
        int toRed = countTakes(zoneTakes, WardedCategories.RED);
        int toRedZones = countZones(zoneTakes, WardedCategories.RED);
        int toViolet = countTakes(zoneTakes, WardedCategories.VIOLET);
        int toVioletZones = countZones(zoneTakes, WardedCategories.VIOLET);

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

        int[][] zoneCountArray = IntStream.range(0, zoneTakes.length)
                .mapToObj(i -> new int[] { i, zoneTakes[i]}).sorted(Comparator.comparingInt(a -> a[1])).toArray(int[][]::new);
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
                    System.out.println(String.format("     : %" + (zoneCountArray[51][0] + 1) + 's', ":"));
                    i = nextToMax;
                    if (i == 0) {
                        continue;
                    }
                }
                System.out.print(String.format("%4d + ", i));
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

        System.out.println("Municipality:    " + filename);
        System.out.println("Takes to orange: " + toOrange + " (" + toOrangeZones + " zones)");
        System.out.println("Takes to red:    " + toRed + " (" + toRedZones + " zones)");
        System.out.println("Takes to violet: " + toViolet + " (" + toVioletZones + " zones)");
        System.out.println("Total takes:     " + municipalityTakes);

        if (!printZones) {
            return;
        }
        List<Entry<String, Integer>> sortedZones = zoneMap.entrySet().stream()
                .sorted(Entry.<String, Integer>comparingByValue().thenComparing(Entry.comparingByKey())).collect(Collectors.toList());
        int takes = 0;
        int zones = 0;
        for (Entry<String, Integer> entry : sortedZones) {
            if (entry.getValue() >= WardedCategories.VIOLET.getTakes()) {
                return;
            }
            if (entry.getValue() == takes) {
                System.out.println(entry.getValue() + " - " + entry.getKey());
                zones += 1;
            } else if (zones < 10 || entry.getValue() < WardedCategories.RED.getTakes()) {
                System.out.println(entry.getValue() + " - " + entry.getKey());
                takes = entry.getValue();
                zones += 1;
            } else {
                return;
            }
        }
    }
    
    private static void initZoneMaps(List<Map<Zone, Integer>> zoneMaps, HeatmapCategories category) {
        initZoneMaps(zoneMaps, category.takes);
    }
    
    private static void initZoneMaps(List<Map<Zone, Integer>> zoneMaps, HeatmapCategories category, HeatmapCategories nextCategory) {
        initZoneMaps(zoneMaps, IntStream.range(category.getTakes(), nextCategory.getTakes()).toArray());
    }
    
    private static void initZoneMaps(List<Map<Zone, Integer>> zoneMaps, int... takes) {
        Map<Zone, Integer> map = new HashMap<>();
        for (int take : takes) {
            zoneMaps.add(map);
        }
    }
    
    private void writeHeatmapFolder(KMLWriter out, Map<Zone, Integer> zoneCounts, String folderName) {
        if (zoneCounts.isEmpty()) {
            return;
        }
        out.writeFolder(folderName);
        zoneCounts.entrySet().stream()
                .sorted(HeatmapTest::compareEntries)
                .forEach(zoneCountEntry -> out.writePlacemark(String.format("%d - %s", zoneCountEntry.getValue(), zoneCountEntry.getKey().getName()),
                        "", zoneCountEntry.getKey().getLongitude(), zoneCountEntry.getKey().getLatitude()));
    }

    private static int compareEntries(Entry<Zone, Integer> o1, Entry<Zone, Integer> o2) {
        int countDiff = o1.getValue() - o2.getValue();
        if (countDiff != 0) {
            return  countDiff;
        }
        return o1.getKey().getName().compareTo(o2.getKey().getName());
    }

    private int countZones(int[] zoneTakes, WardedCategories limit) {
        return IntStream.range(0, limit.getTakes())
                .map(i -> zoneTakes[i])
                .sum();
    }
    
    private int countTakes(int[] zoneTakes, WardedCategories limit) {
        int limitTakes = limit.getTakes();
        return IntStream.range(0, limitTakes)
                .map(i -> (limitTakes - i) * zoneTakes[i])
                .sum();
    }
    
    public static Map<String, Integer> readTakenZones() throws Exception {
        return URLReaderTest.readProperties("warded.unique.php.html", TakenZones::fromHTML);
    }

    public static Map<String, Integer> readLeifonsTakenZones() throws Exception {
        return URLReaderTest.readProperties("Leifons-sthlm-unique.php.html", TakenZones::fromHTML);
    }
}
