package br.com.bsdev.evibbra.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.bsdev.evibbra.entities.User;

/**
 * Service responsible for managing JWT (JSON Web Token) generation and
 * validation. This service generates tokens based on user details and validates
 * tokens for authentication purposes.
 */
@Component
public class TokenService {

	// Name of the application, used as the issuer in the JWT
	private final String applicationName;

	// Secret key used for signing and verifying the JWT
	private final String secret;

	/**
	 * Constructor to initialize application name and secret key from properties.
	 * 
	 * @param applicationName the name of the application, used as the issuer of the
	 *                        JWT.
	 * @param secret          the secret key used to sign and verify the JWT.
	 */
	public TokenService(@Value("${spring.application.name}") String applicationName,
			@Value("${api.security.token.secret}") String secret) {
		this.applicationName = applicationName;
		this.secret = secret;
	}

	// Token expiration time in milliseconds (30 minutes)
	public static final int TOKEN_EXPIRACAO = 1800_000;

	/**
	 * Generates a JWT token for the provided user.
	 * 
	 * @param user the user for whom the token is generated.
	 * @return a signed JWT token as a String.
	 */
	public String gerarToken(User user) {
		var algoritmo = Algorithm.HMAC256(secret);
		return JWT.create().withIssuer(applicationName) // Set the issuer as the application name
				.withSubject(user.getEmail()) // Set the subject as the user's email
				.withExpiresAt(generateExpiresAt()) // Set the token expiration time
				.withClaim("name", user.getName()) // Set the claim as the user's name
				.sign(algoritmo); // Sign the token with the HMAC256 algorithm and secret
	}

	/**
	 * Extracts the subject (usually the user's identifier) from the JWT.
	 * 
	 * @param tokenJWT the JWT token as a String.
	 * @return the subject (user email) of the token.
	 * @throws JWTVerificationException if the token is invalid.
	 */
	public String getSubject(String tokenJWT) throws JWTVerificationException {
		return verifyToken(tokenJWT).getSubject();
	}

	/**
	 * Validates if the token is valid for the given user details.
	 * 
	 * @param token       the JWT token to validate.
	 * @param userDetails the user details to match with the token.
	 * @return true if the token is valid and not expired, false otherwise.
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getSubject(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	/**
	 * Checks if the token has expired.
	 * 
	 * @param token the JWT token to check.
	 * @return true if the token is expired, false otherwise.
	 */
	protected boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Extracts the expiration date from the JWT token.
	 * 
	 * @param token the JWT token as a String.
	 * @return the expiration date of the token.
	 */
	private Date extractExpiration(String token) {
		return verifyToken(token).getExpiresAt();
	}

	/**
	 * Verifies the JWT token using the secret key and application name as issuer.
	 * 
	 * @param tokenJWT the JWT token to verify.
	 * @return the decoded JWT token if valid.
	 * @throws JWTVerificationException if the token is invalid.
	 */
	protected DecodedJWT verifyToken(String tokenJWT) throws JWTVerificationException {
		var algoritmo = Algorithm.HMAC256(secret);
		return JWT.require(algoritmo).withIssuer(applicationName) // Verify the issuer as the application name
				.build().verify(tokenJWT); // Verify the token
	}

	/**
	 * Generates the expiration date for the token based on the current time and
	 * TOKEN_EXPIRACAO.
	 * 
	 * @return an Instant object representing the token's expiration time.
	 */
	public Instant generateExpiresAt() {
		return new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO).toInstant();
	}

}