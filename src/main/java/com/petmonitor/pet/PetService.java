package com.petmonitor.pet;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.pet.repository.PetDAO;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ApplicationScoped
@Named(value = "petService")
public class PetService implements Serializable {

    private PetDAO petDAO;


    public PetService() {
        petDAO = new PetDAO();
    }

    public Set<Pet> searchPet(String keyword) {

        return petDAO.findPetsByName(keyword);
    }

    public void updatePet(Pet pet) {

        petDAO.update(pet);
    }
}
