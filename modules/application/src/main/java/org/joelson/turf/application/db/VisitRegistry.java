package org.joelson.turf.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.util.persistence.EntityRegistry;

public class VisitRegistry extends EntityRegistry<VisitEntity> {

    VisitRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
