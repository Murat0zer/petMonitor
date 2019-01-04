package com.petmonitor.pet.model;

import com.petmonitor.owner.model.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String species;
    private String breed;
    private String name;
    private int age;
    private String description;

    @ManyToOne
    private Owner owner;


}
