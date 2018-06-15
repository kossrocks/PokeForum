package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
	public List<PokemonModel> getAllPokemons() {
		TypedQuery<PokemonModel> typedQuery = entityManager.createQuery("select e from PokemonModel e order by e.id",
				PokemonModel.class);
		List<PokemonModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}


}
