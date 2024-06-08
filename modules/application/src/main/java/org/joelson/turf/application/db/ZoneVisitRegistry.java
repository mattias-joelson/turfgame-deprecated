package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

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
        TypedQuery<ZoneVisitEntity> query = createQuery("SELECT e FROM ZoneVisitEntity e WHERE e.user=:user AND e.zone=:zone"); //NON-NLS
        query.setParameter("user", user); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        return query.getResultStream()
                .findAny()
                .orElse(null);
    }
}
