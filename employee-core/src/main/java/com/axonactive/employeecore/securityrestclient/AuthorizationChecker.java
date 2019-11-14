package com.axonactive.employeecore.securityrestclient;

import javax.ejb.Stateless;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.axonactive.employeecore.exception.UnauthorizedAccessException;

@Stateless
public class AuthorizationChecker {

	private static final String CODE = "401 (Unauthorized user)";

	private static final String BEARER = "Bearer ";

	public void checkAuthorizedToken(String token) {
		if (token == null) {
			throw new UnauthorizedAccessException(CODE, "Invalid request");
		}
		try {
			String secretKey = "aavn123";
			Algorithm algorithm = Algorithm.HMAC512(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token.substring(BEARER.length()).trim());
		} catch (JWTVerificationException e) {

			throw new UnauthorizedAccessException(CODE, "You are unauthorized");
		} catch (IllegalArgumentException e) {
			throw new UnauthorizedAccessException(CODE, "Token is not well format");
		}
	}

	public void checkAuthorizedAccessToken(String accessToken, String refreshToken) {
		if (accessToken == null) {
			throw new UnauthorizedAccessException(CODE, "Invalid request");
		}
		String secretKey = "aavn123";
		Algorithm algorithm = Algorithm.HMAC512(secretKey);
		JWTVerifier verifier = JWT.require(algorithm).build();
		if(verifier.verify(accessToken.substring(BEARER.length()).trim()) == null)
		{
			throw new UnauthorizedAccessException(CODE, "You are unauthorized");
		}
		try {
			verifier.verify(accessToken.substring(BEARER.length()).trim());
		} catch (JWTVerificationException e) {
            verifier.verify(refreshToken.substring(BEARER.length()).trim());
		} catch (IllegalArgumentException e) {
			throw new UnauthorizedAccessException(CODE, "Token is not well format");
		}
	}

}
