package org.joelson.turf.idioten;

import jakarta.persistence.PersistenceException;
import org.joelson.turf.idioten.controller.FeedImporter;
import org.joelson.turf.idioten.controller.ProgressUpdater;
import org.joelson.turf.idioten.db.DatabaseEntityManager;
import org.joelson.turf.turfgame.util.FeedsPathComparator;
import org.joelson.turf.util.FilesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IdiotenServer implements Runnable {

    private static final Path DB_PATH = Path.of("output", "idioten");

    private DatabaseEntityManager entityManager = null;
    private boolean shutdown = false;

    public static void main(String[] args) throws IOException {
        IdiotenServer server = new IdiotenServer();
        //new Thread(server).start();
        server.openDatabase();
        if (args.length > 0) {
            server.readVisits(args);
        }
        server.printVisits();
        //server.stop();
        server.closeDatabase();
    }

    @Override
    public void run() {
        openDatabase();

        while (true) {
            if (shutdown) {
                break;
            }
        }

        closeDatabase();
    }

    public void stop() {
        shutdown = true;
    }

    private void openDatabase() {
        if (Files.exists(DB_PATH) && Files.isDirectory(DB_PATH)) {
            System.out.println(DB_PATH + " exists and is a directory.");
        } else {
            throw new IllegalArgumentException(DB_PATH.toString());
        }

        entityManager = openDB(DB_PATH, false);
        if (entityManager == null) {
            entityManager = openDB(DB_PATH, true);
        }
        if (entityManager == null) {
            throw new IllegalArgumentException("Could not create DB " + DB_PATH);
        }
    }


    private void closeDatabase() {
        entityManager.close();
    }

    private void readVisits(String[] filenames) throws IOException {
        ProgressUpdater progressUpdater = new ProgressUpdater(entityManager);
        FeedImporter feedImporter = new FeedImporter(entityManager, progressUpdater);
        for (String filename : filenames) {
            FilesUtil.forEachFile(Path.of(filename), true, new FeedsPathComparator(), feedImporter::addVisits);
        }
    }

    private void printVisits() {
        //entityManager.getVisits().forEach(System.out::println);
        //entityManager.getAssits().forEach(System.out::println);
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
