package br.com.bsdev.evibbra.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

	// Injecting the API URL from application properties
	@Value("${textbelt.api.url}")
	private String apiUrl;

	// Injecting the API key from application properties
	@Value("${textbelt.api.key}")
	private String apiKey;

	// Method to send SMS
	public String send(String phone, String message) {
		RestTemplate restTemplate = new RestTemplate(); // Creating an instance of RestTemplate for HTTP requests

		// Defining the headers for the HTTP request
		HttpHeaders headers = new HttpHeaders();

		// Defining the body of the request as a map of key-value pairs
		Map<String, String> body = new HashMap<>();
		body.put("phone", phone); // Phone number for the SMS
		body.put("message", message); // Message content
		body.put("key", apiKey); // API key for authorization

		// Creating an HTTP entity that combines headers and body
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		// Executing the POST request using the API URL and returning the response as a
		// string
		ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

		// Returning the body of the response (e.g., success or error message)
		return response.getBody();
	}

}
