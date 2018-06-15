package at.fh.swenga.jpa.controller;

import java.io.OutputStream;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import at.fh.swenga.jpa.dao.DocumentRepository;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserDocumentDao;
import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.User;

public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	DocumentRepository documentDao;
	
	@Autowired
	UserDocumentDao userDocumentDao;
	
	
	
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
	
}
