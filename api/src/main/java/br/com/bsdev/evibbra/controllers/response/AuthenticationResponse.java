package br.com.bsdev.evibbra.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AuthenticationResponse represents the response returned to the client 
 * upon successful authentication.
 */
@Getter // Lombok annotation to generate getters for all fields
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields as parameters
public class AuthenticationResponse {

    // The JWT token generated upon successful authentication
    private String token;

    // The duration in seconds until the token expires
    private Long expiresIn;

}