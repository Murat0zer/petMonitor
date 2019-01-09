package com.petmonitor.pet.model;

import com.petmonitor.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Pet")
@Table(name = "pet")
public class Pet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

        private String species;
        private String breed;
        private String name;
        private int age;
        private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return getId() == pet.getId() &&
                getAge() == pet.getAge() &&
                getSpecies().equals(pet.getSpecies()) &&
                getBreed().equals(pet.getBreed()) &&
                getName().equals(pet.getName()) &&
                getDescription().equals(pet.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSpecies(), getBreed(), getName(), getAge(), getDescription());
    }


    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", description='" + description + '\'' +
                '}';
    }
}
