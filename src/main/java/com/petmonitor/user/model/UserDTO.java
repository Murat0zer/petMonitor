package com.petmonitor.user.model;

import com.petmonitor.pet.model.Pet;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UserDTO implements Serializable {

    @NotBlank(message = "{user.name.blank}")
    private String name;

    @NotBlank(message = "{user.surname.blank}")
    private String surname;

    @Size(min = 3, message = "{user.username.length}")
    @NotBlank(message = "{user.username.length}")
    private String username;

    @NotBlank(message = "{user.phoneNumber.blank}")
    private String phoneNumber;

    @NotBlank(message = "{user.email.invalid}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @Length(min = 6, message = "{user.password.length}")
    @NotBlank(message = "{user.password.length}")
    private String password;

    private List<Pet> pets;

}
