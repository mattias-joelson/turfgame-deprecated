package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.application.model.ZoneData;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;

class ZoneRegistry extends EntityRegistry<ZoneEntity> {

    ZoneRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public ZoneEntity findByName(String name) {
        return findAnyOrNull("name", name);
    }

    public ZoneEntity create(RegionEntity region, ZoneData zoneData) {
        return create(zoneData.getId(), zoneData.getName(), region, zoneData.getDateCreated(), zoneData.getLatitude(),
                zoneData.getLongitude(), zoneData.getTp(), zoneData.getPph());
    }

    public ZoneEntity create(
            int id, String name, RegionEntity region, Instant dateCreated, double latitude, double longitude, int tp,
            int pph) {
        ZoneEntity zoneEntity = ZoneEntity.build(id, name, region, dateCreated, latitude, longitude, tp, pph);
        persist(zoneEntity);
        return zoneEntity;
    }
}
