package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.idioten.model.ZoneData;
import org.joelson.turf.util.persistence.EntityRegistry;

class ZoneRegistry extends EntityRegistry<ZoneEntity> {

    ZoneRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public ZoneEntity findByName(String name) {
        return findAnyOrNull("name", name);
    }

    public ZoneEntity create(ZoneData zoneData) {
        return create(zoneData.getId(), zoneData.getName());
    }

    public ZoneEntity create(int id, String name) {
        ZoneEntity zoneEntity = ZoneEntity.build(id, name);
        persist(zoneEntity);
        return zoneEntity;
    }
}
