package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.apiv4.ZonesTest;
import org.joelson.mattias.turfgame.lundkvist.MunicipalityTest;
import org.joelson.mattias.turfgame.util.KMLWriter;
import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Test;

import java.io.IOException;
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
    public void visitDanderydTest() throws IOException {
        visitMunicipalityTest("danderyd_month.kml", MunicipalityTest.getDanderydZones());
    }
    
    @Test
    public void visitSolnaTest() throws IOException {
        visitMunicipalityTest("solna_month.kml", MunicipalityTest.getSolnaZones());
    }
    @Test
    public void visitSundbybergTest() throws IOException {
        visitMunicipalityTest("sundbyberg_month.kml", MunicipalityTest.getSundbybergZones());
    }

    private void visitMunicipalityTest(String filename, Map<String, Boolean> municipalityZones) throws IOException {
        Monthly monthly = getMonthly();
        List<Zone> zones = ZonesTest.getAllZones();
    
        Set<String> visited = new HashSet<>();
        Set<String> unvisited = new HashSet<>();
        
        Set<String> monthlyVisits = monthly.getZones().stream().map(MonthlyZone::getName).collect(Collectors.toSet());
        
        municipalityZones.keySet().forEach(name -> ((monthlyVisits.contains(name)) ? visited : unvisited).add(name));
        Set<Zone> visitedZones = new HashSet<>();
        Set<Zone> unvisitedZones = new HashSet<>();
        zones.forEach(zone -> {
            if (visited.contains(zone.getName())) { visitedZones.add(zone); }
            if (unvisited.contains(zone.getName())) { unvisitedZones.add(zone);
            } });
    
        KMLWriter out = new KMLWriter(filename);
        out.writeFolder("unvisited");
        unvisitedZones.stream()
                .sorted(Comparator.comparing(Zone::getName))
                .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
        out.writeFolder("visited");
        visitedZones.stream()
                .sorted(Comparator.comparing(Zone::getName))
                .forEach(zone -> out.writePlacemark(zone.getName(), "", zone.getLongitude(), zone.getLatitude()));
        out.close();
        System.out.println(filename + ": " + (unvisitedZones.size() + visitedZones.size()) + " (" + municipalityZones.size() + ")");
        assertEquals(municipalityZones.size(), unvisitedZones.size() + visitedZones.size());
    }
    
    private static Monthly getMonthly() throws IOException {
        //        return readProperties("/monthly_oberoff_round96.html");
        return readProperties("/monthly_oberoff_round114.html");
    }
    
    private static Monthly readProperties(String resource) throws IOException {
        return URLReaderTest.readProperties(resource, s -> Monthly.fromHTML(OBEROFF, ROUND, s));
    }
}
