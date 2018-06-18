package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TypeModel;

@Controller
public class CalculatorController {
	
	@Autowired
	AttackDao attackDao;
	
	@Autowired
	PokemonDao pokemonDao;
	
	@Autowired
	SpeciesDao speciesDao;
	
	@Secured({ "ROLE_USER" })
	@RequestMapping("/calculate")
	public String calculate(Model model, Principal principal, @RequestParam("yourPokemon") int yourPokemonId, @RequestParam("yourLevel") int yourLevel, @RequestParam("yourAttack") int attackId, @RequestParam("enemyPokemon") int enemyId, @RequestParam("enemyLevel") int enemyLevel ) {

		PokemonModel yourPokemon = pokemonDao.getPokemonById(yourPokemonId);
		SpeciesModel enemyPokemon = speciesDao.searchSpeciesById(enemyId);
		AttackModel yourAttack = attackDao.searchAttackById(attackId);
		
		float yourAtkpoints = 0;
		float enemyDefpoints = 0;
		float damage = 0;
		
		yourPokemon.setLevel(yourLevel);
		yourPokemon.recalculateStats();
		
		if(yourAttack.getCategory().getName().equalsIgnoreCase("special")) {
			yourAtkpoints = yourPokemon.getSpecialAttack();
			enemyDefpoints = (2f * enemyPokemon.getBaseSpecialDefense() * enemyLevel / 100f) + 5;
		}else if(yourAttack.getCategory().getName().equalsIgnoreCase("physical")) {
			yourAtkpoints = yourPokemon.getAttack();
			enemyDefpoints = (2f * enemyPokemon.getBaseDefense() * enemyLevel / 100f) + 5;
		}
		
		float stab = 1;
		float adv = 1;
		
		for(TypeModel type : yourPokemon.getTypes()) {
			if(type.getName().equals(yourAttack.getName())) {
				stab += 0.5f;
			}			
		}
		
		for(TypeModel type : enemyPokemon.getTypes()) {
			if(yourAttack.getType().getGoodAgainst().contains(type.getName().toLowerCase())) {
				adv *= 1.5f;
			}
		}
		
		damage = ((yourLevel * (2/5) + 2) * yourAttack.getBasePower() * (yourAtkpoints / enemyDefpoints) + 2) * stab * adv;
		
		
		//write damage and add Attributes!!!!!!
		
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);	
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("yourPokemons", pokemons);
		
		List<SpeciesModel> allPokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", allPokemons);
		
		
		

		
		return "calculator";
	}
}
