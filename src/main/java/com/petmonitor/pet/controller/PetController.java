package com.petmonitor.pet.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("petController")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class PetController implements Serializable {
}
