package com.petmonitor.user.repository;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.User;
import com.petmonitor.util.GenericDAO;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class UserDAO extends GenericDAO<User, Long> {


    public List<Pet> getOwnerPets(long id) {

        String queryString = "select p from Pet p where p.user.id=:id";

        return entityManager.createQuery(queryString, Pet.class).setParameter("id", id).getResultList();
    }

    public Optional<User> findByUsername(String username) {
        entityManager.getTransaction().begin();

        String queryString = "select u from User u where u.username = :username";
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);

        User user = User.builder().build();
        try {
            user = query.setParameter("username", username).getSingleResult();
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

        return Optional.of(user);
    }

    public Set<User> findUsersByName(String name) {

        name = "%"+ name +"%";
        String queryString = "select u from User u where u.name LIKE :name";
        TypedQuery<User> typedQuery = entityManager.createQuery(queryString, User.class);
        List<User> userList = typedQuery.setParameter("name", name).getResultList();

        return new HashSet<>(userList);

    }
}
