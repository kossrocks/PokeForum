package at.fh.swenga.jpa.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.AttackModel;


@Repository
@Transactional
public class AttackDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(AttackModel attack) {
		entityManager.persist(attack);
	}
	public List<AttackModel> getAllAttacks() {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery("select e from AttackModel e order by e.id",
				AttackModel.class);
		List<AttackModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<AttackModel> searchAttack(String searchString) {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery(
				"select e from AttackModel e where e.name like :search order by e.name asc",
				AttackModel.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<AttackModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

}
