package org.joelson.mattias.turfgame.roundanalyze;

import org.joelson.mattias.turfgame.apiv4.Zone;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private final String username;
    private final List<Session> sessions;
    private final Map<Integer, Take> ownedZones;

    public User(String user) {
        this.username = user;
        sessions = new ArrayList<>();
        ownedZones = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    private Session getLastSession() {
        return (sessions.size() > 0) ? sessions.get(sessions.size() - 1) : null;
    }

    public void addTake(LocalDateTime dateTime, Zone zone, boolean neutralized) {
        Take take = new Take(dateTime, zone.getId(), zone.getTakeoverPoints(), neutralized, zone.getPointsPerHour());
        if (ownedZones.get(zone.getId()) != null) {
            throw new IllegalArgumentException("Alredy owns zone " + zone);
        }
        ownedZones.put(zone.getId(), take);
        addVisit(take);
    }

    public void addTake(LocalDateTime dateTime, Zone zone, boolean neutralized, Duration duration) {
        Take take = new Take(dateTime, zone.getId(), zone.getTakeoverPoints(), neutralized, zone.getPointsPerHour());
        take.lost(dateTime.plus(duration));
        addVisit(take);
    }

    public void addAssist(LocalDateTime dateTime, Zone zone, boolean neutralized) {
        addVisit(new Assist(dateTime, zone.getId(), zone.getTakeoverPoints(), neutralized));
    }

    public void addRevisit(LocalDateTime dateTime, Zone zone) {
        addVisit(new Revisit(dateTime, zone.getId(), zone.getTakeoverPoints() / 2));
    }

    private void addVisit(Visit visit) {
        Session lastSession = getLastSession();
        if (lastSession != null && lastSession.belongsToSession(visit)) {
            lastSession.addVisit(visit);
        } else {
            Session newSession = new Session(visit);
            sessions.add(newSession);
        }
    }

    public void addLoss(LocalDateTime dateTime, Zone zone) {
        Take take = ownedZones.remove(zone.getId());
        if (take == null) {
            System.err.println("Trying to apply loss zone " + zone + " at " + dateTime + " but not taken!");
        } else {
            take.lost(dateTime);
        }
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
