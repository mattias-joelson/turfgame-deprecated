package org.joelson.turf.idioten.db;

import org.joelson.turf.idioten.model.RevisitData;
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

public class DatabaseEntityManagerUserTest extends DatabaseEntityManagerTest {

    private static final ZoneData ZONE = new ZoneData(1, "ZoneZone");

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
    public void testFindUserByName() {
        UserData firstUser = new UserData(1, "User");
        Instant firstTime = getTruncatedInstantNow();
        getEntityManager().addTake(new TakeData(ZONE, firstUser, firstTime), List.of());
        UserData userEntity = getEntityManager().getUser(firstUser.getName());
        assertEquals(firstUser, userEntity);

        UserData nextUser = new UserData(firstUser.getId() + 1, firstUser.getName());
        Instant nextTime = addMinutes(firstTime, 30);
        getEntityManager().addTake(new TakeData(ZONE, nextUser, nextTime), List.of());
        userEntity = getEntityManager().getUser(firstUser.getName());
        assertEquals(nextUser, userEntity);

        assertEquals(Set.of(nextUser, firstUser), Set.copyOf(getEntityManager().getUsers()));
    }

    @Test
    public void testUserRenamedAndSameName() {
        UserData user = new UserData(1, "player");
        Instant time = getTruncatedInstantNow();
        UserData userRevisit = new UserData(1, "player-renamed");
        Instant timeRevisit = addDays(time, 1);
        UserData userNext = new UserData(2, userRevisit.getName());
        Instant timeNext = addMinutes(timeRevisit, 30);

        getEntityManager().addTake(new TakeData(ZONE, user, time), List.of());
        getEntityManager().addTake(new TakeData(ZONE, userNext, timeNext), List.of());
        getEntityManager().addRevisit(new RevisitData(ZONE, userRevisit, timeRevisit), List.of());

        List<VisitData> visits = getEntityManager().getVisits();
        assertEquals(Set.of(new RevisitData(ZONE, userRevisit, timeRevisit), new TakeData(ZONE, userNext, timeNext),
                new TakeData(ZONE, userRevisit, time)), Set.copyOf(visits));
        for (VisitData visit : visits) {
            assertEquals(ZONE, visit.getZone());
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
        assertEquals(Set.of(userNext, userRevisit), Set.copyOf(getEntityManager().getUsers()));
        assertEquals(List.of(new ZoneData(ZONE.getId(), ZONE.getName())), getEntityManager().getZones());
    }
}
