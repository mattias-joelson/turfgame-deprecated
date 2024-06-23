package org.joelson.turf.dailyinc.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class DatabaseEntityManagerTest {

    public static final String PERSISTENCE_H2 = "turfgame-daily-increase-test-h2";

    private DatabaseEntityManager entityManager;

    public DatabaseEntityManager getEntityManager() {
        return entityManager;
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
}
