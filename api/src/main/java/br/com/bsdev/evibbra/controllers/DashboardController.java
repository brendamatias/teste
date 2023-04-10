package br.com.bsdev.evibbra.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.bsdev.evibbra.controllers.response.AvailableAmountResponse;
import br.com.bsdev.evibbra.controllers.response.BalanceResponse;
import br.com.bsdev.evibbra.controllers.response.CategoryExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceInvoiceResponse;
import br.com.bsdev.evibbra.services.DashboardService;

@RestController
public class DashboardController implements BaseController {

	// The base path for the Dashboard-related endpoints
	public static final String PATH = "dashboard";

	private final DashboardService dashboardService;

	// Constructor-based dependency injection for the DashboardService
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping(PATH + "/available-invoice-amount/{year}")
	public ResponseEntity<AvailableAmountResponse> getAvailableInvoiceAmountByCompetenceYear(@PathVariable(name = "year") Integer year) {
		AvailableAmountResponse response = dashboardService.getAvailableInvoiceAmountByCompetenceYear(year);

		// Validate if the response or its values are null
		if (response == null || response.getTotalInvoiceAmount() == null || response.getAnnualInvoiceLimit() == null) {
			return ResponseEntity.noContent().build(); // Return 204 No Content if data is not available
		}

		// Return the valid response if all checks pass
		return ResponseEntity.ok(response);
	}

	// Endpoint to get total invoice amounts by competence year
	@GetMapping(PATH + "/total-invoice-amounts/{year}")
	public ResponseEntity<List<CompetenceInvoiceResponse>> getTotalInvoiceAmountsByYear(@PathVariable(name = "year") Integer year) {
		List<CompetenceInvoiceResponse> response = dashboardService.getTotalAmountByCompetenceYear(year);
		return ResponseEntity.ok(response);
	}

	// Endpoint to get total expense amounts by competence year
	@GetMapping(PATH + "/total-expense-amounts/{year}")
	public ResponseEntity<List<CompetenceExpenseResponse>> getTotalExpenseAmountsByYear(@PathVariable(name = "year") Integer year) {
		List<CompetenceExpenseResponse> response = dashboardService.getTotalExpenseAmountByCompetenceYear(year);
		return ResponseEntity.ok(response);
	}

	// Endpoint to get monthly balance
	@GetMapping(PATH + "/monthly-balance/{year}")
	public ResponseEntity<List<BalanceResponse>> getMonthlyBalance(@PathVariable(name = "year") Integer year) {
		List<BalanceResponse> balance = dashboardService.getMonthlyBalance(year);
		return ResponseEntity.ok(balance);
	}

	// Endpoint to get total expenses grouped by category for a given competence year
	@GetMapping(PATH + "/expenses/category/{year}")
	public ResponseEntity<List<CategoryExpenseResponse>> getTotalExpenseByCategory(@PathVariable(name = "year") Integer year) {
		// Call the service method to get the data
		List<CategoryExpenseResponse> expensesByCategory = dashboardService
				.getTotalExpenseByCategoryAndCompetenceYear(year);

		// Return the response entity with the list and HTTP status
		return ResponseEntity.ok(expensesByCategory);
	}

}