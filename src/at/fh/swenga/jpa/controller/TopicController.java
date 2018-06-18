package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.EntryRepository;
import at.fh.swenga.jpa.dao.TopicDao;
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
	EntryRepository entryRepository;

	@RequestMapping("/deleteTopic")
	@Transactional
	public String deleteData(Model model, @RequestParam int id, Principal principal) {

		TopicModel topic = topicDao.getTopic(id);
		String usernameOwner = topic.getOwner().getUserName();
		if (usernameOwner.equals(principal.getName()) || principal.getName().equalsIgnoreCase("admin")) {
			entryDao.deleteById(id);
			topicDao.deleteById(id);
			return "forward:index";
		} else {
			throw new AccessDeniedException("You are not allowed to delete this!");
		}
	}

	@RequestMapping("/listEntries")
	public String listEntries(Model model, @RequestParam int id, Principal principal) {

		
		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);
		
		List<EntryModel> entries = entryDao.getAllEntriesInTopic(id);
		model.addAttribute("entries", entries);

		TopicModel topic = topicDao.getTopic(id);
		model.addAttribute("topic", topic);
		
		boolean isAdmin = false;
		if(user.getUserName().equalsIgnoreCase("admin")) isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		return "listEntries";
	}

	@RequestMapping(value = "/addTopic", method = RequestMethod.GET)
	public String showAddTopicForm(Model model) {

		return "addTopic";
	}

	@RequestMapping(value = "/addTopic", method = RequestMethod.POST)
	public String addTopic(Model model, @RequestParam("title") String title,
			@RequestParam("firstEntry") String firstEntry, Principal principal) {

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
	public String addEntry(Model model, @RequestParam("entryText") String entry, @RequestParam("topicId") int topicId,
			Principal principal) {

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

		List<EntryModel> entries = entryDao.getAllEntriesInTopic(topicId);
		model.addAttribute("entries", entries);

		model.addAttribute("topic", topic);
		
		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);
		
		boolean isAdmin = false;
		if(user.getUserName().equalsIgnoreCase("admin")) isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		return "listEntries";

	}

	@RequestMapping(value = "/editEntry", method = RequestMethod.GET)
	public String showEditEntry(Model model, @RequestParam int id, Principal principal) {

		EntryModel entry = entryDao.getEntry(id);

		String usernameOwner = entry.getOwner().getUserName();
		if (usernameOwner.equals(principal.getName()) || principal.getName().equalsIgnoreCase("admin")) {

			model.addAttribute("entry", entry);

			return "addEntry";
		} else {
			throw new AccessDeniedException("You are not allowed to edit this, Bitch!");
		}
	}

	@RequestMapping(value = "/editEntry", method = RequestMethod.POST)
	public String editedEntry(Model model, @RequestParam int id, @RequestParam("entryText") String text, Principal principal) {
		EntryModel entry = entryDao.getEntry(id);
		entry.setContent(text);
		entry.setEdited(true);
		entryDao.merge(entry);

		TopicModel topic = topicDao.getTopic(entry.getTopic().getId());
		List<EntryModel> entries = entryDao.getAllEntriesInTopic(topic.getId());
		model.addAttribute("entries", entries);

		model.addAttribute("topic", topic);
		
		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);
		
		boolean isAdmin = false;
		if(user.getUserName().equalsIgnoreCase("admin")) isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		return "listEntries";
	}

	@RequestMapping("/deleteEntry")
	public String deleteEntry(Model model, @RequestParam int id, @RequestParam int topicId, Principal principal) {

		EntryModel entry = entryDao.getEntry(id);
		String username = entry.getOwner().getUserName();
		if (username.equals(principal.getName()) || principal.getName().equalsIgnoreCase("admin")) {
			entryDao.delete(id);

			List<EntryModel> entries = entryDao.getAllEntriesInTopic(topicId);
			model.addAttribute("entries", entries);

			TopicModel topic = topicDao.getTopic(topicId);

			model.addAttribute("topic", topic);
			
			User user = userDao.getUser(principal.getName());
			model.addAttribute("user", user);
			
			boolean isAdmin = false;
			if(user.getUserName().equalsIgnoreCase("admin")) isAdmin = true;
			model.addAttribute("isAdmin", isAdmin);

			return "listEntries";
		} else {
			throw new AccessDeniedException("You are not allowed to delete this!");
		}

	}

	@RequestMapping("/searchTopics")
	public String searchIt(Model model, @RequestParam String searchString, Principal principal) {
			
		List<TopicModel> topics = topicDao.searchTopic(searchString);
		
		model.addAttribute("topics", topics);
		
		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);
		
		boolean isAdmin = false;
		if(user.getUserName().equalsIgnoreCase("admin")) isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		return "index";
	}

	
	
	
	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}

	/*
	 * @ExceptionHandler()
	 * 
	 * @ResponseStatus(code=HttpStatus.FORBIDDEN) public String handle403(Exception
	 * ex) {
	 * 
	 * return "login";
	 * 
	 * }
	 */
}
