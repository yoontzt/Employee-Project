package com.axonactive.employeeui.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = -4711341167972451211L;

	public SystemException() {
		super();
	}

	 public SystemException(String message) {
	        super(message);
	    }
}
