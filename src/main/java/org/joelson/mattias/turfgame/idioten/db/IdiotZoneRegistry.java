package org.joelson.mattias.turfgame.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.mattias.turfgame.idioten.model.ZoneData;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

class IdiotZoneRegistry extends EntityRegistry<IdiotZoneEntity> {

    IdiotZoneRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public IdiotZoneEntity findByName(String name) {
        return findAnyOrNull("name", name);
    }

    public IdiotZoneEntity create(ZoneData zoneData) {
        return create(zoneData.getId(), zoneData.getName());
    }

    public IdiotZoneEntity create(int id, String name) {
        IdiotZoneEntity zoneEntity = IdiotZoneEntity.build(id, name);
        persist(zoneEntity);
        return zoneEntity;
    }
}
