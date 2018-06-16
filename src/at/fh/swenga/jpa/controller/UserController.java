package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import at.fh.swenga.jpa.dao.DocumentRepository;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserDocumentDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;


@Controller
public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	DocumentRepository documentDao;
	
	@Autowired
	UserDocumentDao userDocumentDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	

	@RequestMapping("/searchUsers")
	public String searchUser(Model model, @RequestParam String searchString) {
			
		List<User> users = userDao.searchUser(searchString); 
		
		model.addAttribute("users", users);

		return "users";
	}
	

	@RequestMapping(value = "/disableUser", method = RequestMethod.GET)
    public String disableUser(Model model, @RequestParam int id) {
		
		User user = userDao.getUserById(id);
		user.setEnabled(false);
		userDao.merge(user);
		
		
		List<User> users = userDao.getAllUsers();
		model.addAttribute("users",users);

		
		return "users";
    }
	
	@RequestMapping(value = "/enableUser", method = RequestMethod.GET)
    public String enableUser(Model model, @RequestParam int id) {
		
		User user = userDao.getUserById(id);
		user.setEnabled(true);
		userDao.merge(user);
		
		
		List<User> users = userDao.getAllUsers();
		model.addAttribute("users",users);

		
		return "users";
    }
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String showUploadForm(Model model) {
			
			//get current User
			Object curUser = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			if (curUser instanceof UserDetails) {
				String userName = ((UserDetails) curUser).getUsername();
				User user = userDao.getUser(userName);
			
			if (user!=null) {
				model.addAttribute("user", user);
				return "profile";
			} else {
				model.addAttribute("errorMessage", "Couldn't find user " + userName);
				return "forward:list";
			}
			}
			else {return "forward:list";}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadPicture(Model model, @RequestParam("myFile") MultipartFile file) {
		//get current User
		Object curUser = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (curUser instanceof UserDetails) {
			String userName = ((UserDetails) curUser).getUsername();
			
		try {
 
			User user = userDao.getUser(userName);
 
			// Already a picture available -> delete it
			if (user.getPicture()!=null) {
				documentDao.delete(user.getPicture());
				user.setPicture(null);
			}
 
			//Create a new picture and set all available infos 
			DocumentModel pic = new DocumentModel();
			pic.setContent(file.getBytes());
			pic.setContentType(file.getContentType());
			pic.setCreated(new Date());
			pic.setFilename(file.getOriginalFilename());
			pic.setName(file.getName());
			user.setPicture(pic);
			userDocumentDao.save(user);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error:" + e.getMessage());
		}}
 
		return "forward:profile";
	}
	
	
	
	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public String showChangeUserForm(Model model, Principal principal) {
		User user = userDao.getUser(principal.getName());
		if (user != null) {
			model.addAttribute("user", user);
			return "editUser";
		} else {
			model.addAttribute("errorMessage", "Couldn't find user");
			return "forward:/login";
		}
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public String editUser(Principal principal, @Valid User changedUser, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid<br>";
			}
			model.addAttribute("errorMessage", errorMessage);
			return "forward:/profile";
		}

		User user = userDao.getUser(principal.getName());

		if (user == null) {
			model.addAttribute("errorMessage", "User does not exist!<br>");
		} else {
			userDao.persist(user);
			user.setFirstName(changedUser.getFirstName());
			user.setLastName(changedUser.getLastName());
			
			userDao.merge(user);
			
			model.addAttribute("message", "Changed user " + changedUser.getUserName());
		}

		return "forward:/profile";
	}
}
