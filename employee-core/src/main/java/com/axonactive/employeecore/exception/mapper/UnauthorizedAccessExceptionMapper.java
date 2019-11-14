package com.axonactive.employeecore.exception.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.axonactive.employeecore.exception.ErrorMessage;
import com.axonactive.employeecore.exception.UnauthorizedAccessException;


@Provider
public class UnauthorizedAccessExceptionMapper implements ExceptionMapper<UnauthorizedAccessException> {
	@Override
	public Response toResponse(UnauthorizedAccessException exception) {
		Date d = new Date();
		SimpleDateFormat timeGMT = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss z");
		timeGMT.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
		String timeStampLocal = timeGMT.format(d);
		ErrorMessage errorResponse =new ErrorMessage(exception.getErrorCode(), exception.getMessage(), timeStampLocal);
		return Response.status(Status.UNAUTHORIZED).entity(errorResponse).build();
	}

}
