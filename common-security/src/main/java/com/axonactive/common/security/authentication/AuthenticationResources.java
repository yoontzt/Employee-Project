package com.axonactive.common.security.authentication;

import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("auth")
public class AuthenticationResources {

	@EJB
	AuthenticationServices jwtAuthServices;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Token getAuthenticationToken(String usernameAndPwd) throws NoSuchAlgorithmException {
		Token token = jwtAuthServices.createToken(usernameAndPwd);
		return token;
	}

	@POST
	@Path("/encrypt")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Token getAuthenticationTokenWithEncryption(@Valid User user) throws NoSuchAlgorithmException {
		Token token = jwtAuthServices.createEncryptToken(user);
		return token;
	}
	
	@POST
	@Path("/decode")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void decodeToken(String token) {
		 jwtAuthServices.checkAuthorizedToken(token);
	}
	
	@POST
	@Path("/refresh")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Token getAccessTokenViaRefreshToken(@HeaderParam("refresh_token") String refreshToken,String user) throws NoSuchAlgorithmException {
		Token token = jwtAuthServices.generateAccessToken(refreshToken, user);
		return token;
	}
}
