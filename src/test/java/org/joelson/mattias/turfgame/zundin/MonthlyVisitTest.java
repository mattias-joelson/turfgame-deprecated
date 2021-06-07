package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.joelson.mattias.turfgame.warded.HeatmapTest;
import org.joelson.mattias.turfgame.warded.TakenZoneTest;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class MonthlyVisitTest {

    private static final String OBEROFF = "Oberoff";
    private static final int ROUND = 119;
    
    @Test
    public void visitDanderydTest() throws Exception {
        visitMunicipalityTest("Danderyd", "danderyd_month.kml", MunicipalityTest.getDanderydZones().keySet());
    }

    @Test
    public void visitJarfallaTest() throws Exception {
        visitMunicipalityTest("Järfälla", "jarfalla_month.kml", MunicipalityTest.getJarfallaZones().keySet());
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
    public void combinedNonSnurrTest() throws Exception {
        Set<String> combinedZones = HeatmapTest.getDSSZones();
        List<Integer> remainingZones = MissionTest.readSolnaSnurrZones();
        remainingZones = remainingZones.subList(185, remainingZones.size());
        List<Zone> zones1 = ZonesTest.getAllZones();
        Map<Integer, Zone> zoneMap = zones1.stream()
                .collect(Collectors.toMap(Zone::getId, Function.identity()));
        remainingZones.forEach(id -> combinedZones.remove(zoneMap.get(id).getName()));
        HeatmapTest.readTakenZones().entrySet().stream()
                .filter(entry -> combinedZones.contains(entry.getKey()))
                .filter(entry -> entry.getValue() >= 51)
                .forEach(entry -> combinedZones.remove(entry.getKey()));

        visitMunicipalityTest("NonSnurr", "dss_nonsnurr.kml", combinedZones);
    }

    private static void visitMunicipalityTest(String municipality, String filename, Set<String> municipalityZones) throws Exception {
        visitMunicipalityTest(municipality, filename, municipalityZones, false);
    }

    private static void visitMunicipalityTest(String municipality, String filename, Set<String> municipalityZones, boolean partitionUnvisited) throws Exception {
        Monthly monthly = getMonthly();
        List<Zone> zones = ZonesTest.getAllZones();
    
        Set<String> monthlyVisits = monthly.getZones().stream().map(MonthlyZone::getName).collect(Collectors.toSet());
        
        Set<String> visited = municipalityZones.stream()
                .filter(monthlyVisits::contains)
                .collect(Collectors.toSet());
        Set<String> unvisited = municipalityZones.stream()
                .filter(zoneName -> !monthlyVisits.contains(zoneName))
                .collect(Collectors.toSet());
        Set<String> unvisitedPurple = new HashSet<>();
        if (partitionUnvisited) {
            Map<String, Integer> takenZones = TakenZoneTest.readTakenZones();
            unvisited.stream()
                    .filter(takenZones::containsKey)
                    .filter(zoneName -> takenZones.get(zoneName) > 50)
                    .forEach(unvisitedPurple::add);
            unvisited.removeAll(unvisitedPurple);
        }

        Set<Zone> visitedZones = zones.stream()
                .filter(zone -> visited.contains(zone.getName()))
                .collect(Collectors.toSet());
        Set<Zone> unvisitedZones = zones.stream()
                .filter(zone -> unvisited.contains(zone.getName()))
                .collect(Collectors.toSet());
        Set<Zone> unvisitedPurpleZones = zones.stream()
                .filter(zone -> unvisitedPurple.contains(zone.getName()))
                .collect(Collectors.toSet());
    
        try (KMLWriter out = new KMLWriter(filename)) {
            out.writeFolder(municipality + " unvisited");
            unvisitedZones.stream()
                    .sorted(Comparator.comparing(Zone::getName))
                    .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
            if (!unvisitedPurple.isEmpty()) {
                out.writeFolder(municipality + " unvisited purple");
                unvisitedPurpleZones.stream()
                        .sorted(Comparator.comparing(Zone::getName))
                        .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
            }
            out.writeFolder(municipality + " visited");
            visitedZones.stream()
                    .sorted(Comparator.comparing(Zone::getName))
                    .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
        }
        System.out.println(filename + ": " + (unvisitedZones.size() + visitedZones.size()) + " (" + municipalityZones.size() + ')');
        assertEquals(municipalityZones.size(), unvisitedZones.size() + unvisitedPurpleZones.size() + visitedZones.size());
    }

    private static Monthly getMonthly() throws Exception {
        //return readProperties("monthly_oberoff_round96.html");
        return readProperties("monthly_0beroff_round131_fairy.html");
    }
    
    private static Monthly readProperties(String resource) throws Exception {
        return URLReaderTest.readProperties(resource, s -> Monthly.fromHTML(OBEROFF, ROUND, s));
    }
}
