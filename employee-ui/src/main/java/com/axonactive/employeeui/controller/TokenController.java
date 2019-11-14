package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.axonactive.employeeui.dto.TokenDTO;
import com.axonactive.employeeui.exception.UnauthorizedAccessException;

public class TokenController {

	private LoginController loginController;

	private static final String CODE = "401 (Unauthorized user)";

	private static final String BEARER = "Bearer ";

	TokenDTO token = new TokenDTO();

	public String getToken() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		loginController = (LoginController) sessionMap.get("loginController");
		token = loginController.getTokenDto();
		return token.getTokenValue();
	}

	public void checkAuthorizedToken(String token) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (token == null) {
			throw new UnauthorizedAccessException(CODE, "Invalid request");
		}
		try {
			String secretKey = "aavn123";
			Algorithm algorithm = Algorithm.HMAC512(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).build();
			String tokenString = token.trim().startsWith(BEARER) ? token.trim().substring(BEARER.length())
					: token.trim();
			verifier.verify(tokenString);
		} catch (JWTVerificationException e) {
			context.getExternalContext().invalidateSession();
			context.getExternalContext().redirect("login.xhtml");
			throw new UnauthorizedAccessException(CODE, "You are unauthorized");
		} catch (IllegalArgumentException e) {
			throw new UnauthorizedAccessException(CODE, "Token is not well format");
		}
	}
}
