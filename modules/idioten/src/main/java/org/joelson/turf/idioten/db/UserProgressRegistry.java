package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;
import java.util.stream.Stream;

public class UserProgressRegistry extends EntityRegistry<UserProgressEntity> {

    public UserProgressRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public Stream<UserProgressEntity> findAllByUser(UserEntity user) {
        return findAll("user", user);
    }

    public Stream<UserProgressEntity> findAllByType(UserProgressType type) {
        return findAll("type", type);
    }

    public Stream<UserProgressEntity> findAllByDate(Instant date) {
        return findAll("date", date);
    }

    public UserProgressEntity find(UserEntity user, UserProgressType type, Instant date) {
        TypedQuery<UserProgressEntity> query = createQuery(
                "SELECT up FROM UserProgressEntity up WHERE up.user=:user AND up.type=:type AND up.date=:date");
        query.setParameter("user", user);
        query.setParameter("type", type);
        query.setParameter("date", date);
        return query.getSingleResultOrNull();
    }

    public UserProgressEntity create(UserEntity user, UserProgressType type, Instant date, int previousDayCompleted,
            int dayCompleted, Instant time) {
        return persist(UserProgressEntity.build(user, type, date, previousDayCompleted, dayCompleted, time));
    }

    public UserProgressEntity increaseUserProgressDayCompleted(
            UserEntity user, UserProgressType type, Instant date, Instant time) {
        UserProgressEntity userProgress = find(user, type, date);
        userProgress.setDayCompleted(userProgress.getDayCompleted() + 1);
        userProgress.setTimeCompleted(time);
        return persist(userProgress);
    }
}
