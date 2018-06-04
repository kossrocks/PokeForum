package at.fh.swenga.jpa.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class TopicController {
	
	@Autowired
	TopicDao topicDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping("/deleteTopic")
	public String deleteData(Model model, @RequestParam int id) {
		topicDao.delete(id);

		return "forward:index";
	}
	
	@GetMapping("/addTopic")
	public String showAddTopicForm(Model model) {
		return "addTopic";
	}
	
	@PostMapping("/addTopic")
	public String addTopic(@Valid TopicModel newTopicModel, BindingResult bindingResult, Model model) {

		// Any errors? -> Create a String out of all errors and return to the page
		if (bindingResult.hasErrors()) {
			String errorMessage = "";
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMessage += fieldError.getField() + " is invalid<br>";
			}
			model.addAttribute("errorMessage", errorMessage);

			// Multiple ways to "forward"
			return "forward:/index";
		}

		// Look for employee in the List. One available -> Error
		TopicModel topic = topicDao.getTopic(newTopicModel.getId());

		if (topic != null) {
			model.addAttribute("errorMessage", "Topic already exists!<br>");
		} else {
			topicDao.persist(newTopicModel);
			model.addAttribute("message", "New Topic '" + newTopicModel.getTitle() + "' added.");
		}

		return "forward:/index";
	}
	
	
	
	
	
	
	/*@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}

	/*@ExceptionHandler()	 
	@ResponseStatus(code=HttpStatus.FORBIDDEN) 
	public String handle403(Exception ex) {
		 
	return "login";
		 
	}*/
}
