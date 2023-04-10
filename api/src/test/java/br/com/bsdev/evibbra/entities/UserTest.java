package br.com.bsdev.evibbra.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the User entity to ensure proper instantiation and UserDetails functionality.
 */
class UserTest {

    private User testUser;
    private User testUserFull;

    @BeforeEach
    void setUp() {
        // Create a User object with test data
        testUser = new User("Test User", "test@example.com", "password123", "71999999999");
        testUserFull = new User();
        testUserFull.setId(1L);
        testUserFull.setEmail("test@example.com");
        testUserFull.setName("Test User");
        testUserFull.setPhoneNumber("71999999999");
        testUserFull.setPassword("password123");
        testUserFull.setCreatedAt(LocalDateTime.now());
        testUserFull.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testUserConstructor() {
        // Assert that the User object is created correctly
        assertEquals("Test User", testUser.getName(), "The name should match the expected value");
        assertEquals("test@example.com", testUser.getEmail(), "The email should match the expected value");
        assertEquals("password123", testUser.getPassword(), "The password should match the expected value");
        assertEquals("71999999999", testUser.getPhoneNumber(), "The phone number should match the expected value");

        // Assert that createdAt is not null and is set to the current time
        LocalDateTime now = LocalDateTime.now();
        assertEquals(testUser.getCreatedAt().getMinute(), now.getMinute(), "CreatedAt minute should match the current minute");
    }

    @Test
    void testUserFullConstructor() {
        // Assert that the User object is created correctly
        assertEquals(1L, testUserFull.getId(), "The id should match the expected value");
        assertEquals("Test User", testUserFull.getName(), "The name should match the expected value");
        assertEquals("test@example.com", testUserFull.getEmail(), "The email should match the expected value");
        assertEquals("password123", testUserFull.getPassword(), "The password should match the expected value");
        assertEquals("71999999999", testUserFull.getPhoneNumber(), "The phone number should match the expected value");

        // Assert that createdAt is not null and is set to the current time
        LocalDateTime now = LocalDateTime.now();
        assertEquals(testUserFull.getCreatedAt().getMinute(), now.getMinute(), "CreatedAt minute should match the current minute");
        assertEquals(testUserFull.getUpdatedAt().getMinute(), now.getMinute(), "UpdatedAt minute should match the current minute");
    }

    @Test
    void testGetUsername() {
        // Assert that getUsername returns the correct email
        assertEquals(testUser.getEmail(), testUser.getUsername(), "The username should match the email");
    }

    @Test
    void testGetAuthorities() {
        // Assert that getAuthorities returns an empty collection
        assertEquals(0, testUser.getAuthorities().size(), "The authorities should be empty");
    }
}
