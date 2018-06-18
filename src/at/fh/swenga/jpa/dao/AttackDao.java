package at.fh.swenga.jpa.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.SpeciesModel;


@Repository
@Transactional
public class AttackDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(AttackModel attack) {
		entityManager.persist(attack);
	}
	
	
	public void merge(AttackModel attack) {
		entityManager.merge(attack);
	}
	
	
	public List<AttackModel> getAllAttacks() {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery("select e from AttackModel e order by e.name",
				AttackModel.class);
		List<AttackModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<AttackModel> searchAttack(String searchString) {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery(

				"select e from AttackModel e where e.name like :search or e.category.name like :search or e.battleEffect like :search or e.type.name like :search order by e.name",

				AttackModel.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<AttackModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public AttackModel getAttackByName(String name) {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery(
				"select e from AttackModel e where e.name = :search",
				AttackModel.class);
		typedQuery.setParameter("search", name);
		AttackModel typedResultList = typedQuery.getSingleResult();
		return typedResultList;
	}
	
	
	public AttackModel searchAttackByName(String name) {
		try {
			TypedQuery<AttackModel> typedQuery = entityManager.createQuery("select u from AttackModel u where u.name = :name",
					AttackModel.class);
			typedQuery.setParameter("name", name);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	
	
	
	public AttackModel searchAttackById(int id) {
		try {
			TypedQuery<AttackModel> typedQuery = entityManager.createQuery("select a from AttackModel a where a.id = :name",
					AttackModel.class);
			typedQuery.setParameter("name", id);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	public int deleteById(int id) {
		int count = entityManager.createQuery("DELETE FROM AttackModel a WHERE a.id = :id").setParameter("id", id).executeUpdate();
		return count;
	}
	
	
	

}
