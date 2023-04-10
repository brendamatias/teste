package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.bsdev.evibbra.controllers.response.InvoiceResponse;
import br.com.bsdev.evibbra.dtos.InvoiceRegisterDto;
import br.com.bsdev.evibbra.entities.Company;
import br.com.bsdev.evibbra.entities.Invoice;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CompanyRepository;
import br.com.bsdev.evibbra.repositories.InvoiceRepository;

class InvoiceServiceTest {

	private InvoiceService invoiceService;
	private InvoiceRepository invoiceRepository;
	private CompanyRepository companyRepository;
	private NotificationService notificationService;

	@BeforeEach
	void setUp() {
		// Initialize mock repositories before each test
		invoiceRepository = mock(InvoiceRepository.class);
		companyRepository = mock(CompanyRepository.class);
		notificationService = mock(NotificationService.class);
		invoiceService = new InvoiceService(invoiceRepository, companyRepository, notificationService);
	}

	@Test
	void testGetAll() {
		// Arrange: Create a sample invoice and set up the mock repository
		Invoice invoice = new Invoice(1, 100.0, "Test Service", 10, 2023, null, new Company());
		when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

		// Act: Retrieve all invoices
		List<InvoiceResponse> invoices = invoiceService.getAll();

		// Assert: Verify that the retrieved list is as expected
		assertEquals(1, invoices.size());
		assertEquals(invoice.getInvoiceNumber(), invoices.get(0).getNumber());
		verify(invoiceRepository, times(1)).findAll(); // Ensure findAll was called once
	}

	@Test
	void testSave() {
		// Arrange: Create a DTO and a company to be returned by the mock repository
		InvoiceRegisterDto dto = new InvoiceRegisterDto(1, 100.0, "Test Service", 1L, 10, 2023, null);
		Company company = new Company(); // Mocked company entity
		company.setId(1L);
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act: Save a new invoice
		InvoiceResponse response = invoiceService.save(dto);

		// Assert: Verify that the response matches the expected values
		assertEquals(dto.getNumber(), response.getNumber());
		assertEquals(dto.getAmount(), response.getAmount());
		verify(companyRepository, times(1)).findById(1L); // Ensure findById was called once
		verify(invoiceRepository, times(1)).save(any(Invoice.class)); // Ensure save was called once
	}

	@Test
	void testSaveCompanyNotFound() {
		// Arrange: Create a DTO with a non-existent company ID
		InvoiceRegisterDto dto = new InvoiceRegisterDto(1, 100.0, "Test Service", 999L, 10, 2023, null);
		when(companyRepository.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert: Ensure a ResourceNotFoundException is thrown
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> invoiceService.save(dto));
		assertEquals("Company not found with ID: 999", exception.getMessage());
	}

	@Test
	void testUpdate() {
		// Arrange: Create a DTO and an existing invoice
		InvoiceRegisterDto dto = new InvoiceRegisterDto(1, 150.0, "Updated Service", 1L, 10, 2023, null);
		Company company = new Company(); // Mocked company entity
		company.setId(1L);

		// Mock the existing invoice
		Invoice existingInvoice = new Invoice(1, 100.0, "Test Service", 10, 2023, null, company);

		when(invoiceRepository.findById(1L)).thenReturn(Optional.of(existingInvoice));
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

		// Act: Update the existing invoice
		InvoiceResponse updatedResponse = invoiceService.update(1L, dto);

		// Assert: Verify that the updated response matches the expected values
		assertEquals(dto.getNumber(), updatedResponse.getNumber());
		assertEquals(dto.getAmount(), updatedResponse.getAmount());
		verify(invoiceRepository, times(1)).save(existingInvoice); // Ensure save was called once
	}

	@Test
	void testUpdateInvoiceNotFound() {
		// Arrange: Create a DTO for a non-existing invoice
		InvoiceRegisterDto dto = new InvoiceRegisterDto(1, 150.0, "Updated Service", 1L, 10, 2023, null);
		when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert: Ensure a ResourceNotFoundException is thrown
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> invoiceService.update(1L, dto));
		assertEquals("Invoice not found with ID: 1", exception.getMessage());
	}

	@Test
	void testUpdateCompanyNotFound() {
		// Arrange: Create a DTO and a non-existing company ID
		InvoiceRegisterDto dto = new InvoiceRegisterDto(1, 150.0, "Updated Service", 999L, 10, 2023, null);
		when(invoiceRepository.findById(1L)).thenReturn(Optional.of(new Invoice()));
		when(companyRepository.findById(999L)).thenReturn(Optional.empty());

		// Act & Assert: Ensure a ResourceNotFoundException is thrown
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> invoiceService.update(1L, dto));
		assertEquals("Company not found with ID: 999", exception.getMessage());
	}

	@Test
	void testGetInvoice() {
		// Arrange: Create a sample invoice
		Invoice invoice = new Invoice(1, 100.0, "Test Service", 10, 2023, null, new Company());
		when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

		// Act: Retrieve the invoice
		InvoiceResponse response = invoiceService.getInvoice(1L);

		// Assert: Verify the retrieved invoice is as expected
		assertEquals(invoice.getInvoiceNumber(), response.getNumber());
		verify(invoiceRepository, times(1)).findById(1L); // Ensure findById was called once
	}

	@Test
	void testGetInvoiceNotFound() {
		// Arrange: Mock the invoice repository to return empty
		when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert: Ensure a ResourceNotFoundException is thrown
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> invoiceService.getInvoice(1L));
		assertEquals("Invoice not found with ID: 1", exception.getMessage());
	}

	@Test
	void testDelete() {
		// Arrange: Create a sample invoice
		Invoice invoice = new Invoice(1, 100.0, "Test Service", 10, 2023, null, new Company());
		when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

		// Act: Delete the invoice
		invoiceService.delete(1L);

		// Assert: Verify that the invoice was deleted
		verify(invoiceRepository, times(1)).delete(invoice); // Ensure delete was called once
	}

	@Test
	void testDeleteInvoiceNotFound() {
		// Arrange: Mock the invoice repository to return empty
		when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

		// Act & Assert: Ensure a ResourceNotFoundException is thrown
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> invoiceService.delete(1L));
		assertEquals("Invoice not found with ID: 1", exception.getMessage());
	}

	@Test
	void testGetAllByCompetenceOrCompany() {
		// Arrange: Create a company and invoices
		Company company = new Company();
		company.setId(1L);
		Invoice invoice1 = new Invoice(1, 100.0, "Service 1", 10, 2023, null, company);
		Invoice invoice2 = new Invoice(2, 200.0, "Service 2", 10, 2023, null, company);
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		when(invoiceRepository.findAllByCompanyAndCompetenceYearAndCompetenceMonth(company, 2023, 10))
				.thenReturn(List.of(invoice1, invoice2));

		// Act: Retrieve invoices by company and competence
		List<InvoiceResponse> responses = invoiceService.getAllByCompetenceOrCompany(YearMonth.of(2023, 10), 1L);

		// Assert: Verify the responses match the expected invoices
		assertEquals(2, responses.size());
		verify(invoiceRepository, times(1)).findAllByCompanyAndCompetenceYearAndCompetenceMonth(company, 2023, 10);
	}

	@Test
	void testGetAllByCompetenceOrCompanyBothNull() {
		// Act & Assert: Ensure an IllegalArgumentException is thrown
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> invoiceService.getAllByCompetenceOrCompany(null, null));
		assertEquals("At least one filter must be provided: competence or companyId.", exception.getMessage());
	}

}