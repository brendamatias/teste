package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.dtos.CompanyRegisterDto;
import br.com.bsdev.evibbra.entities.Company;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CompanyRepository;

class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService; // Service to be tested

    @Mock
    private CompanyRepository companyRepository; // Mocked repository

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test for retrieving all companies
    @Test
    void testAll() {
        // Given
        List<Company> companies = new ArrayList<>();
        companies.add(new Company("12345678000195", "Company A", "Legal Name A"));
        companies.add(new Company("98765432000196", "Company B", "Legal Name B"));

        // When
        when(companyRepository.findAll()).thenReturn(companies);

        // Then
        List<CompanyResponse> responses = companyService.all();
        assertEquals(2, responses.size()); // Verify the size of the response list
        assertEquals("Company A", responses.get(0).getName()); // Verify the first company's name
    }

    // Test for saving a new company
    @Test
    void testSave() {
        // Given
        CompanyRegisterDto dto = new CompanyRegisterDto("Company A", "12345678000195", "Legal Name A");
        Company company = new Company("12345678000195", "Company A", "Legal Name A");
        company.setId(1L); // Set an ID for the saved company

        // When
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Then
        CompanyResponse response = companyService.save(dto);
        assertEquals("Company A", response.getName()); // Verify the saved company's name
        assertEquals("12345678000195", response.getCnpj()); // Verify the saved company's CNPJ
    }

    // Test for updating a non-existent company (exception)
    @Test
    void testUpdateCompanyNotFound() {
        // Given
        Long companyId = 999L; // Non-existent ID
        CompanyRegisterDto dto = new CompanyRegisterDto("Non-existent Company", "12345678000195", "Legal Name");

        // When
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> companyService.update(companyId, dto)); // Expect exception
    }

    // Test for updating an existing company
    @Test
    void testUpdateCompany() {
        // Given
        Long companyId = 1L; // Existing company ID
        Company existingCompany = new Company("12345678000195", "Old Company Name", "Old Legal Name");
        existingCompany.setId(companyId);

        CompanyRegisterDto dto = new CompanyRegisterDto("Updated Company", "12345678000195", "Updated Legal Name");

        // When
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);

        // Then
        CompanyResponse response = companyService.update(companyId, dto);
        assertEquals("Updated Company", response.getName()); // Verify the updated company's name
        assertEquals("12345678000195", response.getCnpj()); // Verify the company's CNPJ remains the same
    }

    // Test for updating the status of an existing company to archived
    @Test
    void testUpdateStatusToArchived() {
        // Given
        Long companyId = 1L; // Existing company ID
        Company existingCompany = new Company("12345678000195", "Company A", "Legal Name A");
        existingCompany.setId(companyId);
        existingCompany.setInactivatedAt(null); // Initially active

        // When
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);

        // Then
        CompanyResponse response = companyService.updateStatusArchivedOrActive(companyId);
        assertEquals(LocalDateTime.now().getMinute(), response.getInactivatedAt().getMinute()); // Verify inactivatedAt is now set
    }

    // Test for updating the status of an existing company to active
    @Test
    void testUpdateStatusToActive() {
        // Given
        Long companyId = 1L; // Existing company ID
        Company existingCompany = new Company("12345678000195", "Company A", "Legal Name A");
        existingCompany.setId(companyId);
        existingCompany.setInactivatedAt(LocalDateTime.now()); // Initially archived

        // When
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);

        // Then
        CompanyResponse response = companyService.updateStatusArchivedOrActive(companyId);
        assertEquals(null, response.getInactivatedAt()); // Verify inactivatedAt is now null (active)
    }

    // Test for retrieving a company by ID (success)
    @Test
    void testGetCompanyById() {
        // Given
        Long companyId = 1L; // Existing company ID
        Company existingCompany = new Company("12345678000195", "Company A", "Legal Name A");
        existingCompany.setId(companyId);

        // When
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));

        // Then
        CompanyResponse response = companyService.getCompany(companyId);
        assertEquals("Company A", response.getName()); // Verify the retrieved company's name
        assertEquals("12345678000195", response.getCnpj()); // Verify the retrieved company's CNPJ
    }

    // Test for retrieving a company by ID (not found exception)
    @Test
    void testGetCompanyByIdNotFound() {
        // Given
        Long companyId = 999L; // Non-existent company ID

        // When
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> companyService.getCompany(companyId)); // Expect exception
    }
}
