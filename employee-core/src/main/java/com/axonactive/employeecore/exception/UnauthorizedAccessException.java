package com.axonactive.employeecore.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class UnauthorizedAccessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String errorCode;
	
    public UnauthorizedAccessException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}


	public String getErrorCode() {
		return errorCode;
	}


	@Override
	public String toString() {
		return "UnauthorizedAccessException [errorCode=" + errorCode +", message="+ getMessage() + "]";
	}
    
	
}
