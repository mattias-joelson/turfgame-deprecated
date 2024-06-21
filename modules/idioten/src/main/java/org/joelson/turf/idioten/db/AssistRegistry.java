package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.util.Comparator;
import java.util.stream.Stream;

class AssistRegistry extends EntityRegistry<AssistEntity> {

    AssistRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    private static Stream<AssistEntity> sortByTime(Stream<AssistEntity> assists) {
        return assists.sorted(Comparator.comparing(o -> o.getVisit().getTime()));
    }

    public Stream<AssistEntity> findAllByUser(UserEntity user) {
        return sortByTime(findAll("user", user));
    }

    public Stream<AssistEntity> findAllByVisit(VisitEntity visit) {
        return findAll("visit", visit);
    }

    public AssistEntity create(VisitEntity visit, UserEntity user) {
        return persist(AssistEntity.build(visit, user));
    }
}
