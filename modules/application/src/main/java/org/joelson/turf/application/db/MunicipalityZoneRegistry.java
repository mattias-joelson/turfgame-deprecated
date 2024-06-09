package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.util.persistence.EntityRegistry;

class MunicipalityZoneRegistry extends EntityRegistry<MunicipalityZoneEntity> {
    MunicipalityZoneRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public MunicipalityZoneEntity findZone(ZoneEntity zone) {
        return findAll("zone", zone).reduce((element, otherElement) -> {
            throw new IllegalStateException("Multiple zone entries");
        }).orElse(null);
    }

    public MunicipalityZoneEntity create(MunicipalityEntity municipality, ZoneEntity zone) {
        MunicipalityZoneEntity municipalityZone = MunicipalityZoneEntity.build(municipality, zone);
        persist(municipalityZone);
        return municipalityZone;
    }
}
