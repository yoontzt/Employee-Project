package com.axonactive.employeeui.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FakeCertificateDTO implements Serializable {

	private static final long serialVersionUID = -8829491495585746010L;

	private Integer certificateId;

	@Size(max = 255, message = "{Certificate.Name.Invalid.Message}")
	private String nameOfCertificate;

	@Size(max = 100, message = "{Certificate.Type.Invalid.Message}")
	private String typeOfCertificate;

	private String achievedTime;
}
