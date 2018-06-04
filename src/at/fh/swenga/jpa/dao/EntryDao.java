package at.fh.swenga.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
		TypedQuery<EntryModel> typedQuery = entityManager.createQuery("select e from EntryModel e where e.topic.id = :id",
				EntryModel.class);
		typedQuery.setParameter("id", id);
		List<EntryModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
	
	public void persist(EntryModel entry) {
		entityManager.persist(entry);
	}

}
