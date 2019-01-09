package com.petmonitor.user.controller;


import com.petmonitor.pet.PetService;
import com.petmonitor.pet.controller.PetController;
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

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named("userController")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class UserController implements Serializable {

    private UserDTO userDTO;

    private UserService userService;

    private PetController petController;

    private PetService petService;

    private Pet pet;

    private User currentUser;

    private Set<User> users;

    private String searchString;

    private boolean authentication;

    @Inject
    public UserController(UserService userService, PetService petService, PetController petController) {
        this.userService = userService;
        this.userDTO = UserDTO.builder().build();
        this.pet = Pet.builder().build();
        this.petService = petService;
        this.petController = petController;
    }

    public void loadCurrentUser() {
        this.currentUser = userService.loadCurrentUser();
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


        if (!( SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
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

        userService.save(currentUser);

        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
        navigationHandler.handleNavigation(fc, null, "/user.xhtml?faces-redirect=true");
        fc.renderResponse();
    }

    public boolean checkAuthentication(String userId) {

        authentication = currentUser.getRoles().contains(Role.ADMIN) || currentUser.getId() == Long.parseLong(userId);
        return authentication;
    }

    public void removePet(Pet pet) {
        try {
            userService.removePet(pet);
            petController.getPets().remove(pet);
        } catch (Exception e) {
            throw e;
        }
    }

    public void addPet(Pet pet) {
        userService.addPet(pet);
    }

    public void addPet() {
        userService.addPet(pet);
    }

    public void findUsers() {
        users = userService.searchUser(searchString);
    }

    public String getUserDetailPage() {
        return "userdetail.xhtml";
    }
}
