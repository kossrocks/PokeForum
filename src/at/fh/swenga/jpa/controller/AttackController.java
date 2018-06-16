package at.fh.swenga.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.model.AttackModel;

@Controller
public class AttackController {

	@Autowired
	AttackDao attackDao;
	
	@RequestMapping("/searchAttacks")
	public String searchIt(Model model, @RequestParam String searchString) {
			
		List<AttackModel> attacks = attackDao.searchAttack(searchString);
		
		model.addAttribute("attacks", attacks);

		return "attacks";
	}
	
	
}
