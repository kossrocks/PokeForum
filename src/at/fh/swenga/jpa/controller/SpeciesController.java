package at.fh.swenga.jpa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.model.SpeciesModel;

@Controller
public class SpeciesController {

	
	@Autowired
	SpeciesDao speciesDao;
	
	
	@RequestMapping("/searchSpecies")
	public String searchSpecies(Model model, @RequestParam String searchString) {

		List<SpeciesModel> specieses = speciesDao.getAllSpecies();
		List<SpeciesModel> filteredSpecieses = new ArrayList<SpeciesModel>();
		
		for(SpeciesModel species : specieses) {
			
			if(species.getName().toLowerCase().contains(searchString.toLowerCase()) || species.getTypes().get(0).getName().toLowerCase().contains(searchString.toLowerCase()) || (species.getTypes().size() > 1 && species.getTypes().get(1).getName().toLowerCase().contains(searchString.toLowerCase()))){
				filteredSpecieses.add(species);
			}
			
		}
		model.addAttribute("pokemons", filteredSpecieses);
		return "pokemon";
	}
	
}
