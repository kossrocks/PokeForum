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
	
	public void merge(DocumentModel document) {
		entityManager.merge(document);
	}
	
	public int deleteById(int id) {
		int count = entityManager.createQuery("DELETE FROM DocumentModel WHERE id = :id").setParameter("id", id).executeUpdate();
		return count;
	}
}
