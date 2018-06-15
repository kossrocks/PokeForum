package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;
@Repository
@Transactional
public class EntryDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public List<EntryModel> getAllEntries() {
		TypedQuery<EntryModel> typedQuery = entityManager.createQuery("select e from EntryModel e",
				EntryModel.class);
		List<EntryModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public List<EntryModel> getAllEntriesInTopic(int id){
		TypedQuery<EntryModel> typedQuery = entityManager.createQuery("select e from EntryModel e where e.topic.id = :id order by e.id",
				EntryModel.class);
		typedQuery.setParameter("id", id);
		List<EntryModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public EntryModel getEntry(int id){
		TypedQuery<EntryModel> typedQuery = entityManager.createQuery("select e from EntryModel e where e.id = :id",
				EntryModel.class);
		typedQuery.setParameter("id", id);
		EntryModel typedResultList = typedQuery.getResultList().get(0);
		return typedResultList;
	}
	
	
	public void persist(EntryModel entry) {
		entityManager.persist(entry);
	}
	public void merge(EntryModel entry) {
		entityManager.merge(entry);
	}
	
	@Secured("ROLE_ADMIN")
	public void delete(EntryModel entry) {
		entityManager.remove(entry);
	}
	
	@Secured("ROLE_ADMIN")
	public void delete(int id) {
		EntryModel entry = getEntry(id);
		if (entry != null)
			delete(entry);
	}
}
