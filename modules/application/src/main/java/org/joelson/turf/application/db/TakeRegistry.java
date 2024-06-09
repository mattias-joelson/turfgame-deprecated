package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;
import java.util.stream.Stream;

public class TakeRegistry extends EntityRegistry<TakeEntity> {

    TakeRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public TakeEntity find(ZoneEntity zone, Instant when) {
        TypedQuery<TakeEntity> query = createQuery("SELECT e FROM TakeEntity e WHERE e.zone=:zone AND e.when=:when");
        query.setParameter("zone", zone);
        query.setParameter("when", when);
        return query.getResultStream().findAny().orElse(null);
    }

    public Stream<TakeEntity> findAfter(ZoneEntity zone, Instant when) {
        TypedQuery<TakeEntity> query = createQuery(
                "SELECT e FROM TakeEntity e WHERE e.zone=:zone AND e.when>:when ORDER BY e.when");
        query.setParameter("zone", zone);
        query.setParameter("when", when);
        return query.getResultStream();
    }
}
