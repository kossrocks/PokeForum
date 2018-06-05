package at.fh.swenga.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.TypeModel;
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
	
	@Autowired
	TypeDao typeDao;

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

		User firstUser = new User("user1", "password");
		firstUser.encryptPassword();
		firstUser.addUserRole(userRole);
		userDao.persist(firstUser);

		User secondUser = new User("GoPokemon", "password");
		secondUser.encryptPassword();
		secondUser.addUserRole(userRole);
		userDao.persist(secondUser);

		User thirdUser = new User("TheVeryBest", "password");
		thirdUser.encryptPassword();
		thirdUser.addUserRole(userRole);
		userDao.persist(thirdUser);

		TopicModel topic0 = new TopicModel("Who is here?", userDao.getUser("admin"));
		topicDao.persist(topic0);

		TopicModel topic1 = new TopicModel("Who is the very best?", userDao.getUser("TheVeryBest"));
		topicDao.persist(topic1);

		TopicModel topic2 = new TopicModel("List your favorite Pokemon", userDao.getUser("GoPokemon"));
		topicDao.persist(topic2);

		EntryModel entry000 = new EntryModel(userDao.getUser("admin"), "Everybody who is here should write something.",
				topicDao.getTopic(1), false);
		entryDao.persist(entry000);

		EntryModel entry001 = new EntryModel(userDao.getUser("user1"), "something", topicDao.getTopic(1), false);
		entryDao.persist(entry001);

		EntryModel entry002 = new EntryModel(userDao.getUser("TheVeryBest"), "XDD", topicDao.getTopic(1), false);
		entryDao.persist(entry002);

		EntryModel entry100 = new EntryModel(userDao.getUser("TheVeryBest"), "Who is the very best? obviously me ;)",
				topicDao.getTopic(2), false);
		entryDao.persist(entry100);

		EntryModel entry101 = new EntryModel(userDao.getUser("GoPokemon"), "-_-'", topicDao.getTopic(2), false);
		entryDao.persist(entry101);

		EntryModel entry200 = new EntryModel(userDao.getUser("GoPokemon"),
				"My favorite Pokemon are all the Pokemon :) ", topicDao.getTopic(3), false);
		entryDao.persist(entry200);

		EntryModel entry201 = new EntryModel(userDao.getUser("user1"), "I like rattata", topicDao.getTopic(3), false);
		entryDao.persist(entry201);

		EntryModel entry202 = new EntryModel(userDao.getUser("user"), "Hey you copied my name!!", topicDao.getTopic(3),
				false);
		entryDao.persist(entry202);
		
		TypeModel normal = new TypeModel();
		TypeModel fire = new TypeModel();
		TypeModel fighting = new TypeModel();
		TypeModel water = new TypeModel();
		TypeModel flying = new TypeModel();
		TypeModel grass = new TypeModel();
		TypeModel poison = new TypeModel();
		TypeModel electric = new TypeModel();
		TypeModel ground = new TypeModel();
		TypeModel psychic = new TypeModel();
		TypeModel rock = new TypeModel();
		TypeModel ice = new TypeModel();
		TypeModel bug = new TypeModel();
		TypeModel dragon = new TypeModel();
		TypeModel ghost = new TypeModel();
		TypeModel dark = new TypeModel();		
		TypeModel steel = new TypeModel();
		TypeModel fairy = new TypeModel();
		
		normal.addWeakAgainst(rock);
		normal.addWeakAgainst(steel);
		normal.addNoDamageAgainst(ghost);
				
		fighting.addGoodAgainst(normal);
		fighting.addGoodAgainst(ice);
		fighting.addGoodAgainst(steel);
		fighting.addGoodAgainst(rock);
		fighting.addGoodAgainst(dark);
		fighting.addWeakAgainst(flying);
		fighting.addWeakAgainst(poison);
		fighting.addWeakAgainst(bug);
		fighting.addWeakAgainst(psychic);
		fighting.addWeakAgainst(fairy);
		fighting.addNoDamageAgainst(ghost);
		
		flying.addGoodAgainst(grass);
		flying.addGoodAgainst(bug);
		flying.addGoodAgainst(fighting);
		flying.addWeakAgainst(rock);
		flying.addWeakAgainst(steel);
		flying.addWeakAgainst(electric);
		
		poison.addGoodAgainst(grass);
		poison.addGoodAgainst(fairy);
		poison.addWeakAgainst(poison);
		poison.addWeakAgainst(ground);
		poison.addWeakAgainst(rock);
		poison.addWeakAgainst(ghost);
		poison.addNoDamageAgainst(steel);
		
		ground.addGoodAgainst(fire);
		ground.addGoodAgainst(electric);
		ground.addGoodAgainst(rock);
		ground.addGoodAgainst(steel);
		ground.addGoodAgainst(poison);
		ground.addWeakAgainst(bug);
		ground.addWeakAgainst(grass);
		ground.addNoDamageAgainst(flying);
		
		rock.addGoodAgainst(ice);
		rock.addGoodAgainst(flying);
		rock.addGoodAgainst(fire);
		rock.addGoodAgainst(bug);
		rock.addWeakAgainst(fighting);
		rock.addWeakAgainst(ground);
		rock.addWeakAgainst(steel);
		
		bug.addGoodAgainst(psychic);
		bug.addGoodAgainst(dark);
		bug.addGoodAgainst(grass);
		bug.addWeakAgainst(fighting);
		bug.addWeakAgainst(flying);
		bug.addWeakAgainst(poison);
		bug.addWeakAgainst(ghost);
		bug.addWeakAgainst(steel);
		bug.addWeakAgainst(fire);
		bug.addWeakAgainst(fairy);
		
		ghost.addGoodAgainst(ghost);
		ghost.addGoodAgainst(psychic);
		ghost.addWeakAgainst(dark);
		ghost.addNoDamageAgainst(normal);
		
		steel.addGoodAgainst(ice);
		steel.addGoodAgainst(rock);
		steel.addGoodAgainst(fairy);
		steel.addWeakAgainst(steel);
		steel.addWeakAgainst(fire);
		steel.addWeakAgainst(water);
		steel.addWeakAgainst(electric);
		
		fire.addGoodAgainst(grass);
		fire.addGoodAgainst(steel);
		fire.addGoodAgainst(ice);
		fire.addGoodAgainst(bug);
		fire.addWeakAgainst(dragon);
		fire.addWeakAgainst(water);
		fire.addWeakAgainst(rock);
		fire.addWeakAgainst(fire);
		
		water.addGoodAgainst(fire);
		water.addGoodAgainst(rock);
		water.addGoodAgainst(ground);
		water.addWeakAgainst(water);
		water.addWeakAgainst(grass);
		water.addWeakAgainst(dragon);
		
		electric.addGoodAgainst(flying);
		electric.addGoodAgainst(water);
		electric.addWeakAgainst(electric);
		electric.addWeakAgainst(grass);
		electric.addWeakAgainst(dragon);
		electric.addNoDamageAgainst(ground);
		
		psychic.addGoodAgainst(fighting);
		psychic.addGoodAgainst(poison);
		psychic.addWeakAgainst(steel);
		psychic.addWeakAgainst(psychic);
		psychic.addNoDamageAgainst(dark);

		ice.addGoodAgainst(dragon);
		ice.addGoodAgainst(flying);
		ice.addGoodAgainst(grass);
		ice.addGoodAgainst(ground);
		ice.addWeakAgainst(steel);
		ice.addWeakAgainst(fire);
		ice.addWeakAgainst(water);
		ice.addWeakAgainst(ice);
		
		dragon.addGoodAgainst(dragon);
		dragon.addWeakAgainst(steel);
		dragon.addNoDamageAgainst(fairy);
		
		dark.addGoodAgainst(ghost);
		dark.addGoodAgainst(psychic);
		dark.addWeakAgainst(fighting);
		dark.addWeakAgainst(dark);
		dark.addWeakAgainst(fairy);
		
		fairy.addGoodAgainst(dragon);
		fairy.addGoodAgainst(fighting);
		fairy.addGoodAgainst(dark);
		fairy.addWeakAgainst(poison);
		fairy.addWeakAgainst(steel);
		fairy.addWeakAgainst(fire);
		
		typeDao.persist(normal);
		typeDao.persist(water);
		typeDao.persist(fire);
		typeDao.persist(grass);
		typeDao.persist(electric);
		typeDao.persist(ice);
		typeDao.persist(ghost);
		typeDao.persist(ground);
		typeDao.persist(steel);
		typeDao.persist(rock);
		typeDao.persist(fighting);
		typeDao.persist(flying);
		typeDao.persist(bug);
		typeDao.persist(fairy);
		typeDao.persist(dark);
		typeDao.persist(psychic);
		typeDao.persist(dragon);
		typeDao.persist(poison);
		
		
		

		return "forward:login";
	}

	
	
	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}

	
}