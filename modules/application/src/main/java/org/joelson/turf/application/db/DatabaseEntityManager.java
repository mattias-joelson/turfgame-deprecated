package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import org.joelson.turf.application.model.AssistData;
import org.joelson.turf.application.model.MunicipalityData;
import org.joelson.turf.application.model.RegionData;
import org.joelson.turf.application.model.RegionHistoryData;
import org.joelson.turf.application.model.RevisitData;
import org.joelson.turf.application.model.TakeData;
import org.joelson.turf.application.model.UserData;
import org.joelson.turf.application.model.VisitData;
import org.joelson.turf.application.model.ZoneData;
import org.joelson.turf.application.model.ZoneHistoryData;
import org.joelson.turf.application.model.ZonePointsHistoryData;
import org.joelson.turf.application.model.ZoneVisitData;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseEntityManager {

    public static final String PERSISTANCE_NAME = "turfgame-h2";
    private static final String JAVAX_PERSISTENCE_JDBC_URL_PROPERTY = "jakarta.persistence.jdbc.url";
    private static final String JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY
            = "jakarta.persistence.schema-generation.database.action";
    private static final String DATABASE_NAME = "turfgame_h2";
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private MunicipalityRegistry municipalityRegistry;
    private MunicipalityZoneRegistry municipalityZoneRegistry;
    private RegionRegistry regionRegistry;
    private RegionHistoryRegistry regionHistoryRegistry;
    private TakeRegistry takeRegistry;
    private UserRegistry userRegistry;
    private VisitRegistry visitRegistry;
    private ZoneRegistry zoneRegistry;
    private ZoneHistoryRegistry zoneHistoryRegistry;
    private ZonePointsHistoryRegistry zonePointsHistoryRegistry;
    private ZoneVisitRegistry zoneVisitRegistry;

    public DatabaseEntityManager(String unit) {
        this(unit, null);
    }

    public DatabaseEntityManager(String unit, Map<String, String> properties) throws PersistenceException {
        entityManagerFactory = Persistence.createEntityManagerFactory(unit, properties);
    }

    private static boolean hasChangedData(ZoneEntity storedZone, ZoneData zoneData) {
        return !Objects.equals(storedZone.getName(), zoneData.getName())
                || storedZone.getRegion().getId() != zoneData.getRegion().getId()
                || !Objects.equals(storedZone.getDateCreated(), zoneData.getDateCreated())
                || storedZone.getLatitude() != zoneData.getLatitude()
                || storedZone.getLongitude() != zoneData.getLongitude();
    }

    private static boolean hasChangedPoints(ZoneEntity storedZone, ZoneData zoneData) {
        return storedZone.getTp() != zoneData.getTp() || storedZone.getPph() != zoneData.getPph();
    }

    public static Map<String, String> createPersistancePropertyMap(
            Path directoryPath, boolean openExisting, boolean dropAndCreateTables) {
        return Map.of(JAVAX_PERSISTENCE_JDBC_URL_PROPERTY, createJdbcURL(directoryPath, openExisting),
                JAVAX_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION_PROPERTY,
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
            return regionRegistry.findByName(name).toData();
        }
    }

    public List<RegionData> getRegions() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return regionRegistry.findAll().map(RegionEntity::toData).toList();
        }
    }

    public List<RegionHistoryData> getRegionHistory() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return regionHistoryRegistry.findAll().map(RegionHistoryEntity::toData).toList();
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
            return zoneRegistry.findByName(name).toData();
        }
    }

    public List<ZoneData> getZones() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneRegistry.findAll().map(ZoneEntity::toData).toList();
        }
    }

    public List<ZoneHistoryData> getZoneDataHistory() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneHistoryRegistry.findAll().map(ZoneHistoryEntity::toData).toList();
        }
    }

    public List<ZonePointsHistoryData> getZonePointsHistory() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zonePointsHistoryRegistry.findAll().map(ZonePointsHistoryEntity::toData).toList();
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

    public List<UserData> getUsers() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return userRegistry.findAll().map(UserEntity::toData).toList();
        }
    }

    public UserData getUser(int id) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            UserEntity user = userRegistry.find(id);
            return (user != null) ? user.toData() : null;
        }
    }

    public UserData getUser(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return userRegistry.findAnyOrNull("name", name).toData();
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

    public List<VisitData> getVisits(UserData userData) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            UserEntity user = userRegistry.find(userData.getId());
            List<VisitEntity> visits = visitRegistry.findAll("user", user).toList();
            return visits.stream().map(visit -> getVisitData(userData, visit)).toList();
        }
    }

    private VisitData getVisitData(UserData userData, VisitEntity visit) {
        TakeEntity take = visit.getTake();
        Instant when = take.getWhen();
        ZoneEntity zone = take.getZone();
        ZoneHistoryEntity zoneHistory = zoneHistoryRegistry.findLatestBefore(zone, when);
        if (zoneHistory == null) {
            System.out.println("Zone " + zone.getName() + " stored after visit data.");
            zoneHistory = zoneHistoryRegistry.findLatest(zone);
        }
        ZonePointsHistoryEntity zonePointsHistory = zonePointsHistoryRegistry.findLatestBefore(zone, when);
        if (zonePointsHistory == null) {
            System.out.println("Zone " + zone.getName() + " stored after visit data.");
            zonePointsHistory = zonePointsHistoryRegistry.findLatest(zone);
        }

        ZoneData zoneData = new ZoneData(zone.getId(), zone.getName(), zoneHistory.getRegion().toData(),
                zoneHistory.getDateCreated(), zoneHistory.getLatitude(), zoneHistory.getLongitude(),
                zonePointsHistory.getTp(), zonePointsHistory.getPph());
        return switch (visit.getType()) {
            case TAKE -> new TakeData(zoneData, when, userData, getVisitDuration(visit));
            case ASSIST -> {
                UserEntity taker = take.getTakeVisit().getUser();
                yield new AssistData(zoneData, when, taker.toData(), userData);
            }
            case REVISIT -> new RevisitData(zoneData, when, userData);
            default -> throw new IllegalStateException("Unknown visit type " + visit.getType());
        };
    }

    public List<VisitData> getAllVisits() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return visitRegistry.findAll().map(visit -> getVisitData(visit.getUser().toData(), visit)).toList();
        }
    }

    private VisitData getVisitDataOld(VisitEntity visit) {
        TakeEntity take = visit.getTake();
        Instant when = take.getWhen();
        ZoneEntity zone = take.getZone();
        ZoneHistoryEntity zoneHistory = zoneHistoryRegistry.findLatestBefore(zone, when);
        if (zoneHistory == null) {
            System.out.println("Zone " + zone.getName() + " stored after visit data.");
            zoneHistory = zoneHistoryRegistry.findLatest(zone);
        }
        ZonePointsHistoryEntity zonePointsHistory = zonePointsHistoryRegistry.findLatestBefore(zone, when);
        if (zonePointsHistory == null) {
            System.out.println("Zone " + zone.getName() + " stored after visit data.");
            zonePointsHistory = zonePointsHistoryRegistry.findLatest(zone);
        }

        ZoneData zoneData = new ZoneData(zone.getId(), zone.getName(), zoneHistory.getRegion().toData(),
                zoneHistory.getDateCreated(), zoneHistory.getLatitude(), zoneHistory.getLongitude(),
                zonePointsHistory.getTp(), zonePointsHistory.getPph());
        UserData userData = visit.getUser().toData();
        return switch (visit.getType()) {
            case TAKE -> new TakeData(zoneData, when, userData, getVisitDuration(visit));
            case ASSIST -> {
                UserEntity taker = take.getTakeVisit().getUser();
                yield new AssistData(zoneData, when, taker.toData(), userData);
            }
            case REVISIT -> new RevisitData(zoneData, when, userData);
            default -> throw new IllegalStateException("Unknown visit type " + visit.getType());
        };
    }

    private Duration getVisitDuration(VisitEntity visit) {
        Optional<TakeEntity> loss = takeRegistry.findAfter(visit.getTake().getZone(), visit.getTake().getWhen())
                .findFirst();
        if (loss.isPresent()) {
            return Duration.between(visit.getTake().getWhen(), loss.get().getWhen());
        }
        return null;
    }

    public void updateVisits(Iterable<VisitData> visits) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            for (VisitData visitData : visits) {
                TakeEntity take = updateTake(visitData);
                switch (visitData) {
                    case TakeData takeData -> updateVisit(take, userRegistry.find(visitData.getTaker().getId()),
                            VisitType.TAKE);
                    case AssistData assistData -> {
                        updateVisit(take, userRegistry.find(visitData.getTaker().getId()), VisitType.TAKE);
                        updateVisit(take, userRegistry.find(assistData.getAssister().getId()), VisitType.ASSIST);
                    }
                    case RevisitData revisitData -> updateVisit(take, userRegistry.find(visitData.getTaker().getId()),
                            VisitType.REVISIT);
                    default -> throw new RuntimeException("Not instantiated yet!");
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
        if (take.getVisits().stream().noneMatch(visitEntity -> visitEntity.getUser().equals(user))) {
            VisitEntity visit = VisitEntity.build(take, user, type);
            visitRegistry.persist(visit);
        }
    }

    public List<UserData> getZoneVisitUsers() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return zoneVisitRegistry.findAllUsers().map(UserEntity::toData).toList();
        }
    }

    public List<ZoneVisitData> getZoneVisits(UserData userData) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            UserEntity user = userRegistry.find(userData.getId());
            return zoneVisitRegistry.findAll("user", user).map(ZoneVisitEntity::toData).toList();
        }
    }

    public void updateZoneVisits(Iterable<ZoneVisitData> zoneVisits) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            for (ZoneVisitData zoneVisitData : zoneVisits) {
                updateZoneVisit(zoneVisitData);
            }
            transaction.commit();
        }
    }

    private void updateZoneVisit(ZoneVisitData zoneVisitData) {
        UserEntity user = userRegistry.find(zoneVisitData.getUser().getId());
        ZoneEntity zone = zoneRegistry.find(zoneVisitData.getZone().getId());
        ZoneVisitEntity zoneVisit = zoneVisitRegistry.find(user, zone);
        if (zoneVisit == null) {
            zoneVisitRegistry.create(user, zone, zoneVisitData.getVisits());
        } else {
            zoneVisit.setVisits(zoneVisitData.getVisits());
        }
    }

    public MunicipalityData getMunicipality(String name) {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            MunicipalityEntity municipality = municipalityRegistry.findByName(name);
            return (municipality != null) ? municipality.toData() : null;
        }
    }

    public List<MunicipalityData> getMunicipalities() {
        try (Transaction transaction = new Transaction()) {
            transaction.use();
            return municipalityRegistry.findAll().map(MunicipalityEntity::toData).toList();
        }
    }

    public void updateMunicipality(MunicipalityData municipalityData) {
        try (Transaction transaction = new Transaction()) {
            transaction.begin();
            MunicipalityEntity municipality = getOrCreateMunicipality(municipalityData);
            municipalityData.getZones().forEach(zoneData -> updateMunicipalityZone(municipality, zoneData));
            transaction.commit();
        }
    }

    private MunicipalityEntity getOrCreateMunicipality(MunicipalityData municipalityData) {
        RegionEntity region = regionRegistry.find(municipalityData.getRegion().getId());
        String municipalityName = municipalityData.getName();
        MunicipalityEntity municipality = municipalityRegistry.find(region, municipalityName);
        if (municipality == null) {
            municipality = municipalityRegistry.create(region, municipalityName);
        }
        return municipality;
    }

    private void updateMunicipalityZone(MunicipalityEntity municipality, ZoneData zoneData) {
        ZoneEntity zone = zoneRegistry.find(zoneData.getId());
        MunicipalityZoneEntity municipalityZone = municipalityZoneRegistry.findZone(zone);
        if (municipalityZone == null) {
            municipalityZoneRegistry.create(municipality, zone);
        } else if (municipalityZone.getMunicipality().getId() != municipality.getId()) {
            municipalityZone.setMunicipality(municipality);
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
            municipalityRegistry = new MunicipalityRegistry(entityManager);
            municipalityZoneRegistry = new MunicipalityZoneRegistry(entityManager);
            regionRegistry = new RegionRegistry(entityManager);
            regionHistoryRegistry = new RegionHistoryRegistry(entityManager);
            takeRegistry = new TakeRegistry(entityManager);
            userRegistry = new UserRegistry(entityManager);
            visitRegistry = new VisitRegistry(entityManager);
            zoneRegistry = new ZoneRegistry(entityManager);
            zoneHistoryRegistry = new ZoneHistoryRegistry(entityManager);
            zonePointsHistoryRegistry = new ZonePointsHistoryRegistry(entityManager);
            zoneVisitRegistry = new ZoneVisitRegistry(entityManager);
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
            municipalityRegistry = null;
            municipalityZoneRegistry = null;
            regionRegistry = null;
            regionHistoryRegistry = null;
            takeRegistry = null;
            userRegistry = null;
            visitRegistry = null;
            zoneRegistry = null;
            zoneHistoryRegistry = null;
            zonePointsHistoryRegistry = null;
            zoneVisitRegistry = null;
        }
    }
}
