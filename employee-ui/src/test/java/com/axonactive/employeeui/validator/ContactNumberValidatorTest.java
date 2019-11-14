package com.axonactive.employeeui.validator;

import static org.junit.Assert.*;

import org.junit.Test;

public class ContactNumberValidatorTest {
	ContactNumberValidator contactNumberValidator = new ContactNumberValidator();

	@Test
	public void test() {
		assertTrue(contactNumberValidator.isValid("(+84)123456", null));
		assertTrue(contactNumberValidator.isValid("(+84)1234567", null));
		assertTrue(contactNumberValidator.isValid("+(84)1234567", null));
		assertTrue(contactNumberValidator.isValid("(+84)12345678901", null));
		assertTrue(contactNumberValidator.isValid("(+84)123456789012", null));
		assertFalse(contactNumberValidator.isValid("[+84]12345678901", null));
		assertFalse(contactNumberValidator.isValid("[+84)12345678901", null));
		assertFalse(contactNumberValidator.isValid("+[84]12345678901", null));
		assertTrue(contactNumberValidator.isValid("+8412345678901", null));
	}
}
