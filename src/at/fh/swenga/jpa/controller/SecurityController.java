package at.fh.swenga.jpa.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;
 
@Controller
public class SecurityController {
 
	@Autowired
	UserDao userDao;
 
	@Autowired
	UserRoleDao userRoleDao;
	
	@Autowired
	TopicDao topicDao;
	
	@Autowired 
	EntryDao entryDao;
 
	@RequestMapping("/fillUsers")
	@Transactional
	public String fillData(Model model) {
 
		UserRole userRole = userRoleDao.getRole("ROLE_USER");
		if (userRole == null)
			userRole = new UserRole("ROLE_USER");
 
		User admin = new User("ADMIN", "password");
		admin.encryptPassword();
		admin.addUserRole(userRole);
		userDao.persist(admin);
 
		User user = new User("user", "password");
		user.encryptPassword();
		user.addUserRole(userRole);
		userDao.persist(user);
		
		User firstUser = new User("user1","password");
		firstUser.encryptPassword();
		firstUser.addUserRole(userRole);
		userDao.persist(firstUser);
		
		User secondUser = new User("GoPokemon","password");
		secondUser.encryptPassword();
		secondUser.addUserRole(userRole);
		userDao.persist(secondUser);
		
		User thirdUser = new User("TheVeryBest","password");
		thirdUser.encryptPassword();
		thirdUser.addUserRole(userRole);
		userDao.persist(thirdUser);
		
		TopicModel topic0 = new TopicModel("Who is here?", userDao.getUser("admin"));
		topicDao.persist(topic0);
		
		TopicModel topic1 = new TopicModel("Who is the very best?", userDao.getUser("TheVeryBest"));
		topicDao.persist(topic1);
		
		TopicModel topic2 = new TopicModel("List your favorite Pokemon", userDao.getUser("GoPokemon"));
		topicDao.persist(topic2);
		
		EntryModel entry000 = new EntryModel(userDao.getUser("admin"),"Everybody who is here should write something.",topicDao.getTopic(1), false);
		entryDao.persist(entry000);
		
		EntryModel entry001 = new EntryModel(userDao.getUser("user1"),"something",topicDao.getTopic(1), false);
		entryDao.persist(entry001);
		
		EntryModel entry002 = new EntryModel(userDao.getUser("TheVeryBest"),"XDD",topicDao.getTopic(1), false);
		entryDao.persist(entry002);

		EntryModel entry100 = new EntryModel(userDao.getUser("TheVeryBest"),"Who is the very best? obviously me ;)",topicDao.getTopic(2), false);
		entryDao.persist(entry100);
		
		EntryModel entry101 = new EntryModel(userDao.getUser("GoPokemon"),"-_-'",topicDao.getTopic(2), false);
		entryDao.persist(entry101);
		
		EntryModel entry200 = new EntryModel(userDao.getUser("GoPokemon"),"My favorite Pokemon are all the Pokemon :) ",topicDao.getTopic(3), false);
		entryDao.persist(entry200);
		
		EntryModel entry201 = new EntryModel(userDao.getUser("user1"),"I like rattata",topicDao.getTopic(3), false);
		entryDao.persist(entry201);
		
		EntryModel entry202 = new EntryModel(userDao.getUser("user"),"Hey you copied my name!!",topicDao.getTopic(3), false);
		entryDao.persist(entry202);
		
		
		
		
		
		
		
		
		return "forward:login";
	}
 
	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {
 
		return "error";
 
	}
}