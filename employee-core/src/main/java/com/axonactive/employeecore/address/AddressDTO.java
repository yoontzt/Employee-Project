package com.axonactive.employeecore.address;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

	private int addressId;

	@NotNull
	private String address;

	@NotNull
	private String city;

	@NotNull
	private String country;

}
