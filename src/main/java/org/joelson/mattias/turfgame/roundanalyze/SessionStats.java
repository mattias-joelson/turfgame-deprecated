package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.apiv4.Zone;
import org.joelson.mattias.turfgame.util.ZoneUtil;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class SessionStats {

    private final Session session;

    private final Stats stats;

    public SessionStats(Session session, Map<Integer, Zone> zoneIdMap) {
        this.session = session;
        stats = analyzeSession(session, zoneIdMap);
    }

    public Session getSession() {
        return session;
    }

    public Stats getStats() {
        return stats;
    }

    public void prettyPrint(int sessionNumber) {
        System.out.println("===========\n" + "Session "+ sessionNumber + "\n" + "===========");
        stats.prettyPrint();
    }

    private static Stats analyzeSession(Session session, Map<Integer, Zone> zoneIdMap) {
        List<Visit> visits = session.getVisits();
        Duration duration = Duration.between(visits.get(0).getTime(), visits.get(visits.size() - 1).getTime());

        int takes = 0;
        int neutralTakes = 0;
        int takesTP = 0;
        int takesPoint = 0;
        int assists = 0;
        int neutralAssists = 0;
        int assistsTP = 0;
        int revisits = 0;
        int revisitsTP = 0;

        double distance = 0.0;
        Zone previousZone = null;
        Zone northernmost = null;
        Zone southernmost = null;
        Zone easternmost = null;
        Zone westernmost = null;

        for (int i = 0; i < visits.size(); i += 1) {
            Visit visit = visits.get(i);
            if (visit instanceof Take) {
                takes += 1;
                if (((Take) visit).isNeutralized()) {
                    neutralTakes += 1;
                }
                takesTP += visit.getTp();
                takesPoint += visit.getPoints();
            } else if (visit instanceof Assist) {
                assists += 1;
                if (((Assist) visit).isNeutralized()) {
                    neutralAssists += 1;
                }
                assistsTP += visit.getTp();
            } else {
                revisits += 1;
                revisitsTP += visit.getTp();
            }
            Zone currentZone = zoneIdMap.get(visit.getZoneId());
            if (previousZone != null) {
                distance += ZoneUtil.calcDistance(previousZone, currentZone);
                northernmost = Stats.northernmost(northernmost, currentZone);
                southernmost = Stats.southernmost(southernmost, currentZone);
                easternmost = Stats.easternmost(easternmost, currentZone);
                westernmost = Stats.westernmost(westernmost, currentZone);
            } else {
                northernmost = currentZone;
                southernmost = currentZone;
                easternmost = currentZone;
                westernmost = currentZone;
            }
            previousZone = currentZone;
        }

        return new Stats(takes, neutralTakes, takesTP, takesPoint, assists, neutralAssists, assistsTP,
                revisits, revisitsTP, duration, distance, northernmost, southernmost, easternmost, westernmost);
    }
}
