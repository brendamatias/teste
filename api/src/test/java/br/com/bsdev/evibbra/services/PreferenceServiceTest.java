package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.bsdev.evibbra.dtos.PreferenceRegisterDto;
import br.com.bsdev.evibbra.entities.Preference;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.PreferenceRepository;

class PreferenceServiceTest {

	@InjectMocks
	private PreferenceService preferenceService; // Service under test

	@Mock
	private PreferenceRepository preferenceRepository; // Mocked repository

	private Preference preference; // Sample preference entity

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize mocks
		preference = new Preference(); // Create a new Preference instance
		preference.setAnnualInvoiceLimit(10000.0);
		preference.setEmailNotificationsEnabled(true);
		preference.setSmsNotificationsEnabled(false);
	}

	@Test
	void testGetPreference_Success() {
		// Arrange: Mocking repository behavior
		when(preferenceRepository.findById(1L)).thenReturn(java.util.Optional.of(preference));

		// Act: Calling the service method
		Preference result = preferenceService.getPreference();

		// Assert: Verifying the result
		assertEquals(preference, result);
		verify(preferenceRepository, times(1)).findById(1L); // Verify that the repository method was called once
	}

	@Test
	void testGetPreference_NotFound() {
		// Arrange: Mocking repository behavior to return empty
		when(preferenceRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		// Act & Assert: Expecting ResourceNotFoundException to be thrown
		assertThrows(ResourceNotFoundException.class, () -> {
			preferenceService.getPreference();
		});

		verify(preferenceRepository, times(1)).findById(1L); // Verify that the repository method was called once
	}

	@Test
	void testUpdatePreference_Success() {
		// Arrange: Mocking repository behavior
		when(preferenceRepository.findById(1L)).thenReturn(java.util.Optional.of(preference));
		when(preferenceRepository.save(any(Preference.class))).thenReturn(preference);

		// Create a DTO to update preferences
		PreferenceRegisterDto registerDto = new PreferenceRegisterDto();
		registerDto.setAnnualInvoiceLimit(12000.0);
		registerDto.setEmailNotificationsEnabled(false);
		registerDto.setSmsNotificationsEnabled(true);

		// Act: Calling the update method
		Preference updatedPreference = preferenceService.updatePreference(registerDto);

		// Assert: Verify that the updated values are correct
		assertEquals(12000.0, updatedPreference.getAnnualInvoiceLimit());
		assertEquals(false, updatedPreference.getEmailNotificationsEnabled());
		assertEquals(true, updatedPreference.getSmsNotificationsEnabled());

		// Verify repository interactions
		verify(preferenceRepository, times(1)).findById(1L);
		verify(preferenceRepository, times(1)).save(preference);
	}

	@Test
	void testConstructorWithAllFields() {
		// Arrange: Set up the values for the DTO fields
		Double annualInvoiceLimit = 15000.0;
		Boolean emailNotificationsEnabled = true;
		Boolean smsNotificationsEnabled = false;

		// Act: Create an instance of PreferenceRegisterDto using the constructor
		PreferenceRegisterDto preferenceRegisterDto = new PreferenceRegisterDto(annualInvoiceLimit,
				emailNotificationsEnabled, smsNotificationsEnabled);

		// Assert: Check if the fields are set correctly
		assertEquals(annualInvoiceLimit, preferenceRegisterDto.getAnnualInvoiceLimit());
		assertEquals(emailNotificationsEnabled, preferenceRegisterDto.getEmailNotificationsEnabled());
		assertEquals(smsNotificationsEnabled, preferenceRegisterDto.getSmsNotificationsEnabled());
	}
}