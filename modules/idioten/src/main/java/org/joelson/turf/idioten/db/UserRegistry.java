package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.util.persistence.EntityRegistry;

class IdiotPlayerRegistry extends EntityRegistry<IdiotPlayerEntity> {

    IdiotPlayerRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
