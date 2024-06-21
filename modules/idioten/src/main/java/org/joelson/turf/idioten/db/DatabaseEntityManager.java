package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import org.joelson.turf.idioten.model.AssistData;
import org.joelson.turf.idioten.model.RevisitData;
import org.joelson.turf.idioten.model.TakeData;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.VisitData;
import org.joelson.turf.idioten.model.ZoneData;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DatabaseEntityManager {

    public static final String PERSISTENCE_NAME = "turfgame-idioten-h2";
    private static final String JAKARTA_PERSISTENCE_JDBC_URL_PROPERTY = "jakarta.persistence.jdbc.url";
    private static final String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY =
            "jakarta.persistence.schema-generation.database.action";
    private static final String DATABASE_NAME = "turfgame_idioten_h2";
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private AssistRegistry assistRegistry;
    private UserRegistry userRegistry;
    private VisitRegistry visitRegistry;
    private ZoneRegistry zoneRegistry;

    public DatabaseEntityManager(String unit) {
        this(unit, null);
    }

    public DatabaseEntityManager(String unit, Map<String, String> properties) throws PersistenceException {
        entityManagerFactory = Persistence.createEntityManagerFactory(unit, properties);
    }

    public static Map<String, String> createPersistancePropertyMap(
            Path directoryPath, boolean openExisting, boolean dropAndCreateTables) {
        return Map.of(JAKARTA_PERSISTENCE_JDBC_URL_PROPERTY, createJdbcURL(directoryPath, openExisting),
                JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY,
                (dropAndCreateTables) ? "drop-and-create" : "none");
    }

    private static String createJdbcURL(Path directoryPath, boolean openExisting) {
        return String.format("jdbc:h2:%s/%s;IFEXISTS=%s;", directoryPath, DATABASE_NAME,
                (openExisting) ? "TRUE" : "FALSE");
    }

    public void importDatabase(Path importFile) throws SQLException {
        executeSQL(String.format("RUNSCRIPT FROM '%s'", importFile));
    }

    public void exportDatabase(Path exportFile) throws SQLException {
        executeSQL(String.format("SCRIPT TO '%s'", exportFile));
    }

    private void executeSQL(String sql) throws SQLException {
        Map<String, Object> properties = entityManagerFactory.getProperties();
        String jdbcURL = String.valueOf(properties.get(JAKARTA_PERSISTENCE_JDBC_URL_PROPERTY));
        try (Connection connection = DriverManager.getConnection(jdbcURL);
                Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void close() {
        entityManagerFactory.close();
    }

    public List<AssistData> getAssists() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return assistRegistry.findAll().map(AssistEntity::toData).toList();
        }

    }

    public UserData getUser(int id) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return toUserData(userRegistry.find(id));
        }
    }

    public UserData getUser(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return toUserData(userRegistry.findByName(name));
        }
    }

    private UserData toUserData(UserEntity user) {
        return (user != null) ? user.toData() : null;
    }

    public List<UserData> getUsers() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return userRegistry.findAll().map(UserEntity::toData).toList();
        }
    }

    private static Stream<VisitData> sortByTime(Stream<VisitData> visits) {
        return visits.sorted(DatabaseEntityManager::compareVisitData);
    }

    private static int compareVisitData(VisitData v1, VisitData v2) {
        int timeCompare = v1.getTime().compareTo(v2.getTime());
        if (timeCompare == 0) {
            if (v2 instanceof AssistData) {
                return (v1 instanceof AssistData) ? 0 : -1;
            } else {
                return (v1 instanceof AssistData) ? 1 : 0;
            }
        } else {
            return timeCompare;
        }
    }

    public List<VisitData> getVisits(ZoneData zoneData) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            ZoneEntity zone = zoneRegistry.find(zoneData.getId());
            if (zone == null) {
                return List.of();
            }
            return sortByTime(visitRegistry.findAllByZone(zone).flatMap(
                    visit -> Stream.concat(Stream.of(visit.toData()),
                            assistRegistry.findAllByVisit(visit).map(AssistEntity::toData)))).toList();
        }
    }

    public List<VisitData> getVisits(UserData userData) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            UserEntity user = userRegistry.find(userData.getId());
            if (user == null) {
                return List.of();
            }
            return sortByTime(Stream.concat(visitRegistry.findAllByUser(user).map(VisitEntity::toData),
                    assistRegistry.findAllByUser(user).map(AssistEntity::toData))).toList();
        }
    }

    public List<VisitData> getVisits(Instant from, Instant to) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return sortByTime(visitRegistry.findAllBetween(from, to).flatMap(
                    visit -> Stream.concat(Stream.of(visit.toData()),
                            assistRegistry.findAllByVisit(visit).map(AssistEntity::toData)))).toList();
        }
    }

    public List<VisitData> getVisits() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return visitRegistry.findAll().map(VisitEntity::toData).toList();
        }
    }

    public List<VisitData> addTake(TakeData takeData, Iterable<UserData> assisted) {
        return addVisit(takeData, VisitType.TAKE, assisted);
    }

    public List<VisitData> addRevisit(RevisitData revisitData, Iterable<UserData> assisted) {
        return addVisit(revisitData, VisitType.REVISIT, assisted);
    }

    private List<VisitData> addVisit(VisitData visitData, VisitType visitType, Iterable<UserData> assisted) {
        try (Transaction transaction = new Transaction()) {
            List<VisitData> visits = new ArrayList<>();
            transaction.begin();
            Instant time = visitData.getTime();
            ZoneEntity zone = zoneRegistry.getUpdateOrCreate(visitData.getZone(), time);
            VisitEntity visit = visitRegistry.find(zone, time);
            if (visit == null) {
                UserEntity user = userRegistry.getUpdateOrCreate(visitData.getUser(), time);
                VisitEntity newVisit = visitRegistry.create(zone, user, time, visitType);
                visits.add(newVisit.toData());
                for (UserData assister : assisted) {
                    AssistEntity assist = assistRegistry.create(newVisit, userRegistry.getUpdateOrCreate(assister, time));
                    visits.add(assist.toData());
                }
            }
            transaction.commit();
            return visits;
        }
    }

    public ZoneData getZone(int id) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return toZoneData(zoneRegistry.find(id));
        }
    }

    public ZoneData getZone(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return toZoneData(zoneRegistry.findByName(name));
        }
    }

    private ZoneData toZoneData(ZoneEntity zone) {
        return (zone != null) ? zone.toData() : null;
    }

    public List<ZoneData> getZones() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneRegistry.findAll().map(ZoneEntity::toData).toList();
        }
    }

    private final class Transaction implements AutoCloseable {

        private Transaction() {
            startTransaction();
        }

        private void startTransaction() {
            if (entityManager != null) {
                throw new IllegalStateException("Starting new transaction inside existing.");
            }
            entityManager = entityManagerFactory.createEntityManager();
            assistRegistry = new AssistRegistry(entityManager);
            userRegistry = new UserRegistry(entityManager);
            visitRegistry = new VisitRegistry(entityManager);
            zoneRegistry = new ZoneRegistry(entityManager);
        }

        void use() {
        }

        void begin() {
            entityManager.getTransaction().begin();
        }

        void commit() {
            entityManager.getTransaction().commit();
        }

        @Override
        public void close() {
            endTransaction();
        }

        private void endTransaction() {
            if (entityManager == null) {
                throw new IllegalStateException("Stopping non existing transaction.");
            }
            entityManager.close();
            entityManager = null;
            assistRegistry = null;
            userRegistry = null;
            visitRegistry = null;
            zoneRegistry = null;
        }
    }
}
