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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseEntityManagerVisitTest {

    public static final String PERSISTENCE_H2 = "turfgame-idioten-test-h2";
    ZoneData ZONE = new ZoneData(17, "Donken");
    UserData USER = new UserData(4711, "Gurkan");

    private DatabaseEntityManager entityManager;

    private static Instant getTruncatedInstantNow() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
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
    public void testAddTake() {
        Instant time = getTruncatedInstantNow();
        TakeData take = new TakeData(ZONE, USER, time);
        entityManager.addTake(take, List.of());

        assertEquals(1, entityManager.getZones().size());
        assertEquals(ZONE.getId(), entityManager.getZone(ZONE.getName()).getId());
        assertEquals(ZONE.getName(), entityManager.getZone(ZONE.getId()).getName());
        assertNull(entityManager.getZone(ZONE.getId() + 1));
        assertNull(entityManager.getZone(ZONE.getName() + "!"));

        assertEquals(1, entityManager.getUsers().size());
        assertEquals(USER.getId(), entityManager.getUser(USER.getName()).getId());
        assertEquals(USER.getName(), entityManager.getUser(USER.getId()).getName());
        assertNull(entityManager.getUser(USER.getId() + 1));
        assertNull(entityManager.getUser(USER.getName() + "!"));

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(1, visits.size());
        assertEquals(take, visits.get(0));

        assertEquals(0, entityManager.getAssists().size());
    }

    @Test
    public void testAddRevisit() {
        Instant time = getTruncatedInstantNow();
        RevisitData revisit = new RevisitData(ZONE, USER, time);
        entityManager.addRevisit(revisit, List.of());

        assertEquals(1, entityManager.getZones().size());
        assertEquals(ZONE.getId(), entityManager.getZone(ZONE.getName()).getId());
        assertEquals(ZONE.getName(), entityManager.getZone(ZONE.getId()).getName());
        assertNull(entityManager.getZone(ZONE.getId() + 1));
        assertNull(entityManager.getZone(ZONE.getName() + "!"));

        assertEquals(1, entityManager.getUsers().size());
        assertEquals(USER.getId(), entityManager.getUser(USER.getName()).getId());
        assertEquals(USER.getName(), entityManager.getUser(USER.getId()).getName());
        assertNull(entityManager.getUser(USER.getId() + 1));
        assertNull(entityManager.getUser(USER.getName() + "!"));

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(1, visits.size());
        assertEquals(revisit, visits.get(0));

        assertEquals(0, entityManager.getAssists().size());
    }

    @Test
    public void testAddTakeWithAssists() {
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        TakeData take = new TakeData(ZONE, USER, time);
        UserData assister1 = new UserData(167, "assist-1");
        UserData assister2 = new UserData(173, "assist-2");
        List<UserData> assisted = List.of(assister1, assister2);
        entityManager.addTake(take, assisted);

        assertEquals(1, entityManager.getZones().size());
        assertEquals(ZONE.getId(), entityManager.getZone(ZONE.getName()).getId());
        assertEquals(ZONE.getName(), entityManager.getZone(ZONE.getId()).getName());
        assertNull(entityManager.getZone(ZONE.getId() + 1));
        assertNull(entityManager.getZone(ZONE.getName() + "!"));

        List<UserData> users = entityManager.getUsers();
        assertEquals(1 + assisted.size(), users.size());
        assertTrue(users.contains(USER));
        assertTrue(users.contains(assister1));
        assertTrue(users.contains(assister2));

        List<VisitData> visits = entityManager.getVisits();
        assertEquals(1, visits.size());
        assertEquals(take, visits.get(0));

        AssistData assistData1 = new AssistData(ZONE, assister1, time, USER);
        AssistData assistData2 = new AssistData(ZONE, assister2, time, USER);
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
}
