package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
