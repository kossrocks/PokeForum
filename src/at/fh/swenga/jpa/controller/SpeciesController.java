package at.fh.swenga.jpa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.dao.TypeDao;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TypeModel;

@Controller
public class SpeciesController {

	
	@Autowired
	SpeciesDao speciesDao;
	
	@Autowired
	TypeDao typeDao;
	
	
	@RequestMapping("/searchSpecies")
	public String searchSpecies(Model model, @RequestParam String searchString) {

		List<SpeciesModel> specieses = speciesDao.getAllSpecies();
		List<SpeciesModel> filteredSpecieses = new ArrayList<SpeciesModel>();
		
		for(SpeciesModel species : specieses) {
			
			if(species.getName().toLowerCase().contains(searchString.toLowerCase()) || species.getTypes().get(0).getName().toLowerCase().contains(searchString.toLowerCase()) || (species.getTypes().size() > 1 && species.getTypes().get(1).getName().toLowerCase().contains(searchString.toLowerCase()))){
				filteredSpecieses.add(species);
			}
			
		}
		
		model.addAttribute("message", "You found " + filteredSpecieses.size() + " species.");
		
		model.addAttribute("pokemons", filteredSpecieses);
		return "pokemon";
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addSpecies", method = RequestMethod.GET)
	public String showAddSpeciesForm(Model model) {
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types",types);
		
		return "editSpecies";
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addSpecies", method = RequestMethod.POST)
	public String addSpeciesForm(Model model, @RequestParam("name") String name, @RequestParam("type1") int type1, @RequestParam("type2") int type2, @RequestParam("baseHealthPoints") float baseHealthPoints, @RequestParam("baseAttack") float baseAttack, @RequestParam("baseDefense") float baseDefense, @RequestParam("baseSpecialAttack") float baseSpecialAttack, @RequestParam("baseSpecialDefense") float baseSpecialDefense,@RequestParam("baseSpeed") float baseSpeed) {
		
		if(speciesDao.searchSpeciesByName(name.toLowerCase()) == null) {
			
			SpeciesModel newSpecies = new SpeciesModel();
			
			TypeModel addedType1 = typeDao.getType(type1);
			
			newSpecies.setName(name);
			newSpecies.addType(addedType1);
			if(type2 != 0) {
				TypeModel addedType2 = typeDao.getType(type2);
				newSpecies.addType(addedType2);
			}
			newSpecies.setBaseHealthPoints(baseHealthPoints);
			newSpecies.setBaseAttack(baseAttack);
			newSpecies.setBaseDefense(baseDefense);
			newSpecies.setBaseSpecialAttack(baseSpecialAttack);
			newSpecies.setBaseSpecialDefense(baseSpecialDefense);
			newSpecies.setBaseSpeed(baseSpeed);
			
			speciesDao.merge(newSpecies);
			model.addAttribute("message", "New Species " + name + " added");
			
		}else {
			model.addAttribute("errorMessage","Species already exists");
		}
		
		
		List<SpeciesModel> pokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", pokemons);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);			
		
		return "pokemon";
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping("/deleteSpecies")
	public String deleteSpecies(Model model, @RequestParam int id) {
		
		String speciesName = speciesDao.searchSpeciesById(id).getName();
		
		speciesDao.deleteById(id);
		
		
		
		model.addAttribute("message", "Species " + speciesName + " deleted");
		
		List<SpeciesModel> pokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", pokemons);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);			
		
		return "pokemon";
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/editSpecies", method = RequestMethod.GET)
	public String showEditSpeciesForm(Model model, @RequestParam int id) {
		
		SpeciesModel species = speciesDao.searchSpeciesById(id);
		model.addAttribute("species", species);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types",types);
		
		return "editSpecies";
	}
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/editSpecies", method = RequestMethod.POST)
	public String editSpeciesForm(Model model,@RequestParam int id, @RequestParam("name") String name, @RequestParam("type1") int type1, @RequestParam("type2") int type2, @RequestParam("baseHealthPoints") float baseHealthPoints, @RequestParam("baseAttack") float baseAttack, @RequestParam("baseDefense") float baseDefense, @RequestParam("baseSpecialAttack") float baseSpecialAttack, @RequestParam("baseSpecialDefense") float baseSpecialDefense,@RequestParam("baseSpeed") float baseSpeed) {
		
		if(speciesDao.searchSpeciesById(id) != null) {
			
			SpeciesModel newSpecies = speciesDao.searchSpeciesById(id);
			
			newSpecies.setTypes(new ArrayList<TypeModel>());
			TypeModel addedType1 = typeDao.getType(type1);
			
			newSpecies.setName(name);
			newSpecies.addType(addedType1);
			if(type2 != 0) {
				TypeModel addedType2 = typeDao.getType(type2);
				newSpecies.addType(addedType2);
			}
			newSpecies.setBaseHealthPoints(baseHealthPoints);
			newSpecies.setBaseAttack(baseAttack);
			newSpecies.setBaseDefense(baseDefense);
			newSpecies.setBaseSpecialAttack(baseSpecialAttack);
			newSpecies.setBaseSpecialDefense(baseSpecialDefense);
			newSpecies.setBaseSpeed(baseSpeed);
			
			speciesDao.merge(newSpecies);
			model.addAttribute("message", "Species " + name + " edited");
			
		}else {
			model.addAttribute("errorMessage","Species doesn't exists");
		}
		
		
		List<SpeciesModel> pokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", pokemons);
		
		List<TypeModel> types = typeDao.getAllTypes();
		model.addAttribute("types", types);			
		
		return "pokemon";
	}
}
