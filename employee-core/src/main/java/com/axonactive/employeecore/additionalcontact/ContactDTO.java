package com.axonactive.employeecore.additionalcontact;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ContactDTO {

	private Integer contactId;

	private String type;

	@Size(max = 255, message = "{Contact.Value.Invalid.Message}")
	private String value;
}
