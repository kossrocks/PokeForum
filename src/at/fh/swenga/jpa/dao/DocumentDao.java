package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.annotation.Secured;
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

	@Secured({ "ROLE_USER" })
	public void merge(DocumentModel document) {
		entityManager.merge(document);
	}

	@Secured({ "ROLE_USER" })
	public int deleteById(int id) {
		int count = entityManager.createQuery("DELETE FROM DocumentModel WHERE id = :id").setParameter("id", id)
				.executeUpdate();
		return count;
	}

	public DocumentModel searchPictureById(int id) {
		try {
			TypedQuery<DocumentModel> typedQuery = entityManager
					.createQuery("select a from DocumentModel a where a.id = :name", DocumentModel.class);
			typedQuery.setParameter("name", id);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
