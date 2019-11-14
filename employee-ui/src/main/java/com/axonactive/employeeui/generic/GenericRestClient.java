package com.axonactive.employeeui.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.axonactive.employeeui.exception.SystemException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class GenericRestClient<D> {
	
	private HttpClient httpClient = HttpClientBuilder.create().build();

	
	Gson gson = new GsonBuilder().setDateFormat("dd-MMM-yyyy").setPrettyPrinting().create();

	
	public D  getOne(String url ,Class<D> d) throws ClientProtocolException, IOException{
		HttpResponse httpResponse = getResponseFromGet(url);
		return gson.fromJson(getJsonDataFromResponse(httpResponse),d);
		
	}

	private String getJsonDataFromResponse(HttpResponse httpResponse) throws IOException {
		StringBuilder json = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8))) {

			String line = "";
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			httpResponse.getEntity().getContent().close();
		}
		return json.toString();
	}

	private HttpResponse getResponseFromGet(String url) throws IOException, ClientProtocolException {
		HttpGet getRequest = new HttpGet(url);
		HttpResponse response = httpClient.execute(getRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new SystemException("error" + statusCode);
		}
		return response;
	}
	
	
	
	

}
