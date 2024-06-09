package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.application.model.ZoneData;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;

class ZoneHistoryRegistry extends EntityRegistry<ZoneHistoryEntity> {

    ZoneHistoryRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public ZoneHistoryEntity create(ZoneEntity zone, Instant from, RegionEntity region, ZoneData zoneData) {
        return create(zone, from, zoneData.getName(), region, zoneData.getDateCreated(), zoneData.getLatitude(),
                zoneData.getLongitude());
    }

    public ZoneHistoryEntity create(
            ZoneEntity zone, Instant from, String name, RegionEntity region, Instant dateCreated, double latitude,
            double longitude) {
        ZoneHistoryEntity zoneHistoryEntity = ZoneHistoryEntity.build(zone, from, name, region, dateCreated, latitude,
                longitude);
        persist(zoneHistoryEntity);
        return zoneHistoryEntity;
    }

    public ZoneHistoryEntity findLatestBefore(ZoneEntity zone, Instant when) {
        TypedQuery<ZoneHistoryEntity> query = createQuery(
                "SELECT e FROM ZoneHistoryEntity e WHERE e.zone=:zone AND e.from<=:when ORDER BY e.from DESC");
        query.setParameter("zone", zone);
        query.setParameter("when", when);
        return query.getResultStream().findFirst().orElse(null);
    }

    public ZoneHistoryEntity findLatest(ZoneEntity zone) {
        TypedQuery<ZoneHistoryEntity> query = createQuery(
                "SELECT e FROM ZoneHistoryEntity e WHERE e.zone=:zone ORDER BY e.from DESC");
        query.setParameter("zone", zone);
        return query.getResultStream().findFirst().orElseThrow();
    }
}
