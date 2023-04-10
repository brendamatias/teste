package br.com.bsdev.evibbra.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.bsdev.evibbra.controllers.response.ExpenseResponse;
import br.com.bsdev.evibbra.dtos.ExpenseRegisterDto;
import br.com.bsdev.evibbra.entities.Category;
import br.com.bsdev.evibbra.entities.Company;
import br.com.bsdev.evibbra.entities.Expense;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;
import br.com.bsdev.evibbra.repositories.CategoryRepository;
import br.com.bsdev.evibbra.repositories.CompanyRepository;
import br.com.bsdev.evibbra.repositories.ExpenseRepository;

class ExpenseServiceTest {

	@Mock
	private ExpenseRepository expenseRepository;

	@Mock
	private CompanyRepository companyRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private ExpenseService expenseService;

	private Expense expense;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Create a sample Expense object for the tests
		expense = new Expense("Test Expense", 100.0, LocalDate.now(), 10, 2024, null, null);
		expense.setId(1L); // Set an ID for the Expense
	}

	@Test
	void testGetAll() {
		// Arrange: Create a mock expense
		Expense expense = new Expense("Expense 1", 100.0, LocalDate.now(), 1, 2023, new Category(), new Company());
		when(expenseRepository.findAll()).thenReturn(List.of(expense));

		// Act: Retrieve all expenses
		List<ExpenseResponse> expenses = expenseService.getAll();

		// Assert: Verify the response
		assertEquals(1, expenses.size());
		assertEquals(expense.getExpenseName(), expenses.get(0).getExpenseName());
	}

	@Test
	void testSave() {
		// Arrange: Create a DTO for a new expense
		ExpenseRegisterDto dto = new ExpenseRegisterDto("New Expense", 200.0, LocalDate.now(), 1, 2023, 1L, 1L);

		// Create a mock company and category
		Company company = new Company();
		company.setId(1L);
		company.setName("Test Company");

		Category category = new Category();
		category.setId(1L);
		category.setName("Test Category");

		// Mock the behavior of repositories
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

		// Create the expense to be saved
		Expense expenseToSave = new Expense("New Expense", 200.0, LocalDate.now(), 1, 2023, category, company);

		// Mock the save operation
		when(expenseRepository.save(any(Expense.class))).thenReturn(expenseToSave);

		// Act: Save the new expense
		ExpenseResponse savedExpense = expenseService.save(dto);

		// Assert: Verify the saved expense details
		assertEquals("New Expense", savedExpense.getExpenseName());
		assertEquals(200.0, savedExpense.getAmount());
		assertEquals(company.getId(), savedExpense.getCompany().getId());
		assertEquals(category.getId(), savedExpense.getCategory().getId());
	}

	@Test
	void testUpdateExpenseNotFound() {
		// Arrange: Create a DTO for updating an expense
		ExpenseRegisterDto dto = new ExpenseRegisterDto("Updated Expense", 250.0, LocalDate.now(), 1, 2023, 1L, 1L);

		// Mock the behavior of the repository to return an empty Optional
		when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

		// Act and Assert: Verify that the exception is thrown
		assertThrows(ResourceNotFoundException.class, () -> expenseService.update(1L, dto));
	}

	@Test
	void testSaveCategoryNotFound() {
		// Arrange: Create a DTO for a new expense
		ExpenseRegisterDto dto = new ExpenseRegisterDto("New Expense", 200.0, LocalDate.now(), 1, 2023, 1L, 1L);

		// Mock the behavior of the repositories
		when(companyRepository.findById(1L)).thenReturn(Optional.of(new Company()));
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		// Act and Assert: Verify that the exception is thrown
		assertThrows(ResourceNotFoundException.class, () -> expenseService.save(dto));
	}

	@Test
	void testSaveCompanyNotFound() {
		// Arrange: Create a DTO for a new expense
		ExpenseRegisterDto dto = new ExpenseRegisterDto("New Expense", 200.0, LocalDate.now(), 1, 2023, null, 1L);

		// Mock the behavior of the repositories
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
		when(companyRepository.findById(1L)).thenReturn(Optional.empty());

		// Act and Assert: Verify that the exception is thrown
		assertThrows(ResourceNotFoundException.class, () -> expenseService.save(dto));
	}

	@Test
	void testGetExpenseNotFound() {
		// Arrange: Mock the repository to return an empty Optional for a non-existent
		// expense
		when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

		// Act and Assert: Verify that the exception is thrown when retrieving a
		// non-existent expense
		assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpense(1L));
	}

	@Test
	void testUpdateExpense() {
		// Arrange: Create a DTO for updating an expense
		ExpenseRegisterDto dto = new ExpenseRegisterDto("Updated Expense", 250.0, LocalDate.now(), 1, 2023, 1L, 1L);

		// Mock the behavior of the repository to return an existing expense
		when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

		// Create a mock company and category for the updated expense
		Company company = new Company();
		company.setId(1L);
		company.setName("Test Company");

		Category category = new Category();
		category.setId(1L);
		category.setName("Test Category");

		// Mock the behavior of repositories
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

		// Mock the save operation
		when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

		// Act: Update the existing expense
		ExpenseResponse updatedExpense = expenseService.update(1L, dto);

		// Assert: Verify the updated expense details
		assertEquals("Updated Expense", updatedExpense.getExpenseName());
		assertEquals(250.0, updatedExpense.getAmount());
		assertEquals(company.getId(), updatedExpense.getCompany().getId());
		assertEquals(category.getId(), updatedExpense.getCategory().getId());
	}
}
