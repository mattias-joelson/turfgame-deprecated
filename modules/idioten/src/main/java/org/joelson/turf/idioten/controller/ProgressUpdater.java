package org.joelson.turf.idioten.controller;

import org.joelson.turf.idioten.db.DatabaseEntityManager;
import org.joelson.turf.idioten.model.VisitData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProgressUpdater {

    private final DatabaseEntityManager entityManager;

    public ProgressUpdater(DatabaseEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void updateWithVisits(List<VisitData> visits) {
        if (visits.isEmpty()) {
            return;
        }
        Instant date = visits.get(0).getTime().truncatedTo(ChronoUnit.DAYS);
        for (VisitData visit : visits) {
            entityManager.increaseUserVisits(visit.getUser(), date);
        }
    }
}
