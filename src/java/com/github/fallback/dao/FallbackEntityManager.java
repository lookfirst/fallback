package com.github.fallback.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.fallback.util.NotFoundException;

/**
 * Gives us a working entity manager. An alternative to the DAO based access.
 * Not used currently, but may want to in the future, so keeping this around.
 */
@Service
public class FallbackEntityManager implements EntityManager {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(FallbackEntityManager.class);

    @PersistenceContext
    private EntityManager base;

    public EntityManager getEntityManager() {
        return this.base;
    }

    /** */
    public FallbackEntityManager() {
    }

    /**
     * Similar to find(), but throws a NotFoundException instead of returning null
     * when the key does not exist.
     */
    public <T> T get(Class<T> entityClass, Object primaryKey) throws NotFoundException {
        T val = this.find(entityClass, primaryKey);
        if (val == null) {
            throw new NotFoundException("No such " + entityClass.getName() + ": " + primaryKey);
        }

        return val;
    }

    @Override
    public void persist(Object arg0) {
        this.base.persist(arg0);
    }

    @Override
    public <T> T merge(T arg0) {
        return this.base.merge(arg0);
    }

    @Override
    public void remove(Object arg0) {
        this.base.remove(arg0);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1) {
        return this.base.find(arg0, arg1);
    }

    @Override
    public <T> T getReference(Class<T> arg0, Object arg1) {
        return this.base.getReference(arg0, arg1);
    }

    @Override
    public void flush() {
        this.base.flush();
    }

    @Override
    public void setFlushMode(FlushModeType arg0) {
        this.base.setFlushMode(arg0);
    }

    @Override
    public FlushModeType getFlushMode() {
        return this.base.getFlushMode();
    }

    @Override
    public void lock(Object arg0, LockModeType arg1) {
        this.base.lock(arg0, arg1);
    }

    @Override
    public void refresh(Object arg0) {
        this.base.refresh(arg0);
    }

    @Override
    public void clear() {
        this.base.clear();
    }

    @Override
    public boolean contains(Object arg0) {
        return this.base.contains(arg0);
    }

    @Override
    public Query createQuery(String arg0) {
        return this.base.createQuery(arg0);
    }

    @Override
    public Query createNamedQuery(String arg0) {
        return this.base.createNamedQuery(arg0);
    }

    @Override
    public Query createNativeQuery(String arg0) {
        return this.base.createNativeQuery(arg0);
    }

    @Override
    public Query createNativeQuery(String arg0, Class arg1) {
        return this.base.createNativeQuery(arg0, arg1);
    }

    @Override
    public Query createNativeQuery(String arg0, String arg1) {
        return this.createNativeQuery(arg0, arg1);
    }

    @Override
    public void joinTransaction() {
        this.base.joinTransaction();
    }

    @Override
    public Object getDelegate() {
        return this.base.getDelegate();
    }

    @Override
    public void close() {
        this.base.clear();
    }

    @Override
    public boolean isOpen() {
        return this.base.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return this.base.getTransaction();
    }

    public EntityManager getBase() {
        return this.base;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
        return this.base.createNamedQuery(arg0, arg1);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
        return this.base.createQuery(arg0);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
        return this.base.createQuery(arg0, arg1);
    }

    @Override
    public void detach(Object arg0) {
        this.base.detach(arg0);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
        return this.base.find(arg0, arg1, arg2);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
        return this.base.find(arg0, arg1, arg2);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2, Map<String, Object> arg3) {
        return this.base.find(arg0, arg1, arg2, arg3);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return this.base.getCriteriaBuilder();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return this.base.getEntityManagerFactory();
    }

    @Override
    public LockModeType getLockMode(Object arg0) {
        return this.base.getLockMode(arg0);
    }

    @Override
    public Metamodel getMetamodel() {
        return this.base.getMetamodel();
    }

    @Override
    public Map<String, Object> getProperties() {
        return this.base.getProperties();
    }

    @Override
    public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
        this.base.lock(arg0, arg1, arg2);
    }

    @Override
    public void refresh(Object arg0, Map<String, Object> arg1) {
        this.base.refresh(arg0, arg1);
    }

    @Override
    public void refresh(Object arg0, LockModeType arg1) {
        this.base.refresh(arg0, arg1);
    }

    @Override
    public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
        this.base.refresh(arg0, arg1, arg2);
    }

    @Override
    public void setProperty(String arg0, Object arg1) {
        this.base.setProperty(arg0, arg1);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
        return this.base.unwrap(arg0);
    }
}
