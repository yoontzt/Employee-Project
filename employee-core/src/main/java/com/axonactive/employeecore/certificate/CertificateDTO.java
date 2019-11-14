package com.axonactive.employeecore.certificate;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {

	private Integer certificateId;

	@Size(max = 255, message = "{Certificate.Name.Invalid.Message}")
	private String nameOfCertificate;

	@Size(max = 100, message = "{Certificate.Type.Invalid.Message}")
	private String typeOfCertificate;

	private String achievedTime;
}
