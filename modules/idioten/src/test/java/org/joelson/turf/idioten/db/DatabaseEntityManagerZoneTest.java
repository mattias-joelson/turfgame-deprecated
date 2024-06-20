package org.joelson.turf.idioten.db;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DatabaseEntityManagerZoneTest {

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
    public void testZoneRenamed() {
        ZoneData zone = new ZoneData(1, "first");
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        ZoneData zoneNewName = new ZoneData(zone.getId(), "second");
        Instant timeNew = time.plus(1, ChronoUnit.DAYS);
        UserData user = new UserData(1, "player");

        entityManager.addTake(new TakeData(zoneNewName, user, timeNew), List.of());
        entityManager.addTake(new TakeData(zone, user, time), List.of());

        List<VisitData> takes = entityManager.getVisits();
        assertEquals(2, takes.size());
        for (VisitData take : takes) {
            assertInstanceOf(TakeData.class, take);
            assertEquals(zoneNewName, take.getZone());
            assertEquals(user, take.getUser());
        }
        if (takes.get(0).getTime().equals(time)) {
            assertEquals(timeNew, takes.get(1).getTime());
        } else {
            assertEquals(time, takes.get(1).getTime());
        }
        assertEquals(1, entityManager.getUsers().size());
    }
}
