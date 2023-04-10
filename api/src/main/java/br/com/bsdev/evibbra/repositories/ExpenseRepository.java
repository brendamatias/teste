package br.com.bsdev.evibbra.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.bsdev.evibbra.controllers.response.CategoryExpenseResponse;
import br.com.bsdev.evibbra.controllers.response.CompetenceExpenseResponse;
import br.com.bsdev.evibbra.entities.Category;
import br.com.bsdev.evibbra.entities.Expense;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

	List<Expense> findAll();

	// Query to sum the expense amounts by competence year and month
	@Query(name = "Expense.findTotalAmountByCompetenceYear", nativeQuery = true)
	List<CompetenceExpenseResponse> findTotalAmountByCompetenceYear(@Param("competenceYear") Integer competenceYear);

	@Query(name = "Expense.findTotalAmountByCategoryAndCompetenceYear", nativeQuery = true)
	List<CategoryExpenseResponse> findTotalAmountByCategoryAndCompetenceYear(
			@Param("competenceYear") Integer competenceYear);

	List<Expense> findAllByCompetenceYearAndCompetenceMonth(Integer competenceYear, Integer competenceMonth);

	List<Expense> findAllByCategory(Category category);

	List<Expense> findAllByCategoryAndCompetenceYearAndCompetenceMonth(Category category, Integer competenceYear,
			Integer competenceMonth);
}
