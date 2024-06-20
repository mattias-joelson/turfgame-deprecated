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
}
