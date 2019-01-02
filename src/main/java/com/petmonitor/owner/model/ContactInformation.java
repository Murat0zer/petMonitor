package com.petmonitor.owner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.faces.bean.ManagedBean;

@ManagedBean
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformation {
	
	private String phoneNumber;
	private String eMail;

}
