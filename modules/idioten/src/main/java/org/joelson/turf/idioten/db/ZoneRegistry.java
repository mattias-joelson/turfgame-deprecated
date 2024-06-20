package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.idioten.model.ZoneData;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;
import java.util.Comparator;

class ZoneRegistry extends EntityRegistry<ZoneEntity> {

    ZoneRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public ZoneEntity findByName(String name) {
        return findAll("name", name).max(Comparator.comparing(ZoneEntity::getTime)).orElse(null);
    }

    public ZoneEntity create(ZoneData zoneData, Instant time) {
        return create(zoneData.getId(), zoneData.getName(), time);
    }

    public ZoneEntity create(int id, String name, Instant time) {
        return persist(ZoneEntity.build(id, name, time));
    }

    public ZoneEntity getUpdateOrCreate(ZoneData zoneData, Instant time) {
        ZoneEntity zone = find(zoneData.getId());
        if (zone == null) {
            zone = create(zoneData, time);
        } else if (time.isAfter(zone.getTime())) {
            if (!zoneData.getName().equals(zone.getName())) {
                zone.setName(zoneData.getName());
            }
            zone.setTime(time);
            persist(zone);
        }
        return zone;
    }
}
