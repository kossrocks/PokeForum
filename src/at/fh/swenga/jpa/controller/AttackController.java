package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.dao.CategoryDao;
import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.CategoryModel;
import at.fh.swenga.jpa.model.EntryModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TopicModel;
import at.fh.swenga.jpa.model.TypeModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class AttackController {

	@Autowired
	AttackDao attackDao;
	
	
	@Autowired
	TypeDao typeDao;
	
	@Autowired
	CategoryDao categoryDao;
	
	// searching for attack names, types, categories, battle effects
	@RequestMapping("/searchAttacks")
	public String searchIt(Model model, @RequestParam String searchString) {
			
		//getting list of attacks that contain searchstring in name, type category and/or battle effect
		List<AttackModel> attacks = attackDao.searchAttack(searchString.toLowerCase());
		
		model.addAttribute("attacks", attacks);
		
		if(attacks.size() == 1) {
			model.addAttribute("message", "You found " + attacks.size() + " attack.");
		}else {
		model.addAttribute("message", "You found " + attacks.size() + " attacks.");
		}

		return "attacks";
	}
		
	// forwarding the admin to the editAttack.html
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addAttack", method = RequestMethod.GET)
	public String showAddSpeciesForm(Model model) {
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks",attacks);		
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);		
		
		List<CategoryModel> categories = categoryDao.getAllCategories();
		model.addAttribute("categories",categories);
		
		return "editAttack";
	}
	
	
	
	
	// creating new attack with the help of various RequestParameters
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addAttack", method = RequestMethod.POST)
	public String addAttackForm(Model model, @RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("category") int category, @RequestParam("basePower") float basePower, @RequestParam("accuracy") float accuracy, @RequestParam("powerPoints") float powerPoints, @RequestParam("battleEffect") String battleEffect) {
		
		
		if(attackDao.searchAttackByName(name.toLowerCase()) == null)  {
			
			AttackModel newAttack = new AttackModel();
			
			TypeModel addType = typeDao.getType(type);
			
			CategoryModel addCategory = categoryDao.getCategory(category);
			
			
			newAttack.setName(name);
			newAttack.setType(addType);
			newAttack.setCategory(addCategory);
			newAttack.setBasePower(basePower);
			newAttack.setAccuracy(accuracy);
			newAttack.setPowerPoints(powerPoints);
			newAttack.setBattleEffect(battleEffect);
			
			
			
			attackDao.merge(newAttack);
			model.addAttribute("message", "New Attack " + name + " has been added");
			
		}else {
			model.addAttribute("errorMessage","Attack already exists");
		}
		
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);		
		
		List<CategoryModel> categories = categoryDao.getAllCategories();
		model.addAttribute("categories",categories);
		
		return "attacks";
	}
	
	// admin can delete every attack
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping("/deleteAttack")
	public String deleteAttack(Model model, @RequestParam int id) {
		
		String attackName = attackDao.searchAttackById(id).getName();
		
		attackDao.deleteById(id);
		
		
		
		model.addAttribute("message", "Attack " + attackName + " deleted");
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);		
		
		List<CategoryModel> categories = categoryDao.getAllCategories();
		model.addAttribute("categories",categories);
		
		return "attacks";
	}
	
	
	//forwarding admin to editAttack page
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/editAttack", method = RequestMethod.GET)
	public String showEditAttackForm(Model model, @RequestParam int id) {
		
		AttackModel attack = attackDao.searchAttackById(id);
		model.addAttribute("attack", attack);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types",types);
		
		List<CategoryModel> categories = categoryDao.getAllCategories();
		model.addAttribute("categories",categories);
		return "editAttack";
	}
	
	
	//allowing admin to edit attack
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/editAttack", method = RequestMethod.POST)
	public String editAttackForm(Model model, @RequestParam("id") int id,@RequestParam("name") String name, @RequestParam("type") int type, @RequestParam("category") int category, @RequestParam("basePower") float basePower, @RequestParam("accuracy") float accuracy, @RequestParam("powerPoints") float powerPoints, @RequestParam("battleEffect") String battleEffect) {
		
		if(attackDao.searchAttackById(id) != null) {
			
			AttackModel newAttack = attackDao.searchAttackById(id);
			
			newAttack.setName(name);
			
			TypeModel addType = typeDao.getType(type);
			newAttack.setType(addType);
			
			CategoryModel addCategory = categoryDao.getCategory(category);
			newAttack.setCategory(addCategory);
			
			
			newAttack.setBasePower(basePower);
			newAttack.setAccuracy(accuracy);
			newAttack.setPowerPoints(powerPoints);
			newAttack.setBattleEffect(battleEffect);
			
			attackDao.merge(newAttack);
			model.addAttribute("message", "Attack " + name + " edited");
			
		}else {
			model.addAttribute("errorMessage","Attack doesn't exists");
		}
		
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);
		
		List<CategoryModel> categories = categoryDao.getAllCategories();
		model.addAttribute("categories",categories);
		
		return "attacks";
	}
	
	
	
	
	
}
