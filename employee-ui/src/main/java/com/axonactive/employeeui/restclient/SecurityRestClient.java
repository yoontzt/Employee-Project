package com.axonactive.employeeui.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.axonactive.employeeui.controller.LoginController;
import com.axonactive.employeeui.dto.TokenDTO;
import com.axonactive.employeeui.exception.UnauthorizedAccessException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Stateless
public class SecurityRestClient {

	private static final String SECRET_KEY = "aavn123";

	private static final String CODE = "401 (Unlauthorized user)";

	private static final String BEARER = "Bearer ";

	private LoginController loginController;

	public String readURL() throws IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("restapi.properties");
		Properties properties = new Properties();
		properties.load(stream);
		return properties.getProperty("SECURITY.RESTAPI.URL");
	}

	public TokenDTO getToken(String username, String passwd) throws IOException {
		TokenDTO tokenDTO = new TokenDTO();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(readURL());
		Gson gson = new GsonBuilder().create();
		StringEntity userHttpEntity = new StringEntity(username+" "+passwd);
		userHttpEntity.setContentType("application/json");
		postRequest.setEntity(userHttpEntity);
		HttpResponse response = httpClient.execute(postRequest);
		Integer statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			StringBuilder jsonData = new StringBuilder();
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				String line = "";
				while ((line = rd.readLine()) != null) {
					jsonData.append(line);
				}
				response.getEntity().getContent().close();
			}
			tokenDTO = gson.fromJson(jsonData.toString(), TokenDTO.class);
		} else {
			throw new UnauthorizedAccessException(statusCode.toString(), "Invalid username and password");
		}
		System.out.println("Token after login "+tokenDTO.getTokenValue());
		return tokenDTO;
	}

	public String checkAccessToken() throws IOException {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		loginController = (LoginController) sessionMap.get("loginController");
		String accessToken = loginController.getTokenDto().getTokenValue();
		String refreshToken = loginController.getTokenDto().getRefreshToken();

		if (accessToken == null) {
			throw new UnauthorizedAccessException(CODE, "Invalid request");
		}
		try {
			String secretKey = SECRET_KEY;
			Algorithm algorithm = Algorithm.HMAC512(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).build();
			String tokenString = accessToken.trim().startsWith(BEARER) ? accessToken.trim().substring(BEARER.length())
					: accessToken.trim();
			verifier.verify(tokenString);
		} catch (JWTVerificationException e) {
			loginController.setTokenDto(generateAccessToken(refreshToken, loginController.getUsername()));
			accessToken = loginController.getTokenDto().getTokenValue();
			return loginController.getTokenDto().getTokenValue();
		} catch (IllegalArgumentException e) {
			throw new UnauthorizedAccessException(CODE, "Token is not well format");
		}
		System.out.println("New token after expired "+accessToken);
		return accessToken;
	}

	public TokenDTO generateAccessToken(String refreshToken, String user) throws IOException {
		TokenDTO tokenDTO = new TokenDTO();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(readURL() + "/refresh");
		postRequest.setHeader("refresh_token", refreshToken);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(user);
		StringEntity userHttpEntity = new StringEntity(json);
		userHttpEntity.setContentType("application/json");
		postRequest.setEntity(userHttpEntity);
		HttpResponse response = httpClient.execute(postRequest);
		Integer statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			StringBuilder jsonData = new StringBuilder();
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				String line = "";
				while ((line = rd.readLine()) != null) {
					jsonData.append(line);
				}
				response.getEntity().getContent().close();
			}
			tokenDTO = gson.fromJson(jsonData.toString(), TokenDTO.class);
		}

		else if (statusCode == 401) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().invalidateSession();
			context.getExternalContext().redirect("login.xhtml");
		}
		return tokenDTO;
	}
}
