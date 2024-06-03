package org.joelson.mattias.turfgame.application.db;

import java.util.stream.Stream;
import javax.persistence.EntityManager;

class MunicipalityZoneRegistry extends EntityRegistry<MunicipalityZoneEntity> {
    MunicipalityZoneRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public MunicipalityZoneEntity findZone(ZoneEntity zone) {
        return findAll("zone", zone) //NON-NLS
                .reduce((element, otherElement) -> { throw new IllegalStateException("Multiple zone entries"); })
                .orElse(null);
    }

    public MunicipalityZoneEntity create(MunicipalityEntity municipality, ZoneEntity zone) {
        MunicipalityZoneEntity municipalityZone = MunicipalityZoneEntity.build(municipality, zone);
        persist(municipalityZone);
        return municipalityZone;
    }
}
