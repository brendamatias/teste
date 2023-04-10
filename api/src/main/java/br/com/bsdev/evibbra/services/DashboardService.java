package br.com.bsdev.evibbra.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.controllers.response.AvailableAmountResponse;
import br.com.bsdev.evibbra.controllers.response.BalanceResponse;
import br.com.bsdev.evibbra.controllers.response.CategoryExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceInvoiceResponse;
import br.com.bsdev.evibbra.repositories.ExpenseRepository;
import br.com.bsdev.evibbra.repositories.InvoiceRepository;

@Service
public class DashboardService {

	private final InvoiceRepository invoiceRepository; // Repository for invoice data
	private final PreferenceService preferenceService; // Service for fetching preferences
	private final ExpenseRepository expenseRepository; // Repository for expense data

	// Constructor-based dependency injection for the necessary repositories and
	// services
	public DashboardService(InvoiceRepository invoiceRepository, PreferenceService preferenceService,
			ExpenseRepository expenseRepository) {
		this.invoiceRepository = invoiceRepository;
		this.preferenceService = preferenceService;
		this.expenseRepository = expenseRepository;
	}

	// Method to calculate the available invoice amount for a company
	public AvailableAmountResponse getAvailableInvoiceAmountByCompetenceYear(Integer year) {
		// Retrieve the total sum of invoices issued by the company
		Double totalInvoiceAmount = invoiceRepository.sumAmountByCompetenceYear(year);

		// Retrieve the company's annual invoice limit from the preferences
		Double annualInvoiceLimit = preferenceService.getPreference().getAnnualInvoiceLimit();

		// Return the response object containing both the total invoices amount and the
		// annual invoice limit
		return new AvailableAmountResponse(totalInvoiceAmount, annualInvoiceLimit);
	}

	// Method to get the total invoices by competence year
	public List<CompetenceInvoiceResponse> getTotalAmountByCompetenceYear(Integer competenceYear) {
		// Retrieve total invoices for the specified year
		List<CompetenceInvoiceResponse> invoices = invoiceRepository.findTotalAmountByCompetenceYear(competenceYear);

		// Check if invoices list is empty and handle accordingly
		if (invoices.isEmpty()) {
			return createEmptyCompetenceInvoiceList(competenceYear);
		}

		// Create a map for easy access to total invoices by month
		Map<Integer, Double> invoiceMap = invoices.stream().collect(Collectors
				.toMap(CompetenceInvoiceResponse::getCompetenceMonth, CompetenceInvoiceResponse::getTotalAmount));

		// Create the full list of invoices for each month
		return createFullInvoiceList(competenceYear, invoiceMap);
	}

	// Method to create a full invoice list based on the invoice map
	private List<CompetenceInvoiceResponse> createFullInvoiceList(Integer competenceYear,
			Map<Integer, Double> invoiceMap) {
		List<CompetenceInvoiceResponse> fullInvoiceList = new ArrayList<>();

		// Loop through each month from January (1) to December (12)
		for (int month = 1; month <= 12; month++) {
			// Get the total invoice for the month or default to 0 if no invoice
			Double totalInvoice = invoiceMap.getOrDefault(month, 0.0);
			fullInvoiceList.add(new CompetenceInvoiceResponse(competenceYear, month, totalInvoice));
		}

		// Sort the list by month
		fullInvoiceList.sort(Comparator.comparingInt(CompetenceInvoiceResponse::getCompetenceMonth));
		return fullInvoiceList;
	}

	// Method to handle the scenario when there are no invoices
	private List<CompetenceInvoiceResponse> createEmptyCompetenceInvoiceList(Integer competenceYear) {
		List<CompetenceInvoiceResponse> emptyList = new ArrayList<>();

		// Create a list with zero invoices for each month
		for (int month = 1; month <= 12; month++) {
			emptyList.add(new CompetenceInvoiceResponse(competenceYear, month, 0.0));
		}

		return emptyList;
	}

