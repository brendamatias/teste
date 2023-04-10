package br.com.bsdev.evibbra.services;

import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.dtos.PreferenceRegisterDto;
import br.com.bsdev.evibbra.entities.Preference;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.PreferenceRepository;

@Service // Annotation indicating that this class is a service in the Spring context
public class PreferenceService {

	private final PreferenceRepository preferenceRepository; // Repository to handle Preference entity

	// Constructor-based dependency injection for PreferenceRepository
	public PreferenceService(PreferenceRepository preferenceRepository) {
		this.preferenceRepository = preferenceRepository;
	}

	// Method to retrieve the current preferences. Assumes only one preferences
	// record exists.
	public Preference getPreference() {
		// Using orElseThrow to handle the case where the preferences record might not
		// exist
		return preferenceRepository.findById(1L)
				.orElseThrow(() -> new ResourceNotFoundException("Preferences record not found."));
	}

	// Method to update preferences based on the incoming DTO
	public Preference updatePreference(PreferenceRegisterDto registerDto) {
		// Retrieve the existing preferences
		var preference = getPreference();

		// Update the preferences with values from the DTO
		preference.setAnnualInvoiceLimit(registerDto.getAnnualInvoiceLimit());
		preference.setEmailNotificationsEnabled(registerDto.getEmailNotificationsEnabled());
		preference.setSmsNotificationsEnabled(registerDto.getSmsNotificationsEnabled());

		// Save the updated preferences back to the repository
		return preferenceRepository.save(preference);
	}
}