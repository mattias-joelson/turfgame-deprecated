package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.RegionDTO;
import org.joelson.mattias.turfgame.application.model.RegionHistoryDTO;
import org.joelson.mattias.turfgame.application.model.ZoneDTO;
import org.joelson.mattias.turfgame.application.model.ZoneDataHistoryDTO;
import org.joelson.mattias.turfgame.application.model.ZonePointsHistoryDTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DatabaseEntityManager {
    
    public static final String PERSISTANCE_DERBY = "turfgame-derby";
    public static final String PERSISTANCE_H2 = "turfgame-h2";
    
    private EntityManagerFactory entityManagerFactory;
    
    public DatabaseEntityManager(String unit) {
        entityManagerFactory = Persistence.createEntityManagerFactory(unit);
    }
    
    public DatabaseEntityManager(String unit, Map<String, String> properties) throws RuntimeException {
        entityManagerFactory  = Persistence.createEntityManagerFactory(unit, properties);
    }
    
    public void close() {
        entityManagerFactory.close();
    }
    
    private EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public RegionDTO getRegion(String name) {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r FROM RegionEntity r WHERE r.name = :name");
        query.setParameter("name", name);
        RegionDTO region = ((RegionEntity) query.getSingleResult()).toDTO();
        entityManager.close();
        return region;
    }

    public List<RegionDTO> getRegions() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r from RegionEntity r");
        List<RegionDTO> regions = ((Stream<RegionEntity>) query.getResultStream())
                .map(RegionEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return regions;
    }
    
    public List<RegionHistoryDTO> getRegionHistory() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r FROM RegionHistoryEntity r");
        List<RegionHistoryDTO> regionHistory = ((Stream<RegionHistoryEntity>) query.getResultStream())
                .map(RegionHistoryEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return regionHistory;
    }
    
    public void updateRegions(Instant from, Iterable<RegionDTO> newRegions, Iterable<RegionDTO> changedRegions) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        newRegions.forEach(regionDTO -> createRegion(entityManager, from, regionDTO));
        changedRegions.forEach(regionDTO -> updateRegion(entityManager, from, regionDTO));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    private static void createRegion(EntityManager entityManager, Instant from, RegionDTO regionDTO) {
        RegionEntity region = RegionEntity.build(regionDTO);
        entityManager.persist(region);
        persistRegionHistory(entityManager, region, from, regionDTO);
    }
    
    private static void updateRegion(EntityManager entityManager, Instant from, RegionDTO regionDTO) {
        RegionEntity region = entityManager.find(RegionEntity.class, regionDTO.getId());
        region.setName(regionDTO.getName());
        region.setCountry(regionDTO.getCountry());
        entityManager.persist(region);
        persistRegionHistory(entityManager, region, from, regionDTO);
    }
    
    private static void persistRegionHistory(EntityManager entityManager, RegionEntity region, Instant from, RegionDTO regionDTO) {
        RegionHistoryEntity regionHistory = RegionHistoryEntity.build(region, from, regionDTO.getName(), regionDTO.getCountry());
        entityManager.persist(regionHistory);
    }
    
    public ZoneDTO getZone(String name) {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZoneEntity z WHERE z.name = :name");
        query.setParameter("name", name);
        ZoneDTO zone = ((ZoneEntity) query.getSingleResult()).toDTO();
        entityManager.close();
        return zone;
    }
    
    public List<ZoneDTO> getZones() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZoneEntity z");
        List<ZoneDTO> zones = ((Stream<ZoneEntity>) query.getResultStream())
                .map(ZoneEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return zones;
    }
    
    public List<ZoneDataHistoryDTO> getZoneDataHistory() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZoneDataHistoryEntity z");
        List<ZoneDataHistoryDTO> zones = ((Stream<ZoneDataHistoryEntity>) query.getResultStream())
                .map(ZoneDataHistoryEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return zones;
    }
    
    public List<ZonePointsHistoryDTO> getZonePointsHistory() {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT z FROM ZonePointsHistoryEntity z");
        List<ZonePointsHistoryDTO> zones = ((Stream<ZonePointsHistoryEntity>) query.getResultStream())
                .map(ZonePointsHistoryEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return zones;
    }
    
    public void updateZones(Instant from, Iterable<ZoneDTO> newZones, Iterable<ZoneDTO> changedZones) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        newZones.forEach(zoneDTO -> createZone(entityManager, from, zoneDTO));
        changedZones.forEach(zoneDTO -> updateZone(entityManager, from, zoneDTO));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    private static void createZone(EntityManager entityManager, Instant from, ZoneDTO zoneDTO) {
        RegionEntity region = entityManager.find(RegionEntity.class, zoneDTO.getRegion().getId());
        ZoneEntity zone = ZoneEntity.build(zoneDTO.getId(), zoneDTO.getName(), region, zoneDTO.getDateCreated(), zoneDTO.getLatitude(), zoneDTO.getLongitude(),
                zoneDTO.getTp(), zoneDTO.getPph());
        entityManager.persist(zone);
        persistZoneDataHistory(entityManager, zone, from, zoneDTO);
        persistZonePointsHistory(entityManager, zone, from, zoneDTO);
    }
    
    private static void updateZone(EntityManager entityManager, Instant from, ZoneDTO zoneDTO) {
        ZoneEntity zone = entityManager.find(ZoneEntity.class, zoneDTO.getId());
        RegionEntity region = entityManager.find(RegionEntity.class, zoneDTO.getRegion().getId());
        if (hasChangedData(zone, zoneDTO)) {
            persistZoneDataHistory(entityManager, zone, from, zoneDTO);
        }
        if (hasChangedPoints(zone, zoneDTO)) {
            persistZonePointsHistory(entityManager, zone, from, zoneDTO);
        }
        zone.setName(zoneDTO.getName());
        zone.setRegion(region);
        zone.setDateCreated(zoneDTO.getDateCreated());
        zone.setLatitude(zoneDTO.getLatitude());
        zone.setLongitude(zoneDTO.getLongitude());
        zone.setTp(zoneDTO.getTp());
        zone.setPph(zoneDTO.getPph());
        entityManager.persist(zone);
    }
    
    private static boolean hasChangedData(ZoneEntity storedZone, ZoneDTO zoneDTO) {
        return !Objects.equals(storedZone.getName(), zoneDTO.getName())
                || storedZone.getRegion().getId() != zoneDTO.getRegion().getId()
                || !Objects.equals(storedZone.getDateCreated(), zoneDTO.getDateCreated())
                || storedZone.getLatitude() != zoneDTO.getLatitude()
                || storedZone.getLongitude() != zoneDTO.getLongitude();
    }
    
    private static boolean hasChangedPoints(ZoneEntity storedZone, ZoneDTO zoneDTO) {
        return storedZone.getTp() != zoneDTO.getTp()
                || storedZone.getPph() != zoneDTO.getPph();
    }
    
    private static void persistZoneDataHistory(EntityManager entityManager, ZoneEntity zone, Instant from, ZoneDTO zoneDTO) {
        RegionEntity region = entityManager.find(RegionEntity.class, zoneDTO.getRegion().getId());
        ZoneDataHistoryEntity zoneDataHistory = ZoneDataHistoryEntity.build(zone, from, zoneDTO.getName(), region, zoneDTO.getDateCreated(),
                zoneDTO.getLatitude(), zoneDTO.getLongitude());
        entityManager.persist(zoneDataHistory);
    }

    private static void persistZonePointsHistory(EntityManager entityManager, ZoneEntity zone, Instant from, ZoneDTO zoneDTO) {
        ZonePointsHistoryEntity zonePointsHistory = ZonePointsHistoryEntity.build(zone, from, zoneDTO.getTp(), zoneDTO.getPph());
        entityManager.persist(zonePointsHistory);
    }
}
