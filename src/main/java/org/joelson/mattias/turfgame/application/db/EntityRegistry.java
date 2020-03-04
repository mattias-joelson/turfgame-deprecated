package org.joelson.mattias.turfgame.application.db;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

class EntityRegistry<T> {
    
    private final EntityManager entityManager;
    private final Class<T> persistentClass;
    
    @SuppressWarnings("unchecked")
    EntityRegistry(EntityManager entityManager) {
        this.entityManager = entityManager;
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
    
    protected T findAnyOrNull(String field, Object value) {
        return findAll(field, value)
                .findAny()
                .orElse(null);
    }
    
    public Stream<T> findAll() {
        TypedQuery<T>  query = entityManager.createQuery(String.format("SELECT e FROM %s e", persistentClass.getName()), persistentClass); //NON-NLS
        return query.getResultStream();
    }
    
    protected Stream<T> findAll(String field, Object value) {
        TypedQuery<T> query = entityManager.createQuery(String.format("SELECT e FROM %s e WHERE e.%s=:value", persistentClass.getName(), field), //NON-NLS
                persistentClass);
        query.setParameter("value", value); //NON-NLS
        return query.getResultStream();
    }
    
    public void persist(T t) {
        entityManager.persist(t);
    }
}
