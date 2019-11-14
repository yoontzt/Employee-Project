package com.axonactive.employeeui.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FakeAddressDTO implements Serializable{
	
	private static final long serialVersionUID = -6787285902337128910L;

	private Integer addressId;

	private String address;
	
	private String city;

	private String country;
	
}
