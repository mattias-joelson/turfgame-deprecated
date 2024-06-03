package org.joelson.mattias.turfgame.application.db;

import org.joelson.mattias.turfgame.application.model.RegionData;

import javax.persistence.EntityManager;

class RegionRegistry extends EntityRegistry<RegionEntity> {
    
    RegionRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public RegionEntity findByName(String name) {
        return findAnyOrNull("name", name); //NON-NLS
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
