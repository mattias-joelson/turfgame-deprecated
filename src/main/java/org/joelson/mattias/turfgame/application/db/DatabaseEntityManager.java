package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.RegionDTO;
import org.joelson.mattias.turfgame.application.model.RegionHistoryDTO;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
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
        String sql = "SELECT r from RegionEntity r";
        Query query = entityManager.createQuery(sql);
        List<RegionDTO> regionDTOS = ((List<RegionEntity>) query.getResultList()).stream()
                .map(RegionEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return regionDTOS;
    }
    
    public List<RegionHistoryDTO> getRegionHistory() {
        EntityManager entityManager = createEntityManager();
        String sql = "SELECT r FROM RegionHistoryEntity r";
        Query query = entityManager.createQuery(sql);
        List<RegionHistoryDTO> regionHistoryDTOS = ((List<RegionHistoryEntity>) query.getResultList()).stream()
                .map(RegionHistoryEntity::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return regionHistoryDTOS;
    }
    
    public void updateRegions(Instant from, Iterable<RegionDTO> newRegions, Iterable<RegionDTO> changedRegions) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        newRegions.forEach(regionDTO -> createRegion(entityManager, from, regionDTO));
        changedRegions.forEach(regionDTO -> updateRegion(entityManager, from, regionDTO));
        entityManager.getTransaction().commit();
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
}
