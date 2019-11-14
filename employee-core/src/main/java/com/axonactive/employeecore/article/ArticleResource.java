package com.axonactive.employeecore.article;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.axonactive.employeecore.exception.EntityNotFoundException;

@Stateless
@Path("/employees/articles")
public class ArticleResource {

	@EJB
	ArticleService articleService;

	@Inject
	Logger logger;

	@POST
	@Path("/upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response uploadFile(MultipartFormDataInput input){
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		String fileLocation;
		try {
			fileLocation = articleService.saveUploadedFileInServer(uploadForm);
			return Response.status(200).entity(" .File Location is " + fileLocation).build();
		} catch (IOException e) {
			throw new EntityNotFoundException("Error while saving file in server");
		}

	}
	
}
