package org.joelson.turf.idioten.db;

import org.joelson.turf.idioten.model.UserData;
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
}
