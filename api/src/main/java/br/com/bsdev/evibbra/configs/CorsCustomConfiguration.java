package br.com.bsdev.evibbra.configs;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class to customize CORS (Cross-Origin Resource Sharing)
 * settings. This allows configuring allowed origins, HTTP methods, and headers
 * for requests coming from different domains.
 */
@Configuration
public class CorsCustomConfiguration {

	// The allowed URLs for CORS requests, provided via application properties.
	@Value("${cors.urls}")
	private String corsUrls;

	/**
	 * Creates a CorsFilter bean to handle CORS requests with the configured
	 * settings.
	 * 
	 * @return CorsFilter configured with allowed origins, methods, and headers.
	 */
	@Bean
	CorsFilter corsFilter() {

		// Create a new CORS configuration
		var configuration = new CorsConfiguration();

		// Set allowed origins by splitting the comma-separated URLs from properties
		configuration.setAllowedOrigins(Arrays.asList(corsUrls.split(",")));

		// Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
		configuration.setAllowedMethods(Collections.singletonList("*"));

		// Allow all HTTP headers (Authorization, Content-Type, etc.)
		configuration.setAllowedHeaders(Collections.singletonList("*"));

		// Disable credentials (cookies or Authorization headers won't be sent across
		// origins)
		configuration.setAllowCredentials(false);

		// Define which paths this configuration will apply to (in this case, all paths)
		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		// Return the configured CORS filter
		return new CorsFilter(source);
	}
}