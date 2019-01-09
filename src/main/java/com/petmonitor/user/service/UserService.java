package com.petmonitor.user.service;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.Role;
import com.petmonitor.user.model.User;
import com.petmonitor.user.repository.UserDAO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Named;
import javax.validation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@Named(value = "userService")
public class UserService implements UserDetailsService, Serializable {

    private UserDAO userDAO;

    private User currentUser;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public UserService() {
        userDAO = new UserDAO();
    }

    public User save(User user) {

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations.iterator().next().getMessage(), constraintViolations);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();

        roles.add((Role.OWNER));

        user.setRoles(roles);
        return userDAO.save(user).orElseThrow(RuntimeException::new);
    }

    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userDAO.findByUsername(username);
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("user not found by '" + username));
    }

    public User findUserById(long id) {
        return userDAO.get(id).orElseThrow(() -> new RuntimeException("user not found by id=" + id));
    }

    public List<User> getAll() {
        return (List<User>) userDAO.getAll();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public List<Pet> getOwnerPets(long id) {
        return userDAO.getOwnerPets(id);
    }

    public void removePet(Pet pet) {

        this.loadCurrentUser();
        if (checkAuthentication(pet)) {
            currentUser.removePet(pet);
            userDAO.save(currentUser);
        } else {
            throw new AccessDeniedException("You have no authorize to do this");
        }
    }

    public void addPet(Pet pet) {

        this.loadCurrentUser();
        currentUser.addPet(pet);
        userDAO.save(currentUser);

    }

    public Set<User> searchUser(String keyword) {

        return userDAO.findUsersByName(keyword);
    }

    private boolean checkAuthentication(Pet pet) {

        this.loadCurrentUser();
        return currentUser.getRoles().contains(Role.ADMIN) || currentUser.getId() == pet.getUser().getId();
    }

    public User loadCurrentUser() {

        currentUser = userDAO.findByUsername(this.getCurrentUserName()).orElseThrow(RuntimeException::new);
        return currentUser;

    }

    private String getCurrentUserName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
    }
}
