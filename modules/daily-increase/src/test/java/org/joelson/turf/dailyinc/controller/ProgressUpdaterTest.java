package org.joelson.turf.dailyinc.controller;

import org.joelson.turf.dailyinc.db.DatabaseEntityManagerTest;
import org.joelson.turf.dailyinc.model.RevisitData;
import org.joelson.turf.dailyinc.model.TakeData;
import org.joelson.turf.dailyinc.model.UserData;
import org.joelson.turf.dailyinc.model.UserVisitsData;
import org.joelson.turf.dailyinc.model.ZoneData;
import org.joelson.turf.dailyinc.util.InstantUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressUpdaterTest extends DatabaseEntityManagerTest {

    public static final ZoneData ZONE = new ZoneData(1, "Zone");
    public static final UserData USER = new UserData(1, "Taker");
    public static final UserData ASSISTER = new UserData(USER.getId() + 1, "Assister");

    @Test
    public void testSingleTake() {
        Instant time = InstantUtil.getInstantNowTruncatedToSecond();

        ProgressUpdater progressUpdater = new ProgressUpdater(getEntityManager());
        progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(ZONE, USER, time),
                List.of(ASSISTER)));

        List<UserVisitsData> userVisits = getEntityManager().getUserVisits();
        Instant date = time.truncatedTo(ChronoUnit.DAYS);
        assertEquals(List.of(new UserVisitsData(USER, date, 1), new UserVisitsData(ASSISTER, date, 1)), userVisits);
    }

    @Test
    public void testMultipleDates() {
        Instant takeTime = InstantUtil.getInstantNowTruncatedToSecond();
        Instant revisitTime = InstantUtil.addDays(takeTime, 1);
        Instant nextTime = InstantUtil.addDays(revisitTime, 1);

        ProgressUpdater progressUpdater = new ProgressUpdater(getEntityManager());
        progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(ZONE, USER, takeTime),
                List.of(ASSISTER)));
        progressUpdater.updateWithVisits(getEntityManager().addRevisit(new RevisitData(ZONE, USER, revisitTime),
                List.of(ASSISTER)));
        progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(ZONE, ASSISTER, nextTime),
                List.of()));

        List<UserVisitsData> userVisits = getEntityManager().getUserVisits();
        Instant takeDate = takeTime.truncatedTo(ChronoUnit.DAYS);
        Instant revisitDate = revisitTime.truncatedTo(ChronoUnit.DAYS);
        Instant nextDate = nextTime.truncatedTo(ChronoUnit.DAYS);
        assertEquals(List.of(new UserVisitsData(USER, takeDate, 1), new UserVisitsData(USER, revisitDate, 1),
                new UserVisitsData(ASSISTER, takeDate, 1), new UserVisitsData(ASSISTER, revisitDate, 1),
                new UserVisitsData(ASSISTER, nextDate, 1)), userVisits);
    }

    @Test
    public void testFeedImporter() {
        FeedImporterTest.readFeedResource(getEntityManager());
        Instant date = InstantUtil.toInstant("2024-06-17T00:00:00");

        List<UserVisitsData> userVisits = getEntityManager().getUserVisits();
        assertEquals(getEntityManager().getUsers().size(), userVisits.size());
        int visits = 0;
        for (UserVisitsData userVisit : userVisits) {
            assertEquals(date, userVisit.getDate());
            visits += userVisit.getVisits();
        }
        assertEquals(getEntityManager().getVisits().size() + getEntityManager().getAssists().size(), visits);

        UserData userData1 = new UserData(113416, "jösses...då");
        UserData userData2 = new UserData(414549, "puffa");
        assertEquals(List.of(new UserVisitsData(userData1, date, 16)), getEntityManager().getUserVisits(userData1));
        assertEquals(List.of(new UserVisitsData(userData2, date, 15)), getEntityManager().getUserVisits(userData2));
        assertEquals(userVisits.size(), getEntityManager().getUserVisits(date).size());
    }
}
