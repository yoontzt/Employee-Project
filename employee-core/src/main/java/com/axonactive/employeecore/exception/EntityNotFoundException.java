package com.axonactive.employeecore.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
}

