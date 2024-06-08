package org.joelson.turf.zundin;

import org.joelson.turf.lundkvist.MunicipalityTest;
import org.joelson.turf.turfgame.apiv4.Zone;
import org.joelson.turf.turfgame.apiv4.ZonesTest;
import org.joelson.turf.util.KMLWriter;
import org.joelson.turf.warded.HeatmapTest;
import org.joelson.turf.warded.TakenZoneTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonthlyVisitTest {

    private static final String OBEROFF = "Oberoff";
    private static final int ROUND = 119;

    private static void visitMunicipalityTest(String municipality, String filename, Set<String> municipalityZones)
            throws Exception {
        visitMunicipalityTest(municipality, filename, municipalityZones, false);
    }

    private static void visitMunicipalityTest(
            String municipality, String filename, Set<String> municipalityZones, boolean partitionUnvisited)
            throws Exception {
        Monthly monthly = MonthlyTest.getMonthly();
        List<Zone> zones = ZonesTest.getAllZones();

        Set<String> monthlyVisits = monthly.getZones().stream().map(MonthlyZone::getName).collect(Collectors.toSet());

        Set<String> visited = municipalityZones.stream().filter(monthlyVisits::contains).collect(Collectors.toSet());
        Set<String> unvisited = municipalityZones.stream().filter(zoneName -> !monthlyVisits.contains(zoneName))
                .collect(Collectors.toSet());
        Set<String> unvisitedPurple = new HashSet<>();
        if (partitionUnvisited) {
            Map<String, Integer> takenZones = TakenZoneTest.readTakenZones();
            unvisited.stream().filter(takenZones::containsKey).filter(zoneName -> takenZones.get(zoneName) > 50)
                    .forEach(unvisitedPurple::add);
            unvisited.removeAll(unvisitedPurple);
        }

        Set<Zone> visitedZones = zones.stream().filter(zone -> visited.contains(zone.getName())).collect(
                Collectors.toSet());
        Set<Zone> unvisitedZones = zones.stream().filter(zone -> unvisited.contains(zone.getName())).collect(
                Collectors.toSet());
        Set<Zone> unvisitedPurpleZones = zones.stream().filter(zone -> unvisitedPurple.contains(zone.getName()))
                .collect(Collectors.toSet());

        try (KMLWriter out = new KMLWriter(filename)) {
            out.writeFolder(municipality + " unvisited");
            unvisitedZones.stream().sorted(Comparator.comparing(Zone::getName)).forEach(
                    zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
            if (!unvisitedPurple.isEmpty()) {
                out.writeFolder(municipality + " unvisited purple");
                unvisitedPurpleZones.stream().sorted(Comparator.comparing(Zone::getName)).forEach(
                        zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
            }
            out.writeFolder(municipality + " visited");
            visitedZones.stream().sorted(Comparator.comparing(Zone::getName)).forEach(
                    zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
        }
        System.out.println(
                filename + ": " + (unvisitedZones.size() + visitedZones.size()) + " (" + municipalityZones.size()
                        + ')');
        assertEquals(municipalityZones.size(),
                unvisitedZones.size() + unvisitedPurpleZones.size() + visitedZones.size());

//        String filePrefix = filename.substring(0, filename.indexOf(".kml"));
//        try (CSVWriter out = new CSVWriter(filePrefix + "_unvisited.csv")) {
//            unvisitedZones.stream()
//                    .sorted(Comparator.comparing(Zone::getName))
//                    .forEach(zone -> out.writePlacemark(zone.getName(), zone.getLongitude(), zone.getLatitude()));
//        }
//        try (CSVWriter out = new CSVWriter(filePrefix + "_unvisited_purple.csv")) {
//            unvisitedPurpleZones.stream()
//                    .sorted(Comparator.comparing(Zone::getName))
//                    .forEach(zone -> out.writePlacemark(zone.getName(), zone.getLongitude(), zone.getLatitude()));
//        }
//        try (CSVWriter out = new CSVWriter(filePrefix + "_visited.csv")) {
//            visitedZones.stream()
//                    .sorted(Comparator.comparing(Zone::getName))
//                    .forEach(zone -> out.writePlacemark(zone.getName(), zone.getLongitude(), zone.getLatitude()));
//        }
    }

    @Test
    public void visitDanderydTest() throws Exception {
        visitMunicipalityTest("Danderyd", "danderyd_month.kml", MunicipalityTest.getDanderydZones().keySet());
    }

    @Test
    public void visitSolnaTest() throws Exception {
        visitMunicipalityTest("Solna", "solna_month.kml", MunicipalityTest.getSolnaZones().keySet());
    }

    @Test
    public void visitSollentunaTest() throws Exception {
        visitMunicipalityTest("Sollentuna", "sollentuna_month.kml", MunicipalityTest.getSollentunaZones().keySet());
    }

    @Test
    public void visitStockholmTest() throws Exception {
        visitMunicipalityTest("Stockholm", "stockholm_month.kml", MunicipalityTest.getStockholmZones().keySet());
    }

    @Test
    public void visitSundbybergTest() throws Exception {
        visitMunicipalityTest("Sundbyberg", "sundbyberg_month.kml", MunicipalityTest.getSundbybergZones().keySet());
    }

    @Test
    public void visitTäbyTest() throws Exception {
        visitMunicipalityTest("Täby", "taby_month.kml", MunicipalityTest.getTabyZones().keySet());
    }

    @Test
    public void combinedDSSVisitTest() throws Exception {
        visitMunicipalityTest("DSS", "dss_month.kml", HeatmapTest.getDSSZones());
    }

    @Test
    public void combinedCircleVisitTest() throws Exception {
        visitMunicipalityTest("circle", "circle_month.kml", HeatmapTest.getCircleZones(), true);
    }

    @Test
    public void combinedTrueCircleVisitTest() throws Exception {
        visitMunicipalityTest("true_circle", "true_circle_month.kml", HeatmapTest.getTrueCircleZones(), true);
    }

    @Test
    public void combinedFlippVisitTest() throws Exception {
        visitMunicipalityTest("flipp", "flipp_month.kml", Flipp08MissionTest.getFlippZones(), true);
    }

    @Test
    public void combinedCircleVisitHeatmapTest() throws Exception {
        Set<String> circleZones = HeatmapTest.getCircleZones();
        Map<String, Integer> takesZones = HeatmapTest.readTakenZones();
        Map<String, Integer> monthlyVisits = MonthlyTest.getMonthly().getZones().stream().collect(
                Collectors.toMap(MonthlyZone::getName, MonthlyZone::getVisits));
        List<Zone> zones = ZonesTest.getAllZones();
        Map<String, Zone> zoneMap = new HashMap<>();
        zones.forEach(zone -> zoneMap.put(zone.getName(), zone));

        List<CombinedVisitZone> untakenZones = new ArrayList<>();
        List<CombinedVisitZone> yellowZones = new ArrayList<>();
        List<CombinedVisitZone> orangeZones = new ArrayList<>();
        List<CombinedVisitZone> redZones = new ArrayList<>();
        List<CombinedVisitZone> visitedOncePurpleZones = new ArrayList<>();
        List<CombinedVisitZone> visitedPurpleZones = new ArrayList<>();
        List<CombinedVisitZone> unvisitedPurpleZones = new ArrayList<>();
        int numberVisitedOnceZones = 0;
        int numberVisitedZones = 0;

        for (String zoneName : circleZones) {
            Zone zone = zoneMap.get(zoneName);
            if (takesZones.containsKey(zoneName)) {
                int takes = takesZones.get(zoneName);
                int visits = (monthlyVisits.containsKey(zoneName)) ? monthlyVisits.get(zoneName) : 0;
                if (takes < 11) {
                    yellowZones.add(new CombinedVisitZone(zone, takes, visits));
                } else if (takes < 21) {
                    orangeZones.add(new CombinedVisitZone(zone, takes, visits));
                } else if (takes < 51) {
                    redZones.add(new CombinedVisitZone(zone, takes, visits));
                } else if (visits >= 2) {
                    visitedPurpleZones.add(new CombinedVisitZone(zone, takes, visits));
                } else if (visits == 1) {
                    visitedOncePurpleZones.add(new CombinedVisitZone(zone, takes, visits));
                } else {
                    unvisitedPurpleZones.add(new CombinedVisitZone(zone, takes, visits));
                }
                if (visits > 0) {
                    numberVisitedOnceZones += 1;
                    if (visits > 1) {
                        numberVisitedZones += 1;
                    }
                }
            } else {
                untakenZones.add(new CombinedVisitZone(zone, 0, 0));
            }
        }
        System.out.println("Zones visited:                " + numberVisitedOnceZones);
        System.out.println("Zones visited more than once: " + numberVisitedZones);

        KMLWriter out = new KMLWriter("circle_combined_month.kml");
        if (!untakenZones.isEmpty()) {
            out.writeFolder("Untaken Zones");
            untakenZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        if (!yellowZones.isEmpty()) {
            out.writeFolder("Yellow Zones");
            yellowZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        if (!orangeZones.isEmpty()) {
            out.writeFolder("Orange Zones");
            orangeZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        if (!redZones.isEmpty()) {
            out.writeFolder("Red Zones");
            redZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        if (!unvisitedPurpleZones.isEmpty()) {
            out.writeFolder("Unvisited Purple Zones");
            unvisitedPurpleZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        if (!visitedOncePurpleZones.isEmpty()) {
            out.writeFolder("Visited Once Purple Zones");
            visitedOncePurpleZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        if (!visitedPurpleZones.isEmpty()) {
            out.writeFolder("Visited Purple Zones");
            visitedPurpleZones.stream().sorted().forEach(zone -> zone.write(out));
        }
        out.close();
    }

    private static class CombinedVisitZone implements Comparable<CombinedVisitZone> {
        private final Zone zone;
        private final int takes;
        private final int visits;

        private CombinedVisitZone(Zone zone, int takes, int visits) {
            this.zone = zone;
            this.takes = takes;
            this.visits = visits;
        }

        @Override
        public int compareTo(CombinedVisitZone that) {
            return (this.takes == that.takes) ? this.zone.getName().compareTo(that.zone.getName()) :
                    this.takes - that.takes;
        }

        public void write(KMLWriter out) {
            out.writePlacemark(String.format("%d - %s%s", takes, zone.getName(),
                            (visits > 0) ? " (" + visits + " visits)" : " (unvisited)"), "", zone.getLongitude(),
                    zone.getLatitude());
        }
    }
}
