package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.bsdev.evibbra.controllers.response.AvailableAmountResponse;
import br.com.bsdev.evibbra.controllers.response.BalanceResponse;
import br.com.bsdev.evibbra.controllers.response.CategoryExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceInvoiceResponse;
import br.com.bsdev.evibbra.entities.Preference;
import br.com.bsdev.evibbra.repositories.ExpenseRepository;
import br.com.bsdev.evibbra.repositories.InvoiceRepository;

class DashboardServiceTest {

	@Mock
	private InvoiceRepository invoiceRepository; // Mocked invoice repository

	@Mock
	private PreferenceService preferenceService; // Mocked preference service

	@Mock
	private ExpenseRepository expenseRepository; // Mocked expense repository

	@InjectMocks
	private DashboardService dashboardService; // Service under test

	@BeforeEach
	void setUp() {
		// Initialize mocks
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAvailableInvoiceAmountByCompetenceYear() {
		// Given
		Integer year = 2024;
		when(invoiceRepository.sumAmountByCompetenceYear(year)).thenReturn(1000.0);
		when(preferenceService.getPreference()).thenReturn(new Preference(5000.0, true, false));

		// When
		AvailableAmountResponse response = dashboardService.getAvailableInvoiceAmountByCompetenceYear(year);

		// Then
		assertNotNull(response);
		assertEquals(1000.0, response.getTotalInvoiceAmount());
		assertEquals(5000.0, response.getAnnualInvoiceLimit());
	}

	@Test
	void testGetTotalAmountByCompetenceYear_WithInvoices() {
		// Given
		Integer year = 2024;
		CompetenceInvoiceResponse invoice1 = new CompetenceInvoiceResponse(year, 1, 200.0);
		CompetenceInvoiceResponse invoice2 = new CompetenceInvoiceResponse(year, 2, 300.0);
		when(invoiceRepository.findTotalAmountByCompetenceYear(year)).thenReturn(Arrays.asList(invoice1, invoice2));

		// When
		List<CompetenceInvoiceResponse> invoices = dashboardService.getTotalAmountByCompetenceYear(year);

		// Then
		assertNotNull(invoices);
		assertEquals(12, invoices.size());
		assertEquals(200.0, invoices.get(0).getTotalAmount());
		assertEquals(300.0, invoices.get(1).getTotalAmount());
	}

	@Test
	void testGetTotalAmountByCompetenceYear_EmptyInvoices() {
		// Given
		Integer year = 2024;
		when(invoiceRepository.findTotalAmountByCompetenceYear(year)).thenReturn(Collections.emptyList());

		// When
		List<CompetenceInvoiceResponse> invoices = dashboardService.getTotalAmountByCompetenceYear(year);

		// Then
		assertNotNull(invoices);
		assertEquals(12, invoices.size()); // Should return 12 months with 0 amounts
		for (CompetenceInvoiceResponse invoice : invoices) {
			assertEquals(0.0, invoice.getTotalAmount());
		}
	}

	@Test
	void testGetTotalExpenseAmountByCompetenceYear_WithExpenses() {
		// Given
		Integer year = 2024;
		CompetenceExpenseResponse expense1 = new CompetenceExpenseResponse(year, 1, 150.0);
		CompetenceExpenseResponse expense2 = new CompetenceExpenseResponse(year, 2, 250.0);
		when(expenseRepository.findTotalAmountByCompetenceYear(year)).thenReturn(Arrays.asList(expense1, expense2));

		// When
		List<CompetenceExpenseResponse> expenses = dashboardService.getTotalExpenseAmountByCompetenceYear(year);

		// Then
		assertNotNull(expenses);
		assertEquals(12, expenses.size());
		assertEquals(150.0, expenses.get(0).getTotalAmount());
		assertEquals(250.0, expenses.get(1).getTotalAmount());
	}

	@Test
	void testGetTotalExpenseAmountByCompetenceYear_EmptyExpenses() {
		// Given
		Integer year = 2024;
		when(expenseRepository.findTotalAmountByCompetenceYear(year)).thenReturn(Collections.emptyList());

		// When
		List<CompetenceExpenseResponse> expenses = dashboardService.getTotalExpenseAmountByCompetenceYear(year);

		// Then
		assertNotNull(expenses);
		assertEquals(12, expenses.size()); // Should return 12 months with 0 amounts
		for (CompetenceExpenseResponse expense : expenses) {
			assertEquals(0.0, expense.getTotalAmount());
		}
	}

	@Test
	void testGetMonthlyBalance() {
		// Given
		Integer year = 2024;
		CompetenceInvoiceResponse invoice1 = new CompetenceInvoiceResponse(year, 1, 500.0);
		CompetenceInvoiceResponse invoice2 = new CompetenceInvoiceResponse(year, 2, 300.0);
		CompetenceExpenseResponse expense1 = new CompetenceExpenseResponse(year, 1, 200.0);
		CompetenceExpenseResponse expense2 = new CompetenceExpenseResponse(year, 2, 100.0);

		when(invoiceRepository.findTotalAmountByCompetenceYear(year)).thenReturn(Arrays.asList(invoice1, invoice2));
		when(expenseRepository.findTotalAmountByCompetenceYear(year)).thenReturn(Arrays.asList(expense1, expense2));

		// When
		List<BalanceResponse> balances = dashboardService.getMonthlyBalance(year);

		// Then
		assertNotNull(balances);
		assertEquals(12, balances.size());
		assertEquals(200.0, balances.get(0).getTotalAmountExpense()); // Month 1
		assertEquals(500.0, balances.get(0).getTotalAmountInvoice());
		assertEquals(100.0, balances.get(1).getTotalAmountExpense()); // Month 2
		assertEquals(300.0, balances.get(1).getTotalAmountInvoice());
	}

	@Test
	void testGetTotalExpenseByCategoryAndCompetenceYear() {
		// Given
		Integer year = 2024;
		CategoryExpenseResponse categoryExpense = new CategoryExpenseResponse("Utilities", 400.0);
		when(expenseRepository.findTotalAmountByCategoryAndCompetenceYear(year))
				.thenReturn(Arrays.asList(categoryExpense));

		// When
		List<CategoryExpenseResponse> expensesByCategory = dashboardService
				.getTotalExpenseByCategoryAndCompetenceYear(year);

		// Then
		assertNotNull(expensesByCategory);
		assertEquals(1, expensesByCategory.size());
		assertEquals("Utilities", expensesByCategory.get(0).getCategory());
		assertEquals(400.0, expensesByCategory.get(0).getTotalAmount());
	}

	@Test
	void testGetExpensesByCategory_EmptyList_ReturnsEmptyList() {
		// Given
		Integer year = 2024;
		when(expenseRepository.findTotalAmountByCategoryAndCompetenceYear(year)).thenReturn(Collections.emptyList());
		List<CategoryExpenseResponse> result = dashboardService.getTotalExpenseByCategoryAndCompetenceYear(year);
		assertEquals(0, result.size(), "Expected an empty list when there are no expenses.");
	}
}