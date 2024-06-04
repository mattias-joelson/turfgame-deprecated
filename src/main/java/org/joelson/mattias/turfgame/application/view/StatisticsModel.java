package org.joelson.mattias.turfgame.application.view;

import org.joelson.mattias.turfgame.application.model.MunicipalityCollection;
import org.joelson.mattias.turfgame.application.model.MunicipalityData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;
import org.joelson.mattias.turfgame.zundin.Monthly;
import org.joelson.mattias.turfgame.zundin.MonthlyZone;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StatisticsModel {

    private final VisitCollection visits;
    private final Map<String, String> municipalityMap;
    private List<VisitData> currentVisits;

    public StatisticsModel(VisitCollection visits, MunicipalityCollection municipalities, UserData selectedUser) {
        this.visits = visits;
        municipalityMap = initMunicipalities(municipalities);
        updateSelectedUser(selectedUser);
    }

    private static Map<String, String> initMunicipalities(MunicipalityCollection municipalities) {
        Map<String, String> municipalityMap = new HashMap<>();
        for (MunicipalityData municipality : municipalities.getMunicipalities()) {
            String municipalityName = String.format("%s / %s", municipality.getName(), municipality.getRegion().getName());
            municipality.getZones().forEach(zoneData -> municipalityMap.put(zoneData.getName(), municipalityName));
        }
        return municipalityMap;
    }

    public String updateSelectedUser(UserData selectedUser) {
        currentVisits = new ArrayList<>(visits.getVisits(selectedUser));
        currentVisits.sort(Comparator.comparing(VisitData::getWhen));
        return createData();
    }

    public String createData() {
        return "Hello, world!";
    }


//    public String createData() {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//
//        scorePerMunicipality(pw);
//        takesPerMunicipality(pw);
//        scorePerVisit(pw);
//        scorePerVisitPerMunicipality(pw);
//        scorePerVisitPerTP(pw);
//        scorePerVisitPerTPPerMunicpality(pw);
//
//        totalScorePerZone(pw, 20);
//        scorePerZoneTake(pw, 20);
//        pphScorePerZone(pw, 20);
//        pphScorePerZoneTake(pw, 20);
//
//        return sw.toString();
//    }
//
//    private void scorePerMunicipality(PrintWriter pw) {
//        pw.println("Score per municipality");
//        monthly.getZones().stream()
//                .collect(Collectors.groupingBy(MonthlyZone::getMunicipality, Collectors.summingInt(MonthlyZone::getPoints)))
//                .entrySet().stream()
//                .map(entry -> String.format("%6d - %s", entry.getValue(), entry.getKey()))
//                .sorted(Comparator.reverseOrder())
//                .forEach(pw::println);
//    }
//
//    private void takesPerMunicipality(PrintWriter pw) {
//        pw.println("Takes per municipality");
//        monthly.getZones().stream()
//                .collect(Collectors.groupingBy(MonthlyZone::getMunicipality, Collectors.summingInt(MonthlyZone::getVisits)))
//                .entrySet().stream()
//                .map(entry -> String.format("%6d - %s", entry.getValue(), entry.getKey()))
//                .sorted(Comparator.reverseOrder())
//                .forEach(pw::println);
//    }
//
//    private void scorePerVisit(PrintWriter pw) {
//        pw.println("Score per visit");
//        pw.println(getPointsPerVisit(monthly.getZones()));
//    }
//
//    private void scorePerVisitPerMunicipality(PrintWriter pw) {
//        pw.println("Score per visit per municipality");
//        getMunicipalityZones(monthly).entrySet().stream()
//                .map(entry -> String.format("%4d - %s", getPointsPerVisit(entry.getValue()), entry.getKey()))
//                .sorted(Comparator.reverseOrder())
//                .forEach(pw::println);
//    }
//
//    private void scorePerVisitPerTP(PrintWriter pw) {
//        pw.println("Score per visit per TP");
//        getTPZones(monthly.getZones()).entrySet().stream()
//                .map(entry -> String.format("%3d: %d", entry.getKey(), getPointsPerVisit(entry.getValue())))
//                .sorted(Comparator.reverseOrder())
//                .forEach(pw::println);
//    }
//
//    private void scorePerVisitPerTPPerMunicpality(PrintWriter pw) {
//        pw.println("Score per visit per TP per municipality");
//        pw.println(String.format("%20s ", "")
//                + Arrays.stream(POSSIBLE_TP)
//                .mapToObj(tp -> String.format(" %4d      ", tp))
//                .collect(Collectors.joining()));
//        Map<String, List<MonthlyZone>> municipalityZones = getMunicipalityZones(monthly);
//        municipalityZones.put("all", monthly.getZones());
//        municipalityZones.entrySet().stream()
//                .sorted(Entry.comparingByKey())
//                .map(MonthlyStatsTest::getMunicipalityString)
//                .sorted()
//                .forEach(pw::println);
//    }
//
//    private static String getMunicipalityString(Map.Entry<String, List<MonthlyZone>> municipalityEntry) {
//        String municipalityString = String.format("%20s ", municipalityEntry.getKey());
//        municipalityString += getTPZones(municipalityEntry.getValue()).entrySet().stream()
//                .sorted(Entry.comparingByKey())
//                .map(MonthlyStatsTest::getPointsPerVisitString)
//                .collect(Collectors.joining());
//        return municipalityString;
//    }
//
//    private static String getPointsPerVisitString(Map.Entry<Integer, List<MonthlyZone>> tpEntry) {
//        int pointsPerTPVisit = getPointsPerVisit(tpEntry.getValue());
//        if (pointsPerTPVisit == 0) {
//            return "           ";
//        }
//        return String.format(" %4d (%3d)", pointsPerTPVisit, tpEntry.getValue().stream().mapToInt(MonthlyZone::getVisits).sum());
//    }
//
//    private void totalScorePerZone(PrintWriter pw, int limit) {
//        pw.println("Total score per zone");
//        monthly.getZones().stream()
//                .map(zone -> String.format("%4d - %s (%d - %d, %d, %d)", zone.getPoints(), zone.getName(), zone.getVisits(), zone.getTakes(),
//                        zone.getAssists(), zone.getRevisits()))
//                .sorted(Comparator.reverseOrder())
//                .limit(limit)
//                .forEach(pw::println);
//    }
//
//    private void scorePerZoneTake(PrintWriter pw, int limit) {
//        pw.println("Score per take zone");
//        monthly.getZones().stream()
//                .map(zone -> String.format("%4d (%2d) - %s", zone.getPoints() / zone.getVisits(), zone.getVisits(), zone.getName()))
//                .sorted(Comparator.reverseOrder())
//                .limit(limit)
//                .forEach(pw::println);
//    }
//
//    private void pphScorePerZone(PrintWriter pw, int limit) {
//        pw.println("PPH Score per zone");
//        monthly.getZones().stream()
//                .map(zone -> String.format("%4d (%2d) - %s", zone.getPPHPoints(), zone.getVisits(), zone.getName()))
//                .sorted(Comparator.reverseOrder())
//                .limit(limit)
//                .forEach(pw::println);
//    }
//
//    private void pphScorePerZoneTake(PrintWriter pw, int limit) {
//        pw.println("PPH Score per zone visit ");
//        monthly.getZones().stream()
//                .map(zone -> String.format("%4d (%2d) - %s", zone.getPPHPoints() / zone.getVisits(), zone.getVisits(), zone.getName()))
//                .sorted(Comparator.reverseOrder())
//                .limit(limit)
//                .forEach(pw::println);
//    }
//
//    private static int getPointsPerVisit(List<MonthlyZone> zones) {
//        if (zones.isEmpty()) {
//            return 0;
//        }
//        int points = zones.stream().mapToInt(MonthlyZone::getPoints).sum();
//        int visits = zones.stream().mapToInt(MonthlyZone::getVisits).sum();
//        return points / visits;
//    }
//
//    private static Map<String, List<MonthlyZone>> getMunicipalityZones(Monthly monthly) {
//        return  getMunicipalityZones(monthly.getZones());
//    }
//
//    private static Map<String, List<MonthlyZone>> getMunicipalityZones(List<MonthlyZone> zones) {
//        return zones.stream().collect(Collectors.groupingBy(MonthlyZone::getMunicipality));
//    }
//
//    private static Map<Integer, List<MonthlyZone>> getTPZones(List<MonthlyZone> zones) {
//        Map<Integer, List<MonthlyZone>> tpZones = zones.stream().collect(Collectors.groupingBy(MonthlyZone::getTP));
//        Arrays.stream(POSSIBLE_TP).forEach(tp -> {
//            if (!tpZones.containsKey(tp)) {
//                tpZones.put(tp, new ArrayList<>());
//            }
//        });
//        return tpZones;
//    }
}
