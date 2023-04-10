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

import br.com.bsdev.evibbra.controllers.response.ExpenseResponse;
import br.com.bsdev.evibbra.dtos.ExpenseRegisterDto;
import br.com.bsdev.evibbra.services.ExpenseService;

@RestController // Indicates that this class is a REST controller
public class ExpenseController implements BaseController {

	public static final String PATH = "expenses"; // The path for this controller

	private final ExpenseService expenseService;

	// Constructor-based dependency injection for ExpenseService
	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	// GET endpoint to retrieve all expenses
	@GetMapping(PATH)
	public ResponseEntity<List<ExpenseResponse>> getAllExpenses(
			@RequestParam(required = false, name = "competence") YearMonth competence,
			@RequestParam(required = false, name = "category") Long category) {
		List<ExpenseResponse> expenses;
		if (competence != null || category != null) {
			expenses = expenseService.getAllByCompetenceOrCategory(competence, category);
		} else {
			expenses = expenseService.getAll();
		}

		return ResponseEntity.ok(expenses); // Returns HTTP 200 (OK) with the list of expenses
	}

	// GET endpoint to retrieve expense
	@GetMapping(PATH + "/{id}")
	public ResponseEntity<ExpenseResponse> findExpenseById(@PathVariable(name = "id") Long id) {
		var expense = expenseService.getExpense(id);
		return ResponseEntity.ok(expense); // Returns HTTP 200 (OK) with the expense
	}

	// POST endpoint to save a new expense
	@PostMapping(PATH)
	public ResponseEntity<ExpenseResponse> saveExpense(@RequestBody ExpenseRegisterDto dto) {
		var savedExpense = expenseService.save(dto);
		return ResponseEntity.ok(savedExpense); // Return HTTP 200 OK with the saved expense data
	}

	// PUT endpoint to update an existing expense by its ID
	@PutMapping(PATH + "/{id}")
	public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable(name = "id") Long id,
			@RequestBody ExpenseRegisterDto dto) {
		var updatedExpense = expenseService.update(id, dto);
		return ResponseEntity.ok(updatedExpense); // Return HTTP 200 OK with the updated expense data
	}

	// DELETE endpoint to update an existing expense by its ID
	@DeleteMapping(PATH + "/{id}")
	public ResponseEntity<Object> deleteExpense(@PathVariable(name = "id") Long id) {
		expenseService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Expense deleted successfully")); // Returns HTTP
																										// 200 (OK)
	}
}