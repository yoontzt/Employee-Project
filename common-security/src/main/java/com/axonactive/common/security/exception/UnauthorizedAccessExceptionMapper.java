package com.axonactive.common.security.exception;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.axonactive.common.security.authentication.ErrorMessage;

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
