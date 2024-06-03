package org.joelson.mattias.turfgame.application.db;

import javax.persistence.EntityManager;

public class VisitRegistry extends EntityRegistry<VisitEntity> {
    
    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
