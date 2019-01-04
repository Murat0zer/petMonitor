package com.petmonitor.owner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.faces.bean.ManagedBean;
import javax.persistence.Embeddable;

@ManagedBean
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContactInformation {
	
	private String phoneNumber;
	private String eMail;

}
