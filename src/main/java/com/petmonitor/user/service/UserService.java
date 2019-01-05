package com.petmonitor.user.service;

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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
@Named(value = "userService")
public class UserService implements UserDetailsService {

    private UserDAOImpl ownerDao;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService() {
        ownerDao = new UserDAOImpl();
    }

    public User save(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();

        roles.add((Role.OWNER));

        user.setRoles(roles);
        return ownerDao.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = ownerDao.findByUsername(username);
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("user not found by '" + username));
    }


}
