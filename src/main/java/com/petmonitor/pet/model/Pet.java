package com.petmonitor.pet.model;

import com.petmonitor.owner.model.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    private String species;
    private String breed;
    private String name;
    private int age;
    private String description;
    private Owner owner;


}
