package com.petmonitor.user.controller;


import com.petmonitor.pet.PetService;
import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.ContactInformation;
import com.petmonitor.user.model.Role;
import com.petmonitor.user.model.User;
import com.petmonitor.user.model.UserDTO;
import com.petmonitor.user.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Named("userController")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class UserController implements Serializable {

    private UserDTO userDTO;

    private  UserService userService;

    private PetService petService;

    private Pet pet;

    @Inject
    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.userDTO = UserDTO.builder().build();
        this.petService = petService;
    }

    public void addOwner() {

        User user = User.builder()
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .password(userDTO.getPassword())
                .contactInformation(new ContactInformation(userDTO.getPhoneNumber(), userDTO.getEmail()))
                .pets(new HashSet<>())
                .build();
        userService.save(user);

        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
        navigationHandler.handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        fc.renderResponse();
    }


    public String redirectUrlIfUserAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "welcome.xhtml";
        }
        return "";
    }

    public User findUserById(long id) {

        return userService.findUserById(id);
    }

    public List<User> getAll() {
        return userService.getAll();
    }

    public void updateUser() {

        User user = User.builder()
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .password(userDTO.getPassword())
                .contactInformation(new ContactInformation(userDTO.getPhoneNumber(), userDTO.getEmail()))
                .pets(userDTO.getPets())
                .build();
        userService.save(user);
    }

    public void removePet(Pet pet) {
        userService.removePet(pet);
    }

    public void addPet(Pet pet) {
        userService.addPet(pet);
    }
}
