package at.fh.swenga.jpa.dao;
 
import java.util.List;
 
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
 
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
 
import at.fh.swenga.jpa.model.EmployeeModel;
 
@Repository
@Transactional
public class EmployeeDao {
 
	@PersistenceContext
	protected EntityManager entityManager;
 
	public List<EmployeeModel> getEmployees() {
		TypedQuery<EmployeeModel> typedQuery = entityManager.createQuery("select e from EmployeeModel e",
				EmployeeModel.class);
		List<EmployeeModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
 
	public List<EmployeeModel> searchEmployees(String searchString) {
		TypedQuery<EmployeeModel> typedQuery = entityManager.createQuery(
				"select e from EmployeeModel e where e.firstName like :search or e.lastName like :search",
				EmployeeModel.class);
		typedQuery.setParameter("search", "%" + searchString + "%");
		List<EmployeeModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}
 
	public EmployeeModel getEmployee(int i) throws DataAccessException {
		return entityManager.find(EmployeeModel.class, i);
	}
 
	public void persist(EmployeeModel employee) {
		entityManager.persist(employee);
	}
 
	public EmployeeModel merge(EmployeeModel employee) {
		return entityManager.merge(employee);
	}
 
	public void delete(EmployeeModel employee) {
		entityManager.remove(employee);
	}
 
	public int deleteAllEmployees() {
		int count = entityManager.createQuery("DELETE FROM EmployeeModel").executeUpdate();
		return count;
	}
 
	public void delete(int id) {
		EmployeeModel employee = getEmployee(id);
		if (employee != null)
			delete(employee);
	}
}