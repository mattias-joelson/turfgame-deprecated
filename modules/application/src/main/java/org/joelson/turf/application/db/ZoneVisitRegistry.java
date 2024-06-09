package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.util.stream.Stream;

public class ZoneVisitRegistry extends EntityRegistry<ZoneVisitEntity> {

    ZoneVisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public ZoneVisitEntity create(UserEntity user, ZoneEntity zone, int visits) {
        ZoneVisitEntity zoneVisitEntity = ZoneVisitEntity.build(user, zone, visits);
        persist(zoneVisitEntity);
        return zoneVisitEntity;
    }

    public Stream<UserEntity> findAllUsers() {
        return findAll().map(ZoneVisitEntity::getUser).distinct();
    }

    public ZoneVisitEntity find(UserEntity user, ZoneEntity zone) {
        TypedQuery<ZoneVisitEntity> query = createQuery(
                "SELECT e FROM ZoneVisitEntity e WHERE e.user=:user AND e.zone=:zone");
        query.setParameter("user", user);
        query.setParameter("zone", zone);
        return query.getResultStream().findAny().orElse(null);
    }
}
