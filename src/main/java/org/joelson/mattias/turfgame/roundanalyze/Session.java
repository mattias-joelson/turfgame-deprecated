package org.joelson.mattias.turfgame.roundanalyze;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Session {

    private final List<Visit> visits;

    public Session(Visit firstVisit) {
        visits = new ArrayList<>();
        visits.add(firstVisit);
    }

    public boolean belongsToSession(Visit visit) {
        Visit lastVisit = getLastVisit();
        return lastVisit == null
                || Duration.between(lastVisit.getTime(), visit.getTime()).getSeconds() < 3600;
    }

    public void addVisit(Visit visit) {
        if (getLastVisit().getTime().isAfter(visit.getTime())) {
            throw new IllegalArgumentException("Visit " + visit.getTime() + " is before " + getLastVisit().getTime());
        }
        visits.add(visit);
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public Visit getLastVisit() {
        return visits.get(visits.size() - 1);
    }
}
