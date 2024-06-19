package org.joelson.turf.idioten;

import jakarta.persistence.PersistenceException;
import org.joelson.turf.idioten.db.DatabaseEntityManager;
import org.joelson.turf.idioten.model.RevisitData;
import org.joelson.turf.idioten.model.TakeData;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.ZoneData;
import org.joelson.turf.turfgame.FeedObject;
import org.joelson.turf.turfgame.apiv5.FeedTakeover;
import org.joelson.turf.turfgame.apiv5.User;
import org.joelson.turf.turfgame.apiv5.Zone;
import org.joelson.turf.turfgame.apiv5util.FeedsReader;
import org.joelson.turf.turfgame.util.FeedsPathComparator;
import org.joelson.turf.util.FilesUtil;
import org.joelson.turf.util.TimeUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IdiotenServer implements Runnable {

    private static final Path DB_PATH = Path.of("output", "idioten");

    private DatabaseEntityManager entityManager = null;
    private boolean shutdown = false;

    public static void main(String[] args) throws IOException {
        IdiotenServer server = new IdiotenServer();
        //new Thread(server).start();
        server.openDatabase();
        for (String filename : args) {
            FilesUtil.forEachFile(Path.of(filename), true, new FeedsPathComparator(), server::addVisits);
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

    private void addVisits(Path path) {
        new FeedsReader().handleFeedObjectFile(path, p -> {
        }, this::handleTakeover);
    }

    private void printVisits() {
        //entityManager.getVisits().forEach(System.out::println);
        //entityManager.getAssits().forEach(System.out::println);
    }

    private void handleTakeover(FeedObject feedObject) {
        if (feedObject instanceof FeedTakeover feedTakeover) {
            Zone zone = feedTakeover.getZone();
            User previousOwner = zone.getPreviousOwner();
            User currentOwner = zone.getCurrentOwner();
            User[] assisted = feedTakeover.getAssists();
            ZoneData zoneData = new ZoneData(zone.getId(), zone.getName());
            UserData userData = new UserData(currentOwner.getId(), currentOwner.getName());
            Instant time = TimeUtil.turfAPITimestampToInstant(feedTakeover.getTime());
            List<UserData> assists = Collections.emptyList();
            if (assisted != null && assisted.length > 0) {
                assists = Arrays.stream(assisted).map(user -> new UserData(user.getId(), user.getName())).toList();
            }
            if (previousOwner == null || previousOwner.getId() != currentOwner.getId()) {
                entityManager.addTake(new TakeData(zoneData, userData, time), assists);
            } else {
                entityManager.addRevisit(new RevisitData(zoneData, userData, time), assists);
            }
        }
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
