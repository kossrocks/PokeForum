package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.SpeciesModel;

@Repository
@Transactional
public class SpeciesDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(SpeciesModel species) {
		entityManager.persist(species);
	}

}
