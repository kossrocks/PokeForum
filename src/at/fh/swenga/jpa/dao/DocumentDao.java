package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.DocumentModel;

@Repository
@Transactional
public class DocumentDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(DocumentModel document) {
		entityManager.persist(document);
	}

}
