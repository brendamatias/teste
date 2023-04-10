package br.com.bsdev.evibbra.controllers;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bsdev.evibbra.controllers.response.InvoiceResponse;
import br.com.bsdev.evibbra.dtos.InvoiceRegisterDto;
import br.com.bsdev.evibbra.services.InvoiceService;

@RestController // Indicates that this class is a REST controller in Spring
public class InvoiceController implements BaseController {

	public static final String PATH = "invoices"; // The path for this controller

	private final InvoiceService invoiceService;

	// Constructor-based dependency injection for InvoiceService
	public InvoiceController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

	// GET endpoint to retrieve all invoices
	@GetMapping(PATH)
	public ResponseEntity<List<InvoiceResponse>> getAllInvoices(
			@RequestParam(required = false, name = "competence") YearMonth competence,
			@RequestParam(required = false, name = "company") Long company) {
		List<InvoiceResponse> invoices;
		if (competence != null || company != null) {
			invoices = invoiceService.getAllByCompetenceOrCompany(competence, company);
		} else {
			invoices = invoiceService.getAll();
		}

		return ResponseEntity.ok(invoices); // Returns HTTP 200 (OK) with the list of invoices
	}

	// GET endpoint to retrieve invoice
	@GetMapping(PATH + "/{id}")
	public ResponseEntity<InvoiceResponse> findInvoiceById(@PathVariable(name = "id") Long id) {
		var invoice = invoiceService.getInvoice(id);
		return ResponseEntity.ok(invoice); // Returns HTTP 200 (OK) with the invoice
	}

	// POST endpoint to save a new invoice
	@PostMapping(PATH)
	public ResponseEntity<InvoiceResponse> saveInvoice(@RequestBody InvoiceRegisterDto dto) {
		var savedInvoice = invoiceService.save(dto);
		return ResponseEntity.ok(savedInvoice); // Returns HTTP 200 (OK) with the saved invoice data
	}

	// PUT endpoint to update an existing invoice by its ID
	@PutMapping(PATH + "/{id}")
	public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable(name = "id") Long id,
			@RequestBody InvoiceRegisterDto dto) {
		var updatedInvoice = invoiceService.update(id, dto);
		return ResponseEntity.ok(updatedInvoice); // Returns HTTP 200 (OK) with the updated invoice data
	}

	// DELETE endpoint to update an existing invoice by its ID
	@DeleteMapping(PATH + "/{id}")
	public ResponseEntity<Object> deleteInvoice(@PathVariable(name = "id") Long id) {
		invoiceService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Invoice deleted successfully")); // Returns HTTP
																										// 200 (OK)
	}
}
