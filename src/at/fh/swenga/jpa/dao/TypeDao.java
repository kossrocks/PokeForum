package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.TypeModel;

@Repository
@Transactional
public class TypeDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(TypeModel type) {
		entityManager.persist(type);
	}

}
