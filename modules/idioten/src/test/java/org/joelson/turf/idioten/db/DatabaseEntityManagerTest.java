package org.joelson.mattias.turfgame.idioten.db;

import org.joelson.mattias.turfgame.idioten.model.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseEntityManagerTest {

    public static final String PERSISTANCE_H2 = "turfgame-idioten-test-h2";

    @Test
    public void testUpdatePlayers() {
        DatabaseEntityManager entityManager = new DatabaseEntityManager(PERSISTANCE_H2);
        entityManager.updatePlayers(List.of(new PlayerData(1, "one")), List.of());
        assertEquals(1, entityManager.getPlayers().size());
        entityManager.updatePlayers(List.of(new PlayerData(2, "two")), List.of(new PlayerData(1, "onesize")));
        assertEquals(2, entityManager.getPlayers().size());
        assertEquals("onesize", entityManager.getPlayer(1).getName());
        entityManager.close();
    }
}