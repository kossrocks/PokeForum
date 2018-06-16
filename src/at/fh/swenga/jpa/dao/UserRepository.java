package at.fh.swenga.jpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import at.fh.swenga.jpa.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	List<User> findByUserName(String userName);

}
