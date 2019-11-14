package com.axonactive.employeeui.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.Logger;

import com.axonactive.employeeui.controller.LoginController;
import com.axonactive.employeeui.dto.FakeDepartmentDTO;
import com.axonactive.employeeui.dto.TokenDTO;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.exception.UnauthorizedAccessException;
import com.axonactive.employeeui.generic.GenericRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Stateless
public class DepartmentRestClient extends GenericRestClient<FakeDepartmentDTO> {
	FacesContext context = FacesContext.getCurrentInstance();

	ResourceBundle bundle = context.getApplication().getResourceBundle(context, "config");

	private String url = bundle.getString("DEPARTMENT.RESTAPI.URL");

	private static final String ERROR_MESSAGE = "Failed with HTTP error code : ";

	private static final String BEARER = "Bearer ";

	private LoginController loginController;

	private HttpClient httpClient = HttpClientBuilder.create().build();

	private HttpResponse httpResponse;

	private Gson gson = new GsonBuilder().create();

	TokenDTO token = new TokenDTO();

	@Inject
	SecurityRestClient securityRestClient;

	@Inject
	Logger logger;

	public List<FakeDepartmentDTO> getAllDepartmentList() throws IOException {
		HttpGet getRequest = new HttpGet(url);
		StringBuilder jsonData = getDataFromResponse(getRequest);
		Type listType = new TypeToken<List<FakeDepartmentDTO>>() {
		}.getType();
		return gson.fromJson(jsonData.toString(), listType);
	}

	public FakeDepartmentDTO getDepartmentByID(Integer id) throws IOException {
		return getOne(url + id, FakeDepartmentDTO.class);
//		HttpGet getRequest = new HttpGet(url + id);
//		StringBuilder jsonData = getDataFromResponse(getRequest);
//		return gson.fromJson(jsonData.toString(), FakeDepartmentDTO.class);
	}

	public void addNewDepartment(FakeDepartmentDTO departmentDto) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		loginController = (LoginController) sessionMap.get("loginController");
		token = loginController.getTokenDto();
		HttpPost postRequest = new HttpPost(url);
		postRequest.setHeader(HttpHeaders.AUTHORIZATION, BEARER + token.getTokenValue());
		String json = gson.toJson(departmentDto);
		StringEntity employeeEntity = new StringEntity(json);
		employeeEntity.setContentType("application/json");
		postRequest.setEntity(employeeEntity);
		httpResponse = httpClient.execute(postRequest);
		Integer statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != 201) {
			context.getExternalContext().invalidateSession();
			context.getExternalContext().redirect("login.xhtml");
			throw new UnauthorizedAccessException("401", "You are unauthorized");
		}
	}

	private StringBuilder getDataFromResponse(HttpGet getRequest) throws ClientProtocolException, IOException {
		httpResponse = httpClient.execute(getRequest);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new SystemException(ERROR_MESSAGE + statusCode);
		}
		StringBuilder jsonData = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8))) {

			String line = "";
			while ((line = reader.readLine()) != null) {
				jsonData.append(line);
			}
			httpResponse.getEntity().getContent().close();
		}

		return jsonData;
	}
}