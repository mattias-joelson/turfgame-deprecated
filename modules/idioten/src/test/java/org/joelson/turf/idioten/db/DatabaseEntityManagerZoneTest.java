package org.joelson.turf.idioten.db;

import org.joelson.turf.idioten.model.TakeData;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.VisitData;
import org.joelson.turf.idioten.model.ZoneData;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DatabaseEntityManagerZoneTest extends DatabaseEntityManagerTest {

    private static final UserData USER = new UserData(1, "User");

    private static Instant getTruncatedInstantNow() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    private static Instant addMinutes(Instant instant, long minutes) {
        return instant.plus(minutes, ChronoUnit.MINUTES);
    }

    private static Instant addDays(Instant instant, long days) {
        return instant.plus(days, ChronoUnit.MINUTES);
    }

    @Test
    public void testFindZoneByName() {
        ZoneData firstZone = new ZoneData(1, "Zone");
        Instant firstTime = getTruncatedInstantNow();
        getEntityManager().addTake(new TakeData(firstZone, USER, firstTime), List.of());
        ZoneData zoneEntity = getEntityManager().getZone(firstZone.getName());
        assertEquals(firstZone, zoneEntity);

        ZoneData nextZone = new ZoneData(firstZone.getId() + 1, firstZone.getName());
        Instant nextTime = addMinutes(firstTime, 30);
        getEntityManager().addTake(new TakeData(nextZone, USER, nextTime), List.of());
        zoneEntity = getEntityManager().getZone(firstZone.getName());
        assertEquals(nextZone, zoneEntity);

        assertEquals(Set.of(nextZone, firstZone), Set.copyOf(getEntityManager().getZones()));
    }

    @Test
    public void testZoneRenamed() {
        ZoneData zone = new ZoneData(1, "first");
        Instant time = getTruncatedInstantNow();
        ZoneData zoneNewName = new ZoneData(zone.getId(), "second");
        Instant timeNew = addDays(time, 1);

        getEntityManager().addTake(new TakeData(zoneNewName, USER, timeNew), List.of());
        getEntityManager().addTake(new TakeData(zone, USER, time), List.of());

        List<VisitData> takes = getEntityManager().getVisits();
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
        assertEquals(1, getEntityManager().getUsers().size());
    }
}
