package at.fh.swenga.jpa.controller;

import java.io.OutputStream;
import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import at.fh.swenga.jpa.dao.DocumentDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.User;

public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	DocumentDao documentDao;
	
	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
	
	/**
	 * Display the upload form
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String showUploadForm(Model model, @RequestParam("id") int userId) {
		model.addAttribute("userId", userId);
		return "uploadPicture";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadDocument(Model model, @RequestParam("id") int userId,
			@RequestParam("myFile") MultipartFile file) {
 
		try {

			User user = userDao.getUserById(userId);
 
			// Already a document available -> delete it
			if (user.getPicture() != null) {
				documentDao.delete(user.getPicture());
				// Don't forget to remove the relationship too
				user.setPicture(null);
			}
 
			// Create a new document and set all available infos
 
			DocumentModel picture = new DocumentModel();
			picture.setContent(file.getBytes());
			picture.setContentType(file.getContentType());
			picture.setCreated(new Date());
			picture.setFilename(file.getOriginalFilename());
			picture.setName(file.getName());
			user.setPicture(picture);
			userDao.persist(user);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error:" + e.getMessage());
		}
 
		return "forward:/profile";
	}
	
	@RequestMapping("/download")
	public void download(@RequestParam("pictureId") int pictureId, HttpServletResponse response) {
 
 
		DocumentModel doc = documentDao.getDocumentById(pictureId);
 
		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + doc.getFilename() + "\""); 
			OutputStream out = response.getOutputStream();
				// application/octet-stream
			response.setContentType(doc.getContentType());
			out.write(doc.getContent());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
