package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.EmployeeModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;

@Repository
@Transactional
public class TopicDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public List<TopicModel> getAllTopics() {
		TypedQuery<TopicModel> typedQuery = entityManager.createQuery("select e from TopicModel e order by e.id",
				TopicModel.class);
		List<TopicModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public List<TopicModel> getAllTopicsSortById() {
		TypedQuery<TopicModel> typedQuery = entityManager.createQuery("select e from TopicModel e ORDER BY e.id",
				TopicModel.class);
		List<TopicModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public TopicModel getTopic(int topicID) {
		TypedQuery<TopicModel> typedQuery = entityManager.createQuery("select u from TopicModel u where u.id = :id",
				TopicModel.class);
		typedQuery.setParameter("id", topicID);
		TopicModel typedResultList = typedQuery.getResultList().get(0);
		return typedResultList;
	}
	
	public TopicModel getTopicByName(String name) {
		TypedQuery<TopicModel> typedQuery = entityManager.createQuery("select u from TopicModel u where u.title = :name",
				TopicModel.class);
		typedQuery.setParameter("name", name);
		if(typedQuery.getResultList().isEmpty()) {
			return null;
		}
		TopicModel typedResultList = typedQuery.getResultList().get(0);
		return typedResultList;
	}
	
	public void persist(TopicModel topic) {
		entityManager.persist(topic);
	}
	
	public void merge(TopicModel topic) {
		entityManager.merge(topic);
	}

	@Secured("ROLE_ADMIN")
	public void delete(TopicModel topic) {
		entityManager.remove(topic);
	}
	
	@Secured("ROLE_ADMIN")
	public void delete(int id) {
		TopicModel topic = getTopic(id);
		if (topic != null)
			delete(topic);
	}
}
