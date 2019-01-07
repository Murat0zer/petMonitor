package com.petmonitor.user;


import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.controller.UserController;
import com.petmonitor.user.model.ContactInformation;
import com.petmonitor.user.model.Role;
import com.petmonitor.user.model.User;
import com.petmonitor.user.model.UserDTO;
import com.petmonitor.user.repository.RoleDAOImpl;
import com.petmonitor.user.repository.UserDAOImpl;
import com.petmonitor.user.service.UserService;
import com.petmonitor.util.GeneralDAO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.NoResultException;
import javax.validation.*;
import java.io.InputStream;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@Slf4j
public class UserTests {

    private static Validator validator;

    private static Properties properties = new Properties();

    private User user;

    private UserDTO userDTO;

    private UserDAOImpl userDAO = new UserDAOImpl();

    @BeforeClass
    public static void setUp() {

        RoleDAOImpl roleDAO = new RoleDAOImpl();

        roleDAO.save(Role.OWNER);
        roleDAO.save(Role.ADMIN);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("ValidationMessages.properties");

        try {
            properties.load(input);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Before
    public void initTestData() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userDTO = UserDTO.builder()
                .password(passwordEncoder.encode("123456"))
                .username("username_1234")
                .name("user")
                .surname("surname")
                .phoneNumber("53210")
                .email("a@bc.com")
                .pets(new ArrayList<>())
                .build();

        user = User.builder()
                .contactInformation(new ContactInformation(userDTO.getPhoneNumber(), userDTO.getEmail()))
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .pets(userDTO.getPets())
                .surname(userDTO.getSurname())
                .username(userDTO.getUsername())
                .build();

        int petCount = 5;
        for (int i =0; i<petCount; i++){
            Pet pet = Pet.builder()
                    .name("pet name" + i)
                    .species("species" +i)
                    .breed("breed" +i)
                    .age(1 + i)
                    .description("description" + i)
                    .build();
            user.addPet(pet);
        }
    }

    @Test
    public void createOwner() {

        UserService userService = new UserService();
        User persistedUser = userService.save(user);

        assertThat(validator.validate(user), IsEmptyCollection.empty());

        assertEquals(persistedUser, user);
        userService.delete(persistedUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createOwner_InsufficientPassword() {

        userDTO.setPassword("123");
        user.setPassword("123");

        String expectedMessage = properties.getProperty("user.password.length");
        this.checkValidationMessage(expectedMessage);

    }

    @Test(expected = ConstraintViolationException.class)
    public void createOwner_BlankUsername() {

        user.setUsername("u");
        userDTO.setUsername("u");

        String expectedMessage = properties.getProperty("user.username.length");
        this.checkValidationMessage(expectedMessage);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createOwner_BlankName() {
        user.setName("");
        userDTO.setName("");

        String expectedMessage = properties.getProperty("user.name.blank");
        this.checkValidationMessage(expectedMessage);

    }

    @Test(expected = ConstraintViolationException.class)
    public void createOwner_BlankSurname() {
        user.setSurname("");
        userDTO.setSurname("");

        String expectedMessage = properties.getProperty("user.surname.blank");
        this.checkValidationMessage(expectedMessage);

    }

    @Test(expected = ConstraintViolationException.class)
    public void createOwner_BlankPhoneNumber() {
        user.getContactInformation().setPhoneNumber("");
        userDTO.setPhoneNumber("");

        String expectedMessage = properties.getProperty("user.phoneNumber.blank");
        this.checkValidationMessage(expectedMessage);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createOwner_InvalidEmail() {
        user.getContactInformation().setEmail("invalid email");
        userDTO.setEmail("invalid email");

        String expectedMessage = properties.getProperty("user.email.invalid");
        this.checkValidationMessage(expectedMessage);
    }


    @Test
    public void findOwnerById() {
        UserController userController = new UserController(new UserService());

        userController.getUserService().save(user);
        long id = user.getId();
        User persistedUser = userController.findUserById(id);

        assertEquals(persistedUser, user);

        userDAO.delete(persistedUser);
    }

    @Test(expected = NoResultException.class)
    public void findOwner_DoesntExists() {
        UserController userController = new UserController(new UserService());
        userController.findUserById(123L);
    }

    @Test(expected = NoResultException.class)
    public void deleteOwner() {
        userDAO.save(user);
        long id = user.getId();

        GeneralDAO generalDAO = new GeneralDAO();
        userDAO.delete(generalDAO.get(id, User.class).orElseThrow(RuntimeException::new));
        UserController userController = new UserController(new UserService());
        userController.findUserById(id);

    }

    @Test
    public void getAllOwners() {

        int expectedUserCount = 5;
        UserController userController = new UserController(new UserService());
        User newUser;
        for (int i = 0; i < expectedUserCount; i++) {
            newUser = User.builder()
                    .contactInformation(new ContactInformation("12345", "abc@b.com"))
                    .name("name")
                    .password("password")
                    .pets(new ArrayList<>())
                    .surname("surname")
                    .username("username" + i)
                    .build();
            userDAO.save(newUser);
        }
        List<User> users = userController.getAll();

        assertEquals(expectedUserCount, users.size());

        users.forEach(user1 -> userDAO.delete(userDAO.get(user1.getId()).orElseThrow(RuntimeException::new)));
    }

    @Test
    public void updateOwner() {

        UserService userService = new UserService();
        User newUser = User.builder()
                .contactInformation(new ContactInformation("12345", "abc@b.com"))
                .name("name")
                .password("password")
                .pets(new ArrayList<>())
                .surname("surname")
                .username("username")
                .build();

        userService.save(user);
        long id = user.getId();

        user.setUsername("changed username");

        userService.updateUser(user);

        userService.save(newUser);

        User updatedUser = userService.findUserById(id);
        assertNotEquals(updatedUser, newUser);

        userService.delete(newUser);
        userService.delete(user);
    }

    @Test
    public void getOwnerPets() {
        userDAO.save(user);
        int petCount = user.getPets().size();
        UserService userService = new UserService();
        List<Pet> pets = userService.getOwnerPets(user.getId());
        assertEquals(petCount, pets.size());
        userDAO.delete(user);
    }
    private void checkValidationMessage(String expectedMessage) {

        Set<ConstraintViolation<UserDTO>> constraintViolations = validator.validate(userDTO);

        String message = constraintViolations.iterator().next().getMessage();
        assertEquals(expectedMessage, message);
        assertThat(constraintViolations, hasSize(1));

        UserController userController = new UserController(new UserService());

        userController.setUserDTO(userDTO);
        userController.addOwner();
    }


}
