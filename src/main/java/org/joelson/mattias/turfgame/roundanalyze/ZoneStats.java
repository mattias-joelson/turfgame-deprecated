package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.apiv4.Zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZoneStats {

    private final List<Set<Integer>> zoneVisits;
    private final int uniqueZonesVisited;

    public ZoneStats(User user) {
        zoneVisits = analyzeZoneVisits(user.getSessions());
        uniqueZonesVisited = countUniqueZonesVisited(zoneVisits);
    }

    public void prettyPrint() {
        System.out.printf("Unique zones visited:            %d%n", uniqueZonesVisited);
        System.out.printf("Maximum visits to single zone:   %d (%d zones)%n",
                zoneVisits.size() - 1, zoneVisits.get(zoneVisits.size() - 1).size());
        System.out.printf("Zones visited at least 2 times:  %d%n", countAtLeastVisits(zoneVisits, 2));
        System.out.printf("Zones visited at least 11 times: %d%n", countAtLeastVisits(zoneVisits, 11));
        System.out.printf("Zones visited at least 21 times: %d%n", countAtLeastVisits(zoneVisits, 21));
        System.out.printf("Zones visited at least 51 times: %d%n", countAtLeastVisits(zoneVisits, 51));
    }

    private static List<Set<Integer>> analyzeZoneVisits(List<Session> sessions) {
        Map<Integer, Integer> zoneIdToVisitsMap = new HashMap<>();
        sessions.forEach(session -> session.getVisits()
                .forEach(visit -> increaseCount(zoneIdToVisitsMap, visit.getZoneId())));
        int maxVisits = zoneIdToVisitsMap.values().stream()
                .mapToInt(value -> value)
                .max().orElseThrow();
        List<Set<Integer>> zoneVisits = new ArrayList<>(maxVisits + 1);
        for (int i = 0; i < maxVisits + 1; i += 1) {
            zoneVisits.add(new HashSet<>());
        }
        zoneIdToVisitsMap.forEach((zoneId, visits) -> zoneVisits.get(visits).add(zoneId));
        return zoneVisits;
    }

    private static void increaseCount(Map<Integer, Integer> zoneIdToVisitsMap, int zoneId) {
        if (zoneIdToVisitsMap.containsKey(zoneId)) {
            zoneIdToVisitsMap.put(zoneId, zoneIdToVisitsMap.get(zoneId) + 1);
        } else {
            zoneIdToVisitsMap.put(zoneId, 1);
        }
    }

    private static int countUniqueZonesVisited(List<Set<Integer>> zoneVisits) {
        return countAtLeastVisits(zoneVisits, 1);
    }

    private static int countAtLeastVisits(List<Set<Integer>> zoneVisits, int minimumVisits) {
        int zonesVisited = 0;
        for (int i = minimumVisits; i < zoneVisits.size(); i += 1) {
            zonesVisited += zoneVisits.get(i).size();
        }
        return zonesVisited;
    }
}
