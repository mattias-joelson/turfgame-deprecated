package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class MonthlyVisitTest {

    private static final String OBEROFF = "Oberoff";
    private static final int ROUND = 114;
    
    @Test
    public void visitDanderydTest() throws Exception {
        visitMunicipalityTest("Danderyd", "danderyd_month.kml", MunicipalityTest.getDanderydZones());
    }
    
    @Test
    public void visitSolnaTest() throws Exception {
        visitMunicipalityTest("Solna", "solna_month.kml", MunicipalityTest.getSolnaZones());
    }
    
    @Test
    public void visitStockholmTest() throws Exception {
        visitMunicipalityTest("Stockholm", "stockholm_month.kml", MunicipalityTest.getStockholmZones());
    }

    @Test
    public void visitSundbybergTest() throws Exception {
        visitMunicipalityTest("Sundbyberg", "sundbyberg_month.kml", MunicipalityTest.getSundbybergZones());
    }

    private static void visitMunicipalityTest(String municipality, String filename, Map<String, Boolean> municipalityZones) throws Exception {
        Monthly monthly = getMonthly();
        List<Zone> zones = ZonesTest.getAllZones();
    
        Set<String> monthlyVisits = monthly.getZones().stream().map(MonthlyZone::getName).collect(Collectors.toSet());
        
        Set<String> visited = municipalityZones.keySet().stream()
                .filter(monthlyVisits::contains)
                .collect(Collectors.toSet());
        Set<String> unvisited = municipalityZones.keySet().stream()
                .filter(zoneName -> !monthlyVisits.contains(zoneName))
                .collect(Collectors.toSet());

        Set<Zone> visitedZones = zones.stream()
                .filter(zone -> visited.contains(zone.getName()))
                .collect(Collectors.toSet());
        Set<Zone> unvisitedZones = zones.stream()
                .filter(zone -> unvisited.contains(zone.getName()))
                .collect(Collectors.toSet());
    
        try (KMLWriter out = new KMLWriter(filename)) {
            out.writeFolder(municipality + " unvisited");
            unvisitedZones.stream()
                    .sorted(Comparator.comparing(Zone::getName))
                    .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
            out.writeFolder(municipality + " visited");
            visitedZones.stream()
                    .sorted(Comparator.comparing(Zone::getName))
                    .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
        }
        System.out.println(filename + ": " + (unvisitedZones.size() + visitedZones.size()) + " (" + municipalityZones.size() + ')');
        assertEquals(municipalityZones.size(), unvisitedZones.size() + visitedZones.size());
    }
    
    private static Monthly getMonthly() throws Exception {
        //return readProperties("monthly_oberoff_round96.html");
        return readProperties("monthly_oberoff_round115.html");
    }
    
    private static Monthly readProperties(String resource) throws Exception {
        return URLReaderTest.readProperties(resource, s -> Monthly.fromHTML(OBEROFF, ROUND, s));
    }
}
