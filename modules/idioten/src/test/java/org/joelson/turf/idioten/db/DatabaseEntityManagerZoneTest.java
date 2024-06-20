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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DatabaseEntityManagerZoneTest {

    public static final String PERSISTENCE_H2 = "turfgame-idioten-test-h2";
    private static final UserData USER = new UserData(1, "User");

    private DatabaseEntityManager entityManager;

    private static Instant getTruncatedInstantNow() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    private static Instant addMinutes(Instant instant, long minutes) {
        return instant.plus(minutes, ChronoUnit.MINUTES);
    }

    private static Instant addDays(Instant instant, long days) {
        return instant.plus(days, ChronoUnit.MINUTES);
    }

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
    public void testFindZoneByName() {
        ZoneData firstZone = new ZoneData(1, "Zone");
        Instant firstTime = getTruncatedInstantNow();
        entityManager.addTake(new TakeData(firstZone, USER, firstTime), List.of());
        ZoneData zoneEntity = entityManager.getZone(firstZone.getName());
        assertEquals(firstZone, zoneEntity);

        ZoneData nextZone = new ZoneData(firstZone.getId() + 1, firstZone.getName());
        Instant nextTime = addMinutes(firstTime, 30);
        entityManager.addTake(new TakeData(nextZone, USER, nextTime), List.of());
        zoneEntity = entityManager.getZone(firstZone.getName());
        assertEquals(nextZone, zoneEntity);

        assertEquals(Set.of(nextZone, firstZone), Set.copyOf(entityManager.getZones()));
    }

    @Test
    public void testZoneRenamed() {
        ZoneData zone = new ZoneData(1, "first");
        Instant time = getTruncatedInstantNow();
        ZoneData zoneNewName = new ZoneData(zone.getId(), "second");
        Instant timeNew = addDays(time, 1);

        entityManager.addTake(new TakeData(zoneNewName, USER, timeNew), List.of());
        entityManager.addTake(new TakeData(zone, USER, time), List.of());

        List<VisitData> takes = entityManager.getVisits();
        assertEquals(2, takes.size());
        for (VisitData take : takes) {
            assertInstanceOf(TakeData.class, take);
            assertEquals(zoneNewName, take.getZone());
            assertEquals(USER, take.getUser());
        }
        if (takes.get(0).getTime().equals(time)) {
            assertEquals(timeNew, takes.get(1).getTime());
        } else {
            assertEquals(time, takes.get(1).getTime());
        }
        assertEquals(1, entityManager.getUsers().size());
    }
}
