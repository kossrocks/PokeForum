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
	
	public List<SpeciesModel> searchSpecies(String searchString) {
		TypedQuery<SpeciesModel> typedQuery = entityManager.createQuery( 
				/*
				"select p from SpeciesModel p where p.name like :search or p.types.get(0) | p.types.name like :search order by p.id",
				SpeciesModel.class);
				*/
				
				"select p from SpeciesModel p where p.types.name like :search order by p.id",
				SpeciesModel.class);
				
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<SpeciesModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

}
