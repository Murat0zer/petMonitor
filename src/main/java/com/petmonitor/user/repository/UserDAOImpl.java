package com.petmonitor.user.repository;

import com.petmonitor.user.model.User;
import com.petmonitor.util.Dao;
import com.petmonitor.util.EntityManagerUtility;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.Startup;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

@RequestScoped
@ManagedBean
public class UserDAOImpl implements Dao<User> {

    private EntityManager entityManager;

    public UserDAOImpl() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<User> get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public User save(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        return user;

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    public Optional<User> findByUsername(String username) {
        entityManager.getTransaction().begin();

        String queryString = "select u from User u where u.username = :username";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);

        User user = User.builder().build();
        try {
            user = query.setParameter("username", username).getSingleResult();

        } catch (Exception e){
            entityManager.getTransaction().rollback();
        }

        return Optional.of(user);
    }
}
