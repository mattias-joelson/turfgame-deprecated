package org.joelson.mattias.turfgame.application.db;

import java.time.Instant;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class TakeRegistry extends EntityRegistry<TakeEntity> {
    
    TakeRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public TakeEntity find(ZoneEntity zone, Instant when) {
        Query query = createQuery("SELECT e FROM TakeEntity e WHERE e.zone=:zone AND e.when=:when"); //NON-NLS
        query.setParameter("zone", zone); //NON-NLS
        query.setParameter("when", when); //NON-NLS
        try {
            return (TakeEntity) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
