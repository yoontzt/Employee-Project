
package com.axonactive.employeecore.exception.test;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.mapper.EntityNotFoundExceptionMapper;

@RunWith(PowerMockRunner.class)

@PrepareForTest({ TimeZone.class, Response.class })
public class EntityNotFoundExceptionMapperTest {

	@InjectMocks
	EntityNotFoundExceptionMapper entityNotFoundException;

	@Mock
	SimpleDateFormat timeGMT;

	@Test
	public void testToResponse_ShouldReturnBadRequest_WhenExceptionIsGiven() {

		Response.status(Status.NOT_FOUND).entity("").build();

		Response response = entityNotFoundException.toResponse(new EntityNotFoundException(""));

		PowerMockito.verifyStatic(Response.class);
		Response.status(Status.NOT_FOUND);

		assertEquals(404, response.getStatus());
	}

}
