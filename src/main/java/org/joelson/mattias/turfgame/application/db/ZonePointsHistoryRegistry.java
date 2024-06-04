package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.mattias.turfgame.application.model.ZoneData;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

import java.time.Instant;

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
    
    public ZonePointsHistoryEntity findLatestBefore(ZoneEntity zone, Instant when) {
        TypedQuery<ZonePointsHistoryEntity> query
                = createQuery("SELECT e FROM ZonePointsHistoryEntity e WHERE e.zone=:zone AND e.from<=:when ORDER BY e.from DESC"); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        query.setParameter("when", when); //NON-NLS
        return query.getResultStream()
                .findFirst()
                .orElse(null);
    }

    public ZonePointsHistoryEntity findLatest(ZoneEntity zone) {
        TypedQuery<ZonePointsHistoryEntity> query
                = createQuery("SELECT e FROM ZonePointsHistoryEntity e WHERE e.zone=:zone ORDER BY e.from DESC"); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        return query.getResultStream()
                .findFirst()
                .orElseThrow();
    }
}
