package org.joelson.turf.dailyinc.db;

import org.joelson.turf.dailyinc.model.AssistData;
import org.joelson.turf.dailyinc.model.RevisitData;
import org.joelson.turf.dailyinc.model.TakeData;
import org.joelson.turf.dailyinc.model.UserData;
import org.joelson.turf.dailyinc.model.VisitData;
import org.joelson.turf.dailyinc.model.ZoneData;
import org.joelson.turf.dailyinc.util.InstantUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseEntityManagerVisitTest extends DatabaseEntityManagerTest{

    ZoneData ZONE = new ZoneData(17, "Donken");
    UserData USER = new UserData(4711, "Gurkan");

    @Test
    public void testAddTake() {
        Instant time = InstantUtil.getInstantNowTruncatedToSecond();
        TakeData take = new TakeData(ZONE, USER, time);
        getEntityManager().addTake(take, List.of());

        assertEquals(1, getEntityManager().getZones().size());
        assertEquals(ZONE.getId(), getEntityManager().getZone(ZONE.getName()).getId());
        assertEquals(ZONE.getName(), getEntityManager().getZone(ZONE.getId()).getName());
        assertNull(getEntityManager().getZone(ZONE.getId() + 1));
        assertNull(getEntityManager().getZone(ZONE.getName() + "!"));

        assertEquals(1, getEntityManager().getUsers().size());
        assertEquals(USER.getId(), getEntityManager().getUser(USER.getName()).getId());
        assertEquals(USER.getName(), getEntityManager().getUser(USER.getId()).getName());
        assertNull(getEntityManager().getUser(USER.getId() + 1));
        assertNull(getEntityManager().getUser(USER.getName() + "!"));

        List<VisitData> visits = getEntityManager().getVisits();
        assertEquals(1, visits.size());
        assertEquals(take, visits.get(0));

        assertEquals(0, getEntityManager().getAssists().size());
    }

    @Test
    public void testAddRevisit() {
        Instant time = InstantUtil.getInstantNowTruncatedToSecond();
        RevisitData revisit = new RevisitData(ZONE, USER, time);
        getEntityManager().addRevisit(revisit, List.of());

        assertEquals(1, getEntityManager().getZones().size());
        assertEquals(ZONE.getId(), getEntityManager().getZone(ZONE.getName()).getId());
        assertEquals(ZONE.getName(), getEntityManager().getZone(ZONE.getId()).getName());
        assertNull(getEntityManager().getZone(ZONE.getId() + 1));
        assertNull(getEntityManager().getZone(ZONE.getName() + "!"));

        assertEquals(1, getEntityManager().getUsers().size());
        assertEquals(USER.getId(), getEntityManager().getUser(USER.getName()).getId());
        assertEquals(USER.getName(), getEntityManager().getUser(USER.getId()).getName());
        assertNull(getEntityManager().getUser(USER.getId() + 1));
        assertNull(getEntityManager().getUser(USER.getName() + "!"));

        List<VisitData> visits = getEntityManager().getVisits();
        assertEquals(1, visits.size());
        assertEquals(revisit, visits.get(0));

        assertEquals(0, getEntityManager().getAssists().size());
    }

    @Test
    public void testAddTakeWithAssists() {
        Instant time = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        TakeData take = new TakeData(ZONE, USER, time);
        UserData assister1 = new UserData(167, "assist-1");
        UserData assister2 = new UserData(173, "assist-2");
        List<UserData> assisted = List.of(assister1, assister2);
        getEntityManager().addTake(take, assisted);

        assertEquals(1, getEntityManager().getZones().size());
        assertEquals(ZONE.getId(), getEntityManager().getZone(ZONE.getName()).getId());
        assertEquals(ZONE.getName(), getEntityManager().getZone(ZONE.getId()).getName());
        assertNull(getEntityManager().getZone(ZONE.getId() + 1));
        assertNull(getEntityManager().getZone(ZONE.getName() + "!"));

        List<UserData> users = getEntityManager().getUsers();
        assertEquals(1 + assisted.size(), users.size());
        assertTrue(users.contains(USER));
        assertTrue(users.contains(assister1));
        assertTrue(users.contains(assister2));

        List<VisitData> visits = getEntityManager().getVisits();
        assertEquals(1, visits.size());
        assertEquals(take, visits.get(0));

        AssistData assistData1 = new AssistData(ZONE, assister1, time, USER);
        AssistData assistData2 = new AssistData(ZONE, assister2, time, USER);
        List<AssistData> assists = getEntityManager().getAssists();
        assertEquals(assisted.size(), getEntityManager().getAssists().size());
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
