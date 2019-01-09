package com.petmonitor.pet.repository;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.util.GenericDAO;

import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PetDAO extends GenericDAO<Pet, Long> {

    public Set<Pet> findPetsByName(String name) {

        String queryString = "select p from Pet p where p.name LIKE :name";
        name = "%"+ name +"%";
        TypedQuery<Pet> typedQuery = entityManager.createQuery(queryString, Pet.class);
        List<Pet> userList = typedQuery.setParameter("name", name).getResultList();

        return new HashSet<>(userList);

    }
}
