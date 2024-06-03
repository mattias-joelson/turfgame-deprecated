package org.joelson.mattias.turfgame.application.db;

import javax.persistence.EntityManager;

class UserRegistry extends EntityRegistry<UserEntity> {
    
    UserRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
