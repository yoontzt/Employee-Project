package com.axonactive.employeeui.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
	private @Getter @Setter String errorCode;
	private @Getter @Setter String errorMessages;
	private @Getter @Setter  String timeStamp;

	
}