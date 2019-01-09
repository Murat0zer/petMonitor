package com.petmonitor.pet.controller;

import com.petmonitor.pet.PetService;
import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.controller.UserController;
import com.petmonitor.user.model.User;
import com.petmonitor.user.repository.UserDAO;
import com.petmonitor.user.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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

    @Inject
    UserController userController;

    private Set<Pet> pets;
    private String searchString;
    private boolean authentication = false;
    private Pet pet = Pet.builder().build();

    public void findPets() {
        pets = petService.searchPet(searchString);

    }

    public boolean checkAuthentication(String petId) {
        UserService userService = new UserService();

        User user = userService.loadCurrentUser();

        authentication = user.getPets().stream().anyMatch(pet1 -> pet1.getId() == Long.parseLong(petId));
        return authentication;
    }

    public void getPetById(String id) {
        pet = petService.getPetDAO().get(Long.parseLong(id)).orElseThrow(RuntimeException::new);
    }

    public void updatePet() {

        User currentUser = userController.getCurrentUser();
        currentUser.getPets()
                .stream()
                .filter(pet1 -> pet.getId() == pet1.getId())
                .forEach(pet1 -> {
                    pet1.setAge(pet.getAge());
                    pet1.setBreed(pet.getBreed());
                    pet1.setDescription(pet.getDescription());
                    pet1.setName(pet.getName());
                });
        UserDAO userDAO = new UserDAO();
        userDAO.save(currentUser);

        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
        navigationHandler.handleNavigation(fc, null, "/welcome.xhtml?faces-redirect=true");
        fc.renderResponse();
    }

    public String getPetDetailPage() {
        return "petdetail.xhtml";
    }


}
