package br.com.bsdev.evibbra.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.bsdev.evibbra.controllers.PreferenceController;
import br.com.bsdev.evibbra.dtos.PreferenceRegisterDto;
import br.com.bsdev.evibbra.entities.Preference;
import br.com.bsdev.evibbra.services.PreferenceService;

class PreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests and responses.

    @Mock
    private PreferenceService preferenceService;  // Mock the service to avoid hitting the actual service layer.

    @InjectMocks
    private PreferenceController preferenceController;  // Controller under test with the service injected.

    // Setup method to initialize mocks and the mockMvc object before each test case.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(preferenceController).build();
    }

    // Test case for the GET request to fetch preferences.
    @Test
    void testGetPreference() throws Exception {
        // Mock data: preference entity returned by the service.
        Preference mockPreference = new Preference();
        mockPreference.setAnnualInvoiceLimit(50000.0);
        mockPreference.setEmailNotificationsEnabled(true);
        mockPreference.setSmsNotificationsEnabled(false);

        // Mock the service to return the mockPreference when called.
        when(preferenceService.getPreference()).thenReturn(mockPreference);

        // Perform the GET request and verify the response content and status.
        mockMvc.perform(get("/api/v1/preferences")
                .contentType(MediaType.APPLICATION_JSON))  // Specify that we expect JSON in the response.
                .andExpect(status().isOk())  // Expect status 200 OK.
                .andExpect(jsonPath("$.annualInvoiceLimit").value(50000.0))  // Check the annualInvoiceLimit field.
                .andExpect(jsonPath("$.emailNotificationsEnabled").value(true))  // Check the emailNotificationsEnabled field.
                .andExpect(jsonPath("$.smsNotificationsEnabled").value(false));  // Check the smsNotificationsEnabled field.
    }

    // Test case for the PUT request to update preferences.
    @Test
    void testUpdatePreference() throws Exception {
        // Mock data: preference entity that will be returned after update.
        Preference updatedPreference = new Preference();
        updatedPreference.setAnnualInvoiceLimit(75000.0);
        updatedPreference.setEmailNotificationsEnabled(true);
        updatedPreference.setSmsNotificationsEnabled(true);

        // Mock the service to return the updatedPreference when updating.
        when(preferenceService.updatePreference(any(PreferenceRegisterDto.class))).thenReturn(updatedPreference);

        // Create a JSON string to simulate the request body for the PUT request.
        String updateJson = "{\"annualInvoiceLimit\":75000, \"emailNotificationsEnabled\":true, \"smsNotificationsEnabled\":true}";

        // Perform the PUT request to update preferences and verify the response.
        mockMvc.perform(put("/api/v1/preferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))  // Send the JSON data in the request body.
                .andExpect(status().isOk())  // Expect status 200 OK.
                .andExpect(jsonPath("$.annualInvoiceLimit").value(75000.0))  // Check the updated annualInvoiceLimit field.
                .andExpect(jsonPath("$.emailNotificationsEnabled").value(true))  // Check the updated emailNotificationsEnabled field.
                .andExpect(jsonPath("$.smsNotificationsEnabled").value(true));  // Check the updated smsNotificationsEnabled field.
    }
}
