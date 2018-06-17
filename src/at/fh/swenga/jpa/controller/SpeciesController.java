package at.fh.swenga.jpa.controller;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class SpeciesController {

	
	@Autowired
	SpeciesDao speciesDao;
	
	
	@RequestMapping("/searchSpecies")
	public String searchSpecies(Model model, @RequestParam String searchString) {

		List<SpeciesModel> specieses = speciesDao.searchSpecies(searchString);

		model.addAttribute("pokemons", specieses);

		return "pokemon";
	}
	
}
