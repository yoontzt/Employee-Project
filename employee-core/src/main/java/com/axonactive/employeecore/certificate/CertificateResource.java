package com.axonactive.employeecore.certificate;

import java.net.URI;

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

import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;

@Stateless
@Path("/employees/{employeeId}/certificates")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateResource {

	@EJB
	CertificateService certificateService;
	@Inject
	Logger logger;
	@Context
	UriInfo uriInfo;
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllCertificateListOfEmployee(@PathParam("employeeId") Integer employeeId) {
		logger.info("Get all certificate list of an employeeId: " + employeeId);
		return Response.status(Status.OK).entity(certificateService.findAllCertificateByEmployeeId(employeeId)).build();
	}
	
	@GET
	@Path("/{certificateID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCertificate(@PathParam("employeeId") Integer employeeId, @PathParam("certificateID") Integer id) {
		try {
			logger.info("Find certificate by id");
			CertificateDTO certificateDTO = certificateService.findACertificateOfEmployee(employeeId, id);
			return Response.status(Status.OK).entity(certificateDTO).build();
		} catch (EntityNotFoundException exception) {
			logger.error("Find certificate by id failed with id: " + id);
			throw new EntityNotFoundException(exception.getMessage());
		}
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response addNewCertificate(@PathParam("employeeId") Integer employeeId,@Valid CertificateDTO certificateDTO) {
		try {
			logger.info("Add certificate into system");
			int createdId = certificateService.addCertificate(employeeId, certificateDTO);
			URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(createdId)).build();
			logger.info("Add certificate into system successfully!");
			return Response.created(uri).status(Status.CREATED).build();
		} catch (ValidationException exception) {
			throw new ValidationException(exception.getMessage());
		}
	}

	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateCertificate(@PathParam("employeeId") Integer employeeId,@Valid CertificateDTO certificateDTO) {
		try {
			logger.info("Update certificate into system");
			certificateService.updateCertificate(certificateDTO, employeeId);
			logger.info("Update certificate into system successfully!");
			return Response.status(Status.NO_CONTENT).build();
		} catch (ValidationException exception) {
			throw new ValidationException(exception.getMessage());
		}
	}

	@DELETE
	@Path("/{certificateID}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response deleteCertificate(@PathParam("employeeId") Integer employeeId,
			@PathParam("certificateID") Integer certificateID) {
		try {
			logger.info("Delete certificate!");
			certificateService.deleteCertificate(employeeId, certificateID);
			logger.info("Delete certificate successfully!");
			return Response.status(Status.NO_CONTENT).build();
		} catch (ValidationException exception) {
			logger.error("Delete certificate failed: " + exception);
			throw new ValidationException(exception.getMessage());
		}
	}
}
