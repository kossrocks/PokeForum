package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.PokemonModel;

@Repository
@Transactional
public class PokemonDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public void persist(PokemonModel pokemon) {
		entityManager.persist(pokemon);
	}

	@Secured({ "ROLE_USER" })
	public void merge(PokemonModel pokemon) {
		entityManager.merge(pokemon);
	}

	public List<PokemonModel> getAllPokemons() {
		TypedQuery<PokemonModel> typedQuery = entityManager.createQuery("select e from PokemonModel e order by e.id",
				PokemonModel.class);
		List<PokemonModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<PokemonModel> getAllPokemonsOfUser(String name) {
		TypedQuery<PokemonModel> typedQuery = entityManager.createQuery(
				"select e from PokemonModel e where e.owner.userName = :id order by e.id", PokemonModel.class);
		typedQuery.setParameter("id", name);
		List<PokemonModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public PokemonModel getPokemonById(int id) {
		TypedQuery<PokemonModel> typedQuery = entityManager.createQuery("select e from PokemonModel e where e.id = :id",
				PokemonModel.class);
		typedQuery.setParameter("id", id);
		PokemonModel typedResultList = typedQuery.getSingleResult();
		return typedResultList;
	}

	@Secured({ "ROLE_USER" })
	public int deleteById(int id) {
		int count = entityManager.createQuery("DELETE FROM PokemonModel WHERE id = :id").setParameter("id", id)
				.executeUpdate();
		return count;
	}

}
