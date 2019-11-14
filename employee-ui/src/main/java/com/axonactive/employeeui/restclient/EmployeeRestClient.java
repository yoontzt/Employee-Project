package com.axonactive.employeeui.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import com.axonactive.employeeui.dto.FakeAddEmployeeDTO;
import com.axonactive.employeeui.dto.FakeEmployeeDTO;
import com.axonactive.employeeui.exception.ErrorMessage;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.generic.GenericRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;

@Stateless
public class EmployeeRestClient extends GenericRestClient<FakeEmployeeDTO> {

	public static final String ERROR_MESSAGE = "Failed with HTTP error code : ";

	private static final String BEARER = "Bearer ";

	private @Getter String[] duplicateMessages;

	private HttpResponse httpResponse;

	HttpClient httpClient = HttpClientBuilder.create().build();

	Gson gson = new GsonBuilder().setDateFormat("dd-MMM-yyyy").setPrettyPrinting().create();

	@EJB
	SecurityRestClient securityRestClient;

	@Inject
	Logger logger;

	FacesContext context = FacesContext.getCurrentInstance();

	ResourceBundle bundle = context.getApplication().getResourceBundle(context, "config");

	private String url = bundle.getString("EMPLOYEE.RESTAPI.URL");

	public String readURL() throws IOException {
		InputStream stream = EmployeeRestClient.class.getClassLoader().getResourceAsStream("restapi.properties");
		Properties properties = new Properties();
		properties.load(stream);
		return properties.getProperty("EMPLOYEE.RESTAPI.URL");
	}

	public List<FakeEmployeeDTO> getAllEmployeeList() throws IOException {
		HttpGet getRequest = new HttpGet(readURL());
		StringBuilder jsonData = getDataFromResponse(getRequest);
		Type listType = new TypeToken<List<FakeEmployeeDTO>>() {
		}.getType();
		return gson.fromJson(jsonData.toString(), listType);
	}

	public FakeEmployeeDTO getEmployeeByID(Integer id) throws IOException {
		HttpGet getRequest = new HttpGet(readURL() + id);
		StringBuilder jsonData = getDataFromResponse(getRequest);
		return gson.fromJson(jsonData.toString(), FakeEmployeeDTO.class);
	}

	public List<FakeEmployeeDTO> searchEmployee(String input) throws IOException {
		HttpGet getRequest = new HttpGet(readURL() + "search?input=" + encodeValue(input));
		Type listType = new TypeToken<List<FakeEmployeeDTO>>() {

		}.getType();
		return gson.fromJson(getDataFromResponse(getRequest).toString(), listType);
	}

	public void addNewEmployee(FakeAddEmployeeDTO employee) throws IOException {
		HttpPost postRequest = new HttpPost(readURL());
		StringEntity httpemployeeEntity = gsonToStringEntity(employee);
		httpemployeeEntity.setContentType("application/json");
		postRequest.setEntity(httpemployeeEntity);
		httpResponse = httpClient.execute(postRequest);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == 400) {
			String duplicateErrorMessage = getDuplicateErrorMessage(httpResponse);
			throw new SystemException(duplicateErrorMessage);
		}
		if (statusCode != 201) {
			throw new SystemException(ERROR_MESSAGE + statusCode);
		}
	}

	public void updateEmployee(FakeEmployeeDTO employee) throws IOException {
		String accessToken = securityRestClient.checkAccessToken();
		HttpPut putRequest = new HttpPut(readURL());
		putRequest.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
		StringEntity httpemployeeEntity = gsonToStringEntity(employee);
		httpemployeeEntity.setContentType("application/json");
		putRequest.setEntity(httpemployeeEntity);
		httpResponse = httpClient.execute(putRequest);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != 204) {
			throw new SystemException(ERROR_MESSAGE + statusCode);
		}
	}

	public void deleteEmployee(Integer id) throws IOException {
		String accessToken = securityRestClient.checkAccessToken();
		HttpDelete deleteRequest = new HttpDelete(readURL() + id);
		deleteRequest.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
		ResponseHandler<String> responseHandler = response -> {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 204) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new SystemException("Unexpected response status: " + statusCode);
			}
		};
		httpClient.execute(deleteRequest, responseHandler);
	}

	private StringEntity gsonToStringEntity(FakeAddEmployeeDTO employee) {
		String json = gson.toJson(employee);
		return new StringEntity(json, StandardCharsets.UTF_8);
	}

	private StringEntity gsonToStringEntity(FakeEmployeeDTO employee) {
		String json = gson.toJson(employee);
		return new StringEntity(json, StandardCharsets.UTF_8);
	}

	public void splitMessage(String input) {
		String[] result = input.trim().split(Pattern.quote("."));
		duplicateMessages = result.clone();
	}

	private StringBuilder getDataFromResponse(HttpGet getRequest) throws IOException {
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

	private String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}

	private String getDuplicateErrorMessage(HttpResponse response) throws IOException {
		StringBuilder jsonData = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {

			String line = "";
			while ((line = reader.readLine()) != null) {
				jsonData.append(line);
			}
			response.getEntity().getContent().close();
		}
		ErrorMessage errorMessage = gson.fromJson(jsonData.toString(), ErrorMessage.class);
		splitMessage(errorMessage.getErrorMessages());
		return errorMessage.getErrorMessages();
	}
}