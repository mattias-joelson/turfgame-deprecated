package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.ZoneData;

import java.time.Instant;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

class ZoneHistoryRegistry extends EntityRegistry<ZoneHistoryEntity> {
    
    ZoneHistoryRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public ZoneHistoryEntity create(ZoneEntity zone, Instant from, RegionEntity region, ZoneData zoneData) {
        return create(zone, from, zoneData.getName(), region, zoneData.getDateCreated(), zoneData.getLatitude(), zoneData.getLongitude());
    }
    
    public ZoneHistoryEntity create(ZoneEntity zone, Instant from, String name, RegionEntity region, Instant dateCreated, double latitude, double longitude) {
        ZoneHistoryEntity zoneHistoryEntity = ZoneHistoryEntity.build(zone, from, name, region, dateCreated, latitude, longitude);
        persist(zoneHistoryEntity);
        return zoneHistoryEntity;
    }
    
    public ZoneHistoryEntity findLatestBefore(ZoneEntity zone, Instant when) {
        Query query = createQuery("SELECT e FROM ZoneHistoryEntity e WHERE e.zone=:zone AND e.from<=:when ORDER BY e.from DESC"); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        query.setParameter("when", when); //NON-NLS
        @SuppressWarnings("unchecked")
        List<ZoneHistoryEntity> zoneHistories = query.getResultList();
        return zoneHistories.get(0);
    }
}
