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
	
	// calculate the damage a pokemon makes with certain attack
	@Secured({ "ROLE_USER" })
	@RequestMapping("/calculate")
	public String calculate(Model model, Principal principal, @RequestParam("yourPokemon") int yourPokemonId, @RequestParam("yourLevel") float yourLevel, @RequestParam("yourAttack") int attackId, @RequestParam("enemyPokemon") int enemyId, @RequestParam("enemyLevel") float enemyLevel ) {

		PokemonModel yourPokemon = pokemonDao.getPokemonById(yourPokemonId);
		SpeciesModel enemyPokemon = speciesDao.searchSpeciesById(enemyId);
		AttackModel yourAttack = attackDao.searchAttackById(attackId);
		
		float yourAtkpoints = 0;
		float enemyDefpoints = 0;
		float damage = 0;
		
		//set the level of pokemon in team. It will not be changed in the database. This change only is for calculating purpose
		yourPokemon.setLevel(yourLevel);
		//due to the changed level, its stats need to be recalculated
		yourPokemon.recalculateStats();
		
		//depending on the attack category slightly different function variables are used to calculate the attack damage
		if(yourAttack.getCategory().getName().equalsIgnoreCase("special")) {
			yourAtkpoints = yourPokemon.getSpecialAttack();
			enemyDefpoints = (2f * enemyPokemon.getBaseSpecialDefense() * enemyLevel / 100f) + 5f;
		}else if(yourAttack.getCategory().getName().equalsIgnoreCase("physical")) {
			yourAtkpoints = yourPokemon.getAttack();
			enemyDefpoints = (2f * enemyPokemon.getBaseDefense() * enemyLevel / 100f) + 5f;
		}
		
		float stab = 1;
		float adv = 1;
		
		//if a pokemon has the same type as the attack it is using, it gets an attack bonus
		for(TypeModel type : yourPokemon.getTypes()) {
			if(type.getName().equals(yourAttack.getName())) {
				stab += 0.5f;
			}			
		}
		
		//certain types are good, weak, ineffective against others
		for(TypeModel type : enemyPokemon.getTypes()) {
			if(yourAttack.getType().getGoodAgainst().contains(type.getName().toLowerCase())) {
				adv *= 2f;
			}
			if(yourAttack.getType().getWeakAgainst().contains(type.getName().toLowerCase())) {
				adv *= 0.5f;
			}
			if(yourAttack.getType().getNoDamageAgainst().contains(type.getName().toLowerCase())) {
				adv *= 0f;
			}
		}
		
		//damage is calculated
		damage = ((yourLevel * (2f/5f) + 2f) * yourAttack.getBasePower() * (yourAtkpoints / (50f *enemyDefpoints)) + 2f) * stab * adv;
		
		//reamaining enemy healthpoints
		float enemyHp = (2f * enemyPokemon.getBaseHealthPoints() * enemyLevel / 100f) + 10f + enemyLevel - damage;
		
		//for more user friendly display of remaining hp of enemy. User associates enemy defeat with 0 hp.
		if(enemyHp < 0) enemyHp = 0;
		
		model.addAttribute("message", "Your attack made " + Math.round(damage) + " damage and your enemy has " + Math.round(enemyHp) + " HP left.");
		
		
		model.addAttribute("lastChosen", yourPokemon);
		
		model.addAttribute("lastAttack", yourAttack);
		
		model.addAttribute("lastEnemy",enemyPokemon);
		
		model.addAttribute("lastEnemyLevel", enemyLevel);
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);	
		
		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("yourPokemons", pokemons);
		
		List<SpeciesModel> allPokemons = speciesDao.getAllSpecies();
		model.addAttribute("pokemons", allPokemons);
		
		
		

		
		return "calculator";
	}
}
