package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

import java.time.Instant;
import java.util.stream.Stream;

public class TakeRegistry extends EntityRegistry<TakeEntity> {
    
    TakeRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public TakeEntity find(ZoneEntity zone, Instant when) {
        TypedQuery<TakeEntity> query = createQuery("SELECT e FROM TakeEntity e WHERE e.zone=:zone AND e.when=:when"); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        query.setParameter("when", when); //NON-NLS
        return query.getResultStream()
                .findAny()
                .orElse(null);
    }

    public Stream<TakeEntity> findAfter(ZoneEntity zone, Instant when) {
        TypedQuery<TakeEntity> query = createQuery("SELECT e FROM TakeEntity e WHERE e.zone=:zone AND e.when>:when ORDER BY e.when"); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        query.setParameter("when", when); //NON-NLS
        return query.getResultStream();
    }
}
