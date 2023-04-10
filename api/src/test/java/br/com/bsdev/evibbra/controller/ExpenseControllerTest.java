package br.com.bsdev.evibbra.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import br.com.bsdev.evibbra.controllers.ExpenseController;
import br.com.bsdev.evibbra.controllers.response.ExpenseResponse;
import br.com.bsdev.evibbra.dtos.ExpenseRegisterDto;
import br.com.bsdev.evibbra.services.ExpenseService;

class ExpenseControllerTest {

	private MockMvc mockMvc; // MockMvc instance to simulate HTTP requests and responses

	@Mock
	private ExpenseService expenseService; // Mocking the ExpenseService to control responses during tests

	@InjectMocks
	private ExpenseController expenseController; // The controller under test with the mocked service

	// Set up method to initialize mocks and configure MockMvc before each test
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initialize mocks
		mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build(); // Set up MockMvc with the controller
	}

	// Test case for the GET request to retrieve all expenses
	@Test
	void testGetAllExpenses() throws Exception {
		// Mock response data
		List<ExpenseResponse> mockExpenses = Arrays.asList(
				new ExpenseResponse(1L, "Expense 1", 500.00, LocalDate.now(), 1, 2023, null, null),
				new ExpenseResponse(2L, "Expense 2", 700.00, LocalDate.now(), 2, 2023, null, null));

		// Define the behavior of the mocked service
		when(expenseService.getAll()).thenReturn(mockExpenses);

		// Perform the GET request and verify the response
		mockMvc.perform(get("/api/v1/expenses").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()) // Expect
																													// HTTP
																													// status
																													// 200
																													// OK
				.andExpect(jsonPath("$[0].id").value(1L)) // Verify the first expense ID
				.andExpect(jsonPath("$[0].expenseName").value("Expense 1")) // Verify the first expense name
				.andExpect(jsonPath("$[0].amount").value(500.00)) // Verify the first expense amount
				.andExpect(jsonPath("$[1].id").value(2L)) // Verify the second expense ID
				.andExpect(jsonPath("$[1].expenseName").value("Expense 2")) // Verify the second expense name
				.andExpect(jsonPath("$[1].amount").value(700.00)); // Verify the second expense amount
	}

	// Test case for the POST request to save a new expense
	@Test
	void testSaveExpense() throws Exception {
		// Mock response after saving
		ExpenseResponse savedExpense = new ExpenseResponse(3L, "Expense 3", 900.00, LocalDate.now(), 3, 2023, null,
				null);

		// Define the behavior of the mocked service
		when(expenseService.save(any(ExpenseRegisterDto.class))).thenReturn(savedExpense);

		// Prepare the JSON representation of the new expense
		String expenseJson = "{\"expenseName\":\"Expense 3\", \"amount\":900.00, \"paymentDate\":\"2024-10-06\", \"competenceMonth\":3, \"competenceYear\":2023, \"categoryId\":null, \"companyId\":null}";

		// Perform the POST request and verify the response
		mockMvc.perform(post("/api/v1/expenses").contentType(MediaType.APPLICATION_JSON).content(expenseJson))
				.andExpect(status().isOk()) // Expect HTTP status 200 OK
				.andExpect(jsonPath("$.id").value(3L)) // Verify the saved expense ID
				.andExpect(jsonPath("$.expenseName").value("Expense 3")) // Verify the saved expense name
				.andExpect(jsonPath("$.amount").value(900.00)); // Verify the saved expense amount
	}

	// Test case for the PUT request to update an existing expense
	@Test
	void testUpdateExpense() throws Exception {
		// Mock response after update
		ExpenseResponse updatedExpense = new ExpenseResponse(3L, "Updated Expense 3", 1000.00, LocalDate.now(), 4, 2023,
				null, null);

		// Define the behavior of the mocked service
		when(expenseService.update(anyLong(), any(ExpenseRegisterDto.class))).thenReturn(updatedExpense);

		// Prepare the JSON representation of the updated expense
		String updateExpenseJson = "{\"expenseName\":\"Updated Expense 3\", \"amount\":1000.00, \"paymentDate\":\"2024-10-06\", \"competenceMonth\":4, \"competenceYear\":2023, \"categoryId\":null, \"companyId\":null}";

		// Perform the PUT request and verify the response
		mockMvc.perform(put("/api/v1/expenses/3").contentType(MediaType.APPLICATION_JSON).content(updateExpenseJson))
				.andExpect(status().isOk()) // Expect HTTP status 200 OK
				.andExpect(jsonPath("$.id").value(3L)) // Verify the updated expense ID
				.andExpect(jsonPath("$.expenseName").value("Updated Expense 3")) // Verify the updated expense name
				.andExpect(jsonPath("$.amount").value(1000.00)); // Verify the updated expense amount
	}
}