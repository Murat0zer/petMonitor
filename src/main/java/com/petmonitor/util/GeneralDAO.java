package com.petmonitor.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

public class GeneralDAO {

    private EntityManager entityManager;

    public GeneralDAO() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    public <T> Optional<T> get(long id, Class<T> clazz) {

        T object;
        entityManager.getTransaction().begin();

        String modelName = clazz.getSimpleName();
        String queryString = "select e from " + modelName +" e where e.id = :id";
        TypedQuery<T> query = entityManager.createQuery(queryString, clazz);


        try {
            object = query.setParameter("id", id).getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }

        return Optional.of(object);
    }


}
