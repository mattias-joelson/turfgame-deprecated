package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.AssistData;
import org.joelson.mattias.turfgame.application.model.RegionData;
import org.joelson.mattias.turfgame.application.model.RegionHistoryData;
import org.joelson.mattias.turfgame.application.model.TakeData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitData;
import org.joelson.mattias.turfgame.application.model.ZoneData;
import org.joelson.mattias.turfgame.application.model.ZoneHistoryData;
import org.joelson.mattias.turfgame.application.model.ZonePointsHistoryData;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class DatabaseEntityManager {
    
    private final class Transaction implements AutoCloseable {
        
        private Transaction() {
            startTransaction();
        }
    
        private void startTransaction() {
            if (entityManager != null) {
                throw new IllegalStateException("Starting new transaction inside existing.");
            }
            entityManager = entityManagerFactory.createEntityManager();
            regionRegistry = new RegionRegistry(entityManager);
            regionHistoryRegistry = new RegionHistoryRegistry(entityManager);
            takeRegistry = new TakeRegistry(entityManager);
            userRegistry = new UserRegistry(entityManager);
            visitRegistry = new VisitRegistry(entityManager);
            zoneRegistry = new ZoneRegistry(entityManager);
            zoneHistoryRegistry = new ZoneHistoryRegistry(entityManager);
            zonePointsHistoryRegistry = new ZonePointsHistoryRegistry(entityManager);
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
                throw new IllegalStateException("Stoping non existing transaction.");
            }
            entityManager.close();
            entityManager = null;
            regionRegistry = null;
            regionHistoryRegistry = null;
            takeRegistry = null;
            userRegistry = null;
            visitRegistry = null;
            zoneRegistry = null;
            zoneHistoryRegistry = null;
            zonePointsHistoryRegistry = null;
        }
    
    }
    
    private static final String JAVAX_PERSISTENCE_JDBC_URL_PROPERTY = "javax.persistence.jdbc.url"; //NON-NLS
    private static final String JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY = "javax.persistence.schema-generation.database.action"; //NON-NLS

    public static final String PERSISTANCE_NAME = "turfgame-h2"; //NON-NLS
    private static final String DATABASE_NAME = "turfgame_h2"; //NON-NLS
    
    private final EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;
    private RegionRegistry regionRegistry;
    private RegionHistoryRegistry regionHistoryRegistry;
    private TakeRegistry takeRegistry;
    private UserRegistry userRegistry;
    private VisitRegistry visitRegistry;
    private ZoneRegistry zoneRegistry;
    private ZoneHistoryRegistry zoneHistoryRegistry;
    private ZonePointsHistoryRegistry zonePointsHistoryRegistry;
    
    public DatabaseEntityManager(String unit) {
        this(unit, null);
    }
    
    public DatabaseEntityManager(String unit, Map<String, String> properties) throws PersistenceException {
        entityManagerFactory  = Persistence.createEntityManagerFactory(unit, properties);
    }
    
    public void importDatabase(Path importFile) throws SQLException {
        executeSQL(String.format("RUNSCRIPT FROM '%s'", importFile)); //NON-NLS
    }
    
    public void exportDatabase(Path exportFile) throws SQLException {
        executeSQL(String.format("SCRIPT TO '%s'", exportFile)); //NON-NLS
    }
    
    private void executeSQL(String sql) throws SQLException {
        Map<String, Object> properties = entityManagerFactory.getProperties();
        String jdbcURL = String.valueOf(properties.get(JAVAX_PERSISTENCE_JDBC_URL_PROPERTY));
        try (Connection connection = DriverManager.getConnection(jdbcURL);
                Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    
    }
    
    public void close() {
        entityManagerFactory.close();
    }
    
    public RegionData getRegion(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return regionRegistry.findAnyByName(name).toData();
        }
    }

    public List<RegionData> getRegions() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return regionRegistry.findAll()
                    .map(RegionEntity::toData)
                    .collect(Collectors.toList());
        }
    }
    
    public List<RegionHistoryData> getRegionHistory() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return regionHistoryRegistry.findAll()
                    .map(RegionHistoryEntity::toData)
                    .collect(Collectors.toList());
        }
    }
    
    public void updateRegions(Instant from, Iterable<RegionData> newRegions, Iterable<RegionData> changedRegions) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            newRegions.forEach(regionData -> createRegion(from, regionData));
            changedRegions.forEach(regionData -> updateRegion(from, regionData));
            transaction.commit();
        }
    }
    
    private void createRegion(Instant from, RegionData regionData) {
        RegionEntity region = regionRegistry.create(regionData);
        regionHistoryRegistry.create(region, from, regionData);
    }
    
    private void updateRegion(Instant from, RegionData regionData) {
        RegionEntity region = regionRegistry.find(regionData.getId());
        region.setName(regionData.getName());
        region.setCountry(regionData.getCountry());
        regionRegistry.persist(region);
        regionHistoryRegistry.create(region, from, regionData);
    }
    
    public ZoneData getZone(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneRegistry.findAnyByName(name).toData();
        }
    }
    
    public List<ZoneData> getZones() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneRegistry.findAll()
                    .map(ZoneEntity::toData)
                    .collect(Collectors.toList());
        }
    }
    
    public List<ZoneHistoryData> getZoneDataHistory() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneHistoryRegistry.findAll()
                    .map(ZoneHistoryEntity::toData)
                    .collect(Collectors.toList());
        }
    }
    
    public List<ZonePointsHistoryData> getZonePointsHistory() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zonePointsHistoryRegistry.findAll()
                    .map(ZonePointsHistoryEntity::toData)
                    .collect(Collectors.toList());
        }
    }
    
    public void updateZones(Instant from, Iterable<ZoneData> newZones, Iterable<ZoneData> changedZones) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            newZones.forEach(zoneData -> createZone(from, zoneData));
            changedZones.forEach(zoneData -> updateZone(from, zoneData));
            transaction.commit();
        }
    }
    
    private void createZone(Instant from, ZoneData zoneData) {
        RegionEntity region = regionRegistry.find(zoneData.getRegion().getId());
        ZoneEntity zone = zoneRegistry.create(region, zoneData);
        zoneHistoryRegistry.create(zone, from, region, zoneData);
        zonePointsHistoryRegistry.create(zone, from, zoneData);
    }
    
    private void updateZone(Instant from, ZoneData zoneData) {
        ZoneEntity zone = zoneRegistry.find(zoneData.getId());
        RegionEntity region = regionRegistry.find(zoneData.getRegion().getId());
        if (hasChangedData(zone, zoneData)) {
            zoneHistoryRegistry.create(zone, from, region, zoneData);
        }
        if (hasChangedPoints(zone, zoneData)) {
            zonePointsHistoryRegistry.create(zone, from, zoneData);
        }
        zone.setName(zoneData.getName());
        zone.setRegion(region);
        zone.setDateCreated(zoneData.getDateCreated());
        zone.setLatitude(zoneData.getLatitude());
        zone.setLongitude(zoneData.getLongitude());
        zone.setTp(zoneData.getTp());
        zone.setPph(zoneData.getPph());
        zoneRegistry.persist(zone);
    }
    
    private static boolean hasChangedData(ZoneEntity storedZone, ZoneData zoneData) {
        return !Objects.equals(storedZone.getName(), zoneData.getName())
                || storedZone.getRegion().getId() != zoneData.getRegion().getId()
                || !Objects.equals(storedZone.getDateCreated(), zoneData.getDateCreated())
                || storedZone.getLatitude() != zoneData.getLatitude()
                || storedZone.getLongitude() != zoneData.getLongitude();
    }
    
    private static boolean hasChangedPoints(ZoneEntity storedZone, ZoneData zoneData) {
        return storedZone.getTp() != zoneData.getTp()
                || storedZone.getPph() != zoneData.getPph();
    }
    
    public static Map<String, String> createPersistancePropertyMap(Path directoryPath, boolean openExisting, boolean dropAndCreateTables) {
        return Map.of(
                JAVAX_PERSISTENCE_JDBC_URL_PROPERTY, createJdbcURL(directoryPath, openExisting),
                JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY, (dropAndCreateTables) ? "drop-and-create" : "none"); //NON-NLS
    }
    
    private static String createJdbcURL(Path directoryPath, boolean openExisting) {
        return String.format("jdbc:h2:%s/%s;IFEXISTS=%s;", //NON-NLS
                directoryPath, DATABASE_NAME, (openExisting) ? "TRUE" : "FALSE"); //NON-NLS
    }
    
    public List<UserData> getUsers() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return userRegistry.findAll()
                    .map(UserEntity::toData)
                    .collect(Collectors.toList());
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
    
    public void updateVisits(Iterable<VisitData> visits) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            for (VisitData visitData : visits) {
                TakeEntity take = updateTake(visitData);
                if (visitData instanceof TakeData) {
                    updateVisit(take, userRegistry.find(visitData.getTaker().getId()), VisitType.TAKE);
                } else if (visitData instanceof AssistData) {
                    updateVisit(take, userRegistry.find(visitData.getTaker().getId()), VisitType.TAKE);
                    updateVisit(take, userRegistry.find(((AssistData) visitData).getAssister().getId()), VisitType.ASSIST);
                } else {
                    throw new RuntimeException("Not instantiated yet!");
                }
            }
            transaction.commit();
        }
    }
    
    private TakeEntity updateTake(VisitData visitData) {
        TakeEntity take = takeRegistry.find(zoneRegistry.find(visitData.getZone().getId()), visitData.getWhen());
        if (take == null) {
            take = TakeEntity.build(zoneRegistry.find(visitData.getZone().getId()), visitData.getWhen());
            takeRegistry.persist(take);
        }
        return take;
    }
    
    private void updateVisit(TakeEntity take, UserEntity user, VisitType type) {
        if (visitRegistry.find(take, user) == null) {
            VisitEntity visit = VisitEntity.build(take, user, type);
            visitRegistry.persist(visit);
        }
    }
}
