package br.com.bsdev.evibbra.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.bsdev.evibbra.controllers.InvoiceController;
import br.com.bsdev.evibbra.controllers.response.InvoiceResponse;
import br.com.bsdev.evibbra.dtos.InvoiceRegisterDto;
import br.com.bsdev.evibbra.services.InvoiceService;

class InvoiceControllerTest {

	private MockMvc mockMvc; // MockMvc to simulate HTTP requests and responses

	@Mock
	private InvoiceService invoiceService; // Mocking the InvoiceService to control the responses during tests

	@InjectMocks
	private InvoiceController invoiceController; // Controller under test with the mocked service

	// Set up method to initialize mocks and configure MockMvc before each test
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize mock objects
		mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build(); // Set up MockMvc with the controller
	}

	// Test case for the GET request to retrieve all invoices
	@Test
	void testGetAllInvoices() throws Exception {
		// Mock response data
		List<InvoiceResponse> mockInvoices = Arrays.asList(
				new InvoiceResponse(1L, 1001, 500.00, "Service 1", 1, 2023, null, null),
				new InvoiceResponse(2L, 1002, 700.00, "Service 2", 2, 2023, null, null));

		// Mock the service to return the mock invoice list when getAll() is called
		when(invoiceService.getAll()).thenReturn(mockInvoices);

		// Perform a GET request and verify the response
		mockMvc.perform(get("/api/v1/invoices").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()) // Expecting
																													// HTTP
																													// 200
																													// OK
				.andExpect(jsonPath("$[0].id").value(1L)) // Verify the first invoice ID
				.andExpect(jsonPath("$[0].number").value(1001)) // Verify the first invoice number
				.andExpect(jsonPath("$[0].amount").value(500.00)) // Verify the first invoice amount
				.andExpect(jsonPath("$[1].id").value(2L)) // Verify the second invoice ID
				.andExpect(jsonPath("$[1].number").value(1002)) // Verify the second invoice number
				.andExpect(jsonPath("$[1].amount").value(700.00)); // Verify the second invoice amount
	}

	// Test case for the POST request to save a new invoice
	@Test
	void testSaveInvoice() throws Exception {
		// Mock response after saving
		InvoiceResponse savedInvoice = new InvoiceResponse(3L, 1003, 900.00, "Service 3", 3, 2023, null, null);

		// Mock the service to return the saved invoice when save() is called
		when(invoiceService.save(any(InvoiceRegisterDto.class))).thenReturn(savedInvoice);

		// JSON string to simulate the request body
		String invoiceJson = "{\"number\":1003, \"amount\":900.00, \"serviceDescription\":\"Service 3\", \"competenceMonth\":3, \"competenceYear\":2023}";

		// Perform a POST request and verify the response
		mockMvc.perform(post("/api/v1/invoices").contentType(MediaType.APPLICATION_JSON).content(invoiceJson))
				.andExpect(status().isOk()) // Expecting HTTP 200 OK
				.andExpect(jsonPath("$.id").value(3L)) // Verify the saved invoice ID
				.andExpect(jsonPath("$.number").value(1003)) // Verify the saved invoice number
				.andExpect(jsonPath("$.amount").value(900.00)); // Verify the saved invoice amount
	}

	// Test case for the PUT request to update an existing invoice
	@Test
	void testUpdateInvoice() throws Exception {
		// Mock response after update
		InvoiceResponse updatedInvoice = new InvoiceResponse(3L, 1004, 1000.00, "Updated Service 3", 4, 2023, null, null);

		// Mock the service to return the updated invoice when update() is called
		when(invoiceService.update(anyLong(), any(InvoiceRegisterDto.class))).thenReturn(updatedInvoice);

		// JSON string to simulate the request body for the update
		String updateInvoiceJson = "{\"number\":1004, \"amount\":1000.00, \"serviceDescription\":\"Updated Service 3\", \"competenceMonth\":4, \"competenceYear\":2023}";

		// Perform a PUT request and verify the response
		mockMvc.perform(put("/api/v1/invoices/3").contentType(MediaType.APPLICATION_JSON).content(updateInvoiceJson))
				.andExpect(status().isOk()) // Expecting HTTP 200 OK
				.andExpect(jsonPath("$.id").value(3L)) // Verify the updated invoice ID
				.andExpect(jsonPath("$.number").value(1004)) // Verify the updated invoice number
				.andExpect(jsonPath("$.amount").value(1000.00)); // Verify the updated invoice amount
	}
}