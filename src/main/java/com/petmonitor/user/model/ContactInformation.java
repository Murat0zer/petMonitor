package com.petmonitor.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.faces.bean.ManagedBean;
import javax.persistence.Embeddable;
import java.io.Serializable;

@ManagedBean
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContactInformation implements Serializable {
	
	private String phoneNumber;
	private String email;

}
