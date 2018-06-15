package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.SpeciesModel;

@Repository
@Transactional
public class SpeciesDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public List<SpeciesModel> getAllSpecies() {
		TypedQuery<SpeciesModel> typedQuery = entityManager.createQuery("select e from SpeciesModel e order by e.id",
				SpeciesModel.class);
		List<SpeciesModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public void persist(SpeciesModel species) {
		entityManager.persist(species);
	}

}
