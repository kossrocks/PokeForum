package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	
	
	
	/*
	 * 
	 *  getCategory for AttackController (add/edit Attack)
	 *  
	 *  */
	
	
	
	public CategoryModel getCategory(int id) {
		try {
			TypedQuery<CategoryModel> typedQuery = entityManager.createQuery("select c from CategoryModel c where c.id = :id",
					CategoryModel.class);
			typedQuery.setParameter("id", id);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	public List<CategoryModel> getAllCategories() {
		TypedQuery<CategoryModel> typedQuery = entityManager.createQuery("select c from CategoryModel c order by c.name",
				CategoryModel.class);
		List<CategoryModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

}
