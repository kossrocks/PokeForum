package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import at.fh.swenga.jpa.dao.DocumentDao;
import at.fh.swenga.jpa.dao.DocumentRepository;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRepository;
import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;

@Controller
public class DocumentController {

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DocumentDao documentDao;

	@Autowired
	UserDao userDao;

	@Autowired
	PokemonDao pokemonDao;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadDocument(Model model, Principal principal, @RequestParam("myFile") MultipartFile file) {

		try {

			User user = userDao.getUser(principal.getName());

			//only creating document if it is a picture format
			if (file.getContentType().startsWith("image/")) {

				int pictureId = 0;
				if (user.getPicture() != null) {
					pictureId = user.getPicture().getId();
				}
				DocumentModel document = new DocumentModel();
				document.setContent(file.getBytes());
				document.setContentType(file.getContentType());
				document.setCreated(new Date());
				document.setFilename(file.getOriginalFilename());
				document.setName(file.getName());
				user.setPicture(document);
				userDao.merge(user);

				model.addAttribute("user", user);

				//deleting old picture if there was one
				if (pictureId > 0) {
					documentDao.deleteById(pictureId);
				}

			} else {
				model.addAttribute("errorMessage", "Only image files are allowed to upload");
			}

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error:" + e.getMessage());
		}

		User user = userDao.getUser(principal.getName());

		int id = userDao.getUser(principal.getName()).getId();

		model.addAttribute("user", user);
		model.addAttribute("id", id);

		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);

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
