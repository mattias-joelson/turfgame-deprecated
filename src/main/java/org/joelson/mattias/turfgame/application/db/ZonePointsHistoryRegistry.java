package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.ZoneData;

import java.time.Instant;
import javax.persistence.EntityManager;

class ZonePointsHistoryRegistry extends EntityRegistry<ZonePointsHistoryEntity> {
    
    ZonePointsHistoryRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public ZonePointsHistoryEntity create(ZoneEntity zone, Instant from, ZoneData zoneData) {
        return create(zone, from, zoneData.getTp(), zoneData.getPph());
    }
    
    public ZonePointsHistoryEntity create(ZoneEntity zone, Instant from, int tp, int pph) {
        ZonePointsHistoryEntity zonePointsHistory = ZonePointsHistoryEntity.build(zone, from, tp, pph);
        persist(zonePointsHistory);
        return zonePointsHistory;
    }
}
