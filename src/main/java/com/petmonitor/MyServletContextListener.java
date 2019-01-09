package com.petmonitor;

import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.ContactInformation;
import com.petmonitor.user.model.Role;
import com.petmonitor.user.model.User;
import com.petmonitor.user.model.UserDTO;
import com.petmonitor.user.repository.RoleDAO;
import com.petmonitor.user.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;

@Slf4j
public class MyServletContextListener implements ServletContextListener {

    private RoleDAO roleDAO = new RoleDAO();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        roleDAO.save(Role.ADMIN);
        roleDAO.save(Role.OWNER);


        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = User.builder()
                .password(passwordEncoder.encode("123456"))
                .username("murat")
                .name("user")
                .surname("surname")
                .contactInformation(new ContactInformation("123456", "abc@abc.com"))
                .pets(new HashSet<>())
                .build();


        int petCount = 5;
        for (int i = 0; i < petCount; i++) {
            Pet pet = Pet.builder()
                    .name("pet name" + i)
                    .species("species" + i)
                    .breed("breed" + i)
                    .age(1 + i)
                    .description("description" + i)
                    .build();
            user.addPet(pet);
        }

        UserDAO userDAO = new UserDAO();
        userDAO.save(user);

    }
}
