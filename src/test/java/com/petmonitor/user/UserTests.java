package com.petmonitor.user;


import com.petmonitor.pet.PetService;
import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.controller.UserController;
import com.petmonitor.user.model.ContactInformation;
import com.petmonitor.user.model.Role;
import com.petmonitor.user.model.User;
import com.petmonitor.user.model.UserDTO;
import com.petmonitor.user.repository.RoleDAO;
import com.petmonitor.user.repository.UserDAO;
import com.petmonitor.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.NoResultException;
import javax.validation.*;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@Slf4j
public class UserTests {

    private static Validator validator;

    private static Properties properties = new Properties();
    private static UserController userController = new UserController(new UserService(), new PetService());
    private User user;
    private UserDTO userDTO;
    private UserDAO userDAO = new UserDAO();

    @BeforeClass
    public static void setUp() {

        RoleDAO roleDAO = new RoleDAO();

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
                .pets(new HashSet<>())
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

        userController.getUserService().save(user);
        long id = user.getId();
        User persistedUser = userController.findUserById(id);

        assertEquals(persistedUser.hashCode(), user.hashCode());

        userDAO.delete(persistedUser);
    }

    @Test(expected = NoResultException.class)
    public void findOwner_DoesntExists() {
        userController.findUserById(123L);
    }

    @Test(expected = NoResultException.class)
    public void deleteOwner() {
        userDAO.save(user);
        long id = user.getId();


        userDAO.delete(userDAO.get(id).orElseThrow(RuntimeException::new));
        userController.findUserById(id);

    }

    @Test
    public void getAllOwners() {

        int expectedUserCount = 5;
        User newUser;
        for (int i = 0; i < expectedUserCount; i++) {
            newUser = User.builder()
                    .contactInformation(new ContactInformation("12345", "abc@b.com"))
                    .name("name")
                    .password("password")
                    .pets(new HashSet<>())
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
                .pets(new HashSet<>())
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

    @Test
    public void ownerCreatePet() {
        int petCount = user.getPets().size();
        Pet pet = Pet.builder()
                .name("pet name" + petCount)
                .species("species" + petCount)
                .breed("breed" + petCount)
                .age(1 + petCount)
                .description("description" + petCount)
                .build();
        user.addPet(pet);
        userDAO.save(user);
        List<Pet> pets = userDAO.getOwnerPets(user.getId());
        assertEquals(petCount + 1, pets.size());
    }

    @Test
    public void removePetFromOwner() {
        int petCount = user.getPets().size();
        user.removePet(user.getPets().iterator().next());
        userDAO.save(user);
        List<Pet> pets = userDAO.getOwnerPets(user.getId());
        assertEquals(petCount - 1, pets.size());
    }

    @Test(expected = AccessDeniedException.class)
    public void removeNotOwnedPet() {

        this.authentication();
        userController.removePet(user.getPets().iterator().next());
    }

    @Test(expected = AccessDeniedException.class)
    public void addPetToOwner() {
        this.authentication();
        Pet pet = Pet.builder()
                .name("pet name")
                .species("species")
                .breed("breed")
                .age(1)
                .description("description")
                .build();
        user.addPet(pet);
        userController.addPet(pet);

    }

    private void checkValidationMessage(String expectedMessage) {

        Set<ConstraintViolation<UserDTO>> constraintViolations = validator.validate(userDTO);

        String message = constraintViolations.iterator().next().getMessage();
        assertEquals(expectedMessage, message);
        assertThat(constraintViolations, hasSize(1));

        userController.setUserDTO(userDTO);
        userController.addOwner();
    }

    private void authentication() {
        User unAuthorizedUser = User.builder()
                .contactInformation(new ContactInformation("12345", "abc@b.com"))
                .name("name")
                .password("password")
                .pets(new HashSet<>())
                .surname("surname")
                .username("unAuthorizedUser")
                .build();
        UserService userService = new UserService();
        userService.save(unAuthorizedUser);

        TestingAuthenticationToken testingAuthenticationToken;
        testingAuthenticationToken = new TestingAuthenticationToken(unAuthorizedUser.getUsername(),
                unAuthorizedUser.getPassword()
                , unAuthorizedUser.getRoles().iterator().next().getAuthority());

        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }




}
