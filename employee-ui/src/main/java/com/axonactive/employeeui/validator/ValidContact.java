package com.axonactive.employeeui.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target({ TYPE, FIELD, PARAMETER, ElementType.LOCAL_VARIABLE })
@Constraint(validatedBy = ContactValidator.class)
public @interface ValidContact {

	String message() default "{Additional.Contact.Value.Invalid.Message}";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}

