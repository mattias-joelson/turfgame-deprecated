package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

public class VisitRegistry extends EntityRegistry<VisitEntity> {
    
    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
