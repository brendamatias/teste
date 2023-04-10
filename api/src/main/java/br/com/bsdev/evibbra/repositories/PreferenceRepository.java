package br.com.bsdev.evibbra.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.bsdev.evibbra.entities.Preference;

@Repository
public interface PreferenceRepository extends CrudRepository<Preference, Long> {
}
