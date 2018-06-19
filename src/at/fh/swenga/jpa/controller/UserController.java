package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import at.fh.swenga.jpa.dao.DocumentRepository;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class UserController {

	@Autowired
	UserDao userDao;

	@Autowired
	UserRoleDao userRoleDao;
	
	@Autowired
	PokemonDao pokemonDao;
	
	@RequestMapping("/searchUsers")
	public String searchUser(Model model, @RequestParam String searchString) {

		List<User> users = userDao.searchUser(searchString);

		model.addAttribute("users", users);
		
		if(users.size() == 1) {
			model.addAttribute("message", "You found " + users.size() + " user.");
		}else {
		model.addAttribute("message", "You found " + users.size() + " users.");
		}
		return "users";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/disableUser", method = RequestMethod.GET)
	public String disableUser(Model model, @RequestParam int id) {

		User user = userDao.getUserById(id);
		if (user.getUserName().equalsIgnoreCase("admin")) {
			model.addAttribute("errorMessage", "You cannot disable yourself.");
		} else {
			user.setEnabled(false);
			userDao.merge(user);
			model.addAttribute("message", "Successfully disabled user " + user.getUserName() +".");
		}
		List<User> users = userDao.getAllUsers();
		model.addAttribute("users", users);

		return "users";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/enableUser", method = RequestMethod.GET)
	public String enableUser(Model model, @RequestParam int id) {

		User user = userDao.getUserById(id);
		user.setEnabled(true);
		userDao.merge(user);
		model.addAttribute("message", "Successfully enabled user " + user.getUserName() +".");
		List<User> users = userDao.getAllUsers();
		model.addAttribute("users", users);

		return "users";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public String showEditUser(Model model, Principal principal) {

		User user = userDao.getUser(principal.getName());

		model.addAttribute("user", user);

		return "editUser";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public String editedEntry(Model model, @RequestParam("userName") String username,
			@RequestParam("password") String password, @RequestParam("password1") String password1,
			@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,Principal principal) {
		User user = userDao.getUser(username);
		if (password != null) {
			if (password.equals(password1)) {
				user.setPassword(password);
				user.encryptPassword();
			} else {
				model.addAttribute("errorMessage", "Confirmation password is not the same as password");
				return "forward:editUser";
			}
		}

		if (firstName != null) {
			user.setFirstName(firstName);
		}
		if (lastName != null) {
			user.setLastName(lastName);
		}

		userDao.merge(user);

		model.addAttribute("user", user);
		int id = userDao.getUser(principal.getName()).getId();
		model.addAttribute("id", id);
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		model.addAttribute("message", "You successfully edited your data.");
		
		model.addAttribute("user", user);
		

		return "profile";
	}

	
	
}
