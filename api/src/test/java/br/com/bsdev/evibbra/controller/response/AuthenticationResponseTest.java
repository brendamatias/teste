package br.com.bsdev.evibbra.controller.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.bsdev.evibbra.controllers.response.AuthenticationResponse;

/**
 * Test class for AuthenticationResponse to ensure proper instantiation and getter methods.
 */
class AuthenticationResponseTest {

    @Test
    void testAuthenticationResponse() {
        // Arrange
        String expectedToken = "testToken";
        Long expectedExpiresIn = 3600L;

        // Act
        AuthenticationResponse response = new AuthenticationResponse(expectedToken, expectedExpiresIn);

        // Assert
        assertEquals(expectedToken, response.getToken(), "The token should match the expected token");
        assertEquals(expectedExpiresIn, response.getExpiresIn(), "The expiration time should match the expected value");
    }
}
