package com.petmonitor.user.repository;

import com.petmonitor.user.model.Role;
import com.petmonitor.util.Dao;
import com.petmonitor.util.EntityManagerUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Optional;

public class RoleDAOImpl implements Dao<Role> {

    private EntityManager entityManager;

    public RoleDAOImpl() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<Role> get(long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Role> getAll() {
        return null;
    }

    @Override
    public Role save(Role role) {
        entityManager.getTransaction().begin();
        entityManager.persist(role);
        entityManager.getTransaction().commit();
        return role;
    }

    @Override
    public void update(Role role) {

    }

    @Override
    public void delete(Role role) {

    }
}
