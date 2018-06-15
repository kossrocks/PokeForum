package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.TopicRepository;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class TopicController {

	@Autowired
	TopicDao topicDao;

	@Autowired
	EntryDao entryDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	TopicRepository topicRepository;

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping("/deleteTopic")
	public String deleteData(Model model, @RequestParam int id) {
		topicDao.delete(id);

		return "forward:index";
	}

	@RequestMapping("/listEntries")
	public String listEntries(Model model, @RequestParam int id) {

		List<EntryModel> entries = entryDao.getAllEntriesInTopic(id);
		model.addAttribute("entries", entries);
		
		TopicModel topic = topicDao.getTopic(id);
		model.addAttribute("topic", topic);

		return "listEntries";
	}

	@RequestMapping(value = "/addTopic", method = RequestMethod.GET)
	public String showAddTopicForm(Model model) {
		

		return "addTopic";
	}

	@RequestMapping(value = "/addTopic", method = RequestMethod.POST)
	public String addTopic(Model model,
			@RequestParam("title") String title, @RequestParam("firstEntry") String firstEntry, Principal principal) {

		
		
		
		Date date = new Date();
		
		
		TopicModel topic = new TopicModel();
		topicDao.persist(topic);
		topic.setTitle(title);
		topic.setOwner(userDao.getUser(principal.getName()));
		topic.setLastEdited(date);
		topicDao.merge(topic);
		
		EntryModel entry = new EntryModel();
		entryDao.persist(entry);
		entry.setContent(firstEntry);
		entry.setDayOfCreation(date);
		entry.setEdited(false);
		entry.setOwner(userDao.getUser(principal.getName()));
		entry.setTopic(topic);
		
		entryDao.merge(entry);
		
		return "forward:/index";

	}
	
	@RequestMapping(value = "/addEntry", method = RequestMethod.GET)
	public String showAddEntryForm(Model model, @RequestParam int topicId) {
		model.addAttribute("topicId", topicId);

		return "addEntry";
	}

	@RequestMapping(value = "/addEntry", method = RequestMethod.POST)
	public String addEntry(Model model, @RequestParam("entry") String entry,  @RequestParam("topicId") int topicId, Principal principal) {

		
		
		
		Date date = new Date();
		TopicModel topic = topicDao.getTopic(topicId); 
		
		EntryModel newEntry = new EntryModel();
		entryDao.persist(newEntry);
		newEntry.setContent(entry);
		newEntry.setDayOfCreation(date);
		newEntry.setEdited(false);
		newEntry.setOwner(userDao.getUser(principal.getName()));
		newEntry.setTopic(topic);
		
		entryDao.merge(newEntry);
		
		return "forward:/index";

	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping("/deleteEntry")
	public String deleteEntry(Model model, @RequestParam int id) {
		entryDao.delete(id);
	

		return "forward:index";
	}

	/*
	 * @ExceptionHandler(Exception.class) public String handleAllException(Exception
	 * ex) {
	 * 
	 * return "error";
	 * 
	 * }
	 * 
	 * /*@ExceptionHandler()
	 * 
	 * @ResponseStatus(code=HttpStatus.FORBIDDEN) public String handle403(Exception
	 * ex) {
	 * 
	 * return "login";
	 * 
	 * }
	 */
}
