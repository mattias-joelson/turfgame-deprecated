package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.RegionDTO;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
    
    private static RegionDTO toDTO(RegionEntity region) {
        return new RegionDTO(region.getId(), region.getName(), region.getCountry());
    }
    
    public RegionDTO getRegion(String name) {
        EntityManager entityManager = createEntityManager();
        Query query = entityManager.createQuery("SELECT r FROM RegionEntity  r WHERE r.name = :name");
        query.setParameter("name", name);
        RegionDTO region = toDTO((RegionEntity) query.getSingleResult());
        entityManager.close();
        return region;
    }

    public List<RegionDTO> getRegions() {
        EntityManager entityManager = createEntityManager();
        String sql = "SELECT r from RegionEntity r";
        Query query = entityManager.createQuery(sql);
        List<RegionEntity> regionEntities = query.getResultList();
        List<RegionDTO> regionDTOS = regionEntities.stream()
                .map(DatabaseEntityManager::toDTO)
                .collect(Collectors.toList());
        entityManager.close();
        return regionDTOS;
    }
    
    public void updateRegions(List<RegionDTO> newRegions, List<RegionDTO> changedRegions) {
        EntityManager entityManager = createEntityManager();
        entityManager.getTransaction().begin();
        newRegions.forEach(regionDTO -> entityManager.persist(new RegionEntity(regionDTO.getId(), regionDTO.getName(), regionDTO.getCountry())));
        changedRegions.forEach(regionDTO -> {
            RegionEntity regionEntity = entityManager.find(RegionEntity.class, regionDTO.getId());
            regionEntity.setName(regionDTO.getName());
            regionEntity.setCountry(regionDTO.getCountry());
            entityManager.persist(regionEntity);
        });
        entityManager.getTransaction().commit();
    }
    
}
