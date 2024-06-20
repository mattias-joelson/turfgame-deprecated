package org.joelson.turf.idioten.db;

import org.joelson.turf.idioten.model.AssistData;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseEntityManagerTest {

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
    public void testAddTake() {
        ZoneData zone = new ZoneData(17, "Donken");
        UserData user = new UserData(4711, "Gurkan");
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        TakeData take = new TakeData(zone, user, time);
        entityManager.addTake(take, List.of());

        assertEquals(1, entityManager.getZones().size());
        assertEquals(zone.getId(), entityManager.getZone(zone.getName()).getId());
        assertEquals(zone.getName(), entityManager.getZone(zone.getId()).getName());
        assertNull(entityManager.getZone(zone.getId() + 1));
        assertNull(entityManager.getZone(zone.getName() + "!"));

        assertEquals(1, entityManager.getUsers().size());
        assertEquals(user.getId(), entityManager.getUser(user.getName()).getId());
        assertEquals(user.getName(), entityManager.getUser(user.getId()).getName());
        assertNull(entityManager.getUser(user.getId() + 1));
        assertNull(entityManager.getUser(user.getName() + "!"));

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(1, visits.size());
        assertEquals(take, visits.get(0));

        assertEquals(0, entityManager.getAssists().size());
    }

    @Test
    public void testAddRevisit() {
        ZoneData zone = new ZoneData(167, "SvårZon");
        UserData user = new UserData(71, "Uzam");
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        RevisitData revisit = new RevisitData(zone, user, time);
        entityManager.addRevisit(revisit, List.of());

        assertEquals(1, entityManager.getZones().size());
        assertEquals(zone.getId(), entityManager.getZone(zone.getName()).getId());
        assertEquals(zone.getName(), entityManager.getZone(zone.getId()).getName());
        assertNull(entityManager.getZone(zone.getId() + 1));
        assertNull(entityManager.getZone(zone.getName() + "!"));

        assertEquals(1, entityManager.getUsers().size());
        assertEquals(user.getId(), entityManager.getUser(user.getName()).getId());
        assertEquals(user.getName(), entityManager.getUser(user.getId()).getName());
        assertNull(entityManager.getUser(user.getId() + 1));
        assertNull(entityManager.getUser(user.getName() + "!"));

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(1, visits.size());
        assertEquals(revisit, visits.get(0));

        assertEquals(0, entityManager.getAssists().size());
    }

    @Test
    public void testAddTakeWithAssists() {
        ZoneData zone = new ZoneData(17, "HedvigZon");
        UserData user = new UserData(4711, "Takan");
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        TakeData take = new TakeData(zone, user, time);
        UserData assister1 = new UserData(167, "assist-1");
        UserData assister2 = new UserData(173, "assist-2");
        List<UserData> assisted = List.of(assister1, assister2);
        entityManager.addTake(take, assisted);

        assertEquals(1, entityManager.getZones().size());
        assertEquals(zone.getId(), entityManager.getZone(zone.getName()).getId());
        assertEquals(zone.getName(), entityManager.getZone(zone.getId()).getName());
        assertNull(entityManager.getZone(zone.getId() + 1));
        assertNull(entityManager.getZone(zone.getName() + "!"));

        List<UserData> users = entityManager.getUsers();
        assertEquals(1 + assisted.size(), users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(assister1));
        assertTrue(users.contains(assister2));

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(1, visits.size());
        assertEquals(take, visits.get(0));

        AssistData assistData1 = new AssistData(zone, assister1, time, user);
        AssistData assistData2 = new AssistData(zone, assister2, time, user);
        List<AssistData> assists = entityManager.getAssists();
        assertEquals(assisted.size(), entityManager.getAssists().size());
        AssistData firstAssist = assists.get(0);
        AssistData secondAssist = assists.get(1);
        if (firstAssist.getUser().getName().equals(assister1.getName())) {
            assertEquals(assistData1, firstAssist);
            assertEquals(assistData2, secondAssist);
        } else {
            assertEquals(assistData1, secondAssist);
            assertEquals(assistData2, firstAssist);
        }
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
