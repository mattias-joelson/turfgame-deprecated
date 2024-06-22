package org.joelson.turf.idioten.controller;

import org.joelson.turf.idioten.db.DatabaseEntityManagerTest;
import org.joelson.turf.idioten.model.TakeData;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.UserVisitsData;
import org.joelson.turf.idioten.model.ZoneData;
import org.joelson.turf.idioten.util.InstantUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressUpdaterTest extends DatabaseEntityManagerTest {

    @Test
    public void testSingleTake() {
        ZoneData zone = new ZoneData(1, "Zone");
        UserData user = new UserData(1, "Taker");
        Instant time = InstantUtil.getInstantNowTruncatedtoSecond();
        UserData assister = new UserData(user.getId() + 1, "Assister");

        ProgressUpdater progressUpdater = new ProgressUpdater(getEntityManager());
        progressUpdater.updateWithVisits(getEntityManager().addTake(new TakeData(zone, user, time), List.of(assister)));

        List<UserVisitsData> userVisits = getEntityManager().getUserVisits();
        Instant date = time.truncatedTo(ChronoUnit.DAYS);
        assertEquals(List.of(new UserVisitsData(user, date, 1), new UserVisitsData(assister, date, 1)), userVisits);
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
