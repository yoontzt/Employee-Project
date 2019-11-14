package com.axonactive.employeeui.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.axonactive.employeeui.dto.ContactType;
import com.axonactive.employeeui.dto.FakeContactDTO;

import antlr.StringUtils;

public class ContactValidator implements ConstraintValidator<ValidContact, FakeContactDTO> {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_PATTERN = "^[+]*[(]{0,1}[+]*[0-9]{1,4}[)]{0,1}[-\\(\\sext.0-9\\)]*"; 
	private static final String INVALID_EMAIL = "Email is invalid.";
	private static final String INVALID_PHONE = "Phone is invalid.";
	private static final int MAXIMUM_LENGTH_OF_OTHER_LINK = 255;
	private static final int MAXIMUM_LENGTH_OF_OTHER_CONTACT = 50;
	private static final String INVALID_LENGTH = "Length should be between 1 and ";

	@Override
	public void initialize(ValidContact contactNumber) {
	}

	@Override
	public boolean isValid(FakeContactDTO contact, ConstraintValidatorContext context) {
		String value = contact.getValue();
		 ContactType type = contact.getType();
		
		if(!isValidContactLength(contact)) {
			Integer maxLength = ContactType.LINK == contact.getType() ? MAXIMUM_LENGTH_OF_OTHER_LINK : MAXIMUM_LENGTH_OF_OTHER_CONTACT;
			addMessage(context, INVALID_LENGTH +maxLength+" characters");
			return false;
		}else {
			if(type.equals(ContactType.EMAIL)) {
				if(!isValidEmail(value)) {
					addMessage(context, INVALID_EMAIL);
					return false;
				}
			}else if(type.equals(ContactType.PHONE)) {
				if(!isValidPhone(value)) {
					addMessage(context, INVALID_PHONE);
					return false;
				}
			}
		}
		return true;
		}
		
	public boolean isValidEmail(String email) {
		return (email.matches(EMAIL_PATTERN));
	}

	public boolean isValidPhone(String phone) {
		return (phone.matches(PHONE_PATTERN));
	}

	
	private boolean isValidContactLength(FakeContactDTO contact) {
		if (ContactType.LINK == contact.getType()) {
			return contact.getValue().length() < MAXIMUM_LENGTH_OF_OTHER_LINK;
		}
		return  contact.getValue().length() < MAXIMUM_LENGTH_OF_OTHER_CONTACT;
	}
	
	private void addMessage(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
