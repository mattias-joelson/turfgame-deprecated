package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.apiv4.Zone;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserStats {

    private final User user;
    private final List<SessionStats> sessionStats;
    private final Stats stats;
    private final ZoneStats zoneStats;

    public UserStats(User user, Map<Integer, Zone> zoneIdMap) {
        this.user = user;
        sessionStats = analyzeSessions(user.getSessions(), zoneIdMap);
        stats = sumSessions(sessionStats);
        zoneStats = new ZoneStats(user);
    }

    public User getUser() {
        return user;
    }

    public List<SessionStats> getSessionStats() {
        return sessionStats;
    }

    public Stats getStats() {
        return stats;
    }

    public void prettyPrint() {
        System.out.println("=====\n" + "TOTAL\n" + "=====");
        System.out.println("Number of sessions: " + sessionStats.size());
        System.out.println();
        stats.prettyPrint();
        System.out.println();
        zoneStats.prettyPrint();
    }

    private static List<SessionStats> analyzeSessions(List<Session> sessions, Map<Integer, Zone> zoneIdMap) {
        return sessions.stream()
                .map(session -> new SessionStats(session, zoneIdMap))
                .collect(Collectors.toList());
    }

    private static Stats sumSessions(List<SessionStats> sessionStats) {
        Stats stats = sessionStats.get(0).getStats();
        for (int i = 1; i < sessionStats.size(); i += 1) {
            stats = stats.plus(sessionStats.get(i).getStats());
        }
        return stats;
    }
}
