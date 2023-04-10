package br.com.bsdev.evibbra.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.bsdev.evibbra.entities.Invoice;
import br.com.bsdev.evibbra.controllers.response.CompetenceInvoiceResponse;
import br.com.bsdev.evibbra.entities.Company;


@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

	List<Invoice> findAll();

	@Query("SELECT COALESCE(SUM(i.amount), 0) FROM Invoice i WHERE i.competenceYear = :competenceYear")
	Double sumAmountByCompetenceYear(@Param("competenceYear") Integer competenceYear);
	
	// Query to sum the invoice amounts by competence year and month
	@Query(name = "Invoice.findTotalAmountByCompetenceYear", nativeQuery = true)
    List<CompetenceInvoiceResponse> findTotalAmountByCompetenceYear(@Param("competenceYear") Integer competenceYear);

	List<Invoice> findAllByCompetenceYearAndCompetenceMonth(Integer competenceYear, Integer competenceMonth);
	
	List<Invoice> findAllByCompany(Company company);
	
	List<Invoice> findAllByCompanyAndCompetenceYearAndCompetenceMonth(Company company, Integer competenceYear, Integer competenceMonth);
}
