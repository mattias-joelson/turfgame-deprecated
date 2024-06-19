package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;

class VisitRegistry extends EntityRegistry<VisitEntity> {

    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public VisitEntity find(ZoneEntity zone, Instant time) {
        TypedQuery<VisitEntity> query = createQuery("SELECT v FROM VisitEntity v WHERE v.time=:time AND v.zone=:zone");
        query.setParameter("zone", zone);
        query.setParameter("time", time);
        return query.getSingleResultOrNull();
    }

    public VisitEntity create(ZoneEntity zone, UserEntity user, Instant time, VisitType type) {
        return persist(VisitEntity.build(zone, user, time, type));
    }
}
