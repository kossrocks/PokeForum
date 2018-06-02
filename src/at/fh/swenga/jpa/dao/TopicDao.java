package at.fh.swenga.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.TopicModel;

@Repository
@Transactional
public class TopicDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(TopicModel topic) {
		entityManager.persist(topic);
	}

}
