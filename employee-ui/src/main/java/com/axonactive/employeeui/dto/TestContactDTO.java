package com.axonactive.employeeui.dto;

import java.io.Serializable;

import com.axonactive.employeeui.validator.ValidContact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "type", "value" })
@ValidContact
public class TestContactDTO implements Serializable {

	private static final long serialVersionUID = -8723284714150795494L;

	private Integer contactId;

	private String type;

	private String value;

}
