package at.fh.swenga.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.DocumentDao;
import at.fh.swenga.jpa.dao.EntryDao;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.dao.TopicDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.dao.UserRoleDao;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.TopicModel;

@Controller
public class NavigationController {

	@Autowired
	TopicDao topicDao;
	
	@Autowired
	AttackDao attackDao;
	
	@Autowired
	DocumentDao documentDao;
	
	@Autowired
	EntryDao entryDao;
	
	@Autowired
	PokemonDao pokemonDao;
	
	@Autowired
	SpeciesDao speciesDao;
	
	@Autowired
	TypeDao typeDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserRoleDao userRoleDao;
	
	@RequestMapping(value = { "/", "index" })
	public String index(Model model) {

		List<TopicModel> topics = topicDao.getAllTopics();
		model.addAttribute("topics", topics);
		
		
		
		
		return "index";
	}
	
	@RequestMapping("/signUp")
	public String signUp(Model model) {



		
		return "signUp";
	}
	
	@RequestMapping("/listEntries")
	public String listEntries(Model model, @RequestParam int id) {
		
		List<EntryModel> entries = entryDao.getAllEntriesInTopic(id);
		model.addAttribute("entries",entries);


		
		return "listEntries";
	}	
	
	/*public String deleteData(Model model, @RequestParam int id) {
		employeeDao.delete(id);

		return "forward:list";
	}*/
	
	@RequestMapping("/profile")
	public String profile(Model model) {



		
		return "profile";
	}
	
	@RequestMapping("/users")
	public String users(Model model) {



		
		return "users";
	}
	
	@RequestMapping("/attacks")
	public String attacks(Model model) {



		
		return "attacks";
	}
	
	@RequestMapping("/pokemon")
	public String pokemon(Model model) {



		
		return "pokemon";
	}
	
	@RequestMapping("/editTeamMember")
	public String editTeamMember(Model model) {



		
		return "editTeamMember";
	}
	
	@RequestMapping("/addEntry")
	public String addEntry(Model model) {



		
		return "addEntry";
	}
	
	@RequestMapping("/addTopic")
	public String addTopic(Model model) {



		
		return "addTopic";
	}
	
	@RequestMapping("/editSpecies")
	public String editSpecies(Model model) {



		
		return "editSpecies";
	}
	
}

