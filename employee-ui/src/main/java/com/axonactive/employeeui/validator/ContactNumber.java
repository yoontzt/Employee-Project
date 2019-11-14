package com.axonactive.employeeui.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD,})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ContactNumberValidator.class})
public @interface ContactNumber {

	String message() default "{Employee.ContactNumber.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
