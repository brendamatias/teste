package br.com.bsdev.evibbra.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.bsdev.evibbra.entities.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
	
	List<Company> findAll();
}
