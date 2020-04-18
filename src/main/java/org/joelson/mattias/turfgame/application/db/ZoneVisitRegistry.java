package org.joelson.mattias.turfgame.application.db;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ZoneVisitRegistry extends EntityRegistry<ZoneVisitEntity> {

    ZoneVisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public ZoneVisitEntity create(UserEntity user, ZoneEntity zone, int visits) {
        ZoneVisitEntity zoneVisitEntity = ZoneVisitEntity.build(user, zone, visits);
        persist(zoneVisitEntity);
        return zoneVisitEntity;
    }

    public ZoneVisitEntity find(UserEntity user, ZoneEntity zone) {
        TypedQuery<ZoneVisitEntity> query = createQuery("SELECT e FROM ZoneVisitEntity e WHERE e.user=:user AND e.zone=:zone"); //NON-NLS
        query.setParameter("user", user); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        return query.getResultStream()
                .findAny()
                .orElse(null);
    }
}
