package com.petmonitor.owner.controller;


import com.petmonitor.owner.model.Owner;
import com.petmonitor.owner.service.OwnerService;
import com.petmonitor.owner.service.OwnerServiceImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@ManagedBean
@SessionScoped
public class OwnerController {

    @NotBlank(message = "Name can not be blank !")
    private String name;
    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void addOwner() {

        OwnerService ownerService = new OwnerServiceImpl();
        Owner owner = Owner.builder().name(name).surname("surname").password(password).build();
        ownerService.save(owner);
    }
}
