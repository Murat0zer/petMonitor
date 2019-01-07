package com.petmonitor.pet.repository;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.util.Dao;
import com.petmonitor.util.EntityManagerUtility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;


public class PetDAOImpl implements Dao<Pet> {

    private EntityManager entityManager;

    public PetDAOImpl() {

        EntityManagerFactory entityManagerFactory = EntityManagerUtility.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<Pet> get(long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Pet> getAll() {
        return null;
    }

    @Override
    public Pet save(Pet pet) {
        return null;
    }

    @Override
    public void update(Pet pet) {

    }

    @Override
    public void delete(Pet pet) {

    }
}
