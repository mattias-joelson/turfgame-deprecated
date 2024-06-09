package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.util.persistence.EntityRegistry;

class UserRegistry extends EntityRegistry<UserEntity> {

    UserRegistry(EntityManager entityManager) {
        super(entityManager);
    }
}