	// Method to get the total expenses by competence year
	public List<CompetenceExpenseResponse> getTotalExpenseAmountByCompetenceYear(Integer competenceYear) {
		// Retrieve total expenses for the specified year
		List<CompetenceExpenseResponse> expenses = expenseRepository.findTotalAmountByCompetenceYear(competenceYear);

		// Check if expenses list is empty and handle accordingly
		if (expenses.isEmpty()) {
			return createEmptyCompetenceExpenseList(competenceYear);
		}

		// Create a map for easy access to total expenses by month
		Map<Integer, Double> expenseMap = expenses.stream().collect(Collectors
				.toMap(CompetenceExpenseResponse::getCompetenceMonth, CompetenceExpenseResponse::getTotalAmount));

		// Create the full list of expenses for each month
		return createFullExpenseList(competenceYear, expenseMap);
	}

	// Method to create a full expense list based on the expense map
	private List<CompetenceExpenseResponse> createFullExpenseList(Integer competenceYear,
			Map<Integer, Double> expenseMap) {
		List<CompetenceExpenseResponse> fullExpenseList = new ArrayList<>();

		// Loop through each month from January (1) to December (12)
		for (int month = 1; month <= 12; month++) {
			// Get the total expense for the month or default to 0 if no expense
			Double totalExpense = expenseMap.getOrDefault(month, 0.0);
			fullExpenseList.add(new CompetenceExpenseResponse(competenceYear, month, totalExpense));
		}

		// Sort the list by month
		fullExpenseList.sort(Comparator.comparingInt(CompetenceExpenseResponse::getCompetenceMonth));
		return fullExpenseList;
	}

	// Method to handle the scenario when there are no expenses
	private List<CompetenceExpenseResponse> createEmptyCompetenceExpenseList(Integer competenceYear) {
		List<CompetenceExpenseResponse> emptyList = new ArrayList<>();

		// Create a list with zero expenses for each month
		for (int month = 1; month <= 12; month++) {
			emptyList.add(new CompetenceExpenseResponse(competenceYear, month, 0.0));
		}

		return emptyList;
	}

	// Method to get the monthly balance by competence year
	public List<BalanceResponse> getMonthlyBalance(Integer competenceYear) {
		// Get the total invoice amounts by competence year
		List<CompetenceInvoiceResponse> invoices = invoiceRepository.findTotalAmountByCompetenceYear(competenceYear);

		// Get the total expense amounts by competence year
		List<CompetenceExpenseResponse> expenses = expenseRepository.findTotalAmountByCompetenceYear(competenceYear);

		// Create a map for easy access to expense totals by month
		Map<Integer, Double> expenseMap = expenses.stream().collect(Collectors
				.toMap(CompetenceExpenseResponse::getCompetenceMonth, CompetenceExpenseResponse::getTotalAmount));

		// Create a map for easy access to invoice totals by month
		Map<Integer, Double> invoiceMap = invoices.stream().collect(Collectors
				.toMap(CompetenceInvoiceResponse::getCompetenceMonth, CompetenceInvoiceResponse::getTotalAmount));

		// Create the final balance response list for each month of the year
		List<BalanceResponse> balanceResponses = new ArrayList<>();

		// Loop through each month from January (1) to December (12)
		for (int month = 1; month <= 12; month++) {
			// Get the total expense for the month or default to 0
			Double totalExpense = expenseMap.getOrDefault(month, 0.0);
			// Get the total invoice for the month or default to 0
			Double totalInvoice = invoiceMap.getOrDefault(month, 0.0);

			// Create and add the balance response for the month
			balanceResponses.add(new BalanceResponse(competenceYear, month, totalExpense, totalInvoice));
		}

		// Sort the balance responses by month
		balanceResponses.sort(Comparator.comparingInt(BalanceResponse::getCompetenceMonth));
		return balanceResponses;
	}

	// Method to get total expenses by category for a given competence year
	public List<CategoryExpenseResponse> getTotalExpenseByCategoryAndCompetenceYear(Integer competenceYear) {
		// Retrieve total expenses grouped by category for the specified year
		List<CategoryExpenseResponse> expensesByCategory = expenseRepository
				.findTotalAmountByCategoryAndCompetenceYear(competenceYear);

		// If the list is empty, you can return an empty list or handle it accordingly
		if (expensesByCategory.isEmpty()) {
			return new ArrayList<>();
		}

		// Return the list of expenses grouped by category
		return expensesByCategory;
	}
}