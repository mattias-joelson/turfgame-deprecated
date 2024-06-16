package org.joelson.turf.idioten.db;

import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.ZoneData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseEntityManagerTest {

    public static final String PERSISTANCE_H2 = "turfgame-idioten-test-h2";

    @Test
    public void testUpdateUsers() {
        DatabaseEntityManager entityManager = new DatabaseEntityManager(PERSISTANCE_H2);
        entityManager.updateUsers(List.of(new UserData(1, "one")), List.of());
        assertEquals(1, entityManager.getUsers().size());
        entityManager.updateUsers(List.of(new UserData(2, "two")), List.of(new UserData(1, "onesize")));
        assertEquals(2, entityManager.getUsers().size());
        assertEquals("onesize", entityManager.getUser(1).getName());
        entityManager.close();
    }

    @Test
    public void testUpdateZones() {
        DatabaseEntityManager entityManager = new DatabaseEntityManager(PERSISTANCE_H2);
        entityManager.updateZones(List.of(new ZoneData(1, "Zonen")), List.of());
        assertEquals(1, entityManager.getZones().size());
        entityManager.updateZones(List.of(new ZoneData(2, "NyZon")), List.of(new ZoneData(1, "Zonett")));
        assertEquals(2, entityManager.getZones().size());
        assertEquals("Zonett", entityManager.getZone(1).getName());
        entityManager.close();
    }
}
