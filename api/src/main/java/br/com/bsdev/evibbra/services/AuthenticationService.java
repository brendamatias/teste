package br.com.bsdev.evibbra.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.dtos.UserLoginDto;
import br.com.bsdev.evibbra.dtos.UserRegisterDto;
import br.com.bsdev.evibbra.entities.User;
import br.com.bsdev.evibbra.repositories.UserRepository;

@Service
public class AuthenticationService {

	// Repository for managing user data in the database
	private final UserRepository userRepository;

	// Encoder for encoding user passwords
	private final PasswordEncoder passwordEncoder;

	// Manager for handling authentication processes
	private final AuthenticationManager authenticationManager;

	// Constructor to inject dependencies
	public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository; // Initialize user repository
		this.passwordEncoder = passwordEncoder; // Initialize password encoder
		this.authenticationManager = authenticationManager; // Initialize authentication manager
	}

	/**
	 * Authenticates a user with the provided login credentials.
	 *
	 * @param input The UserLoginDto containing email and password.
	 * @return The authenticated User object.
	 * @throws Exception if authentication fails.
	 */
	public User authenticate(UserLoginDto input) {
		// Authenticate the user by creating a UsernamePasswordAuthenticationToken
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		// Retrieve the user from the repository based on the email, or throw an
		// exception if not found
		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}

	/**
	 * Registers a new user by saving their details to the database.
	 *
	 * @param input The UserRegisterDto containing user details.
	 * @return The newly registered User object.
	 */
	public User signup(UserRegisterDto input) {
		// Encode the user's password before saving
		var password = passwordEncoder.encode(input.getPassword());

		// Create a new User object with the provided details
		var user = new User(input.getName(), input.getEmail(), password, input.getPhoneNumber());

		// Save the user to the repository and return the saved User object
		return userRepository.save(user);
	}

}