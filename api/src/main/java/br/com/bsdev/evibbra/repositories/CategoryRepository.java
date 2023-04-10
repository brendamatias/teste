package br.com.bsdev.evibbra.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.bsdev.evibbra.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	@Query("FROM Category WHERE status = 'active'")
	List<Category> findAllActive();
	
	List<Category> findAll();
}
