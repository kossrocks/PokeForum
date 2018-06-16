package at.fh.swenga.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.CategoryDao;
import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRepository;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.CategoryModel;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.SpeciesModel;
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

	@Autowired
	SpeciesDao speciesDao;

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	AttackDao attackDao;

	@Autowired
	UserRepository userRepository;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String handleLogin() {
		return "login";
	}
	
	@RequestMapping(value = "/guest", method = RequestMethod.GET)
	public void fillGuestData() {
		
		
		handleLogin();
	}
	
	
	@RequestMapping("/fillUsers")
	@Transactional
	public String fillData(Model model) {

		UserRole userRole = userRoleDao.getRole("ROLE_USER");
		if (userRole == null)
			userRole = new UserRole("ROLE_USER");
		
		UserRole adminRole = userRoleDao.getRole("ROLE_ADMIN");
		if (adminRole == null)
			adminRole = new UserRole("ROLE_ADMIN");
		
		UserRole guestRole = userRoleDao.getRole("ROLE_GUEST");
		if (guestRole == null)
			guestRole = new UserRole("ROLE_GUEST");

		User admin = new User("ADMIN", "password");
		admin.encryptPassword();
		admin.addUserRole(userRole);
		admin.addUserRole(adminRole);
		userDao.persist(admin);

		User user = new User("user", "password");
		user.encryptPassword();
		user.addUserRole(userRole);
		userDao.persist(user);
		
		User guest = new User("guest","guest");
		guest.encryptPassword();
		guest.addUserRole(guestRole);
		userDao.persist(guest);

		User firstUser = new User("user1", "password");
		firstUser.encryptPassword();
		firstUser.addUserRole(userRole);
		firstUser.setEnabled(false);
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

		TypeModel normal = new TypeModel("Normal");
		TypeModel fire = new TypeModel("Fire");
		TypeModel fighting = new TypeModel("Fighting");
		TypeModel water = new TypeModel("Water");
		TypeModel flying = new TypeModel("Flying");
		TypeModel grass = new TypeModel("Grass");
		TypeModel poison = new TypeModel("Poison");
		TypeModel electric = new TypeModel("Electric");
		TypeModel ground = new TypeModel("Ground");
		TypeModel psychic = new TypeModel("Psychic");
		TypeModel rock = new TypeModel("Rock");
		TypeModel ice = new TypeModel("Ice");
		TypeModel bug = new TypeModel("Bug");
		TypeModel dragon = new TypeModel("Dragon");
		TypeModel ghost = new TypeModel("Ghost");
		TypeModel dark = new TypeModel("Dark");
		TypeModel steel = new TypeModel("Steel");
		TypeModel fairy = new TypeModel("Fairy");

		normal.addWeakAgainst("rock");
		normal.addWeakAgainst("steel");
		normal.addNoDamageAgainst("ghost");

		fighting.addGoodAgainst("normal");
		fighting.addGoodAgainst("ice");
		fighting.addGoodAgainst("steel");
		fighting.addGoodAgainst("rock");
		fighting.addGoodAgainst("dark");
		fighting.addWeakAgainst("flying");
		fighting.addWeakAgainst("poison");
		fighting.addWeakAgainst("bug");
		fighting.addWeakAgainst("psychic");
		fighting.addWeakAgainst("fairy");
		fighting.addNoDamageAgainst("ghost");

		flying.addGoodAgainst("grass");
		flying.addGoodAgainst("bug");
		flying.addGoodAgainst("fighting");
		flying.addWeakAgainst("rock");
		flying.addWeakAgainst("steel");
		flying.addWeakAgainst("electric");

		poison.addGoodAgainst("grass");
		poison.addGoodAgainst("fairy");
		poison.addWeakAgainst("poison");
		poison.addWeakAgainst("ground");
		poison.addWeakAgainst("rock");
		poison.addWeakAgainst("ghost");
		poison.addNoDamageAgainst("steel");

		ground.addGoodAgainst("fire");
		ground.addGoodAgainst("electric");
		ground.addGoodAgainst("rock");
		ground.addGoodAgainst("steel");
		ground.addGoodAgainst("poison");
		ground.addWeakAgainst("bug");
		ground.addWeakAgainst("grass");
		ground.addNoDamageAgainst("flying");

		rock.addGoodAgainst("ice");
		rock.addGoodAgainst("flying");
		rock.addGoodAgainst("fire");
		rock.addGoodAgainst("bug");
		rock.addWeakAgainst("fighting");
		rock.addWeakAgainst("ground");
		rock.addWeakAgainst("steel");

		bug.addGoodAgainst("psychic");
		bug.addGoodAgainst("dark");
		bug.addGoodAgainst("grass");
		bug.addWeakAgainst("fighting");
		bug.addWeakAgainst("flying");
		bug.addWeakAgainst("poison");
		bug.addWeakAgainst("ghost");
		bug.addWeakAgainst("steel");
		bug.addWeakAgainst("fire");
		bug.addWeakAgainst("fairy");

		ghost.addGoodAgainst("ghost");
		ghost.addGoodAgainst("psychic");
		ghost.addWeakAgainst("dark");
		ghost.addNoDamageAgainst("normal");

		steel.addGoodAgainst("ice");
		steel.addGoodAgainst("rock");
		steel.addGoodAgainst("fairy");
		steel.addWeakAgainst("steel");
		steel.addWeakAgainst("fire");
		steel.addWeakAgainst("water");
		steel.addWeakAgainst("electric");

		fire.addGoodAgainst("grass");
		fire.addGoodAgainst("steel");
		fire.addGoodAgainst("ice");
		fire.addGoodAgainst("bug");
		fire.addWeakAgainst("dragon");
		fire.addWeakAgainst("water");
		fire.addWeakAgainst("rock");
		fire.addWeakAgainst("fire");

		water.addGoodAgainst("fire");
		water.addGoodAgainst("rock");
		water.addGoodAgainst("ground");
		water.addWeakAgainst("water");
		water.addWeakAgainst("grass");
		water.addWeakAgainst("dragon");

		electric.addGoodAgainst("flying");
		electric.addGoodAgainst("water");
		electric.addWeakAgainst("electric");
		electric.addWeakAgainst("grass");
		electric.addWeakAgainst("dragon");
		electric.addNoDamageAgainst("ground");

		psychic.addGoodAgainst("fighting");
		psychic.addGoodAgainst("poison");
		psychic.addWeakAgainst("steel");
		psychic.addWeakAgainst("psychic");
		psychic.addNoDamageAgainst("dark");

		ice.addGoodAgainst("dragon");
		ice.addGoodAgainst("flying");
		ice.addGoodAgainst("grass");
		ice.addGoodAgainst("ground");
		ice.addWeakAgainst("steel");
		ice.addWeakAgainst("fire");
		ice.addWeakAgainst("water");
		ice.addWeakAgainst("ice");

		dragon.addGoodAgainst("dragon");
		dragon.addWeakAgainst("steel");
		dragon.addNoDamageAgainst("fairy");

		dark.addGoodAgainst("ghost");
		dark.addGoodAgainst("psychic");
		dark.addWeakAgainst("fighting");
		dark.addWeakAgainst("dark");
		dark.addWeakAgainst("fairy");

		fairy.addGoodAgainst("dragon");
		fairy.addGoodAgainst("fighting");
		fairy.addGoodAgainst("dark");
		fairy.addWeakAgainst("poison");
		fairy.addWeakAgainst("steel");
		fairy.addWeakAgainst("fire");

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

		SpeciesModel bulbasaur = new SpeciesModel("Bulbasaur", 45, 49, 49, 65, 65, 45);
		SpeciesModel ivysaur = new SpeciesModel("Ivysaur", 60, 62, 63, 80, 80, 60);
		SpeciesModel venusaur = new SpeciesModel("Venusaur", 80, 82, 83, 100, 100, 80);
		SpeciesModel charmander = new SpeciesModel("Charmander", 39, 52, 43, 60, 50, 65);
		SpeciesModel charmeleon = new SpeciesModel("Charmeleon", 58, 64, 58, 80, 65, 80);
		SpeciesModel charizard = new SpeciesModel("Charizard", 78, 84, 78, 109, 85, 109);
		SpeciesModel squirtle = new SpeciesModel("Squirtle", 44, 48, 65, 50, 64, 43);
		SpeciesModel wartortle = new SpeciesModel("Wartortle", 59, 63, 80, 65, 80, 58);
		SpeciesModel blastoise = new SpeciesModel("Blastoise", 79, 83, 100, 85, 105, 78);
		SpeciesModel pikachu = new SpeciesModel("Pikachu", 35, 55, 40, 50, 50, 90);
		SpeciesModel raichu = new SpeciesModel("Raichu", 60, 90, 55, 90, 80, 110);

		bulbasaur.addType(grass);
		bulbasaur.addType(poison);
		ivysaur.addType(grass);
		ivysaur.addType(poison);
		venusaur.addType(grass);
		venusaur.addType(poison);

		charmander.addType(fire);
		charmeleon.addType(fire);
		charizard.addType(fire);
		charizard.addType(flying);

		squirtle.addType(water);
		wartortle.addType(water);
		blastoise.addType(water);

		pikachu.addType(electric);
		raichu.addType(electric);

		speciesDao.persist(bulbasaur);
		speciesDao.persist(ivysaur);
		speciesDao.persist(venusaur);
		speciesDao.persist(charmander);
		speciesDao.persist(charmeleon);
		speciesDao.persist(charizard);
		speciesDao.persist(squirtle);
		speciesDao.persist(wartortle);
		speciesDao.persist(blastoise);
		speciesDao.persist(pikachu);
		speciesDao.persist(raichu);

		CategoryModel physical = new CategoryModel("Physical");
		CategoryModel special = new CategoryModel("Special");
		CategoryModel status = new CategoryModel("Status");

		categoryDao.persist(status);
		categoryDao.persist(special);
		categoryDao.persist(physical);

		AttackModel tackle = new AttackModel("Tackle", normal, physical, 40, 100, 35, "Attacks opponent.");
		AttackModel thunderShock = new AttackModel("Thunder Shock", electric, special, 40, 100, 30,
				"May paralyze opponent.");
		AttackModel waterGun = new AttackModel("Water Gun", water, special, 40, 100, 25, "Attacks opponent.");
		AttackModel vineWhip = new AttackModel("Vine Whip", grass, physical, 45, 100, 25, "Attacks opponent.");
		AttackModel ember = new AttackModel("Ember", fire, special, 40, 100, 25, "May burn opponent.");
		AttackModel cut = new AttackModel("Cut", normal, physical, 50, 95, 30, "Attacks opponent.");
		AttackModel dig = new AttackModel("Dig", ground, physical, 80, 100, 10,
				"Digs underground on first turn, attacks on second. Can also escape from caves.");
		AttackModel doubleKick = new AttackModel("Double Kick", fighting, physical, 60, 100, 10,
				"Hits twice in one turn.");
		AttackModel earthquake = new AttackModel("Earthquake", ground, physical, 100, 100, 10,
				"Power is doubled if opponent is underground from using Dig.");
		AttackModel flamethrower = new AttackModel("Flamethrower", fire, special, 90, 100, 15, "May burn opponent.");
		AttackModel thunder = new AttackModel("Thunder", electric, special, 110, 70, 10, "May paralyze opponent.");
		AttackModel toxic = new AttackModel("Toxic", poison, status, 0, 90, 10, "Badly poisons opponent.");
		AttackModel solarBeam = new AttackModel("Solar Beam", grass, special, 120, 100, 10,
				"Charges on first turn, attacks on second.");
		AttackModel surf = new AttackModel("Surf", water, special, 90, 100, 15, "Hits all adjacent Pokémon.");
		AttackModel razorLeaf = new AttackModel("Razor Leaf", grass, physical, 55, 95, 25, "High critical hit ratio.");

		attackDao.persist(tackle);
		attackDao.persist(thunderShock);
		attackDao.persist(waterGun);
		attackDao.persist(vineWhip);
		attackDao.persist(ember);
		attackDao.persist(cut);
		attackDao.persist(dig);
		attackDao.persist(doubleKick);
		attackDao.persist(earthquake);
		attackDao.persist(flamethrower);
		attackDao.persist(thunder);
		attackDao.persist(toxic);
		attackDao.persist(solarBeam);
		attackDao.persist(surf);
		attackDao.persist(razorLeaf);
		
		
		

		return "forward:login";
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addNewUser(@RequestParam("userName") String userName, @RequestParam("password") String password,
			@RequestParam("password1") String password1, Model model) {
		User user = userDao.getUser(userName);
		if (user == null) {
			if (password.equals(password1)) {

				user = new User(userName, password);
				user.encryptPassword();

				UserRole userRole = userRoleDao.getRole("ROLE_USER");
				

				user.addUserRole(userRole);
				userDao.merge(user);

			} else {
				model.addAttribute("errorMessage", "Error: Passwords doesn't match!");
			}

		} else {
			model.addAttribute("errorMessage", "Error: User already exists!");
		}

		return "login";
	}
	/*
	 * @ExceptionHandler(Exception.class) public String handleAllException(Exception 
	 * ex) {
	 * 
	 * return "error";
	 * 
	 * }
	 */
}
