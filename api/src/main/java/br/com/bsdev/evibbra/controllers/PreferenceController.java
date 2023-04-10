package br.com.bsdev.evibbra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; // Added to enable RequestBody
import org.springframework.web.bind.annotation.RestController;

import br.com.bsdev.evibbra.dtos.PreferenceRegisterDto;
import br.com.bsdev.evibbra.entities.Preference;
import br.com.bsdev.evibbra.services.PreferenceService;

// This class acts as a REST controller for managing preferences, extending BaseController for common behaviors.
@RestController
public class PreferenceController implements BaseController {

	public static final String PATH = "preferences"; // The path for this controller
    
	private final PreferenceService preferenceService; // The service to handle preference operations

    // Constructor-based dependency injection for PreferenceService
    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    // HTTP GET method to retrieve the current preferences
    @GetMapping(PATH) // Corrected to use the PATH constant
    public ResponseEntity<PreferenceRegisterDto> getPreference() {
        // Fetch the current preferences using the service
        var preference = preferenceService.getPreference();
        // Map the entity to DTO and return it in the response
        return mappingEntityToDto(preference);
    }

    // HTTP POST method to update preferences
    @PutMapping(PATH) // Corrected to use the PATH constant
    public ResponseEntity<PreferenceRegisterDto> updatePreference(@RequestBody PreferenceRegisterDto dto) {
        // Update the preferences using the service and get the updated entity
        var preference = preferenceService.updatePreference(dto);
        // Map the updated entity to DTO and return it in the response
        return mappingEntityToDto(preference);
    }

    // Helper method to convert Preference entity to PreferenceRegisterDto
    private ResponseEntity<PreferenceRegisterDto> mappingEntityToDto(Preference preference) {
        return ResponseEntity.ok(new PreferenceRegisterDto(
                preference.getAnnualInvoiceLimit(),
                preference.getEmailNotificationsEnabled(),
                preference.getSmsNotificationsEnabled()
        ));
    }

}