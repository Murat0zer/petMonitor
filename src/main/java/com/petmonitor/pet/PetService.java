package com.petmonitor.pet;

import com.petmonitor.pet.repository.PetDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

@ApplicationScoped
@Named(value = "petService")
public class PetService implements Serializable {

    private PetDAO petDAO;

    public PetService() {
        petDAO = new PetDAO();
    }


}
