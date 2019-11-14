package org.joelson.mattias.turfgame.zundin;

import org.junit.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MonthlyStatsTest {
    
    @Test
    public void scorePerMunicipality() throws IOException {
        System.out.println("Score per municipality");
        Monthly monthly = MonthlyTest.getMonthly();
        List<MonthlyZone> zones = monthly.getZones();
        zones.stream()
                .collect(Collectors.groupingBy(zone -> zone.getMunicipality(), Collectors.summingInt(zone -> zone.getPoints())))
                .entrySet().stream()
                .map(entry -> String.format("%6d - %s", entry.getValue(), entry.getKey()))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
                
    }
    
    @Test
    public void takesPerMunicipality() throws IOException {
        System.out.println("Takes per municipality");
        Monthly monthly = MonthlyTest.getMonthly();
        List<MonthlyZone> zones = monthly.getZones();
        zones.stream()
                .collect(Collectors.groupingBy(zone -> zone.getMunicipality(), Collectors.summingInt(zone -> zone.getTakes() + zone.getRevisits() + zone.getAssists())))
                .entrySet().stream()
                .map(entry -> String.format("%6d - %s", entry.getValue(), entry.getKey()))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
        
    }

    @Test
    public void totalScorePerZone() throws IOException {
        System.out.println("Total score per zone");
        Monthly monthly = MonthlyTest.getMonthly();
        List<MonthlyZone> zones = monthly.getZones();
        zones.stream()
                .map(zone -> String.format("%4d - %s", zone.getPoints(), zone.getName()))
                .sorted(Comparator.reverseOrder())
                .limit(20)
                .forEach(System.out::println);
    }
    
    @Test
    public void scorePerZoneTake() throws IOException {
        System.out.println("Score per take zone");
        Monthly monthly = MonthlyTest.getMonthly();
        List<MonthlyZone> zones = monthly.getZones();
        zones.stream()
                .map(zone -> String.format("%4d (%2d) - %s", zone.getPoints() / (zone.getTakes() + zone.getAssists() + zone.getRevisits()), zone.getTakes() + zone.getAssists() + zone.getRevisits(), zone.getName()))
                .sorted(Comparator.reverseOrder())
                .limit(20)
                .forEach(System.out::println);
    }
}
