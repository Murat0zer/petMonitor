package com.petmonitor.user;


import com.petmonitor.user.controller.UserController;
import com.petmonitor.user.model.ContactInformation;
import com.petmonitor.user.model.User;
import com.petmonitor.user.service.UserService;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.RollbackException;
import javax.validation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UserTests {

    private Validator validator;

    private User user;

    @BeforeClass
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Before
    public void initTestData() {

        user = User.builder()
                .password("123456")
                .name("user")
                .surname("surname")
                .contactInformation(new ContactInformation("53210", "a@bc.com"))
                .pets(new ArrayList<>())
                .build();
    }

    @Test
    public void createOwner() {

        UserService userService = new UserService();

        assertThat(validator.validate(user), IsEmptyCollection.empty());

        User persistedUser = userService.save(user);

        assertEquals(persistedUser, user);
    }

    @Test(expected = RollbackException.class)
    public void createOwner_WrongPassword() {

        user.setPassword("123");

        UserController userController = new UserController();
        userController.setName(user.getName());
        userController.setPassword(user.getPassword());
        userController.addOwner();
        Set<ConstraintViolation<User>> exceptions = validator.validate(user);

        Predicate<ConstraintViolation<User>> predicate;
        predicate = ownerConstraintViolation -> ownerConstraintViolation.getInvalidValue().equals(user.getPassword());

        Set<ConstraintViolation<User>> expectedExceptions;
        expectedExceptions = exceptions
                .stream()
                .filter(predicate)
                .collect(Collectors.toCollection(HashSet::new));

        assertThat(expectedExceptions, not(IsEmptyCollection.empty()));

    }

    @Test
    public void findOwner() {

    }
}
