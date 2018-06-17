package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.TypeModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class NavigationController {

	@Autowired
	TopicDao topicDao;
	
	@Autowired
	AttackDao attackDao;
	
	@Autowired
	EntryDao entryDao;
	
	@Autowired
	PokemonDao pokemonDao;
	
	@Autowired
	SpeciesDao speciesDao;
	
	@Autowired
	TypeDao typeDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	@RequestMapping(value = { "/", "index" })
	public String index(Model model,Principal principal) {

		List<TopicModel> topics = topicDao.getAllTopicsSortById();
		model.addAttribute("topics", topics);
		
		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);
		
		boolean isAdmin = false;
		if(user.getUserName().equalsIgnoreCase("admin")) isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		
		return "index";
	}
	
	
	@RequestMapping("/signUp")
	public String signUp(Model model) {

		return "signUp";
	}	
	
	@RequestMapping("/profile")
	public String profile(Model model,Principal principal) {
		
		
		
		int id = userDao.getUser(principal.getName()).getId();
		User user = userDao.getUserById(id);
		model.addAttribute("user",user);
		model.addAttribute("id",id);
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		
		return "profile";
		
	}
	
	@RequestMapping("/userProfile")
	public String userProfile(Model model,@RequestParam int id, Principal principal) {
		
		User user = userDao.getUserById(id);
		model.addAttribute("user",user);
		int idUser = userDao.getUser(principal.getName()).getId();
		model.addAttribute("id",idUser);
		
		return "profile";
	}
	
	@RequestMapping("/users")
	public String users(Model model) {

		List<User> users = userDao.getAllUsers();
		model.addAttribute("users",users);

		
		return "users";
	}
	
	@RequestMapping("/attacks")
	public String attacks(Model model) {
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);	
		
		return "attacks";
	}
	
	@RequestMapping("/pokemon")
	public String pokemon(Model model) {
		
		List<SpeciesModel> pokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", pokemons);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);	
		
		
		
		
		return "pokemon";
	}
	
	@RequestMapping("/editTeamMember")
	public String editTeamMember(Model model) {



		
		return "editTeamMember";
	}
	
	@RequestMapping("/addEntry")
	public String addEntry(Model model) {



		
		return "addEntry";
	}
	
	
	@RequestMapping("/editSpecies")
	public String editSpecies(Model model) {



		
		return "editSpecies";
	}
	
	
	
	/*@RequestMapping("/uploadPicture")
	public String uploadPicture(Model model, @RequestParam int id ) {

		//DocumentModel picture = documentDao.getDocumentById(id);
		//model.addAttribute("picture", picture);
		
		return "uploadPicture";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}*/
	
}

