package br.com.bsdev.evibbra.services;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.controllers.response.CategoryResponse;
import br.com.bsdev.evibbra.controllers.response.CompanyResponse;
import br.com.bsdev.evibbra.controllers.response.ExpenseResponse;
import br.com.bsdev.evibbra.dtos.ExpenseRegisterDto;
import br.com.bsdev.evibbra.entities.Category;
import br.com.bsdev.evibbra.entities.Company;
import br.com.bsdev.evibbra.entities.Expense;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CategoryRepository;
import br.com.bsdev.evibbra.repositories.CompanyRepository;
import br.com.bsdev.evibbra.repositories.ExpenseRepository;

@Service // Indicates that this class is a service in the Spring context
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final CompanyRepository companyRepository;
	private final CategoryRepository categoryRepository;

	// Constructor-based dependency injection for ExpenseRepository,
	// CompanyRepository, and CategoryRepository
	public ExpenseService(ExpenseRepository expenseRepository, CompanyRepository companyRepository,
			CategoryRepository categoryRepository) {
		this.expenseRepository = expenseRepository;
		this.companyRepository = companyRepository;
		this.categoryRepository = categoryRepository;
	}

	// Retrieve all expenses and map them to response DTOs
	public List<ExpenseResponse> getAll() {
		return expenseRepository.findAll().stream().map(this::mapToResponse) // Map each Expense entity to a response
																				// DTO
				.toList();
	}

	public List<ExpenseResponse> getAllByCompetenceOrCategory(YearMonth competence, Long categoryId) {

		// If both competence and categoryId are null, throw an exception to enforce
		// valid input
		if (categoryId == null && competence == null) {
			throw new IllegalArgumentException("At least one filter must be provided: competence or categoryId.");
		}

		// Find category if categoryId is provided, otherwise set to null
		var category = (categoryId != null) ? category(categoryId) : null;

		// Case 1: Both category and competence are provided
		if (category != null && competence != null) {
			return findExpenseByCategoryAndCompetence(category, competence);
		}
		// Case 2: Only category is provided
		else if (category != null) {
			return findExpenseByCategory(category);
		}
		// Case 3: Only competence is provided
		else {
			return findExpenseByCompetence(competence);
		}
	}

	private List<ExpenseResponse> findExpenseByCategoryAndCompetence(Category category, YearMonth competenceYearMonth) {
		// Fetch expenses by category and competence year/month, and map them to
		// ExpenseResponse DTOs
		return expenseRepository.findAllByCategoryAndCompetenceYearAndCompetenceMonth(category,
				competenceYearMonth.getYear(), competenceYearMonth.getMonthValue()).stream().map(this::mapToResponse)
				.toList();
	}

	private List<ExpenseResponse> findExpenseByCategory(Category category) {
		// Fetch expenses by category only and map them to ExpenseResponse DTOs
		return expenseRepository.findAllByCategory(category).stream().map(this::mapToResponse).toList();
	}

	private List<ExpenseResponse> findExpenseByCompetence(YearMonth competenceYearMonth) {
		// Fetch expenses by competence year/month only and map them to ExpenseResponse
		// DTOs
		return expenseRepository.findAllByCompetenceYearAndCompetenceMonth(competenceYearMonth.getYear(),
				competenceYearMonth.getMonthValue()).stream().map(this::mapToResponse).toList();
	}

	// Retrieve one expense and map them to ExpenseResponse DTOs
	public ExpenseResponse getExpense(Long id) {
		return mapToResponse(findExpenseById(id));
	}

	// Save a new expense and return the saved expense as a response DTO
	public ExpenseResponse save(ExpenseRegisterDto dto) {
		var company = dto.getCompanyId() != null ? company(dto.getCompanyId()) : null;
		var category = category(dto.getCategoryId());

		var expense = new Expense(dto.getExpenseName(), dto.getAmount(), dto.getPaymentDate(), dto.getCompetenceMonth(),
				dto.getCompetenceYear(), category, company);

		var savedExpense = expenseRepository.save(expense);
		return mapToResponse(savedExpense);
	}

	// Update an existing expense by ID
	public ExpenseResponse update(Long id, ExpenseRegisterDto dto) {
		var company = dto.getCompanyId() != null ? company(dto.getCompanyId()) : null;
		var category = category(dto.getCategoryId());

		var expense = findExpenseById(id);

		// Update the fields of the expense entity
		expense.setExpenseName(dto.getExpenseName());
		expense.setAmount(dto.getAmount());
		expense.setPaymentDate(dto.getPaymentDate());
		expense.setCompetenceMonth(dto.getCompetenceMonth());
		expense.setCompetenceYear(dto.getCompetenceYear());
		expense.setCategory(category);
		expense.setCompany(company);

		var updatedExpense = expenseRepository.save(expense);
		return mapToResponse(updatedExpense);
	}

	// Delete an existing expense by its ID
	public void delete(Long expenseId) {
		var expense = findExpenseById(expenseId);
		expenseRepository.delete(expense);
	}

	// Helper method to retrieve expense by ID
	private Expense findExpenseById(Long id) {
		return expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));
	}

	// Helper method to retrieve company by ID
	private Company company(Long id) {
		return companyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
	}

	// Helper method to retrieve category by ID
	private Category category(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
	}

	// Method to map Expense entity to ExpenseResponse DTO
	private ExpenseResponse mapToResponse(Expense expense) {
		var companyResponse = expense.getCompany() != null
				? new CompanyResponse(expense.getCompany().getId(), expense.getCompany().getName(),
						expense.getCompany().getCnpj(), expense.getCompany().getLegalName(), expense.getCompany().getInactivatedAt())
				: null;

		var categoryResponse = new CategoryResponse(expense.getCategory().getId(), expense.getCategory().getName(),
				expense.getCategory().getDescription(), expense.getCategory().getStatus());

		return new ExpenseResponse(expense.getId(), expense.getExpenseName(), expense.getAmount(),
				expense.getPaymentDate(), expense.getCompetenceMonth(), expense.getCompetenceYear(), categoryResponse,
				companyResponse);
	}
}