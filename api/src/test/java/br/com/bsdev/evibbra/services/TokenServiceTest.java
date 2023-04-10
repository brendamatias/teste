package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.bsdev.evibbra.entities.User;

// Test class for the TokenService
class TokenServiceTest {

    private TokenService tokenService; // Instance of TokenService to be tested
    private User testUser; // Test user for token generation

    // Method executed before each test
    @BeforeEach
    void setUp() {
        // Initialize the TokenService with test values
        tokenService = new TokenService("eVibbra", "testSecret");
        MockitoAnnotations.openMocks(this); // Initialize Mockito mocks

        // Create a test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("Test Test");
        testUser.setPassword("123456");
    }

    // Test to generate a token for a user
    @Test
    void testGenerateToken() {
        String token = tokenService.gerarToken(testUser); // Generate a token for the test user

        // Verify that the generated token is not null
        assertNotNull(token);
        // Verify that the generated token is a JWT (starts with "eyJ")
        assertTrue(token.startsWith("eyJ"));
    }

    // Test to verify the expiration time of the token
    @Test
    void testGenerateExpiresAt() {
        Instant expirationTime = tokenService.generateExpiresAt(); // Generate expiration time

        // Check if the expiration time is greater than the current time
        assertTrue(expirationTime.isAfter(Instant.now()));
    }

    // Test to get the subject of the token
    @Test
    void testGetSubject() throws JWTVerificationException {
        // Generate a token for the test user
        String token = tokenService.gerarToken(testUser);

        // Call the getSubject method with the generated token
        String subject = tokenService.getSubject(token);

        // Verify that the subject of the token matches the user's email
        assertEquals(testUser.getEmail(), subject);
    }

    // Test to validate if the token is valid for the correct user
    @Test
    void testIsTokenValid() {
        // Generate a token for the test user
        String token = tokenService.gerarToken(testUser);

        // Create a mocked UserDetails for the test
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.getEmail());

        // Check if the token is valid
        boolean isValid = tokenService.isTokenValid(token, userDetails);

        // Verify that the token is valid for the correct user
        assertTrue(isValid, "The token should be valid for the correct user");
    }

    // Test to validate if the token is invalid for a different user
    @Test
    void testIsTokenInvalidForDifferentUser() {
        // Generate a token for the test user
        String token = tokenService.gerarToken(testUser);

        // Create a mocked UserDetails for the test with a different username
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otheruser@example.com");

        // Check if the token is invalid for the different user
        boolean isValid = tokenService.isTokenValid(token, userDetails);

        // Verify that the token is not valid for a different user
        assertFalse(isValid, "The token should not be valid for a different user");
    }
}
