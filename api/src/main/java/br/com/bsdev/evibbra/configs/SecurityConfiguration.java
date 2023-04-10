package br.com.bsdev.evibbra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	/**
	 * Configures the security filter chain for the application.
	 * 
	 * @param http           Configures the HTTP security policies.
	 * @param securityFilter Custom JWT-based authentication filter.
	 * @param corsFilter     CORS filter to allow cross-origin requests.
	 * @return A SecurityFilterChain object that defines security configurations.
	 * @throws Exception If any error occurs during the configuration.
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {

		return http
				// Disable CSRF protection for all requests (useful in stateless APIs).
				.csrf(csrf -> csrf.ignoringRequestMatchers("/**"))

				// Adds the custom JWT authentication filter before Spring's default
				// authentication filter.
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

				// Configure access control for specific endpoints.
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						// Allows public access to Swagger documentation endpoints.
						.requestMatchers("/swagger-ui/**").permitAll().requestMatchers("/swagger-ui.html").permitAll()
						.requestMatchers("/v3/api-docs/**").permitAll()

						// Allows public access to the authentication login endpoint.
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/signup").permitAll()

						// Allows public access to Actuator for application monitoring.
						.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

						// All other endpoints require authentication.
						.requestMatchers("/**").authenticated().anyRequest().authenticated())

				// Builds the security filter chain.
				.build();
	}
}