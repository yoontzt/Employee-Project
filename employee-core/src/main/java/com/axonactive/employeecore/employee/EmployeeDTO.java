package com.axonactive.employeecore.employee;

import java.util.List;

import javax.validation.Valid;

import com.axonactive.employeecore.additionalcontact.ContactDTO;
import com.axonactive.employeecore.certificate.CertificateDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

	private BasicEmployeeDTO employee;

	@Valid
	private List<ContactDTO> additionalContacts;
	
	private List<CertificateDTO> certificates;

}
