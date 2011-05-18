package com.github.fallback.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.ejb.QueryHints;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;

/**
 * Nice BaseDAO class that DAO's can extend. Extends on Spring's JpaDaoSupport.
 */
public abstract class BaseDAO<K, E> extends JpaDaoSupport {
    protected Class<E> entityClass;

    /** Helper method makes "like" queries possible */
    public static final String like(String query)
    {
        return "%" + query + "%";
    }

    /** */
    @SuppressWarnings("unchecked")
    public BaseDAO() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass
                .getActualTypeArguments()[1];
    }

    /** */
    public void persist(E entity) {
        getJpaTemplate().persist(entity);
    }

    /** */
    public void remove(E entity) {
        getJpaTemplate().remove(entity);
    }

    /** */
    public E merge(E entity) {
        return getJpaTemplate().merge(entity);
    }

    /** */
    public void refresh(E entity) {
        getJpaTemplate().refresh(entity);
    }

    /** */
    public E find(K id) {
        return getJpaTemplate().find(entityClass, id);
    }

    /**
     * Similar to find(), but throws a NotFoundException instead of returning null
     * when the key does not exist.
     */
    public E get(K primaryKey) throws NotFoundException
    {
        E val =  this.find(primaryKey);
        if (val == null) {
            throw new NotFoundException("No such " + entityClass.getName() + ": " + primaryKey);
        }

        return val;
    }

    /** */
    public E flush(E entity) {
        getJpaTemplate().flush();
        return entity;
    }

    /** */
    public List<E> findSome(String column, Object value) {
        return this.find("FROM " + entityClass.getName() + " WHERE " + column + "= ?", value);
    }

    /** */
    public List<E> findAll() {
        return this.find("FROM " + entityClass.getName());
    }

    /** */
    @SuppressWarnings("unchecked")
    public Integer removeAll() {
        return (Integer) getJpaTemplate().execute(new JpaCallback() {

            @Override
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query q = em.createQuery("DELETE FROM " + entityClass.getName());
                return q.executeUpdate();
            }

        });
    }

    /** */
    public List<E> find(String queryString) throws DataAccessException {
        return this.find(queryString, (Object[]) null);
    }

    /**
     * By deafult, the queries are cached!
     */
    public List<E> find(final String queryString, final Object... values) throws DataAccessException {
        final JpaTemplate template = getJpaTemplate();

        return template.execute(new JpaCallback<List<E>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<E> doInJpa(EntityManager em) throws PersistenceException {
                Query queryObject = em.createQuery(queryString);
                queryObject.setHint(QueryHints.HINT_CACHEABLE, "true");
                template.prepareQuery(queryObject);
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        queryObject.setParameter(i + 1, values[i]);
                    }
                }
                return queryObject.getResultList();
            }
        });
    }

}
