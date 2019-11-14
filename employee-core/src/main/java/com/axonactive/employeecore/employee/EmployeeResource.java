package com.axonactive.employeecore.employee;

import java.net.URI;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import com.axonactive.employeecore.department.DepartmentService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.securityrestclient.AuthorizationChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Stateless
@Path("/employees")
@SwaggerDefinition(schemes = { SwaggerDefinition.Scheme.HTTP,
		SwaggerDefinition.Scheme.HTTPS }, info = @Info(title = "Employee Management", description = "These are api for the system", version = "1.0.0"))
@Api(tags = "tags")
@Produces(MediaType.APPLICATION_JSON)

public class EmployeeResource {

	@EJB
	EmployeeService employeeService;

	@EJB
	DepartmentService departmentService;
	
	@EJB
	AuthorizationChecker authorizationChecker;

	@Context
	UriInfo uriInfo;

	@Inject
	Logger logger;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Get all employeeList")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public Response getAllList() {
		logger.info("Get All Employee list.");
		return Response.status(Status.OK).entity(employeeService.getAllEmployeeList()).build();
	}

	@GET
	@Path("/search")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Get all employeeList")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public Response getSearchList(@QueryParam("input") String input) {
		return Response.status(Status.OK).entity(employeeService.checkInputAndSearch(input)).build();
	}

	@GET
	@Path("/{EmployeeId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Find employee by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public Response getEmployeeById(@PathParam("EmployeeId") Integer id) {
		try {
			BasicEmployeeDTO employee = employeeService.findEmployeeById(id);
			logger.info("Find an employee : id {}", id);
			return Response.status(Status.OK).entity(employee).build();
		} catch (EntityNotFoundException exception) {
			logger.error("Find an employee failed : id " + id);
			throw exception;
		}
	}

	@POST
	@Path("/addbasicinfo")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Add new Employee")
	@ApiResponses({ @ApiResponse(code = 201, message = "Success") })
	public Response addEmployee(@Valid BasicEmployeeDTO employee) {
		try {
			logger.info("Add new employee into the system.");
			int createdId = employeeService.addEmployee(employee);
			URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(createdId)).build();
			return Response.created(uri).status(Status.CREATED).build();
		} catch (ValidationException exception) {
			logger.error("Added an employee to the system failed : id ");
			throw exception;
		}
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Add new Employee")
	@ApiResponses({ @ApiResponse(code = 201, message = "Success") })
	public Response addNewEmployee(@Valid EmployeeDTO addEmployee) {
		try {
//			authorizationChecker.checkAuthorizedToken(authorizedToken);
			int createdId = employeeService.newAddEmployee(addEmployee);
			URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(createdId)).build();
			return Response.created(uri).status(Status.CREATED).build();
		} catch (ValidationException exception) {
			throw exception;
		}
	}

	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Update Employee")
	@ApiResponses({ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 404, message = "Not Found") })
	public Response updateEmployee(@HeaderParam("Authorization") String authorizedToken,@Valid BasicEmployeeDTO employee) {
		try {
			logger.info("Update an employee : id " + employee.getEmployeeId());
			authorizationChecker.checkAuthorizedToken(authorizedToken);
			employeeService.updateEmployee(employee);
		} catch (ValidationException exception) {
			throw new ValidationException(exception.getMessage());
		}
		logger.info("Updated employee successfully : id " + employee.getEmployeeId());
		return Response.status(Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{EmployeeId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Delete Employee")
	@ApiResponses({ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 404, message = "Not Found") })
	public Response deleteEmployeebyId(@HeaderParam("Authorization") String authorizedToken,@PathParam("EmployeeId") Integer id) {

		try {
			logger.info("Delete an employee : id " + id);
			authorizationChecker.checkAuthorizedToken(authorizedToken);
			employeeService.deleteEmployeeById(id);
		} catch (ValidationException exception) {
			logger.error("Deleted an employee failed.", exception);
			throw new ValidationException(exception.getMessage());
		}
		logger.info("Deleted an employee successfully : id " + id);
		return Response.status(Status.NO_CONTENT).build();
	}

}
