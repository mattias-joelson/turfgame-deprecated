package org.joelson.mattias.turfgame.application.model;

import org.joelson.mattias.turfgame.apiv4.Region;
import org.joelson.mattias.turfgame.application.db.DatabaseEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RegionData {
    
    private final DatabaseEntityManager dbEntity;
    
    public RegionData(DatabaseEntityManager dbEntity) {
        this.dbEntity = dbEntity;
    }
    
    public RegionDTO getRegion(String name) {
        return dbEntity.getRegion(name);
    }

    public void updateRegions(List<Region> regions) {
        List<RegionDTO> storedRegions = dbEntity.getRegions();
        Map<Integer, RegionDTO> storedRegionsMap = storedRegions.stream().collect(Collectors.toMap(RegionDTO::getId, Function.identity()));
        List<RegionDTO> newRegions = new ArrayList<>();
        List<RegionDTO> changedRegions = new ArrayList<>();
        for (Region region : regions) {
            RegionDTO storedRegion = storedRegionsMap.get(region.getId());
            if (storedRegion == null) {
                newRegions.add(new RegionDTO(region.getId(), region.getName(), region.getCountry()));
            } else if (!Objects.equals(region.getName(), storedRegion.getName()) || !Objects.equals(region.getCountry(), storedRegion.getCountry())) {
                changedRegions.add(new RegionDTO(region.getId(), region.getName(), region.getCountry()));
            }
        }
        System.out.println("Adding " + newRegions);
        System.out.println("Updating " + changedRegions);
        dbEntity.updateRegions(newRegions, changedRegions);
    }
    
    //
//        EntityManager entityManager = createEntityManager();
//        String sql = "SELECT r from RegionEntity r";
//        Query query = entityManager.createQuery(sql);
//        List<RegionEntity> existingRegionEntities = query.getResultList();
//        Map<Integer, RegionEntity> regionMap = existingRegionEntities.stream().collect(Collectors.toMap(RegionEntity::getId, Function.identity()));
//        regions.forEach(region -> updateRegion(entityManager, regionMap, region));
//        entityManager.close();
//    }
//
//    private void updateRegion(EntityManager entityManager, Map<Integer, RegionEntity> regionMap, org.joelson.mattias.turfgame.apiv4.Region region) {
//        RegionEntity existingRegionEntity = regionMap.get(region.getId());
//        if (existingRegionEntity == null) {
//            entityManager.getTransaction().begin();
//            RegionEntity newRegionEntity = new RegionEntity(region.getId(), region.getName(), region.getCountry());
//            entityManager.persist(newRegionEntity);
//            entityManager.getTransaction().commit();
//            System.out.println("Added " + newRegionEntity);
//            regionMap.put(newRegionEntity.getId(), newRegionEntity);
//        } else {
//            if (Objects.equals(existingRegionEntity.getName(), region.getName()) && Objects.equals(existingRegionEntity.getCountry(), region.getCountry())) {
//                //System.out.println("Already has " + existingRegionEntity);
//            } else {
//                System.out.println(existingRegionEntity + " and " + region + " differs!");
//                //throw new NullPointerException("error!");
//            }
//        }
//    }
}
