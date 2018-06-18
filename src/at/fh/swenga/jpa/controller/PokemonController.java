package at.fh.swenga.jpa.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
import at.fh.swenga.jpa.model.DocumentModel;
import at.fh.swenga.jpa.model.PokemonModel;
import at.fh.swenga.jpa.model.SpeciesModel;
import at.fh.swenga.jpa.model.TypeModel;
import at.fh.swenga.jpa.model.User;
import at.fh.swenga.jpa.model.UserRole;

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

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addPokemon", method = RequestMethod.GET)
	public String showAddPokemonForm(Model model) {

		List<AttackModel> attacks = attackDao.getAllAttacks();
		model.addAttribute("attacks", attacks);

		List<SpeciesModel> specieses = speciesDao.getAllSpecies();
		model.addAttribute("specieses", specieses);

		return "editTeamMember";
	}

	@Secured({ "ROLE_USER" })
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
			if (shiny.equals("yes"))
				newShiny = true;

			float hp = newSpecies.getBaseHealthPoints();
			float atk = newSpecies.getBaseAttack();
			float def = newSpecies.getBaseDefense();
			float spatk = newSpecies.getBaseSpecialAttack();
			float spdef = newSpecies.getBaseSpecialDefense();
			float spe = newSpecies.getBaseSpeed();
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
			model.addAttribute("message", "New Pokemon " + newPokemon.getSpecies() + " in Team.");
		}

		int id = userDao.getUser(principal.getName()).getId();
		User user = userDao.getUserById(id);
		model.addAttribute("user", user);
		model.addAttribute("id", id);

		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		
		if (user.getPicture() != null) {

			
			DocumentModel pp = user.getPicture();
			byte[] profilePicture = pp.getContent();

			StringBuilder sb = new StringBuilder();
			sb.append("data:image/jpeg;base64,");
			sb.append(Base64.encodeBase64String(profilePicture));
			String image = sb.toString();

			model.addAttribute("image", image);
		}

		return "profile";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editPokemon", method = RequestMethod.GET)
	public String showEditPokemonForm(Model model, @RequestParam int id, Principal principal) {

		PokemonModel pokemon = pokemonDao.getPokemonById(id);
		model.addAttribute("pokemon", pokemon);
		
		User user = userDao.getUser(principal.getName());
		
		boolean isAdmin = false;
		for(UserRole role : user.getUserRoles()) {
			if(role.getRole().equalsIgnoreCase("role_admin")) isAdmin = true;
		} 

		if (pokemon.getOwner().getId() == userDao.getUser(principal.getName()).getId() || isAdmin) {

			List<AttackModel> attacks = attackDao.getAllAttacks();
			model.addAttribute("attacks", attacks);

			List<SpeciesModel> specieses = speciesDao.getAllSpecies();
			model.addAttribute("specieses", specieses);

			return "editTeamMember";
		}else {
			
			model.addAttribute("user", user);
			model.addAttribute("id", user.getId());

			List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
			model.addAttribute("pokemons", pokemons);
			model.addAttribute("errorMessage", "You cannot edit Pokemon that are not yours!");
			
			if (user.getPicture() != null) {

				
				DocumentModel pp = user.getPicture();
				byte[] profilePicture = pp.getContent();

				StringBuilder sb = new StringBuilder();
				sb.append("data:image/jpeg;base64,");
				sb.append(Base64.encodeBase64String(profilePicture));
				String image = sb.toString();

				model.addAttribute("image", image);
			}

			return "profile";
		}
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/editPokemon", method = RequestMethod.POST)
	public String editNewPokemon(Model model, Principal principal, @RequestParam int id,
			@RequestParam("name") String name, @RequestParam("species") String species,
			@RequestParam("level") int level, @RequestParam("attack1") String attack1,
			@RequestParam("attack2") String attack2, @RequestParam("attack3") String attack3,
			@RequestParam("attack4") String attack4, @RequestParam("gender") String gender,
			@RequestParam("shiny") String shiny) {

		if (attack1.equals(attack2) || attack1.equals(attack3) || attack1.equals(attack4) || attack2.equals(attack3)
				|| attack2.equals(attack4) || attack3.equals(attack4)) {
			model.addAttribute("errorMessage", "a Pokemon cannot learn the same attack more than once!");

		} else {
			PokemonModel pokemon = pokemonDao.getPokemonById(id);

			SpeciesModel newSpecies = speciesDao.searchSpeciesByName(species);

			pokemon.setTypes(new ArrayList<TypeModel>());
			TypeModel type1 = newSpecies.getTypes().get(0);
			if (newSpecies.getTypes().size() > 1) {
				TypeModel type2 = newSpecies.getTypes().get(1);
				pokemon.addType(type2);
			}


			AttackModel newAttack1 = attackDao.getAttackByName(attack1);
			AttackModel newAttack2 = attackDao.getAttackByName(attack2);
			AttackModel newAttack3 = attackDao.getAttackByName(attack3);
			AttackModel newAttack4 = attackDao.getAttackByName(attack4);

			boolean newShiny = false;
			if (shiny.equals("yes"))
				newShiny = true;

			float hp = newSpecies.getBaseHealthPoints();
			float atk = newSpecies.getBaseAttack();
			float def = newSpecies.getBaseDefense();
			float spatk = newSpecies.getBaseSpecialAttack();
			float spdef = newSpecies.getBaseSpecialDefense();
			float spe = newSpecies.getBaseSpeed();
			String speciesName = newSpecies.getName();

			pokemon.setName(name);

			pokemon.setSpecies(speciesName);

			pokemon.addType(type1);

			pokemon.setBaseHP(hp);
			pokemon.setBaseATK(atk);
			pokemon.setBaseDEF(def);
			pokemon.setBaseSPATK(spatk);
			pokemon.setBaseSPDEF(spdef);
			pokemon.setBaseSPE(spe);
			pokemon.setLevel(level);
			pokemon.setGender(gender);
			pokemon.setShiny(newShiny);

			pokemon.recalculateStats();

			pokemon.setAttacks(new ArrayList<AttackModel>());

			pokemon.addAttack(newAttack1);
			pokemon.addAttack(newAttack2);
			pokemon.addAttack(newAttack3);
			pokemon.addAttack(newAttack4);

			pokemonDao.merge(pokemon);
			model.addAttribute("message", pokemon.getSpecies() + " successfully edited.");
		}

		int userId = userDao.getUser(principal.getName()).getId();
		User user = userDao.getUserById(userId);
		model.addAttribute("user", user);
		model.addAttribute("id", userId);

		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		
		if (user.getPicture() != null) {

			
			DocumentModel pp = user.getPicture();
			byte[] profilePicture = pp.getContent();

			StringBuilder sb = new StringBuilder();
			sb.append("data:image/jpeg;base64,");
			sb.append(Base64.encodeBase64String(profilePicture));
			String image = sb.toString();

			model.addAttribute("image", image);
		}

		return "profile";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping("/deletePokemon")
	public String deletePokemon(Model model, @RequestParam int id, Principal principal) {

		int userId = userDao.getUser(principal.getName()).getId();
		User user = userDao.getUserById(userId);

		PokemonModel pokemon = pokemonDao.getPokemonById(id);
		
		boolean isAdmin = false;
		
		for(UserRole role : user.getUserRoles()) {
			if(role.getRole().equalsIgnoreCase("role_admin")) isAdmin = true;
		}

		if (pokemon.getOwner().getId() == userId || isAdmin) {

			model.addAttribute("message", pokemonDao.getPokemonById(id).getSpecies() + " successfully deleted.");
			pokemonDao.deleteById(id);
		} else {
			model.addAttribute("errorMessage", "You cannot delete Pokemon that are not yours!");
		}
		model.addAttribute("user", user);
		model.addAttribute("id", userId);

		List<PokemonModel> pokemons = pokemonDao.getAllPokemonsOfUser(principal.getName());
		model.addAttribute("pokemons", pokemons);
		
		if (user.getPicture() != null) {

			
			DocumentModel pp = user.getPicture();
			byte[] profilePicture = pp.getContent();

			StringBuilder sb = new StringBuilder();
			sb.append("data:image/jpeg;base64,");
			sb.append(Base64.encodeBase64String(profilePicture));
			String image = sb.toString();

			model.addAttribute("image", image);
		}

		return "profile";

	}

}
