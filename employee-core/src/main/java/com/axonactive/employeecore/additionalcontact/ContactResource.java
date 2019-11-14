package com.axonactive.employeecore.additionalcontact;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import com.axonactive.employeecore.exception.ValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Stateless
@Path("/employees")
@SwaggerDefinition(schemes = { SwaggerDefinition.Scheme.HTTP,
		SwaggerDefinition.Scheme.HTTPS }, info = @Info(title = "Employee Management", description = "A simple example of apiee", version = "1.0.0"))
@Api(tags = "tags")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource {

	@EJB
	ContactService contactService;

	@Inject
	Logger logger;

	@Context
	UriInfo uriInfo;

	@GET
	@Path("/{employeeId}/contacts")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Fetch all Contact list of an employee")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public List<ContactDTO> getAllContactListOfEmployee(@PathParam("employeeId") Integer employeeId) {
		logger.info("Get all contact list of an employeeId: " + employeeId);
		return contactService.findAllContactByEmployeeId(employeeId);
	}

	@GET
	@Path("/{employeeId}/contacts/{contactID}")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Get a Contact information of an employee")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public Response getContact(@PathParam("employeeId") Integer employeeId, @PathParam("contactID") Integer id) {
		try {
			logger.info("Find contact by id");
			ContactDTO contactDTO = contactService.findAContactOfEmployee(employeeId, id);
			return Response.status(Status.OK).entity(contactDTO).build();
		} catch (ValidationException exception) {
			logger.error("Find certificate by id failed with id: " + id + exception);
			throw new ValidationException(exception.getMessage());
		}
	}

	@POST
	@Path("/{employeeId}/contacts")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Add new contact of an existed employee")
	@ApiResponses({ @ApiResponse(code = 201, message = "Success") })
	public Response addNewContact(@PathParam("employeeId") Integer employeeId, @Valid ContactDTO contactDTO) {
		try {
			Integer createdId = contactService.addContact(employeeId,contactDTO);
			URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(createdId)).build();
			logger.info("Add contact into system successfully! : employee id " + employeeId);
			return Response.created(uri).status(Status.CREATED).build();
		} catch (ValidationException exception) {
			throw new ValidationException(exception.getMessage());
		}
	}

	@PUT
	@Path("/{employeeId}/contacts")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Update  contact of an Employee")
	@ApiResponses({ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 404, message = "Not Found") })
	public Response updateContact(@PathParam("employeeId") Integer employeeId, @Valid ContactDTO contactDTO) {
		try {
			logger.info("Update contact into system");
			contactService.updateContact(contactDTO, employeeId);
			logger.info("Update contact into system successfully! : employee id " + employeeId);
			return Response.status(Status.NO_CONTENT).build();
		} catch (ValidationException exception) {
			throw new ValidationException(exception.getMessage());
		}
	}

	@DELETE
	@Path("/{employeeId}/contacts/{contactID}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Delete  contact of an mployee")
	@ApiResponses({ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 404, message = "Not Found") })
	public Response deleteContact(@PathParam("employeeId") Integer employeeId, @PathParam("contactID") Integer contactID) {
		try {
			logger.info("Delete contact!");
			contactService.deleteContact(contactID, employeeId);
			logger.info("Delete contact successfully!");
			return Response.status(Status.NO_CONTENT).build();
		} catch (ValidationException exception) {
			logger.error("Delete contact failed: " + exception);
			throw new ValidationException(exception.getMessage());
		}
	}
}