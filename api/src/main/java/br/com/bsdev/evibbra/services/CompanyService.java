package br.com.bsdev.evibbra.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.dtos.CompanyRegisterDto;
import br.com.bsdev.evibbra.entities.Company;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CompanyRepository;

@Service // Annotation indicating that this class is a service in the Spring context
public class CompanyService {

	private final CompanyRepository companyRepository; // Repository to handle Company entity

	// Constructor-based dependency injection for CompanyRepository
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	// Retrieve all companies and map them to response DTOs
	public List<CompanyResponse> all() {
		return companyRepository.findAll().stream().map(this::mapping) // Use method reference for clarity
				.sorted((c1, c2) -> c1.getName().compareTo(c2.getName())).toList();
	}

	// Retrieve one company and map them to CompanyResponse DTOs
	public CompanyResponse getCompany(Long id) {
		return mapping(findCompanyById(id));
	}

	// Save a new company and return the saved company as a response DTO
	public CompanyResponse save(CompanyRegisterDto dto) {
		var company = new Company(dto.getCnpj(), dto.getName(), dto.getLegalName());
		var companySave = companyRepository.save(company); // This should save and assign an ID
		return mapping(companySave); // ID must be present here
	}

	// Update an existing company by ID
	public CompanyResponse update(Long id, CompanyRegisterDto dto) {
		var company = findCompanyById(id);
		company.setName(dto.getName());
		company.setCnpj(dto.getCnpj());
		company.setLegalName(dto.getLegalName());

		return mapping(companyRepository.save(company));
	}

	// Update the status of a company to either 'archived' or 'active'
	public CompanyResponse updateStatusArchivedOrActive(Long id) {
		var company = findCompanyById(id);
		company.setInactivatedAt(company.getInactivatedAt() != null ? null : LocalDateTime.now());

		return mapping(companyRepository.save(company));
	}

	// Helper method to retrieve company by ID
	private Company findCompanyById(Long id) {
		return companyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
	}

	// Map Company entity to CompanyResponse DTO
	private CompanyResponse mapping(Company company) {
		return new CompanyResponse(company.getId(), company.getName(), company.getCnpj(), company.getLegalName(),
				company.getInactivatedAt());
	}
}