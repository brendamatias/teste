package br.com.bsdev.evibbra.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.bsdev.evibbra.controllers.response.AvailableAmountResponse;
import br.com.bsdev.evibbra.dtos.EmailRequestDto;
import br.com.bsdev.evibbra.entities.Preference;
import br.com.bsdev.evibbra.entities.User;
import br.com.bsdev.evibbra.repositories.UserRepository;

class NotificationServiceTest {

	@InjectMocks
	private NotificationService notificationService;

	@Mock
	private DashboardService dashboardService;

	@Mock
	private EmailService emailService;

	@Mock
	private PreferenceService preferenceService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SmsService smsService;

	private User user;
	private Authentication authentication;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		// Initialize a sample user for testing
		user = new User();
		user.setEmail("test@example.com");
		user.setName("Test User");
		user.setPhoneNumber("123456789");

		authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	@Test
	void testSendMonthlyLimitNotification() {
		// Mocking the preferences to enable email notifications
		when(preferenceService.getPreference()).thenReturn(new Preference(8100.0, true, false));
		// Mocking the user repository to return a list with one user
		when(userRepository.findAll()).thenReturn(List.of(user));

		// Mocking the available invoice amounts
		AvailableAmountResponse availableAmountResponse = new AvailableAmountResponse(10000.0, 8000.0);
		when(dashboardService.getAvailableInvoiceAmountByCompetenceYear(anyInt())).thenReturn(availableAmountResponse);

		// Call the method to test
		notificationService.sendMonthlyLimitNotification();

		// Verify that an email was sent to the user
		verify(emailService, times(1)).send(any(EmailRequestDto.class));
		// Verify that no SMS was sent
		verify(smsService, never()).send(anyString(), anyString());
	}

	@Test
	void testSendNotificationNearLimit() {
		// Mocking the user session to return the test user
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Mocking the preferences to enable both email and SMS notifications
		when(preferenceService.getPreference()).thenReturn(new Preference(81000.0, true, true));

		// Mocking the available invoice amounts to simulate the user being near the
		// limit
		AvailableAmountResponse availableAmountResponse = new AvailableAmountResponse(10000.0, 8000.0);
		when(dashboardService.getAvailableInvoiceAmountByCompetenceYear(anyInt())).thenReturn(availableAmountResponse);

		// Call the method to test
		notificationService.sendNotificationNearLimit();

		// Verify that an email was sent to the user
		verify(emailService, times(1)).send(any(EmailRequestDto.class));
		// Verify that an SMS was sent to the user
		verify(smsService, times(1)).send(eq(user.getPhoneNumber()), anyString());
	}

	@Test
	void testSendNotificationNearLimit_NoNotifications() {
		// Mocking the user session to return the test user
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Mocking the preferences to disable email and SMS notifications
		when(preferenceService.getPreference()).thenReturn(new Preference(8100.0, false, false));

		// Mocking the available invoice amounts to simulate the user being near the
		// limit
		AvailableAmountResponse availableAmountResponse = new AvailableAmountResponse(10000.0, 8000.0);
		when(dashboardService.getAvailableInvoiceAmountByCompetenceYear(anyInt())).thenReturn(availableAmountResponse);

		// Call the method to test
		notificationService.sendNotificationNearLimit();

		// Verify that no email was sent
		verify(emailService, never()).send(any(EmailRequestDto.class));
		// Verify that no SMS was sent
		verify(smsService, never()).send(anyString(), anyString());
	}
}