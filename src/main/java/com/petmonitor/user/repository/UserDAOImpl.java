package com.petmonitor.user.repository;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.User;
import com.petmonitor.util.Dao;
import com.petmonitor.util.EntityManagerUtility;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDAOImpl implements Dao<User> {

    private EntityManager entityManager;

    public UserDAOImpl() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<User> get(long id) {


        entityManager.getTransaction().begin();

        String queryString = "select u from User u where u.id = :id";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);

        User user;
        try {
            user = query.setParameter("id", id).getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }

        return Optional.of(user);

    }

    @Override
    public Collection<User> getAll() {
        String queryString = "select u from User u";
        return entityManager.createQuery(queryString, User.class).getResultList();

    }

    @Override
    public User save(User user) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(user);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            log.error(e.getMessage());
            entityManager.getTransaction().rollback();
        }

        return user;

    }

    @Override
    public void update(User user) {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(User user) {

        entityManager.getTransaction().begin();
        try {
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            log.error(e.getMessage());
            throw e;
        }
    }

    public Optional<User> findByUsername(String username) {
        entityManager.getTransaction().begin();

        String queryString = "select u from User u where u.username = :username";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);

        User user = User.builder().build();
        try {
            user = query.setParameter("username", username).getSingleResult();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

        return Optional.of(user);
    }

    public List<Pet> getOwnerPets(long id) {

        String queryString = "select p from Pet p where p.user.id=:id";

        return entityManager.createQuery(queryString, Pet.class).setParameter("id", id).getResultList();

    }
}
