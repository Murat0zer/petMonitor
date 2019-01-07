package com.petmonitor.user.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContactInformation implements Serializable {

	@NotBlank
	private String phoneNumber;

	@Email
	private String email;

}
