package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.util.persistence.EntityRegistry;

class AssistRegistry extends EntityRegistry<AssistEntity> {

    AssistRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public AssistEntity create(VisitEntity visit, UserEntity user) {
        return persist(AssistEntity.build(visit, user));
    }
}
