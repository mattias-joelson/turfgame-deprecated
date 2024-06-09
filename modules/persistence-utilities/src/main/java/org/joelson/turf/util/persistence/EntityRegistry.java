package org.joelson.turf.util.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

public class EntityRegistry<T> {

    private final EntityManager entityManager;
    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public EntityRegistry(EntityManager entityManager) {
        this.entityManager = entityManager;
        persistentClass =
                (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected TypedQuery<T> createQuery(String s) {
        return entityManager.createQuery(s, persistentClass);
    }

    public T find(int id) {
        return entityManager.find(persistentClass, id);
    }

    public T find(Object key) {
        return entityManager.find(persistentClass, key);
    }

    public T findAnyOrNull(String field, Object value) {
        return findAll(field, value).findAny().orElse(null);
    }

    public Stream<T> findAll() {
        TypedQuery<T> query = entityManager.createQuery(String.format("SELECT e FROM %s e", persistentClass.getName()),
                persistentClass);
        return query.getResultStream();
    }

    public Stream<T> findAll(String field, Object value) {
        TypedQuery<T> query = entityManager.createQuery(
                String.format("SELECT e FROM %s e WHERE e.%s=:value", persistentClass.getName(), field),
                persistentClass);
        query.setParameter("value", value);
        return query.getResultStream();
    }

    public void persist(T t) {
        entityManager.persist(t);
    }
}
