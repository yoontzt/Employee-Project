package com.axonactive.employeecore.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {
	private String errorCode;
	private String errorMessages;
	private String timeStamp;

	public ErrorMessage() {
	}

	public ErrorMessage(String errorCode, String errorMessage, String timeStamp) {
		super();
		this.errorCode = errorCode;
		this.errorMessages = errorMessage;
		this.timeStamp = timeStamp;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessages() {
		return errorMessages;
	}

	public void seterrorMessage(String errorMessage) {
		this.errorMessages = errorMessage;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	

}
