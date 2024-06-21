package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

class VisitRegistry extends EntityRegistry<VisitEntity> {

    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public Stream<VisitEntity> findAllByZone(ZoneEntity zone) {
        return findAll("zone", zone);
    }

    public Stream<VisitEntity> findAllByUser(UserEntity user) {
        return findAll("user", user);
    }

    public Stream<VisitEntity> findAllBetween(Instant from, Instant to) {
        TypedQuery<VisitEntity> query = createQuery("SELECT v FROM VisitEntity v WHERE v.time>=:from AND v.time<=:to");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return (query.getResultStream());
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
