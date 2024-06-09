package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.application.model.RegionData;
import org.joelson.turf.util.persistence.EntityRegistry;

class RegionRegistry extends EntityRegistry<RegionEntity> {

    RegionRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public RegionEntity findByName(String name) {
        return findAnyOrNull("name", name);
    }

    public RegionEntity create(RegionData regionData) {
        return create(regionData.getId(), regionData.getName(), regionData.getCountry());
    }

    public RegionEntity create(int id, String name, String country) {
        RegionEntity region = RegionEntity.build(id, name, country);
        persist(region);
        return region;
    }
}
