package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.idioten.model.ZoneData;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseEntityManager {

    public static final String PERSISTENCE_NAME = "turfgame-idioten-h2";
    private static final String JAKARTA_PERSISTENCE_JDBC_URL_PROPERTY = "jakarta.persistence.jdbc.url";
    private static final String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY =
            "jakarta.persistence.schema-generation.database.action";
    private static final String DATABASE_NAME = "turfgame_idioten_h2";
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private UserRegistry userRegistry;
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

    public UserData getUser(int id) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            UserEntity userEntity = userRegistry.find(id);
            return (userEntity != null) ? userEntity.toData() : null;
        }
    }

    public UserData getUser(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return userRegistry.findAnyOrNull("name", name).toData();
        }
    }

    public List<UserData> getUsers() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return userRegistry.findAll().map(UserEntity::toData).collect(Collectors.toList());
        }
    }

    public void updateUsers(Iterable<UserData> newUsers, Iterable<UserData> updatedUsers) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            newUsers.forEach(this::createUser);
            updatedUsers.forEach(this::updateUser);
            transaction.commit();
        }
    }

    private void createUser(UserData userData) {
        UserEntity user = UserEntity.build(userData.getId(), userData.getName());
        userRegistry.persist(user);
    }

    private void updateUser(UserData userData) {
        UserEntity user = userRegistry.find(userData.getId());
        user.setName(userData.getName());
        userRegistry.persist(user);
    }

    public ZoneData getZone(int id) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            ZoneEntity zone = zoneRegistry.find(id);
            return (zone != null) ? zone.toData() : null;
        }
    }

    public ZoneData getZone(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneRegistry.findByName(name).toData();
        }
    }

    public List<ZoneData> getZones() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneRegistry.findAll().map(ZoneEntity::toData).collect(Collectors.toList());
        }
    }

    public void updateZones(Iterable<ZoneData> newZones, Iterable<ZoneData> changedZones) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            newZones.forEach(this::createZone);
            changedZones.forEach(this::updateZone);
            transaction.commit();
        }
    }

    private void createZone(ZoneData zoneData) {
        zoneRegistry.create(zoneData);
    }

    private void updateZone(ZoneData zoneData) {
        ZoneEntity zone = zoneRegistry.find(zoneData.getId());
        zone.setName(zoneData.getName());
        zoneRegistry.persist(zone);
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
            userRegistry = new UserRegistry(entityManager);
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
            userRegistry = null;
            zoneRegistry = null;
        }

    }
}
