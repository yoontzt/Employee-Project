package com.axonactive.employeeui.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FakeAddEmployeeDTO implements Serializable{

	private static final long serialVersionUID = 4300224350493436869L;

	private FakeEmployeeDTO employee;

	@Valid
	private List<FakeContactDTO> additionalContacts;
	
    @Valid
	private List<FakeCertificateDTO> certificates;

}
