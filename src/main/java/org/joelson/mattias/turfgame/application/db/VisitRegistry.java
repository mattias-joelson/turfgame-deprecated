package org.joelson.mattias.turfgame.application.db;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class VisitRegistry extends EntityRegistry<VisitEntity> {
    
    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }
    
    public VisitEntity find(TakeEntity take, UserEntity user) {
        Query query = createQuery("SELECT e FROM VisitEntity e WHERE e.take=:take AND e.user=:user"); //NON-NLS
        query.setParameter("take", take); //NON-NLS
        query.setParameter("user", user); //NON-NLS
        try {
            return (VisitEntity) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public VisitEntity findTakeVisit(TakeEntity take) {
        Query query = createQuery("SELECT e FROM VisitEntity e WHERE e.take=:take AND e.type=:type"); //NON-NLS
        query.setParameter("take", take); //NON-NLS
        query.setParameter("type", VisitType.TAKE); //NON-NLS
        return (VisitEntity) query.getSingleResult();
    }
}
