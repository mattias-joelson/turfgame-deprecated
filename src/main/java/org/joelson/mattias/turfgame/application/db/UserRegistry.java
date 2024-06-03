package org.joelson.mattias.turfgame.application.db;

import jakarta.persistence.EntityManager;

class UserRegistry extends EntityRegistry<UserEntity> {
    
    UserRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
