package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;
import java.util.stream.Stream;

class UserVisitsRegistry extends EntityRegistry<UserVisitsEntity> {

    UserVisitsRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public Stream<UserVisitsEntity> findAllByUser(UserEntity user) {
        return findAll("user", user);
    }

    public Stream<UserVisitsEntity> findAllByDate(Instant date) {
        return findAll("date", date);
    }

    public UserVisitsEntity find(UserEntity user, Instant date) {
        TypedQuery<UserVisitsEntity> query =
                createQuery("SELECT uv FROM UserVisitsEntity uv WHERE uv.user=:user AND uv.date=:date");
        query.setParameter("user", user);
        query.setParameter("date", date);
        return query.getSingleResultOrNull();
    }

    public UserVisitsEntity create(UserEntity user, Instant date, int visits) {
        return persist(UserVisitsEntity.build(user, date, visits));
    }
}
