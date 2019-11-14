package com.axonactive.employeeui.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.http.HttpEntity;
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

import com.axonactive.employeeui.dto.FakeCertificateDTO;
import com.axonactive.employeeui.exception.ErrorMessage;
import com.axonactive.employeeui.exception.SystemException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Stateless
public class CertificateRestClient {

	private static final String APPLICATION_JSON = "application/json";

	private static final String CERTIFICATES = "/certificates/";

	private FacesContext context = FacesContext.getCurrentInstance();

	private ResourceBundle bundle = context.getApplication().getResourceBundle(context, "config");

	private static final String ERROR_MESSAGE = "Failed with HTTP error code : ";

	private String url = bundle.getString("CERTIFICATE.RESTAPI.URL");

	private HttpClient client = HttpClientBuilder.create().build();

	private HttpResponse httpResponse;

	private Gson gson = new Gson();

	@Inject
	Logger logger;

	public List<FakeCertificateDTO> getCertificateListOfOneEmployee(Integer empId) throws IOException {
		HttpGet getRequest = new HttpGet(url + empId + CERTIFICATES);
		httpResponse = client.execute(getRequest);

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
		Type listType = new TypeToken<List<FakeCertificateDTO>>() {
		}.getType();
		return gson.fromJson(jsonData.toString(), listType);
	}

	public FakeCertificateDTO getOneCertificatesOfOneEmployee(Integer empId, Integer certificateId) throws IOException {
		HttpGet getRequest = new HttpGet(url + empId + CERTIFICATES + certificateId);
		httpResponse = client.execute(getRequest);

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
		return gson.fromJson(jsonData.toString(), FakeCertificateDTO.class);
	}

	public void addNewCertificate(Integer empId, FakeCertificateDTO certificate) throws IOException {
		HttpPost postRequest = new HttpPost(url + empId + CERTIFICATES);

		StringEntity certificateEntity = new StringEntity(gson.toJson(certificate), StandardCharsets.UTF_8);
		certificateEntity.setContentType(APPLICATION_JSON);
		postRequest.setEntity(certificateEntity);
		httpResponse = client.execute(postRequest);

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != 201) {
			String duplicateErrorMessage = getDuplicateErrorMessage(httpResponse);
			throw new SystemException(duplicateErrorMessage);
		}
	}

	public void updateCertificate(Integer empId, FakeCertificateDTO certificate) throws IOException {
		HttpPut putRequest = new HttpPut(url + empId + CERTIFICATES);

		StringEntity certificateEntity = new StringEntity(gson.toJson(certificate), StandardCharsets.UTF_8);
		certificateEntity.setContentType(APPLICATION_JSON);
		putRequest.setEntity(certificateEntity);
		httpResponse = client.execute(putRequest);

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != 204) {
			throw new SystemException(ERROR_MESSAGE + statusCode);
		}
	}

	public void deleteCertificate(Integer empId, Integer certificateId) throws IOException {
		HttpDelete deleteRequest = new HttpDelete(url + empId + CERTIFICATES + certificateId);

		ResponseHandler<String> responseHandler = response -> {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 204) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new SystemException("Unexpected response status: " + statusCode);
			}
		};
		client.execute(deleteRequest, responseHandler);
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
		return errorMessage.getErrorMessages();
	}
}
