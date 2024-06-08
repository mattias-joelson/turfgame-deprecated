package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

class MunicipalityRegistry extends EntityRegistry<MunicipalityEntity> {

    MunicipalityRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public MunicipalityEntity findByName(String name) {
        return findAll("name", name) //NON-NLS
                .reduce((element, otherElement) -> { throw new IllegalStateException("Multiple municipalities with same name"); })
                .orElse(null);
    }

    public MunicipalityEntity find(RegionEntity region, String name) {
        TypedQuery<MunicipalityEntity> query
                = createQuery("SELECT e FROM MunicipalityEntity e WHERE e.region=:region AND e.name=:name"); //NON-NLS
        query.setParameter("region", region); //NON-NLS
        query.setParameter("name", name); //NON-NLS
        return query.getResultStream()
                .findFirst()
                .orElse(null);
    }

    public MunicipalityEntity create(RegionEntity region, String name) {
        MunicipalityEntity municipality = MunicipalityEntity.build(region, name);
        persist(municipality);
        return municipality;
    }
}
