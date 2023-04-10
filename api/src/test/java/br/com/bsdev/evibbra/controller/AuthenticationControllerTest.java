package br.com.bsdev.evibbra.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.bsdev.evibbra.controllers.AuthenticationController;
import br.com.bsdev.evibbra.dtos.UserLoginDto;
import br.com.bsdev.evibbra.dtos.UserRegisterDto;
import br.com.bsdev.evibbra.entities.User;
import br.com.bsdev.evibbra.services.AuthenticationService;
import br.com.bsdev.evibbra.services.TokenService;

// Test class for AuthenticationController
class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc; // MockMvc for testing controller

	@Mock
	private TokenService jwtService; // Mocked TokenService for JWT operations

	@Mock
	private AuthenticationService authenticationService; // Mocked AuthenticationService for user operations

	@InjectMocks
	private AuthenticationController authenticationController; // The controller being tested

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize mocks before each test
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build(); // Set up MockMvc with the
																						// controller
	}

	@Test
	void testAuthenticateSuccess() throws Exception {
		// Given a valid UserLoginDto and expected User object
		UserLoginDto loginUserDto = new UserLoginDto("test@example.com", "password123");
		User authenticatedUser = new User();
		authenticatedUser.setId(1L);
		authenticatedUser.setEmail("test@example.com");
		authenticatedUser.setName("Test User");
		authenticatedUser.setPassword("password123");
		authenticatedUser.setCreatedAt(LocalDateTime.now());
		authenticatedUser.setUpdatedAt(LocalDateTime.now());

		// Mock the behavior of authenticationService and jwtService
		when(authenticationService.authenticate(loginUserDto)).thenReturn(authenticatedUser);
		when(jwtService.gerarToken(authenticatedUser)).thenReturn("mockedJwtToken");
		when(jwtService.generateExpiresAt()).thenReturn(java.time.Instant.now().plusSeconds(3600));

		// Perform the login request and verify the response
		mockMvc.perform(post("http://localhost:8080/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"test@example.com\", \"password\":\"password123\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.expiresIn").isNumber());
	}

	@Test
	void testRegisterSuccess() throws Exception {
		// Given a valid UserRegisterDto and expected User object
		UserRegisterDto registerUserDto = new UserRegisterDto("test@example.com", "password123", "Test User", "71999999999");
		User registeredUser = new User();
		registeredUser.setId(1L);
		registeredUser.setEmail("test@example.com");
		registeredUser.setName("Test User");
		registeredUser.setPassword("password123");
		registeredUser.setCreatedAt(LocalDateTime.now());
		registeredUser.setUpdatedAt(LocalDateTime.now());

		// Mock the behavior of authenticationService
		when(authenticationService.signup(registerUserDto)).thenReturn(registeredUser);

		// Perform the registration request and verify the response
		mockMvc.perform(post("http://localhost:8080/api/v1/signup").contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"test@example.com\", \"password\":\"password123\", \"name\":\"Test User\"}"))
				.andExpect(status().isOk());
	}

}