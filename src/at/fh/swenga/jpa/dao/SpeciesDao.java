package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Secured({ "ROLE_ADMIN" })
	public void merge(SpeciesModel species) {
		entityManager.merge(species);
	}

	public List<SpeciesModel> searchSpecies(String searchString) {
		TypedQuery<SpeciesModel> typedQuery = entityManager.createQuery(
				/*
				 * "select p from SpeciesModel p where p.name like :search or p.types.get(0) | p.types.name like :search order by p.id"
				 * , SpeciesModel.class);
				 */

				"select p from SpeciesModel p where p.types.name like :search order by p.id", SpeciesModel.class);

		typedQuery.setParameter("search", "%" + searchString + "%");
		List<SpeciesModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public SpeciesModel searchSpeciesByName(String name) {
		try {
			TypedQuery<SpeciesModel> typedQuery = entityManager
					.createQuery("select u from SpeciesModel u where u.name = :name", SpeciesModel.class);
			typedQuery.setParameter("name", name);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public SpeciesModel searchSpeciesById(int id) {
		try {
			TypedQuery<SpeciesModel> typedQuery = entityManager
					.createQuery("select u from SpeciesModel u where u.id = :name", SpeciesModel.class);
			typedQuery.setParameter("name", id);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Secured({ "ROLE_ADMIN" })
	public int deleteById(int id) {
		int count = entityManager.createQuery("DELETE FROM SpeciesModel s WHERE s.id = :id").setParameter("id", id)
				.executeUpdate();
		return count;
	}
}
