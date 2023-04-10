package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.bsdev.evibbra.dtos.UserLoginDto;
import br.com.bsdev.evibbra.dtos.UserRegisterDto;
import br.com.bsdev.evibbra.entities.User;
import br.com.bsdev.evibbra.repositories.UserRepository;

// Test class for AuthenticationService
class AuthenticationServiceTest {

	@Mock
	private UserRepository userRepository; // Mocked UserRepository

	@Mock
	private PasswordEncoder passwordEncoder; // Mocked PasswordEncoder

	@Mock
	private AuthenticationManager authenticationManager; // Mocked AuthenticationManager

	@InjectMocks
	private AuthenticationService authenticationService; // Class under test

	// Method executed before each test
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize mocks
	}

	// Test user data
	private User testUser;
	private UserRegisterDto registerDto;
	private UserLoginDto loginDto;

	// Setup test user and DTOs before each test
	@BeforeEach
	void init() {
		testUser = new User("Test User", "test@example.com", "encodedPassword", "71999999999");

		registerDto = new UserRegisterDto();
		registerDto.setName("Test User");
		registerDto.setEmail("test@example.com");
		registerDto.setPassword("password123");
		registerDto.setPhoneNumber("71999999999");

		loginDto = new UserLoginDto();
		loginDto.setEmail("test@example.com");
		loginDto.setPassword("password123");
	}

	// Test user registration
	@Test
	void testSignup() {
		// Mock password encoding
		when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
		// Mock user repository save method
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		// Call signup method
		User registeredUser = authenticationService.signup(registerDto);

		// Verify that the returned user matches the test user
		assertEquals(testUser.getEmail(), registeredUser.getEmail());
		assertEquals(testUser.getName(), registeredUser.getName());
		// Verify that the user was saved in the repository
		verify(userRepository, times(1)).save(any(User.class));
	}

	// Test user authentication
	@Test
	void testAuthenticate() throws Exception {
		// Simulate successful authentication
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(new UsernamePasswordAuthenticationToken(testUser.getEmail(), "encodedPassword"));

		// Mock user repository to return the test user
		when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(testUser));

		// Call authenticate method
		User authenticatedUser = authenticationService.authenticate(loginDto);

		// Verify that the returned user matches the test user
		assertEquals(testUser.getEmail(), authenticatedUser.getEmail());
		// Verify that the user was fetched from the repository
		verify(userRepository, times(1)).findByEmail(loginDto.getEmail());
	}

	// Test authentication failure when user is not found
	@Test
	void testAuthenticateUserNotFound() throws Exception {
		// Simulate successful authentication
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(new UsernamePasswordAuthenticationToken(testUser.getEmail(), "encodedPassword"));

		// Mock user repository to return an empty Optional
		when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

		// Verify that an exception is thrown when authenticating a non-existent user
		assertThrows(Exception.class, () -> {
			authenticationService.authenticate(loginDto);
		});
	}
}
