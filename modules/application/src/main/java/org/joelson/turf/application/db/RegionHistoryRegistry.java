package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.mattias.turfgame.application.model.RegionData;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

import java.time.Instant;

class RegionHistoryRegistry extends EntityRegistry<RegionHistoryEntity> {
    
    RegionHistoryRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public RegionHistoryEntity create(RegionEntity regionEntity, Instant from, RegionData regionData) {
        return create(regionEntity, from, regionData.getName(), regionData.getCountry());
    }

    public RegionHistoryEntity create(RegionEntity regionEntity, Instant from, String name, String country) {
        RegionHistoryEntity regionHistoryEntity = RegionHistoryEntity.build(regionEntity, from, name, country);
        persist(regionHistoryEntity);
        return regionHistoryEntity;
    }
}
