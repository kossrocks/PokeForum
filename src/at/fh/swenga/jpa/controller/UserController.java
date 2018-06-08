package at.fh.swenga.jpa.controller;

import java.io.OutputStream;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	/**
	 * Display the upload form
	 * @param model
	 * @param employeeId
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String showUploadForm(Model model) {
		
		return "forward:uploadPicture";
	}

	/**
	 * Save uploaded file to the database (as 1:1 relationship to employee)
	 * 
	 * @param model
	 * @param employeeId
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String uploadDocument(Model model, @RequestParam("id") int employeeId,
			@RequestParam("myFile") MultipartFile file) {
 
		try {
 
			Optional<User> userOpt = userDocumentDao.findById(employeeId);
			if (!userOpt.isPresent()) throw new IllegalArgumentException("No user with id "+employeeId);
 
			User user = userOpt.get();
 
			// Already a document available -> delete it
			if (user.getPicture() != null) {
				documentDao.delete(user.getPicture());
				// Don't forget to remove the relationship too
				user.setPicture(null);
			}
 // !!!!! MIME TYPE images -> if(!contentType.startsWith("image/"))
			// Create a new document and set all available infos
 
			DocumentModel document = new DocumentModel();
			document.setContent(file.getBytes());
			document.setContentType(file.getContentType());
			document.setCreated(new Date());
			document.setFilename(file.getOriginalFilename());
			document.setName(file.getName());
			user.setPicture(document);
			userDocumentDao.save(user);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error:" + e.getMessage());
		}
 
		return "forward:/profile";
	}
	
	@RequestMapping("/download")
	public void download(@RequestParam("pictureId") int documentId, HttpServletResponse response) {
 
		Optional<DocumentModel> docOpt = documentDao.findById(documentId);
		if (!docOpt.isPresent()) throw new IllegalArgumentException("No document with id "+documentId);
 
		DocumentModel doc = docOpt.get();
 
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
