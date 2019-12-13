package org.joelson.mattias.turfgame.application.db;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class HibernateDatabase {
    
    public static final String PERSISTANCE_DERBY = "turfgame-derby";
    public static final String PERSISTANCE_H2 = "turfgame-h2";
    
    private EntityManagerFactory entityManagerFactory;
    //private EntityManager entityManager;
    
    public HibernateDatabase(String unit) {
        entityManagerFactory = Persistence.createEntityManagerFactory(unit);
        //entityManager = entityManagerFactory.createEntityManager();
    }
    
    private EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public Point findPoint(int id) {
        EntityManager entityManager = createEntityManager();
        Point point = entityManager.find(Point.class, id);
        entityManager.close();
        return point;
    }
    
    public List<Point> getPoints() {
        EntityManager entityManager = createEntityManager();
        String sql = "SELECT p FROM Point p";
        Query query = entityManager.createQuery(sql);
        List<Point> points = (List<Point>) query.getResultStream().collect(Collectors.toList());
        entityManager.close();
        return points;
    }
    
    public List<RegionEntity> getRegions() {
        EntityManager entityManager = createEntityManager();
        String sql = "SELECT r from RegionEntity r";
        Query query = entityManager.createQuery(sql);
        List<RegionEntity> regionEntities = query.getResultList();
        entityManager.close();
        return regionEntities;
    }
    
    public void updateRegions(List<org.joelson.mattias.turfgame.apiv4.Region> regions) {
        EntityManager entityManager = createEntityManager();
        String sql = "SELECT r from RegionEntity r";
        Query query = entityManager.createQuery(sql);
        List<RegionEntity> existingRegionEntities = query.getResultList();
        Map<Integer, RegionEntity> regionMap = existingRegionEntities.stream().collect(Collectors.toMap(RegionEntity::getId, Function.identity()));
        regions.forEach(region -> updateRegion(entityManager, regionMap, region));
        entityManager.close();
    }
    
    private void updateRegion(EntityManager entityManager, Map<Integer, RegionEntity> regionMap, org.joelson.mattias.turfgame.apiv4.Region region) {
        RegionEntity existingRegionEntity = regionMap.get(region.getId());
        if (existingRegionEntity == null) {
            entityManager.getTransaction().begin();
            RegionEntity newRegionEntity = new RegionEntity(region.getId(), region.getName(), region.getCountry());
            entityManager.persist(newRegionEntity);
            entityManager.getTransaction().commit();
            System.out.println("Added " + newRegionEntity);
            regionMap.put(newRegionEntity.getId(), newRegionEntity);
        } else {
            if (Objects.equals(existingRegionEntity.getName(), region.getName()) && Objects.equals(existingRegionEntity.getCountry(), region.getCountry())) {
                //System.out.println("Already has " + existingRegionEntity);
            } else {
                System.out.println(existingRegionEntity + " and " + region + " differs!");
                //throw new NullPointerException("error!");
            }
        }
    }
    
    //public HibernateDatabase
}
