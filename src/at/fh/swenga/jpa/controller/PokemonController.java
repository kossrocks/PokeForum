package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.fh.swenga.jpa.dao.AttackDao;
import at.fh.swenga.jpa.dao.PokemonDao;
import at.fh.swenga.jpa.dao.SpeciesDao;
import at.fh.swenga.jpa.dao.UserDao;
import at.fh.swenga.jpa.model.AttackModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TypeModel;
import at.fh.swenga.jpa.model.User;

@Controller
public class PokemonController {

	@Autowired
	PokemonDao pokemonDao;

	@Autowired
	AttackDao attackDao;

	@Autowired
	SpeciesDao speciesDao;

	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/addPokemon", method = RequestMethod.GET)
	public String showAddPokemonForm(Model model) {

		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);

		List<SpeciesModel> specieses = speciesDao.getAllSpecies();
		model.addAttribute("specieses", specieses);

		return "editTeamMember";
	}

	@RequestMapping(value = "/addPokemon", method = RequestMethod.POST)
	public String addNewPokemon(Model model, Principal principal, @RequestParam("name") String name,
			@RequestParam("species") String species, @RequestParam("level") int level,
			@RequestParam("attack1") String attack1, @RequestParam("attack2") String attack2,
			@RequestParam("attack3") String attack3, @RequestParam("attack4") String attack4,
			@RequestParam("gender") String gender, @RequestParam("shiny") String shiny) {
		if (attack1.equals(attack2) || attack1.equals(attack3) || attack1.equals(attack4) || attack2.equals(attack3)
				|| attack2.equals(attack4) || attack3.equals(attack4)) {
			model.addAttribute("errorMessage", "a Pokemon cannot learn the same attack more than once!");

		} else {
			PokemonModel newPokemon = new PokemonModel();

			SpeciesModel newSpecies = speciesDao.searchSpeciesByName(species);

			TypeModel type1 = newSpecies.getTypes().get(0);
			if (newSpecies.getTypes().size() > 1) {
				TypeModel type2 = newSpecies.getTypes().get(1);
				newPokemon.addType(type2);
			}

			User currentUser = userDao.getUser(principal.getName());

			AttackModel newAttack1 = attackDao.getAttackByName(attack1);
			AttackModel newAttack2 = attackDao.getAttackByName(attack2);
			AttackModel newAttack3 = attackDao.getAttackByName(attack3);
			AttackModel newAttack4 = attackDao.getAttackByName(attack4);

			boolean newShiny = false;
			if(shiny.equals("yes")) newShiny = true;
			
			int hp = newSpecies.getBaseHealthPoints();
			int atk = newSpecies.getBaseAttack();
			int def = newSpecies.getBaseDefense();
			int spatk = newSpecies.getBaseSpecialAttack();
			int spdef = newSpecies.getBaseSpecialDefense();
			int spe = newSpecies.getBaseSpeed();
			String speciesName = newSpecies.getName();

			newPokemon.setName(name);

			newPokemon.setSpecies(speciesName);

			newPokemon.addType(type1);

			newPokemon.setBaseHP(hp);
			newPokemon.setBaseATK(atk);
			newPokemon.setBaseDEF(def);
			newPokemon.setBaseSPATK(spatk);
			newPokemon.setBaseSPDEF(spdef);
			newPokemon.setBaseSPE(spe);
			newPokemon.setLevel(level);
			newPokemon.setGender(gender);
			newPokemon.setShiny(newShiny);
			newPokemon.setOwner(currentUser);

			newPokemon.recalculateStats();

			newPokemon.addAttack(newAttack1);
			newPokemon.addAttack(newAttack2);
			newPokemon.addAttack(newAttack3);
			newPokemon.addAttack(newAttack4);
			newPokemon.setOwner(currentUser);

			pokemonDao.merge(newPokemon);
		}

		int id = userDao.getUser(principal.getName()).getId();
		User user = userDao.getUserById(id);
		model.addAttribute("user", user);
		model.addAttribute("id", id);

		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);

		return "profile";
	}

	@RequestMapping(value = "/editPokemon", method = RequestMethod.GET)
	public String showEditPokemonForm(Model model, @RequestParam int id) {
		
		PokemonModel pokemon = pokemonDao.getPokemonById(id);
		model.addAttribute("pokemon", pokemon);		
		
		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);

		List<SpeciesModel> specieses = speciesDao.getAllSpecies();
		model.addAttribute("specieses", specieses);
		
		return "editTeamMember";
	}
}
