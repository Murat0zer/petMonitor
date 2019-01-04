package com.petmonitor.owner.model;

import com.petmonitor.pet.model.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@ManagedBean
@SessionScoped
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Name can not be blank !")
    private String name;

    @NotBlank
    private String surname;


    private ContactInformation contactInformation;

    @OneToMany
    private List<Pet> pets;

    @Length(min = 6, max = 20)
    private String password;

}
