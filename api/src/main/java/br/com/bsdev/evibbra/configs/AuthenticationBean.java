package br.com.bsdev.evibbra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.bsdev.evibbra.repositories.UserRepository;

/**
 * Configuration class for setting up authentication-related beans.
 */
@Configuration
public class AuthenticationBean {

	// Repository to interact with user data
	private final UserRepository userRepository;

	// Constructor to inject UserRepository dependency
	public AuthenticationBean(UserRepository userRepository) {
		this.userRepository = userRepository; // Initialize userRepository
	}

	/**
	 * Configures a password encoder using BCrypt.
	 * 
	 * @return A PasswordEncoder that uses the BCrypt hashing algorithm.
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		// BCrypt is a strong, adaptive algorithm for password hashing.
		return new BCryptPasswordEncoder();
	}

	/**
	 * Provides an AuthenticationManager bean to manage authentication processes.
	 *
	 * @param config The AuthenticationConfiguration to retrieve the
	 *               AuthenticationManager.
	 * @return The configured AuthenticationManager.
	 * @throws Exception if an error occurs while retrieving the
	 *                   AuthenticationManager.
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // Get the AuthenticationManager from the config
	}

	/**
	 * Provides a UserDetailsService that loads user-specific data.
	 *
	 * @return A UserDetailsService that retrieves users from the UserRepository.
	 */
	@Bean
	UserDetailsService userDetailsService() {
		return username -> userRepository.findByEmail(username) // Find user by email
				.orElseThrow(() -> new UsernameNotFoundException("User not found")); // Throw exception if not found
	}

	/**
	 * Configures an AuthenticationProvider that uses DaoAuthenticationProvider.
	 *
	 * @return The configured AuthenticationProvider for authentication.
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService()); // Set the UserDetailsService
		authProvider.setPasswordEncoder(passwordEncoder()); // Set the PasswordEncoder

		return authProvider; // Return the configured provider
	}

}