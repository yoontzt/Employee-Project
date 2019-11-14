package com.axonactive.employeeui.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumber, String> {

	private static final String PHONE_PATTERN = "^[+]*[(]{0,1}[+]*[0-9]{1,4}[)]{0,1}[-\\(\\sext.0-9\\)]*"; 
	
	@Override
	public void initialize(ContactNumber contactNumber) {
		//To initialize the attributes of contactNumber
	}

	@Override
	public boolean isValid(String contactField, ConstraintValidatorContext context) {
		return contactField != null && contactField.matches(PHONE_PATTERN);
	}
}
