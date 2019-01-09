package com.petmonitor.pet.controller;

import com.petmonitor.pet.PetService;
import com.petmonitor.pet.model.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

@Named("petController")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class PetController implements Serializable {

    PetService petService = new PetService();
    private Set<Pet> pets;

    private String searchString;

    public void findPets() {
        pets = petService.searchPet(searchString);
    }
}
