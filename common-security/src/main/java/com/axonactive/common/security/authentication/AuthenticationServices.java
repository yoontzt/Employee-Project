package com.axonactive.common.security.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.axonactive.common.security.exception.UnauthorizedAccessException;

import lombok.Getter;
import lombok.Setter;

@Stateless
public class AuthenticationServices {
	private static final String SECRET_KEY = "aavn123";

	private static final String USERNAME = "username";

	private static final String CODE = "401 (Unauthorized user)";

	private static final String BEARER = "Bearer ";

	private @Getter @Setter String displayName;

	@PersistenceContext(name = "common-securty")
	EntityManager em;
	
	public Token createToken(String usernameAndPwd) throws NoSuchAlgorithmException {
		User user = splitUsernameAndPwd(usernameAndPwd);
		this.isValidUser(user);
		String accessToken = null;
		String refreshToken = null;
		String secretKey = SECRET_KEY;
		String issuer = "issuerlegion";
		int timeToLive = 2;
		int timeToLiveRefresh = 5;
		Algorithm algorithm = Algorithm.HMAC512(secretKey);
		accessToken = JWT.create()
				.withIssuer(issuer)
				.withClaim(USERNAME, user.getUsername())
				.withExpiresAt(this.setTokenTimeToLive(timeToLive))
				.sign(algorithm);
		refreshToken = JWT.create()
				.withIssuer(issuer)
				.withClaim(USERNAME, user.getUsername())
				.withExpiresAt(this.setTokenTimeToLive(timeToLiveRefresh))
				.sign(algorithm);
		return new Token(accessToken, timeToLive, "", refreshToken ,displayName);
	}
	
	private User splitUsernameAndPwd(String usernameAndPwd) {
		String[] userString = usernameAndPwd.split(" ");
		User user = new User();
		user.setUsername(userString[0]);
		user.setPassword(userString[1]);
		return user;
	}

	public Token generateAccessToken(String refreshToken,String user) throws NoSuchAlgorithmException {
		String tokenString = refreshToken.trim().startsWith(BEARER) ? refreshToken.trim().substring(BEARER.length())
				: refreshToken.trim();
		checkAuthorizedToken(tokenString);
		String accessToken = null;
		String secretKey = SECRET_KEY;
		String issuer = "issuerlegion";
		int timeToLive = 1;
		Algorithm algorithm = Algorithm.HMAC512(secretKey);
		accessToken = JWT.create()
				.withIssuer(issuer)
				.withClaim(USERNAME, user)
				.withExpiresAt(this.setTokenTimeToLive(timeToLive))
				.sign(algorithm);
		return new Token(accessToken, timeToLive, "", tokenString ,displayName);
	}

	public void isValidUser(User user) throws NoSuchAlgorithmException {
		if (!isCorrectUser(user)) {
			throw new UnauthorizedAccessException(CODE, "Invalid user");
		}
	}
	
	public void isValidEncryptedUser(User user) throws NoSuchAlgorithmException {
		if ((!isCorrectUserWithEncrypt(user))) {
			throw new UnauthorizedAccessException(CODE, "Invalid user");
		}
	}

	public Token createEncryptToken(User user) throws NoSuchAlgorithmException {
		this.isValidEncryptedUser(user);
		String token = null;
		String refreshToken = null;
		String secretKey = SECRET_KEY;
		String issuer = "issuerlegion";
		int timeToLive = 2;
		Algorithm algorithm = Algorithm.HMAC512(secretKey);
		token = JWT.create().withIssuer(issuer).withClaim(USERNAME, user.getUsername())
				.withExpiresAt(this.setTokenTimeToLive(timeToLive)).sign(algorithm);
		return new Token(token, timeToLive, "",refreshToken,displayName);
	}

	/**
	 * 
	 * @param Verify if a Token is valid or not
	 */

	public void checkAuthorizedToken(String token) {
		if (token == null) {
			throw new UnauthorizedAccessException(CODE, "Invalid request");
		}
		try {
			String secretKey = SECRET_KEY;
			Algorithm algorithm = Algorithm.HMAC512(secretKey);
			JWTVerifier verifier = JWT.require(algorithm).build();
			String tokenString = token.trim().startsWith(BEARER) ? token.trim().substring(BEARER.length())
					: token.trim();
			verifier.verify(tokenString);
		} catch (JWTVerificationException e) {
			throw new UnauthorizedAccessException(CODE, "You are unauthorized");
		} catch (IllegalArgumentException e) {
			throw new UnauthorizedAccessException(CODE, "Token is not well format");
		}
	}

	/**
	 * 
	 * @return this method just to provide a User. you should replace this one by
	 *         get Users from database
	 * @throws NoSuchAlgorithmException
	 */

	private boolean isCorrectUser(User user) throws NoSuchAlgorithmException {
		System.out.println("correct User" + user.getUsername() + "   password "+user.getPassword());
		TypedQuery<User> query = em.createNamedQuery(User.CHECK, User.class);
		MessageDigest digest = MessageDigest.getInstance("sha1");
		String hashedPassword = bytesToHex(digest.digest(user.getPassword().getBytes()));
		List<User> resultList = query.setParameter(USERNAME, user.getUsername())
				.setParameter("password", hashedPassword).getResultList();
		displayName = resultList.get(0).getDisplayName();
		return resultList.stream().findFirst().isPresent();
	}

	private boolean isCorrectUserWithEncrypt(User user) throws NoSuchAlgorithmException {
		TypedQuery<User> query = em.createNamedQuery(User.CHECK, User.class);
		List<User> resultList = query.setParameter(USERNAME, user.getUsername())
				.setParameter("password", user.getPassword()).getResultList();
		return resultList.stream().findFirst().isPresent();
	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private Date setTokenTimeToLive(int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
}
