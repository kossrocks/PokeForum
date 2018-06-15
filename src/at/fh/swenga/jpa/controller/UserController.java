package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;


public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	DocumentRepository documentDao;
	
	@Autowired
	UserDocumentDao userDocumentDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	
	
	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
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
	
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public String showAddEmployeeForm(Model model) {
		return "signUp";
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addData(@RequestParam String pwd, @Valid @ModelAttribute User newUserModel, BindingResult bindingResult,Model model) {
		
		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid<br>";
			}
			model.addAttribute("errorMessage", errorMessage);
			return "login";
		}
		
		User user = userDao.getUserById(newUserModel.getId());
		
		if(user!=null){
			model.addAttribute("errorMessage", "User already exists!<br>");
		}
		else {
			UserRole userRole = userRoleDao.getRole("ROLE_USER");
			User u = new User();
			u.setUserName(newUserModel.getUserName());
			u.setPassword(pwd);
			u.setFirstName(newUserModel.getFirstName());
			u.setLastName(newUserModel.getLastName());
			u.setDateOfEntry(newUserModel.getDateOfEntry());
			u.setEnabled(true);
			userDao.persist(u);
			userRoleDao.persist(userRole);
						
			model.addAttribute("message", "New user " + newUserModel.getUserName() + " added.");
		}
		
		return "login";
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public String showChangeUserForm(Model model, @RequestParam int id) {
		User user = userDao.getUserById(id);
		if (user != null) {
			model.addAttribute("user", user);
			return "signUp";
		} else {
			model.addAttribute("errorMessage", "Couldn't find user " + id);
			return "forward:/login";
		}
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public String changeEmployee(@Valid @ModelAttribute User changedUser, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid<br>";
			}
			model.addAttribute("errorMessage", errorMessage);
			return "forward:/profile";
		}

		User user = userDao.getUserById(changedUser.getId());

		if (user == null) {
			model.addAttribute("errorMessage", "User does not exist!<br>");
		} else {
			user.setFirstName(changedUser.getFirstName());
			user.setLastName(changedUser.getLastName());
			user.setDateOfEntry(changedUser.getDateOfEntry());
			user.setPicture(changedUser.getPicture());
			model.addAttribute("message", "Changed user " + changedUser.getId());
		}

		return "forward:/SignUp";
	}
}
