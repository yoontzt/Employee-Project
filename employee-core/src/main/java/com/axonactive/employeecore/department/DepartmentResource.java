package com.axonactive.employeecore.department;

import java.net.URI;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.securityrestclient.AuthorizationChecker;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Stateless
@Path("/departments")
@SwaggerDefinition(schemes = { SwaggerDefinition.Scheme.HTTP,
		SwaggerDefinition.Scheme.HTTPS }, info = @Info(title = "Department Management", description = "This API will be used in Employee Project by Amigo", version = "1.0.0"))
@Api(tags = "tags")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResource {

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
	@ApiOperation(value = "Get all Department")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public Response getAllDepartmentList() {
		logger.info("Get All Department list.");
		return Response.status(Status.OK).entity(departmentService.getAllDepartmentList()).build();
	}

	@GET
	@Path("/{DepartmentId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Find department by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success") })
	public Response getDepartmentById(@PathParam("DepartmentId") Integer id) {
		try {
			DepartmentDTO department = departmentService.findDepartmentDTOById(id);
			logger.info("Find a department : id " + id);
			return Response.status(Status.OK).entity(department).build();
		} catch (EntityNotFoundException exception) {
			logger.error("Find a department failed : id " + id, exception);
			throw exception;
		}
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Add new Department")
	@ApiResponses({ @ApiResponse(code = 201, message = "Success") })
	public Response addDepartment(@HeaderParam("Authorization") String authorizedToken,DepartmentDTO departmentDto) {
		authorizationChecker.checkAuthorizedToken(authorizedToken);
		int deptId = departmentService.addDepartment(departmentDto);
		URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(deptId)).build();
		return Response.created(uri).status(Status.CREATED).build();

	}

	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Update Department")
	@ApiResponses({ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 404, message = "Not Found") })
	public Response updateDepartment(DepartmentDTO departmentDto) {
		departmentService.updateDepartment(departmentDto);
		return Response.status(Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{depId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Delete Employee")
	@ApiResponses({ @ApiResponse(code = 204, message = "Success"), @ApiResponse(code = 404, message = "Not Found") })
	public Response deleteDepartment(@HeaderParam("Authorization") String authorizedToken,
			@PathParam("depId") Integer id) {
		authorizationChecker.checkAuthorizedToken(authorizedToken);
		departmentService.deleteDepartment(id);
		return Response.status(Status.NO_CONTENT).build();
	}

}
