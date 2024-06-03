package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;

public class VisitRegistry extends EntityRegistry<VisitEntity> {
    
    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
