package org.joelson.mattias.turfgame.zundin;

import org.joelson.mattias.turfgame.util.URLReaderTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MonthlyStatsTest {
    
    private Monthly monthly;
    public static final int[] POSSIBLE_TP = new int[]{65, 80, 95, 110, 125, 140, 155, 170, 185};
    
    @Before
    public void before() throws IOException {
        monthly = URLReaderTest.readProperties("/monthly_oberoff_round113.html", s -> Monthly.fromHTML("Oberoff", 113, s));
    }
    
    @Test
    public void allStatistics() {
        scorePerMunicipality();
        takesPerMunicipality();
        scorePerVisit();
        scorePerVisitPerMunicipality();
        scorePerVisitPerTP();
        scorePerVisitPerTPPerMunicpality();
        
        totalScorePerZone(20);
        scorePerZoneTake(20);
        pphScorePerZone(20);
        pphScorePerZoneTake(20);
    }
    
    public void scorePerMunicipality() {
        System.out.println("Score per municipality");
        monthly.getZones().stream()
                .collect(Collectors.groupingBy(MonthlyZone::getMunicipality, Collectors.summingInt(MonthlyZone::getPoints)))
                .entrySet().stream()
                .map(entry -> String.format("%6d - %s", entry.getValue(), entry.getKey()))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }
    
    public void takesPerMunicipality() {
        System.out.println("Takes per municipality");
        monthly.getZones().stream()
                .collect(Collectors.groupingBy(MonthlyZone::getMunicipality, Collectors.summingInt(MonthlyZone::getVisits)))
                .entrySet().stream()
                .map(entry -> String.format("%6d - %s", entry.getValue(), entry.getKey()))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }
    
    public void scorePerVisit() {
        System.out.println("Score per visit");
        System.out.println(getPointsPerVisit(monthly.getZones()));
    }
    
    public void scorePerVisitPerMunicipality() {
        System.out.println("Score per visit per municipality");
        getMunicipalityZones(monthly).entrySet().stream()
                .map(entry -> String.format("%4d - %s", getPointsPerVisit(entry.getValue()), entry.getKey()))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }
    
    public void scorePerVisitPerTP() {
        System.out.println("Score per visit per TP");
        getTPZones(monthly.getZones()).entrySet().stream()
                .map(entry -> String.format("%3d: %d", entry.getKey(), getPointsPerVisit(entry.getValue())))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }

    public void scorePerVisitPerTPPerMunicpality() {
        System.out.println("Score per visit per TP per municipality");
        System.out.println(String.format("%20s ", "")
                + Arrays.stream(POSSIBLE_TP)
                .mapToObj(tp -> String.format(" %4d", tp))
                .collect(Collectors.joining()));
        Map<String, List<MonthlyZone>> municipalityZones = getMunicipalityZones(monthly);
        municipalityZones.put("all", monthly.getZones());
        municipalityZones.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .map(municipalityEntry -> {
                    String municipalityString = String.format("%20s ", municipalityEntry.getKey());
                    municipalityString += getTPZones(municipalityEntry.getValue()).entrySet().stream()
                            .sorted(Entry.comparingByKey())
                            .map(tpEntry -> {
                                int pointsPerTPVisit = getPointsPerVisit(tpEntry.getValue());
                                if (pointsPerTPVisit == 0) {
                                    return "     ";
                                }
                                return String.format(" %4d", pointsPerTPVisit);
                            })
                            .collect(Collectors.joining());
                    return municipalityString;
                })
                .sorted()
                .forEach(System.out::println);
    }

    public void totalScorePerZone(int limit) {
        System.out.println("Total score per zone");
        monthly.getZones().stream()
                .map(zone -> String.format("%4d - %s (%d - %d, %d, %d)", zone.getPoints(), zone.getName(), zone.getVisits(), zone.getTakes(), zone.getAssists(), zone.getRevisits()))
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .forEach(System.out::println);
    }
    
    public void scorePerZoneTake(int limit) {
        System.out.println("Score per take zone");
        monthly.getZones().stream()
                .map(zone -> String.format("%4d (%2d) - %s", zone.getPoints() / zone.getVisits(), zone.getVisits(), zone.getName()))
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .forEach(System.out::println);
    }
    
    public void pphScorePerZone(int limit) {
        System.out.println("PPH Score per zone");
        monthly.getZones().stream()
                .map(zone -> String.format("%4d (%2d) - %s", zone.getPPHPoints(), zone.getVisits(), zone.getName()))
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .forEach(System.out::println);
    }
    
    public void pphScorePerZoneTake(int limit) {
        System.out.println("PPH Score per zone visit ");
        monthly.getZones().stream()
                .map(zone -> String.format("%4d (%2d) - %s", zone.getPPHPoints() / zone.getVisits(), zone.getVisits(), zone.getName()))
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .forEach(System.out::println);
    }
    
    private static int getPointsPerVisit(List<MonthlyZone> zones) {
        if (zones.isEmpty()) {
            return 0;
        }
        int points = zones.stream().mapToInt(MonthlyZone::getPoints).sum();
        int visits = zones.stream().mapToInt(MonthlyZone::getVisits).sum();
        return points / visits;
    }
    
    private static Map<String, List<MonthlyZone>> getMunicipalityZones(Monthly monthly) {
        return  getMunicipalityZones(monthly.getZones());
    }
    
    private static Map<String, List<MonthlyZone>> getMunicipalityZones(List<MonthlyZone> zones) {
        return zones.stream().collect(Collectors.groupingBy(MonthlyZone::getMunicipality));
    }
    
    private static Map<Integer, List<MonthlyZone>> getTPZones(List<MonthlyZone> zones) {
        Map<Integer, List<MonthlyZone>> tpZones = zones.stream().collect(Collectors.groupingBy(MonthlyZone::getTP));
        Arrays.stream(POSSIBLE_TP).forEach(tp -> { if (!tpZones.containsKey(tp)) tpZones.put(tp, new ArrayList<>()); });
        return tpZones;
    }
}
