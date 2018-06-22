package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.TypeModel;
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;

@Controller
public class NavigationController {

	@Autowired
	TopicDao topicDao;
	
	@Autowired
	AttackDao attackDao;
	
	@Autowired
	PokemonDao pokemonDao;
	
	@Autowired
	SpeciesDao speciesDao;
	
	@Autowired
	TypeDao typeDao;
	
	@Autowired
	UserDao userDao;
	
	//getting to the index page where all existing topics are listed
	@RequestMapping(value = { "/", "index" })
	public String index(Model model,Principal principal) {

		List<TopicModel> topics = topicDao.getAllTopicsSortById();
		model.addAttribute("topics", topics);
		
		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);
		
		// if logged in user has role admin. Important for security reasons.
		boolean isAdmin = false;
		for(UserRole role : user.getUserRoles()) {
			if(role.getRole().equalsIgnoreCase("role_admin")) isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);

		
		return "index";
	}
	
	
	@RequestMapping("/signUp")
	public String signUp(Model model) {

		return "signUp";
	}	
	
	// shows profile of logged in user 
	@Secured({ "ROLE_USER" })
	@RequestMapping("/profile")
	public String profile(Model model,Principal principal) {
		
		
		
		int id = userDao.getUser(principal.getName()).getId();
		User user = userDao.getUserById(id);
		model.addAttribute("user",user);
		model.addAttribute("id",id);
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		
		

		model.addAttribute("user", user);
		
		
		model.addAttribute("teamHeader", "Your Team");
		model.addAttribute("profileHeader", "Your Profile");
		
		boolean isAdmin = false;
		for(UserRole role : user.getUserRoles()) {
			if(role.getRole().equalsIgnoreCase("role_admin")) isAdmin = true;
		}
		
		
		if(isAdmin) {
			model.addAttribute("userRole", "Admin");
		}else {
			model.addAttribute("userRole", "User");
		}
		
		return "profile";
		
	}
	
	// shows profile of user with given id
	@Secured({ "ROLE_USER" })
	@RequestMapping("/userProfile")
	public String userProfile(Model model,@RequestParam int id, Principal principal) {
		
		User user = userDao.getUserById(id);
		model.addAttribute("user",user);
		int idUser = userDao.getUser(principal.getName()).getId();
		model.addAttribute("id",idUser);
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(user.getUserName());
		model.addAttribute("pokemons", pokemons);
		
		

		model.addAttribute("user", user);
		
		//checks if current user has this id, if yes it is his profile
		if(idUser == user.getId()) {
			model.addAttribute("teamHeader", "Your Team");
			model.addAttribute("profileHeader", "Your Profile");
		}else {
			model.addAttribute("teamHeader", "Team of " + user.getUserName());
			model.addAttribute("profileHeader", "Profile of "+ user.getUserName());	
		}
		
		boolean isAdmin = false;
		for(UserRole role : user.getUserRoles()) {
			if(role.getRole().equalsIgnoreCase("role_admin")) isAdmin = true;
		}
		
		
		if(isAdmin) {
			model.addAttribute("userRole", "Admin");
		}else {
			model.addAttribute("userRole", "User");
		}
		
		return "profile";
	}
	
	//shows a list of all existing users
	@Secured({ "ROLE_USER" })
	@RequestMapping("/users")
	public String users(Model model, Principal principal) {

		boolean isAdmin = false;
		User user = userDao.getUser(principal.getName());
		for(UserRole role : user.getUserRoles()) {
			if(role.getRole().equalsIgnoreCase("role_admin")) isAdmin = true;
		}
		
		// List with all users that are not disabled, except guest user, because guest user is no real user
		List<User> users = userDao.getAllUsers();
		// List with every user except guest user, because if admin accidently disables guest user, log in as guest won't work
		if(!isAdmin) users = userDao.getAllEnabledUsers();
		
	
		model.addAttribute("users",users);
		model.addAttribute("isAdmin", isAdmin);
		
		return "users";
	}
	
	// show List of all attacks that are in the database
	@RequestMapping("/attacks")
	public String attacks(Model model) {
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);	
		
		return "attacks";
	}
	
	//shows list of all pokemon species that are in the database
	@RequestMapping("/pokemon")
	public String pokemon(Model model) {
		
		List<SpeciesModel> pokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", pokemons);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);	
		
		return "pokemon";
	}
	
	// shows attack damage calculator
	@Secured({ "ROLE_USER" })
	@RequestMapping("/calculator")
	public String calculator(Model model,Principal principal) {
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);	
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("yourPokemons", pokemons);
		
		List<SpeciesModel> allPokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", allPokemons);
		
		return "calculator";
	}
	
	
	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}
	 
	 
	
}

