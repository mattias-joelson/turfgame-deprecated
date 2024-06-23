package org.joelson.turf.dailyinc.db;

import org.joelson.turf.dailyinc.model.TakeData;
import org.joelson.turf.dailyinc.model.UserData;
import org.joelson.turf.dailyinc.model.VisitData;
import org.joelson.turf.dailyinc.model.ZoneData;
import org.joelson.turf.dailyinc.util.InstantUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class DatabaseEntityManagerZoneTest extends DatabaseEntityManagerTest {

    private static final UserData USER = new UserData(1, "User");

    @Test
    public void testFindZoneByName() {
        ZoneData firstZone = new ZoneData(1, "Zone");
        Instant firstTime = InstantUtil.getInstantNowTruncatedToSecond();
        getEntityManager().addTake(new TakeData(firstZone, USER, firstTime), List.of());
        ZoneData zoneEntity = getEntityManager().getZone(firstZone.getName());
        assertEquals(firstZone, zoneEntity);

        ZoneData nextZone = new ZoneData(firstZone.getId() + 1, firstZone.getName());
        Instant nextTime = InstantUtil.addMinutes(firstTime, 30);
        getEntityManager().addTake(new TakeData(nextZone, USER, nextTime), List.of());
        zoneEntity = getEntityManager().getZone(firstZone.getName());
        assertEquals(nextZone, zoneEntity);

        assertEquals(Set.of(nextZone, firstZone), Set.copyOf(getEntityManager().getZones()));
    }

    @Test
    public void testZoneRenamed() {
        ZoneData zone = new ZoneData(1, "first");
        Instant time = InstantUtil.getInstantNowTruncatedToSecond();
        ZoneData zoneNewName = new ZoneData(zone.getId(), "second");
        Instant timeNew = InstantUtil.addDays(time, 1);

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
