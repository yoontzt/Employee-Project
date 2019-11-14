package com.axonactive.employeeui.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.axonactive.employeeui.validator.ContactNumber;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FakeEmployeeDTO implements Serializable{

	private static final long serialVersionUID = 6974853003631296852L;

	private Integer employeeId;

	@Pattern(regexp = "^*[^0-9].*", message = "{Name.Format.Message}")
	@Size(min = 1, max = 50, message = "{Name.Size.Invalid.Message}")
	private String lastname;

	@Pattern(regexp = "^*[^0-9].*", message = "{Name.Format.Message}")
	@Size(min = 1, max = 50, message = "{Name.Size.Invalid.Message}")
	private String firstname;

	@Past(message = "{Date.Should.Past}")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone="GMT+7")
	private Date dateOfBirth;

	private String nationality;
	
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-\\+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})*(( \\([A-Za-z0-9\\.@\\ \\)]+)*)$", message = "{Email.Validate.Message}")
	@Size(min = 1, max = 50, message = "{Email.Size.Invalid.Message}")
	private String primaryEmail;

	@ContactNumber(message = "{Mobilephone.Validate.Message}")
	private String mobilePhone;

	private String skype;

	private String emergencyContact;

	private FakeAddressDTO address;

	private FakeDepartmentDTO department;
}
