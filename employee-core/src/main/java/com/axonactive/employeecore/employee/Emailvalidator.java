package com.axonactive.employeecore.employee;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Emailvalidator implements ConstraintValidator<Email, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return false;
	}

}
