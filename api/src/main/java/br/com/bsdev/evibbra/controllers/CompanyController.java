package br.com.bsdev.evibbra.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.dtos.CompanyRegisterDto;
import br.com.bsdev.evibbra.services.CompanyService;

//Extends BaseController to inherit the /api/v1 path
@RestController
public class CompanyController implements BaseController {

	public static final String PATH = "companies"; // The path for this controller

	private final CompanyService companyService;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	// Endpoint to retrieve all companies
	@GetMapping(PATH)
	public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
		List<CompanyResponse> companies = companyService.all();
		return ResponseEntity.ok(companies);
	}

	// Endpoint to retrieve an existing company
	@GetMapping(PATH + "/{id}")
	public ResponseEntity<CompanyResponse> getCompany(@PathVariable(name = "id") Long id) {
		CompanyResponse company = companyService.getCompany(id);
		return ResponseEntity.ok(company);
	}

	// Endpoint to save a new company
	@PostMapping(PATH)
	public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRegisterDto dto) {
		CompanyResponse company = companyService.save(dto);
		return ResponseEntity.status(201).body(company);
	}

	// Endpoint to update an existing company
	@PutMapping(PATH + "/{id}")
	public ResponseEntity<CompanyResponse> updateCompany(@PathVariable(name = "id") Long id,
			@RequestBody CompanyRegisterDto dto) {
		CompanyResponse company = companyService.update(id, dto);
		return ResponseEntity.ok(company);
	}

	// Endpoint to update the status of a company
	@PatchMapping(PATH + "/{id}/status")
	public ResponseEntity<CompanyResponse> updateCompanyStatus(@PathVariable(name = "id") Long id) {
		CompanyResponse company = companyService.updateStatusArchivedOrActive(id);
		return ResponseEntity.ok(company);
	}
}