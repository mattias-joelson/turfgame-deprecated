package org.joelson.mattias.turfgame.application.db;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.Query;

class EntityRegistry<T> {
    
    private final EntityManager entityManager;
    private final Class<T> persistentClass;
    
    @SuppressWarnings("unchecked")
    EntityRegistry(EntityManager entityManager) {
        this.entityManager = entityManager;
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    public T find(int id) {
        return entityManager.find(persistentClass, id);
    }
    
    public T find(Object key) {
        return entityManager.find(persistentClass, key);
    }
    
    public T findAny(String field, String value) {
        return findAll(field, value).findAny().orElse(null);
    }
    
    public T findSingle(String queryString, String parameterName, Object parameterValue) {
        Query query = entityManager.createQuery(queryString);
        query.setParameter(parameterName, parameterValue);
        @SuppressWarnings("unchecked")
        T entity = (T) query.getSingleResult();
        return entity;
    }
    
    public Stream<T> findAll() {
        Query query = entityManager.createQuery(String.format("SELECT e FROM %s e", persistentClass.getName())); //NON-NLS
        @SuppressWarnings("unchecked")
        Stream<T> result = query.getResultStream();
        return result;
    }
    
    public Stream<T> findAll(String field, String value) {
        Query query = entityManager.createQuery(String.format("SELECT e FROM %s e WHERE e.%s=:value", persistentClass.getName(), field)); //NON-NLS
        query.setParameter("value", value); //NON-NLS
        @SuppressWarnings("unchecked")
        Stream<T> result = query.getResultStream();
        return result;
    }
    
    public void persist(T t) {
        entityManager.persist(t);
    }
}
