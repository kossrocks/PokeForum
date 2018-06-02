package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.EntryModel;

@Repository
@Transactional
public class EntryDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(EntryModel entry) {
		entityManager.persist(entry);
	}

}
