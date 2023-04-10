package br.com.bsdev.evibbra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.bsdev.evibbra.controllers.response.AuthenticationResponse;
import br.com.bsdev.evibbra.dtos.UserLoginDto;
import br.com.bsdev.evibbra.dtos.UserRegisterDto;
import br.com.bsdev.evibbra.entities.User;
import br.com.bsdev.evibbra.services.AuthenticationService;
import br.com.bsdev.evibbra.services.TokenService;

// Extends BaseController to inherit the /api/v1 path
@RestController
public class AuthenticationController implements BaseController {

	// Service for handling JWT token operations
	private final TokenService jwtService;

	// Service for handling authentication processes
	private final AuthenticationService authenticationService;

	// Constructor to inject dependencies
	public AuthenticationController(TokenService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService; // Initialize JWT service
		this.authenticationService = authenticationService; // Initialize authentication service
	}

	/**
	 * Authenticates a user and returns a JWT token if successful.
	 *
	 * @param loginUserDto The UserLoginDto containing user credentials (email and
	 *                     password).
	 * @return ResponseEntity containing the authentication response with JWT token
	 *         and expiration time.
	 */
	@PostMapping("auth/login")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserLoginDto loginUserDto) {
		try {
			User authenticatedUser = authenticationService.authenticate(loginUserDto);
			String jwtToken = jwtService.gerarToken(authenticatedUser);
			var loginResponse = new AuthenticationResponse(jwtToken, jwtService.generateExpiresAt().toEpochMilli());
			return ResponseEntity.ok(loginResponse);
		} catch (Exception e) {
			// Log the exception (consider using a logger)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new AuthenticationResponse("Invalid credentials", 0L));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody UserRegisterDto registerUserDto) {
		var registeredUser = authenticationService.signup(registerUserDto);
		return ResponseEntity.ok(registeredUser);
	}
}