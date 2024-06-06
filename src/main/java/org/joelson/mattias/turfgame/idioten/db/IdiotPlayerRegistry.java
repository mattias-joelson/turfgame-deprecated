package org.joelson.mattias.turfgame.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

class IdiotPlayerRegistry extends EntityRegistry<IdiotPlayerEntity> {

    IdiotPlayerRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
