package com.petmonitor.util;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Slf4j
public class GenericDAO<T, ID extends Serializable> {

    protected EntityManager entityManager;
    private Class<T> entityBeanType;
    private String entityBeanName;

    @SuppressWarnings("unchecked")
    public GenericDAO() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();

        this.entityBeanType = ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);

        this.entityBeanName = this.entityBeanType.getSimpleName();
    }

    public Optional<T> get(ID id) {

        T entity;

        entityManager.getTransaction().begin();

        String queryString = "select e from " + entityBeanName + " e where e.id = :id";

        TypedQuery<T> query = entityManager.createQuery(queryString, entityBeanType);

        try {
            entity = query.setParameter("id", id).getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }

        return Optional.of(entity);
    }

    public Iterable<T> getAll() {
        String queryString = "select e from " + entityBeanName + " e ";
        return entityManager.createQuery(queryString, this.entityBeanType).getResultList();
    }

    public Optional<T> save(T entity) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(entity);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            log.error(e.getMessage());
            entityManager.getTransaction().rollback();
        }

        return Optional.of(entity);

    }

    public void update(T entity) {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            log.error(e.getMessage());
            throw e;
        }
    }


    public void delete(T entity) {

        entityManager.getTransaction().begin();
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            log.error(e.getMessage());
            throw e;
        }
    }


}
