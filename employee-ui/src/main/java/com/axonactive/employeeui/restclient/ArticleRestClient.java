
package com.axonactive.employeeui.restclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.UploadedFile;

@Stateless
public class ArticleRestClient {

	@Inject
	Logger logger;

	public String saveFileToBackEnd(UploadedFile inFile, String employeeId) throws IOException {
		String responseString ="";
		HttpClient httpclient = HttpClientBuilder.create().build();
		String url = readURL() + "/upload";
		HttpPost httppost = new HttpPost(url);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create()
				.addPart("uploadedFile", new InputStreamBody(inFile.getInputstream(), inFile.getFileName()))
				.addPart("employeeId", new StringBody(employeeId, ContentType.TEXT_PLAIN));

		HttpEntity entity = builder.build();
		httppost.setEntity(entity);

		// execute the request
		HttpResponse response = httpclient.execute(httppost);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			HttpEntity responseEntity = response.getEntity();
			responseString = EntityUtils.toString(responseEntity, "UTF-8");
			System.out.println(responseString);
			logger.info("Save File to BackEnd success with " + statusCode + responseString);
		}
		return responseString;
	}

	public String readURL() throws IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("restapi.properties");
		Properties properties = new Properties();
		properties.load(stream);
		return properties.getProperty("ARTICLE.RESTAPI.URL");
	}
}
