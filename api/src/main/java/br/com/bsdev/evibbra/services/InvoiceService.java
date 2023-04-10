package br.com.bsdev.evibbra.services;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.controllers.response.InvoiceResponse;
import br.com.bsdev.evibbra.dtos.InvoiceRegisterDto;
import br.com.bsdev.evibbra.entities.Company;
import br.com.bsdev.evibbra.entities.Invoice;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CompanyRepository;
import br.com.bsdev.evibbra.repositories.InvoiceRepository;

@Service // Annotation indicating that this class is a service in the Spring context
public class InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final CompanyRepository companyRepository;
	private final NotificationService notificationService;

	// Constructor-based dependency injection for repositories
	public InvoiceService(InvoiceRepository invoiceRepository, CompanyRepository companyRepository, NotificationService notificationService) {
		this.invoiceRepository = invoiceRepository;
		this.companyRepository = companyRepository;
		this.notificationService = notificationService;
	}

	// Retrieve all invoices and map them to InvoiceResponse DTOs
	public List<InvoiceResponse> getAll() {
		// Map each Invoice entity to a response DTO
		return invoiceRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public List<InvoiceResponse> getAllByCompetenceOrCompany(YearMonth competence, Long companyId) {

		// If both competence and companyId are null, throw an exception to enforce
		// valid input
		if (companyId == null && competence == null) {
			throw new IllegalArgumentException("At least one filter must be provided: competence or companyId.");
		}

		// Find company if companyId is provided, otherwise set to null
		var company = (companyId != null) ? findCompanyById(companyId) : null;


		// Case 1: Both company and competence are provided
		if (company != null && competence != null) {
			return findInvoicesByCompanyAndCompetence(company, competence);
		}
		// Case 2: Only company is provided
		else if (company != null) {
			return findInvoicesByCompany(company);
		}
		// Case 3: Only competence is provided
		else {
			return findInvoicesByCompetence(competence);
		}
	}

	private List<InvoiceResponse> findInvoicesByCompanyAndCompetence(Company company, YearMonth competenceYearMonth) {
		// Fetch invoices by company and competence year/month, and map them to
		// InvoiceResponse DTOs
		return invoiceRepository.findAllByCompanyAndCompetenceYearAndCompetenceMonth(company,
				competenceYearMonth.getYear(), competenceYearMonth.getMonthValue()).stream().map(this::mapToResponse)
				.toList();
	}

	private List<InvoiceResponse> findInvoicesByCompany(Company company) {
		// Fetch invoices by company only and map them to InvoiceResponse DTOs
		return invoiceRepository.findAllByCompany(company).stream().map(this::mapToResponse).toList();
	}

	private List<InvoiceResponse> findInvoicesByCompetence(YearMonth competenceYearMonth) {
		// Fetch invoices by competence year/month only and map them to InvoiceResponse
		// DTOs
		return invoiceRepository.findAllByCompetenceYearAndCompetenceMonth(competenceYearMonth.getYear(),
				competenceYearMonth.getMonthValue()).stream().map(this::mapToResponse).toList();
	}

	// Retrieve one invoice and map them to InvoiceResponse DTOs
	public InvoiceResponse getInvoice(Long id) {
		return mapToResponse(findInvoiceById(id));
	}

	// Save a new invoice and return the saved invoice as an InvoiceResponse DTO
	public InvoiceResponse save(InvoiceRegisterDto dto) {
		// Find the company by ID, or throw an exception if it doesn't exist
		var company = companyRepository.findById(dto.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + dto.getCompanyId()));

		// Create a new Invoice entity using data from the DTO and the found company
		var invoice = new Invoice(dto.getNumber(), dto.getAmount(), dto.getServiceDescription(),
				dto.getCompetenceMonth(), dto.getCompetenceYear(), dto.getPaymentDate(), company);

		// Save the invoice entity to the repository and map it to the response DTO
		var savedInvoice = invoiceRepository.save(invoice);
		
		// checks whether the invoice values ​​are not close to the limit and generates a notification
		notificationService.sendNotificationNearLimit();
		return mapToResponse(savedInvoice);
	}

	// Update an existing invoice by its ID and return the updated invoice as an
	// InvoiceResponse DTO
	public InvoiceResponse update(Long invoiceId, InvoiceRegisterDto dto) {
		// Find the existing invoice
		var invoice = findInvoiceById(invoiceId);
		
		// checks if the values ​​were changed during the note update
		var isVerifyNotification = dto.getAmount() != invoice.getAmount();

		// Find the associated company
		var company = findCompanyById(dto.getCompanyId());

		// Update the invoice fields
		invoice.setInvoiceNumber(dto.getNumber());
		invoice.setAmount(dto.getAmount());
		invoice.setServiceDescription(dto.getServiceDescription());
		invoice.setCompetenceMonth(dto.getCompetenceMonth());
		invoice.setCompetenceYear(dto.getCompetenceYear());
		invoice.setPaymentDate(dto.getPaymentDate());
		invoice.setCompany(company); // Set the associated company

		// Save the updated invoice
		invoiceRepository.save(invoice);
		
		if (isVerifyNotification) {
			// checks whether the invoice values ​​are not close to the limit and generates a notification
			notificationService.sendNotificationNearLimit();
		}

		// Return the mapped response
		return mapToResponse(invoice);
	}

	// Delete an existing invoice by its ID
	public void delete(Long invoiceId) {
		var invoice = findInvoiceById(invoiceId);
		invoiceRepository.delete(invoice);
	}

	private Invoice findInvoiceById(Long id) {
		return invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
	}

	private Company findCompanyById(Long id) {
		return companyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
	}

	// Helper method to map an Invoice entity to an InvoiceResponse DTO
	private InvoiceResponse mapToResponse(Invoice invoice) {
		// Create a CompanyResponse DTO for the associated company
		var companyResponse = new CompanyResponse(invoice.getCompany().getId(), invoice.getCompany().getName(),
				invoice.getCompany().getCnpj(), invoice.getCompany().getLegalName(), invoice.getCompany().getInactivatedAt());

		// Return the full InvoiceResponse DTO with company information
		return new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getAmount(),
				invoice.getServiceDescription(), invoice.getCompetenceMonth(), invoice.getCompetenceYear(),
				invoice.getPaymentDate(), companyResponse);
	}
}