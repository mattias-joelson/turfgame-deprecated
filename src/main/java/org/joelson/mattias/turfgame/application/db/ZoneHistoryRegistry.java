package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.ZoneData;

import java.time.Instant;
import javax.persistence.EntityManager;

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
}
