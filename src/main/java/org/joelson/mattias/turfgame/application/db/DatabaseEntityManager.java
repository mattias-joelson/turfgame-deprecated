package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.RegionData;
import org.joelson.mattias.turfgame.application.model.RegionHistoryData;
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
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

public class DatabaseEntityManager {
    
    private static final String JAVAX_PERSISTENCE_JDBC_URL_PROPERTY = "javax.persistence.jdbc.url"; //NON-NLS
    private static final String JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY = "javax.persistence.schema-generation.database.action"; //NON-NLS

    public static final String PERSISTANCE_NAME = "turfgame-h2"; //NON-NLS
    private static final String DATABASE_NAME = "turfgame_h2"; //NON-NLS
    
    private EntityManagerFactory entityManagerFactory;
    
    public DatabaseEntityManager(String unit) {
        entityManagerFactory = Persistence.createEntityManagerFactory(unit);
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
    
    private EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public RegionData getRegion(String name) {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r FROM RegionEntity r WHERE r.name = :name"); //NON-NLS
        query.setParameter("name", name); //NON-NLS
        RegionData region = ((RegionEntity) query.getSingleResult()).toData();
        entityManager.close();
        return region;
    }

    public List<RegionData> getRegions() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r from RegionEntity r"); //NON-NLS
        @SuppressWarnings("unchecked")
        List<RegionData> regions = ((Stream<RegionEntity>) query.getResultStream())
                .map(RegionEntity::toData)
                .collect(Collectors.toList());
        entityManager.close();
        return regions;
    }
    
    public List<RegionHistoryData> getRegionHistory() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r FROM RegionHistoryEntity r"); //NON-NLS
        @SuppressWarnings("unchecked")
        List<RegionHistoryData> regionHistory = ((Stream<RegionHistoryEntity>) query.getResultStream())
                .map(RegionHistoryEntity::toData)
                .collect(Collectors.toList());
        entityManager.close();
        return regionHistory;
    }
    
    public void updateRegions(Instant from, Iterable<RegionData> newRegions, Iterable<RegionData> changedRegions) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        newRegions.forEach(regionData -> createRegion(entityManager, from, regionData));
        changedRegions.forEach(regionData -> updateRegion(entityManager, from, regionData));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    private static void createRegion(EntityManager entityManager, Instant from, RegionData regionData) {
        RegionEntity region = RegionEntity.build(regionData.getId(), regionData.getName(), regionData.getCountry());
        entityManager.persist(region);
        persistRegionHistory(entityManager, region, from, regionData);
    }
    
    private static void updateRegion(EntityManager entityManager, Instant from, RegionData regionData) {
        RegionEntity region = entityManager.find(RegionEntity.class, regionData.getId());
        region.setName(regionData.getName());
        region.setCountry(regionData.getCountry());
        entityManager.persist(region);
        persistRegionHistory(entityManager, region, from, regionData);
    }
    
    private static void persistRegionHistory(EntityManager entityManager, RegionEntity region, Instant from, RegionData regionData) {
        RegionHistoryEntity regionHistory = RegionHistoryEntity.build(region, from, regionData.getName(), regionData.getCountry());
        entityManager.persist(regionHistory);
    }
    
    public ZoneData getZone(String name) {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZoneEntity z WHERE z.name = :name"); //NON-NLS
        query.setParameter("name", name); //NON-NLS
        ZoneData zone = ((ZoneEntity) query.getSingleResult()).toData();
        entityManager.close();
        return zone;
    }
    
    public List<ZoneData> getZones() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZoneEntity z"); //NON-NLS
        @SuppressWarnings("unchecked")
        List<ZoneData> zones = ((Stream<ZoneEntity>) query.getResultStream())
                .map(ZoneEntity::toData)
                .collect(Collectors.toList());
        entityManager.close();
        return zones;
    }
    
    public List<ZoneHistoryData> getZoneDataHistory() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZoneHistoryEntity z"); //NON-NLS
        @SuppressWarnings("unchecked")
        List<ZoneHistoryData> zones = ((Stream<ZoneHistoryEntity>) query.getResultStream())
                .map(ZoneHistoryEntity::toData)
                .collect(Collectors.toList());
        entityManager.close();
        return zones;
    }
    
    public List<ZonePointsHistoryData> getZonePointsHistory() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZonePointsHistoryEntity z"); //NON-NLS
        @SuppressWarnings("unchecked")
        List<ZonePointsHistoryData> zones = ((Stream<ZonePointsHistoryEntity>) query.getResultStream())
                .map(ZonePointsHistoryEntity::toData)
                .collect(Collectors.toList());
        entityManager.close();
        return zones;
    }
    
    public void updateZones(Instant from, Iterable<ZoneData> newZones, Iterable<ZoneData> changedZones) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        newZones.forEach(zoneData -> createZone(entityManager, from, zoneData));
        changedZones.forEach(zoneData -> updateZone(entityManager, from, zoneData));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    private static void createZone(EntityManager entityManager, Instant from, ZoneData zoneData) {
        RegionEntity region = entityManager.find(RegionEntity.class, zoneData.getRegion().getId());
        ZoneEntity zone = ZoneEntity.build(zoneData.getId(), zoneData.getName(), region, zoneData.getDateCreated(), zoneData.getLatitude(),
                zoneData.getLongitude(), zoneData.getTp(), zoneData.getPph());
        entityManager.persist(zone);
        persistZoneDataHistory(entityManager, zone, from, zoneData);
        persistZonePointsHistory(entityManager, zone, from, zoneData);
    }
    
    private static void updateZone(EntityManager entityManager, Instant from, ZoneData zoneData) {
        ZoneEntity zone = entityManager.find(ZoneEntity.class, zoneData.getId());
        RegionEntity region = entityManager.find(RegionEntity.class, zoneData.getRegion().getId());
        if (hasChangedData(zone, zoneData)) {
            persistZoneDataHistory(entityManager, zone, from, zoneData);
        }
        if (hasChangedPoints(zone, zoneData)) {
            persistZonePointsHistory(entityManager, zone, from, zoneData);
        }
        zone.setName(zoneData.getName());
        zone.setRegion(region);
        zone.setDateCreated(zoneData.getDateCreated());
        zone.setLatitude(zoneData.getLatitude());
        zone.setLongitude(zoneData.getLongitude());
        zone.setTp(zoneData.getTp());
        zone.setPph(zoneData.getPph());
        entityManager.persist(zone);
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
    
    private static void persistZoneDataHistory(EntityManager entityManager, ZoneEntity zone, Instant from, ZoneData zoneData) {
        RegionEntity region = entityManager.find(RegionEntity.class, zoneData.getRegion().getId());
        ZoneHistoryEntity zoneHistory = ZoneHistoryEntity.build(zone, from, zoneData.getName(), region, zoneData.getDateCreated(),
                zoneData.getLatitude(), zoneData.getLongitude());
        entityManager.persist(zoneHistory);
    }

    private static void persistZonePointsHistory(EntityManager entityManager, ZoneEntity zone, Instant from, ZoneData zoneData) {
        ZonePointsHistoryEntity zonePointsHistory = ZonePointsHistoryEntity.build(zone, from, zoneData.getTp(), zoneData.getPph());
        entityManager.persist(zonePointsHistory);
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
}
