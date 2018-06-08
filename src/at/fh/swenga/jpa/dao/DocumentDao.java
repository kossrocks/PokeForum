package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;

@Repository
@Transactional
public class DocumentDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(DocumentModel document) {
		entityManager.persist(document);
	}
	
	public void delete(DocumentModel document) {
		entityManager.remove(document);
	}
	
	
	public DocumentModel getDocumentById(int id) {
		try {
			TypedQuery<DocumentModel> typedQuery = entityManager.createQuery("select d from DocumentModel u where d.id = :id",
					DocumentModel.class);
			typedQuery.setParameter("id", id);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
