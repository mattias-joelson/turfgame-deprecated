package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;
import org.joelson.mattias.turfgame.util.db.EntityRegistry;

class UserRegistry extends EntityRegistry<UserEntity> {
    
    UserRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
