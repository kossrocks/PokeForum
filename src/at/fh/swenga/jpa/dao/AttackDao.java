package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.TypeModel;

@Repository
@Transactional
public class AttackDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(AttackModel attack) {
		entityManager.persist(attack);
	}
	public List<AttackModel> getAllAttacks() {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery("select e from AttackModel e order by e.name",
				AttackModel.class);
		List<AttackModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

	public List<AttackModel> searchAttack(String searchString) {
		TypedQuery<AttackModel> typedQuery = entityManager.createQuery(
				"select e from AttackModel e where e.name like :search or e.category like :search order by e.name",
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

}
