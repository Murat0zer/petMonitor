package com.petmonitor.owner;


import com.petmonitor.owner.controller.OwnerController;
import com.petmonitor.owner.model.ContactInformation;
import com.petmonitor.owner.model.Owner;
import com.petmonitor.owner.service.OwnerService;
import com.petmonitor.owner.service.OwnerServiceImpl;
import org.hamcrest.collection.IsEmptyCollection;
import org.hibernate.validator.constraints.Length;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OwnerTests {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void createOwner() {

        Owner owner = Owner.builder()
                .password("123456")
                .name("owner")
                .surname("surname")
                .contactInformation(new ContactInformation("53210", "a@bc.com"))
                .pets(new ArrayList<>())
                .build();

        OwnerService ownerService = new OwnerServiceImpl();

        assertThat(validator.validate(owner), IsEmptyCollection.empty());

        Owner persistedOwner = ownerService.save(owner);

        assertEquals(persistedOwner, owner);
    }

    @Test(expected = Exception.class)
    public void createOwnerWithViolatingPasswordAndExpectException() {
        Owner owner = Owner.builder()
                .password("123")
                .name("owner")
                .surname("surname")
                .contactInformation(new ContactInformation("53210", "a@bc.com"))
                .pets(new ArrayList<>())
                .build();

        OwnerController ownerController = new OwnerController();
        ownerController.setName(owner.getName());
        ownerController.setPassword(owner.getPassword());
        ownerController.addOwner();
        Set<ConstraintViolation<Owner>> exceptions = validator.validate(owner);

        Predicate<ConstraintViolation<Owner>> predicate;
        predicate = ownerConstraintViolation -> ownerConstraintViolation.getInvalidValue().equals(owner.getPassword());

        Set<ConstraintViolation<Owner>> expectedExceptions;
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
