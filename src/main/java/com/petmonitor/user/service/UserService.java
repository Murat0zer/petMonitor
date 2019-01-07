package com.petmonitor.user.service;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.Role;
import com.petmonitor.user.model.User;
import com.petmonitor.user.repository.UserDAOImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.validation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@Named(value = "userService")
public class UserService implements UserDetailsService {

    private UserDAOImpl userDAO;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public UserService() {
        userDAO = new UserDAOImpl();
    }

    public User save(User user) {

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if(!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations.iterator().next().getMessage(), constraintViolations);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();

        roles.add((Role.OWNER));

        user.setRoles(roles);
        return userDAO.save(user);
    }

    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
}
