package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

class MunicipalityRegistry extends EntityRegistry<MunicipalityEntity> {

    MunicipalityRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public MunicipalityEntity findByName(String name) {
        return findAll("name", name).reduce((element, otherElement) -> {
            throw new IllegalStateException("Multiple municipalities with same name");
        }).orElse(null);
    }

    public MunicipalityEntity find(RegionEntity region, String name) {
        TypedQuery<MunicipalityEntity> query = createQuery(
                "SELECT e FROM MunicipalityEntity e WHERE e.region=:region AND e.name=:name");
        query.setParameter("region", region);
        query.setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    public MunicipalityEntity create(RegionEntity region, String name) {
        MunicipalityEntity municipality = MunicipalityEntity.build(region, name);
        persist(municipality);
        return municipality;
    }
}
