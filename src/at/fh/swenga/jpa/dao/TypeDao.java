package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
	public List<TypeModel> getAllTypes() {
		TypedQuery<TypeModel> typedQuery = entityManager.createQuery("select e from TypeModel e",
				TypeModel.class);
		List<TypeModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}


}
