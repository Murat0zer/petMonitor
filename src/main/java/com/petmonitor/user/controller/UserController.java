package com.petmonitor.user.controller;


import com.petmonitor.pet.model.Pet;
import com.petmonitor.user.model.ContactInformation;
import com.petmonitor.user.model.User;
import com.petmonitor.user.service.UserService;
import com.sun.el.MethodExpressionLiteral;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.el.MethodExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
@Getter
@Setter
public class UserController {

    @NotBlank(message = "Name can not be blank !")
    private String name;

    @NotBlank
    private String surname;

    @Size(min = 3)
    @NotBlank
    private String username;

    @NumberFormat
    private String phoneNumber;

    @Email
    private String email;

    @Length(min = 6, max = 20)
    private String password;


    public void addOwner() {

        UserService userService = new UserService();
        User user = User.builder()
                .username(username)
                .name(name)
                .surname(surname)
                .password(password)
                .contactInformation(new ContactInformation(phoneNumber, email))
                .pets(new ArrayList<>())
                .build();
        userService.save(user);

        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
        navigationHandler.handleNavigation(fc, null, "/login.xhtml?faces-redirect=true");
        fc.renderResponse();
    }

    public String redirectUrlIfUserAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if( ! (authentication  instanceof AnonymousAuthenticationToken)){
            return "welcome.xhtml";
        }
        return "";
    }
}
