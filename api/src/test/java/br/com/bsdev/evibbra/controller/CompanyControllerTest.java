package br.com.bsdev.evibbra.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.bsdev.evibbra.controllers.CompanyController;
import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.dtos.CompanyRegisterDto;
import br.com.bsdev.evibbra.services.CompanyService;

class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests and responses for testing controller endpoints.

    @Mock
    private CompanyService companyService;  // Mock service layer to simulate business logic without hitting the actual service.

    @InjectMocks
    private CompanyController companyController;  // Injects the mock service into the controller for testing.

    // Initializes mocks and sets up MockMvc to test the controller.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
    }

    // Test case for the "getAllCompanies" method in the controller.
    @Test
    void testGetAllCompanies() throws Exception {
        // Create a list of mock CompanyResponse objects that the service will return.
        List<CompanyResponse> companies = new ArrayList<>();
        companies.add(new CompanyResponse(1L, "Company A", "00000000000191", "Legal Name A", null));
        companies.add(new CompanyResponse(2L, "Company B", "00000000000272", "Legal Name B", null));

        // Mock the behavior of the companyService to return the list of companies.
        when(companyService.all()).thenReturn(companies);

        // Perform the GET request and check that the response status is OK and the data is as expected.
        mockMvc.perform(get("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Check if the status is 200 OK.
                .andExpect(jsonPath("$.length()").value(2))  // Verify that the response contains 2 companies.
                .andExpect(jsonPath("$[0].name").value("Company A"))  // Verify the name of the first company.
                .andExpect(jsonPath("$[1].name").value("Company B"));  // Verify the name of the second company.
    }

    // Test case for creating a new company through the "createCompany" method.
    @Test
    void testCreateCompany() throws Exception {
        // Define the expected response after creating a new company.
        CompanyResponse expectedResponse = new CompanyResponse(1L, "New Company", "00000000000353", "New Legal Name", null);

        // Mock the companyService to return the expected response when saving a new company.
        when(companyService.save(any(CompanyRegisterDto.class))).thenReturn(expectedResponse);

        // Perform the POST request to create the company and verify the response content and status.
        mockMvc.perform(post("/api/v1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Company\", \"cnpj\":\"00000000000353\", \"legalName\":\"New Legal Name\"}"))
                .andExpect(status().isCreated())  // Expect HTTP status 201 Created.
                .andExpect(jsonPath("$.name").value("New Company"))  // Verify the name of the created company.
                .andExpect(jsonPath("$.cnpj").value("00000000000353"))  // Verify the CNPJ of the created company.
                .andExpect(jsonPath("$.legalName").value("New Legal Name"));  // Verify the legal name.
    }

    // Test case for updating an existing company using the "updateCompany" method.
    @Test
    void testUpdateCompany() throws Exception {
        Long companyId = 1L;  // ID of the company to update.

        // Define the expected response after updating the company.
        CompanyResponse expectedResponse = new CompanyResponse(companyId, "Updated Company", "00000000000191", "Updated Legal Name", null);

        // Mock the companyService to return the expected response when updating the company.
        when(companyService.update(eq(companyId), any(CompanyRegisterDto.class))).thenReturn(expectedResponse);

        // Perform the PUT request to update the company and verify the response content and status.
        mockMvc.perform(put("/api/v1/companies/" + companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Company\", \"cnpj\":\"00000000000191\", \"legalName\":\"Updated Legal Name\"}"))
                .andExpect(status().isOk())  // Expect HTTP status 200 OK.
                .andExpect(jsonPath("$.id").value(companyId))  // Verify the ID of the updated company.
                .andExpect(jsonPath("$.name").value("Updated Company"))  // Verify the updated company name.
                .andExpect(jsonPath("$.cnpj").value("00000000000191"))  // Verify the updated CNPJ.
                .andExpect(jsonPath("$.legalName").value("Updated Legal Name"));  // Verify the updated legal name.
    }
}