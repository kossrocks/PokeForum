package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.TopicModel;

@Repository
@Transactional
public class TopicDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public List<TopicModel> getTopics() {
		TypedQuery<TopicModel> typedQuery = entityManager.createQuery("select e from TopicModel e",
				TopicModel.class);
		List<TopicModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public void persist(TopicModel topic) {
		entityManager.persist(topic);
	}

}
