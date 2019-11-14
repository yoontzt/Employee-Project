package com.axonactive.employeecore.exception.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.axonactive.employeecore.exception.ErrorMessage;
import com.axonactive.employeecore.exception.ValidationException;



@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

	@Override
	public Response toResponse(ValidationException exception) {

		Date d = new Date();
		SimpleDateFormat timeGMT = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss z");
		timeGMT.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
		String timeStampLocal = timeGMT.format(d);
		ErrorMessage errorResponse =new ErrorMessage("VALIDATION.ERROR", exception.getMessage(), timeStampLocal);
		return Response.status(Status.BAD_REQUEST).entity(errorResponse).build();
	}
}