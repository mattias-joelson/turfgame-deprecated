package org.joelson.turf.idioten.controller;

import org.joelson.turf.idioten.db.DatabaseEntityManager;
import org.joelson.turf.idioten.db.UserProgressType;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.UserProgressData;
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
        Instant time = visits.get(0).getTime();
        Instant date = time.truncatedTo(ChronoUnit.DAYS);
        for (VisitData visit : visits) {
            updateProgress(visit.getUser(), date, time);
        }
    }

    private void updateProgress(UserData user, Instant date, Instant time) {
        int visits = entityManager.increaseUserVisits(user, date);
        for (UserProgressType type : UserProgressType.values()) {
            UserProgressData userProgress = entityManager.getUserProgress(user, type, date);
            if (userProgress == null) {
                Instant previousDate = date.minus(1, ChronoUnit.DAYS);
                UserProgressData previousUserProgress = entityManager.getUserProgress(user, type, previousDate);
                if (previousUserProgress == null) {
                    entityManager.addUserProgress(user, type, date, 0, 1, time);
                } else if (type == UserProgressType.DAILY_FIBONACCI) {
                    entityManager.addUserProgress(user, type, date, previousUserProgress.getDayCompleted(), 2, time);
                } else {
                    entityManager.addUserProgress(user, type, date, previousUserProgress.getDayCompleted(), 1, time);
                }
            } else if (userProgress.getDayCompleted() <= userProgress.getPreviousDayCompleted()) {
                int visitsNeeded = type.getNeededVisits(userProgress.getDayCompleted() + 1);
                if (visits >= visitsNeeded) {
                    entityManager.increaseUserProgressDayCompleted(user, type, date, time);
                }
            }
        }
    }
}
