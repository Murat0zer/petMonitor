package com.petmonitor.user.model;

import com.petmonitor.pet.model.Pet;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "User")
@Table(name = "user")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;


    @Column(unique = true)
    @Size(min = 3)
    @NotBlank
    private String username;

    @Valid
    @Embedded
    private ContactInformation contactInformation;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Pet> pets = new HashSet<>();

    @NotBlank
    @Size(min = 6)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Builder.Default
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setUser(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setUser(null);
    }

}
