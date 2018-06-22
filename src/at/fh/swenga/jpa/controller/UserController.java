package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.DocumentDao;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;

@Controller
public class UserController {

	@Autowired
	UserDao userDao;
	
	@Autowired
	PokemonDao pokemonDao;
	
	@Autowired
	DocumentDao documentDao;
	
	//searching specific users. Each user is shown that has the searchstring in their username, firstname, or lastname
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

	// admins can disable Users. Disabled Users cannot login to the webpage.
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

	// admins can enable disabled users. They can login to the webpage again
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

	// showing edit user form
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public String showEditUser(Model model, Principal principal) {

		User user = userDao.getUser(principal.getName());

		model.addAttribute("user", user);

		return "editUser";
	}

	// users can change their personal data like firstname, lastname or their password
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public String editedEntry(Model model, @RequestParam("userName") String username,
			@RequestParam("password") String password, @RequestParam("password1") String password1,
			@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("profilePicture") int pictureId ,
			Principal principal) {
		User user = userDao.getUser(username);
		if (password != null) {
			//password can only be changed if they are not empty and equals to the confirmation password
			if (password.equals(password1)) {
				user.setPassword(password);
				user.encryptPassword();
			} else {
				model.addAttribute("errorMessage", "Confirmation password is not the same as password");
				

				model.addAttribute("user", user);
				return "editUser";
			}
		}

		// firstname and lastname are only changed if the textboxes are not empty
		if (firstName != null) {
			user.setFirstName(firstName);
		}
		if (lastName != null) {
			user.setLastName(lastName);
		}
		
		//the ids 1-4 are reserved for Pictures the user can choose from
		if(pictureId > 0) {
			int oldId = 0;
			
			if(user.getPicture() != null) {
				oldId = user.getPicture().getId();
			}
			user.setPicture(documentDao.searchPictureById(pictureId));
			if(oldId > 4) {
				documentDao.deleteById(oldId);
			}
		}
		
		userDao.merge(user);

		model.addAttribute("user", user);
		int id = userDao.getUser(principal.getName()).getId();
		model.addAttribute("id", id);
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		model.addAttribute("message", "You successfully edited your data.");
		
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
			model.addAttribute("userRole", "User");}

		return "profile";
	}

	
	
}
