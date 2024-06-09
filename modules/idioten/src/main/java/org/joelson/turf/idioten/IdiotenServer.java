package org.joelson.turf.idioten;

import jakarta.persistence.PersistenceException;
import org.joelson.turf.idioten.db.DatabaseEntityManager;

import java.nio.file.Files;
import java.nio.file.Path;

public class IdiotenServer implements Runnable {

    private static final Path DB_PATH = Path.of("output", "idioten");

    public static void main(String[] args) {
        new IdiotenServer().run();
    }

    @Override
    public void run() {
        if (Files.exists(DB_PATH) && Files.isDirectory(DB_PATH)) {
            System.out.println(DB_PATH + " exists and is a directory.");
        } else {
            throw new IllegalArgumentException(DB_PATH.toString());
        }

        DatabaseEntityManager entityManager = openDB(DB_PATH, false);
        if (entityManager == null) {
            entityManager = openDB(DB_PATH, true);
        }
        if (entityManager == null) {
            throw new IllegalArgumentException("Could not create DB " + DB_PATH);
        }

        //entityManager.updatePlayers(List.of(new PlayerData(1, "one")), List.of(new PlayerData(2, "two")));
        entityManager.close();
    }

    private DatabaseEntityManager openDB(Path dbPath, boolean createDB) {
        if (!Files.exists(dbPath) || !Files.isDirectory(dbPath)) {
            throw new IllegalArgumentException(dbPath + " is not a directory!");
        }
        try {
            return new DatabaseEntityManager(DatabaseEntityManager.PERSISTENCE_NAME,
                    DatabaseEntityManager.createPersistancePropertyMap(dbPath.toAbsolutePath(), !createDB, createDB));
        } catch (PersistenceException e) {
            return null;
        }
    }
}
