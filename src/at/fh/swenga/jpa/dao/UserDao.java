package at.fh.swenga.jpa.dao;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.jpa.model.User;

@Repository
@Transactional
public class UserDao {

	@PersistenceContext
	protected EntityManager entityManager;

	public User getUser(String userName) {
		try {
			TypedQuery<User> typedQuery = entityManager.createQuery("select u from User u where u.userName = :name",
					User.class);
			typedQuery.setParameter("name", userName);
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void persist(User user) {
		entityManager.persist(user);
	}
}