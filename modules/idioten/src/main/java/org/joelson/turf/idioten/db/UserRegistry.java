package org.joelson.turf.idioten.db;

import jakarta.persistence.EntityManager;
import org.joelson.turf.idioten.model.UserData;
import org.joelson.turf.util.persistence.EntityRegistry;

import java.time.Instant;
import java.util.Comparator;

class UserRegistry extends EntityRegistry<UserEntity> {

    UserRegistry(EntityManager entityManager) {
        super(entityManager);
    }

    public UserEntity findByName(String name) {
        return findAll("name", name).max(Comparator.comparing(UserEntity::getTime)).orElse(null);
    }

    public UserEntity create(UserData userData, Instant time) {
        return create(userData.getId(), userData.getName(), time);
    }

    public UserEntity create(int id, String name, Instant time) {
        return persist(UserEntity.build(id, name, time));
    }

    public UserEntity getUpdateOrCreate(UserData userData, Instant time) {
        UserEntity user = find(userData.getId());
        if (user == null) {
            user = create(userData, time);
        } else if (time.isAfter(user.getTime())) {
            if (!userData.getName().equals(user.getName())) {
                user.setName(userData.getName());
            }
            user.setTime(time);
            persist(user);
        }
        return user;
    }
}
