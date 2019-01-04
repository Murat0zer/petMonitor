package com.petmonitor.owner.repository;

import com.petmonitor.util.Dao;
import com.petmonitor.owner.model.Owner;
import com.petmonitor.util.EntityManagerUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Optional;

public class OwnerDAOImpl implements Dao<Owner> {

    private EntityManager entityManager;

    public OwnerDAOImpl() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<Owner> get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Owner> getAll() {
        return null;
    }

    @Override
    public Owner save(Owner owner) {
        entityManager.getTransaction().begin();
        entityManager.persist(owner);
        entityManager.getTransaction().commit();
        return owner;

    }

    @Override
    public void update(Owner owner) {

    }

    @Override
    public void delete(Owner owner) {

    }
}
