package br.com.bsdev.evibbra.configs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.bsdev.evibbra.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Security filter that processes each incoming HTTP request once and checks for
 * a valid JWT token. The filter is responsible for extracting the JWT from the
 * request, validating it, and setting the authentication context for the
 * request if the token is valid.
 */
@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

	// The header where the JWT is expected to be found
	private static final String AUTHORIZATION_HEADER = "Authorization";

	// The expected prefix in the Authorization header
	private static final String BEARER_PREFIX = "Bearer ";

	private final TokenService tokenService;
	private final UserDetailsService userDetailsService;

	/**
	 * Constructs a SecurityFilter with the provided TokenService and
	 * UserDetailsService.
	 *
	 * @param tokenService       the service responsible for JWT creation and
	 *                           validation.
	 * @param userDetailsService the service to load user details based on the JWT
	 *                           subject.
	 */
	public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
		this.tokenService = tokenService;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Filters each incoming HTTP request by extracting and validating the JWT
	 * token, then setting the authentication context if the token is valid.
	 * 
	 * @param request     the HTTP request object.
	 * @param response    the HTTP response object.
	 * @param filterChain the filter chain to pass the request along to the next
	 *                    filter.
	 * 
	 * @throws ServletException if an exception occurs during filtering.
	 * @throws IOException      if an input or output error occurs.
	 */
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		try {
			// Extracts the JWT token from the request if it exists
			var tokenJWT = getBearerToken(request);

			// Retrieves the current authentication from the security context
			var authentication = SecurityContextHolder.getContext().getAuthentication();

			// If there is no authentication and the JWT is present, validate the token
			if (tokenJWT != null && authentication == null) {
				// Extracts the subject (user) from the token
				String subject = tokenService.getSubject(tokenJWT);

				// Loads user details based on the subject (username)
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject);

				// Creates an authentication token based on the user's authorities
				var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

				// Sets additional authentication details from the request (such as IP address,
				// session ID)
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Sets the authenticated user in the security context
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		} catch (JWTVerificationException e) {
			// If the token is invalid, respond with HTTP 403 Forbidden and the error
			// message in JSON format
			response.setStatus(HttpStatus.FORBIDDEN.value());
			PrintWriter out = response.getWriter();
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			new ObjectMapper().writeValue(out, errorResponse);
			out.flush();

			// Log the error message for debugging purposes
			log.error("JWT verification failed: {}", e.getMessage());
			return; // Exit filter chain, stop further processing
		}

		// Proceed to the next filter in the chain if no exception was thrown
		filterChain.doFilter(request, response);
	}

	/**
	 * Retrieves the Bearer token from the Authorization header in the HTTP request.
	 * 
	 * @param request the HttpServletRequest containing the headers of the request.
	 * @return the Bearer token string without the "Bearer " prefix, or null if the
	 *         header is not present or does not start with "Bearer ".
	 */
	String getBearerToken(HttpServletRequest request) {
		// Retrieve the Authorization header from the request
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

		// Check if the header is not null and starts with the "Bearer " prefix
		if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
			// Return the token by removing the "Bearer " prefix
			return authorizationHeader.substring(BEARER_PREFIX.length());
		}

		// Return null if no Bearer token is found
		return null;
	}
}