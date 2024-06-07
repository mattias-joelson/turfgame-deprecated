package org.joelson.mattias.turfgame.warded;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.ZoneUtil;
import org.joelson.mattias.turfgame.zundin.MonthlyTest;
import org.joelson.mattias.turfgame.zundin.MonthlyZone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TakeDistributionTest {

    private static final int PURPLE_LIMIT = 51;

    private static class ZoneTakeDistribution {
        private final Zone zone;
        private final int monthlyVisits;
        private final int visits;
        private final float percentage;

        private ZoneTakeDistribution(Zone zone, int monthlyVisits, int visits, float percentage) {
            this.zone = zone;
            this.monthlyVisits = monthlyVisits;
            this.visits = visits;
            this.percentage = percentage;
        }

        public Zone getZone() {
            return zone;
        }

        public int getMonthlyVisits() {
            return monthlyVisits;
        }

        public int getVisits() {
            return visits;
        }

        public float getPercentage() {
            return percentage;
        }

        public String toKMLPlacemarkNameString() {
            return String.format("%s - %d - %d - %.2f%%", zone.getName(), visits, monthlyVisits, percentage);
        }

        public String toCSVPlacemarkNameString() {
            return String.format(Locale.ENGLISH, "%s - %d - %d - %.2f%%", zone.getName(), visits, monthlyVisits, percentage);
        }
    }

    @Test
    public void circleTakeDistributionTest() throws Exception {
        createDistributionMap("circle_distribution.kml", HeatmapTest.getCircleZones(), true);
    }

    private static void createDistributionMap(String filename, Set<String> zoneNames, boolean printZones) throws Exception {
        Map<String, Zone> zoneNameMap = ZoneUtil.toNameMap(ZonesTest.getAllZones());
        Map<String, MonthlyZone> monthlyNameMap = toNameMap(MonthlyTest.getMonthly().getZones());
        Map<String, Integer> visitNameMap = TakenZoneTest.readTakenZones();
        List<ZoneTakeDistribution> distributionList = new ArrayList<>(zoneNames.size());

        for (String zoneName : zoneNames) {
            Zone zone = zoneNameMap.get(zoneName);
            MonthlyZone monthlyZone = monthlyNameMap.get(zoneName);
            int visits = (visitNameMap.containsKey(zoneName)) ? visitNameMap.get(zoneName) : 0;
            if (visits >= PURPLE_LIMIT) {
                continue;
            }
            int monthlyVisits = (monthlyZone != null) ? monthlyZone.getVisits() : 0;
            int startVisits = visits - monthlyVisits;
            int visitsRemained = PURPLE_LIMIT - startVisits;
            if (visitsRemained > 0) {
                float percentage = Math.min(100.0f * monthlyVisits / visitsRemained, 100.0f);
                distributionList.add(new ZoneTakeDistribution(zone, monthlyVisits, visits, percentage));
            }
        }

        int currentCalc = findNextCurrent(distributionList);
        int nextMaxVisitsCalc = distributionList.stream()
                .filter(zoneTakeDistribution ->  zoneTakeDistribution.getVisits() == currentCalc)
                .mapToInt(ZoneTakeDistribution::getMonthlyVisits)
                .filter(value -> value > 0)
                .min().orElse(0);
        System.out.println("currentCalc=" + currentCalc);
        System.out.println("nextMaxVisitsCalc=" + nextMaxVisitsCalc);
//        System.out.println("*** Using calc!");
//        int current = currentCalc;
//        int nextMaxVisits = nextMaxVisitsCalc;
        System.out.println("*** Using constants!");
        int current = 51;
        int nextMaxVisits = 7;
        System.out.println("current=" + current);
        System.out.println("nextMaxVisits=" + nextMaxVisits);
        int takesStart = current - nextMaxVisits;
        float cut = 100.0f * nextMaxVisits / (51 - takesStart);
        float cutPlus = 100.0f * (nextMaxVisits + 1) / (51 - takesStart);
        float doneRatio = 700.0f / 7.0f;//25.0f;//700.0f / 30.0f; // FIXME 7 / 31 * 100
        System.out.println("doneRatio=" + doneRatio);

        Set<ZoneTakeDistribution> below = new HashSet<>();
        Set<ZoneTakeDistribution> next = new HashSet<>();
        Set<ZoneTakeDistribution> above = new HashSet<>();
        Set<ZoneTakeDistribution> done = new HashSet<>();

        distributionList.forEach(zoneTakeDistribution -> {
            if (zoneTakeDistribution.getPercentage() < cut || zoneTakeDistribution.getVisits() < current) {
                below.add(zoneTakeDistribution);
            } else if (zoneTakeDistribution.getPercentage() < cutPlus || zoneTakeDistribution.getVisits() < current + 5) {
                next.add(zoneTakeDistribution);
            } else if (zoneTakeDistribution.getPercentage() < doneRatio) {
                above.add(zoneTakeDistribution);
            } else {
                done.add(zoneTakeDistribution);
            }
        });

        KMLWriter out = new KMLWriter(filename);
        writeDistributionSet(out, below, "below");
        writeDistributionSet(out, next, "next");
        writeDistributionSet(out, above, "above");
        writeDistributionSet(out, done, "done");
        out.close();

//        String filePrefix = filename.substring(0, filename.indexOf(".kml"));
//        writeDistributionSet(below, filePrefix + "_below");
//        writeDistributionSet(next, filePrefix + "_next");
//        writeDistributionSet(above, filePrefix + "_above");
//        writeDistributionSet(done, filePrefix + "_done");
//
        if (printZones) {
            System.out.println("cut: " + cut);
            System.out.println("cutPlus: " + cutPlus);
            distributionList.stream()
                    .filter(zoneTakeDistribution -> zoneTakeDistribution.getPercentage() < cutPlus || zoneTakeDistribution.getVisits() < current)
                    .sorted(Comparator.comparing(ZoneTakeDistribution::getVisits)
                            .thenComparing(zoneTakeDistribution -> zoneTakeDistribution.getZone().getName()))
                    .map(ZoneTakeDistribution::toKMLPlacemarkNameString)
                    .forEach(System.out::println);
        }
    }

    private static int findNextCurrent(List<ZoneTakeDistribution> distributionList) {
        int[] zoneCount = new int[52];
        distributionList.forEach(zoneTakeDistribution -> zoneCount[Math.min(51, zoneTakeDistribution.getVisits())] += 1);
        int last = 0;
        for (int step = 0; step < 51; step += 1) {
            if (zoneCount[step] == 0) {
                last = step + 1;
            } else if (step >= last + 4) {
                return last;
            }
        }
        return distributionList.stream()
                .filter(zoneTakeDistribution -> zoneTakeDistribution.getMonthlyVisits() > 0)
                .mapToInt(ZoneTakeDistribution::getVisits)
                .min().orElseThrow();
    }

    private static Map<String, MonthlyZone> toNameMap(List<MonthlyZone> monthlyZones) {
        Map<String, MonthlyZone> monthlyNameMap = new HashMap<>(monthlyZones.size());
        monthlyZones.forEach(zone -> monthlyNameMap.put(zone.getName(), zone));
        return monthlyNameMap;
    }

    private static void writeDistributionSet(KMLWriter out, Set<ZoneTakeDistribution> zones, String name) {
        if (zones.isEmpty()) {
            return;
        }
        out.writeFolder(name);
        zones.stream()
                .sorted(Comparator.comparing(ZoneTakeDistribution::getVisits)
                        .thenComparing(zoneTakeDistribution -> zoneTakeDistribution.getZone().getName()))
                .forEach(zoneTakeDistribution -> writeZone(out, zoneTakeDistribution));
    }

    private static void writeZone(KMLWriter out, ZoneTakeDistribution zoneTakeDistribution) {
        out.writePlacemark(
                zoneTakeDistribution.toKMLPlacemarkNameString(),
                "",
                zoneTakeDistribution.getZone().getLongitude(),
                zoneTakeDistribution.getZone().getLatitude());
    }
//
//    private static void writeDistributionSet(Set<ZoneTakeDistribution> zones, String fileName) {
//        if (zones.isEmpty()) {
//            return;
//        }
//        try (CSVWriter out = new CSVWriter(fileName + ".csv")) {
//            zones.stream()
//                    .sorted(Comparator.comparing(ZoneTakeDistribution::getVisits)
//                            .thenComparing(zoneTakeDistribution -> zoneTakeDistribution.getZone().getName()))
//                    .forEach(zoneTakeDistribution -> writeZone(out, zoneTakeDistribution));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void writeZone(CSVWriter out, ZoneTakeDistribution zoneTakeDistribution) {
//        out.writePlacemark(
//                zoneTakeDistribution.toCSVPlacemarkNameString(),
//                zoneTakeDistribution.getZone().getLongitude(),
//                zoneTakeDistribution.getZone().getLatitude());
//    }
}
