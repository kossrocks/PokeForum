package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;

@Controller
public class TopicController {

	@Autowired
	TopicDao topicDao;

	@Autowired
	EntryDao entryDao;

	@Autowired
	UserDao userDao;

	// deleting specific topic and all its entries
	@Secured({ "ROLE_USER" })
	@RequestMapping("/deleteTopic")
	@Transactional
	public String deleteData(Model model, @RequestParam int id, Principal principal) {

		TopicModel topic = topicDao.getTopic(id);
		String usernameOwner = topic.getOwner().getUserName();
		// only admins and topic owners can delete topics
		if (usernameOwner.equals(principal.getName()) || principal.getName().equalsIgnoreCase("admin")) {
			entryDao.deleteById(id);
			model.addAttribute("message", "Topic '" + topicDao.getTopic(id).getTitle() + "' deleted.");
			topicDao.deleteById(id);

			return "forward:index";
		} else {
			throw new AccessDeniedException("You are not allowed to delete this!");
		}
	}

	// shows all entries of specific Topic
	@RequestMapping("/listEntries")
	public String listEntries(Model model, @RequestParam int id, Principal principal) {

		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);

		// List with all entries of specific topic
		List<EntryModel> entries = entryDao.getAllEntriesInTopic(id);
		model.addAttribute("entries", entries);

		TopicModel topic = topicDao.getTopic(id);
		model.addAttribute("topic", topic);

		boolean isAdmin = false;
		for (UserRole role : user.getUserRoles()) {
			if (role.getRole().equalsIgnoreCase("role_admin"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);

		return "listEntries";
	}

	// shows add topic form
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addTopic", method = RequestMethod.GET)
	public String showAddTopicForm(Model model) {

		return "addTopic";
	}

	// adding new topic to database
	@Secured({ "ROLE_USER" })
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
		model.addAttribute("message", "New Topic " + topic.getTitle() + " added.");

		return "forward:/index";

	}

	// shows add Entry form
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addEntry", method = RequestMethod.GET)
	public String showAddEntryForm(Model model, @RequestParam int topicId) {
		// saving to which topic the new entry should belong
		model.addAttribute("topicId", topicId);

		return "addEntry";
	}

	// adding new entry
	@Secured({ "ROLE_USER" })
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
		if (user.getUserName().equalsIgnoreCase("admin"))
			isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		model.addAttribute("message", "New Entry added.");

		return "listEntries";

	}

	// show edit Entry form
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editEntry", method = RequestMethod.GET)
	public String showEditEntry(Model model, @RequestParam int id, Principal principal) {

		EntryModel entry = entryDao.getEntry(id);

		String usernameOwner = entry.getOwner().getUserName();
		// only the entry owner and admins can change entry
		if (usernameOwner.equals(principal.getName()) || principal.getName().equalsIgnoreCase("admin")) {

			model.addAttribute("entry", entry);

			return "addEntry";
		} else {
			throw new AccessDeniedException("You are not allowed to edit this.");
		}
	}

	// editing existing entry
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editEntry", method = RequestMethod.POST)
	public String editedEntry(Model model, @RequestParam int id, @RequestParam("entryText") String text,
			Principal principal) {
		EntryModel entry = entryDao.getEntry(id);
		entry.setContent(text);
		// if an entry is edited it will be shown on the webpage
		entry.setEdited(true);
		entryDao.merge(entry);

		TopicModel topic = topicDao.getTopic(entry.getTopic().getId());
		List<EntryModel> entries = entryDao.getAllEntriesInTopic(topic.getId());
		model.addAttribute("entries", entries);

		model.addAttribute("topic", topic);

		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);

		boolean isAdmin = false;
		if (user.getUserName().equalsIgnoreCase("admin"))
			isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		model.addAttribute("message", "Entry successfully edited.");

		return "listEntries";
	}

	// only entry owner and admins can delete it
	@Secured({ "ROLE_USER" })
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
			if (user.getUserName().equalsIgnoreCase("admin"))
				isAdmin = true;
			model.addAttribute("isAdmin", isAdmin);

			model.addAttribute("message", "Entry successfully deleted.");

			return "listEntries";
		} else {
			throw new AccessDeniedException("You are not allowed to delete this!");
		}

	}

	// listing all topics that have the searchstring in title or owner name
	@RequestMapping("/searchTopics")
	public String searchIt(Model model, @RequestParam String searchString, Principal principal) {

		List<TopicModel> topics = topicDao.getAllTopics();

		// symbols are not allowed in searchStrings
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(searchString);

		if (m.find()) {
			model.addAttribute("errorMessage", "Symbols are not allowed!");
		} else {
			topics = topicDao.searchTopic(searchString);
			if (topics.size() == 1) {
				model.addAttribute("message", "You found " + topics.size() + " topic.");
			} else {
				model.addAttribute("message", "You found " + topics.size() + " topics.");
			}
		}

		model.addAttribute("topics", topics);

		User user = userDao.getUser(principal.getName());
		model.addAttribute("user", user);

		boolean isAdmin = false;
		if (user.getUserName().equalsIgnoreCase("admin"))
			isAdmin = true;
		model.addAttribute("isAdmin", isAdmin);

		return "index";
	}

	@ExceptionHandler(Exception.class)
	public String handleAllException(Exception ex) {

		return "error";

	}
}
