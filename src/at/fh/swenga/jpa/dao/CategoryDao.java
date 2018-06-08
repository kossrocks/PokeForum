package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.CategoryModel;

@Repository
@Transactional
public class CategoryDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(CategoryModel category) {
		entityManager.persist(category);
	}

}
