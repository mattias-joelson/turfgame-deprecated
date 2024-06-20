package org.joelson.turf.idioten.db;

import org.joelson.turf.idioten.model.RevisitData;
import org.joelson.turf.idioten.model.TakeData;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.VisitData;
import org.joelson.turf.idioten.model.ZoneData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DatabaseEntityManagerUserTest {

    public static final String PERSISTENCE_H2 = "turfgame-idioten-test-h2";

    private DatabaseEntityManager entityManager;

    @BeforeEach
    public void setupEntityManager() {
        entityManager = new DatabaseEntityManager(PERSISTENCE_H2);
    }

    @AfterEach
    public void closeEntityManager() {
        entityManager.close();
        entityManager = null;
    }

    @Test
    public void testUserRenamedAndSameName() {
        ZoneData zone = new ZoneData(1, "first");
        UserData user = new UserData(1, "player");
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        UserData userRevisit = new UserData(1, "player-renamed");
        Instant timeRevisit = time.plus(1, ChronoUnit.DAYS);
        UserData userNext = new UserData(2, userRevisit.getName());
        Instant timeNext = timeRevisit.plus(30, ChronoUnit.MINUTES);

        entityManager.addTake(new TakeData(zone, user, time), List.of());
        entityManager.addTake(new TakeData(zone, userNext, timeNext), List.of());
        entityManager.addRevisit(new RevisitData(zone, userRevisit, timeRevisit), List.of());

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(Set.of(new RevisitData(zone, userRevisit, timeRevisit), new TakeData(zone, userNext, timeNext),
                new TakeData(zone, userRevisit, time)), Set.copyOf(visits));
        for (VisitData visit : visits) {
            assertEquals(zone, visit.getZone());
            if (visit.getTime().equals(time)) {
                assertInstanceOf(TakeData.class, visit);
                assertEquals(userRevisit, visit.getUser());
            } else if (visit.getTime().equals(timeRevisit)) {
                assertInstanceOf(RevisitData.class, visit);
                assertEquals(userRevisit, visit.getUser());
            } else {
                assertInstanceOf(TakeData.class, visit);
                assertEquals(userNext, visit.getUser());
                assertEquals(timeNext, visit.getTime());
            }
        }
        assertEquals(Set.of(userNext, userRevisit), Set.copyOf(entityManager.getUsers()));
        assertEquals(List.of(new ZoneData(zone.getId(), zone.getName())), entityManager.getZones());
    }
}
