package at.fh.swenga.jpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "pokemons")
public class PokemonModel implements java.io.Serializable{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", length = 45)
	private String name;
	
	@ManyToOne (cascade = CascadeType.PERSIST)
	private SpeciesModel species;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	private List<TypeModel> types;
	
	@Column(name = "level")
	private int level;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	private List<AttackModel> attacks;
	
	@Column(name = "HP")
	private float healthPoints;
	
	@Column(name = "ATK")
	private float attack;
	
	@Column(name = "DEF")
	private float defense;
	
	@Column(name = "SPATK")
	private float specialAttack;
	
	@Column(name = "SPDEF")
	private float specialDefense;
	
	@Column(name = "SPE")
	private float speed;
	
	@Column(name = "gender")
	private String gender;
	
	private boolean shiny;
	
	@ManyToOne (cascade = CascadeType.PERSIST)
	private User owner;
	
	@Version
	long version;
	
	public PokemonModel() {
		// TODO Auto-generated constructor stub
	}

	public PokemonModel(String name, SpeciesModel species, int level, String gender,
			boolean shiny) {
		super();
		this.name = name;
		this.species = species;
		this.level = level;
		this.gender = gender;
		this.shiny = shiny;
		
		this.healthPoints = (((2 * species.getBaseHealthPoints()) * level) / 100) + level;
		this.attack = (((2*species.getBaseAttack())*level)/100)+5;
		this.defense = (((2*species.getBaseDefense())*level)/100)+5;
		this.specialAttack = (((2*species.getBaseSpecialAttack())*level)/100)+5;
		this.specialDefense = (((2*species.getBaseSpecialDefense())*level)/100)+5;
		this.speed = (((2*species.getBaseSpeed())*level)/100)+5;
		
		this.types = species.getTypes();
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpeciesModel getSpecies() {
		return species;
	}

	public void setSpecies(SpeciesModel species) {
		this.species = species;
		
		this.healthPoints = (((2 * species.getBaseHealthPoints()) * level) / 100) + level;
		this.attack = (((2*species.getBaseAttack())*level)/100)+5;
		this.defense = (((2*species.getBaseDefense())*level)/100)+5;
		this.specialAttack = (((2*species.getBaseSpecialAttack())*level)/100)+5;
		this.specialDefense = (((2*species.getBaseSpecialDefense())*level)/100)+5;
		this.speed = (((2*species.getBaseSpeed())*level)/100)+5;
		
		this.types = species.getTypes();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		
		this.healthPoints = (((2 * this.species.getBaseHealthPoints()) * level) / 100) + level;
		this.attack = (((2*this.species.getBaseAttack())*level)/100)+5;
		this.defense = (((2*this.species.getBaseDefense())*level)/100)+5;
		this.specialAttack = (((2*this.species.getBaseSpecialAttack())*level)/100)+5;
		this.specialDefense = (((2*this.species.getBaseSpecialDefense())*level)/100)+5;
		this.speed = (((2*this.species.getBaseSpeed())*level)/100)+5;
		
	}

	public List<AttackModel> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<AttackModel> attacks) {
		this.attacks = attacks;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isShiny() {
		return shiny;
	}

	public void setShiny(boolean shiny) {
		this.shiny = shiny;
	}

	public int getId() {
		return id;
	}

	public List<TypeModel> getTypes() {
		return types;
	}

	public float getHealthPoints() {
		return healthPoints;
	}

	public float getAttack() {
		return attack;
	}

	public float getDefense() {
		return defense;
	}

	public float getSpecialAttack() {
		return specialAttack;
	}

	public float getSpecialDefense() {
		return specialDefense;
	}

	public float getSpeed() {
		return speed;
	}

	public void setTypes(List<TypeModel> types) {
		this.types = types;
	}
	
	public void addAttack(AttackModel type) {
		if (attacks== null) {
			attacks= new ArrayList<AttackModel>();
		}
		attacks.add(type);
	}

	public void addType(TypeModel type) {
		if (types== null) {
			types= new ArrayList<TypeModel>();
		}
		types.add(type);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setHealthPoints(float healthPoints) {
		this.healthPoints = healthPoints;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public void setDefense(float defense) {
		this.defense = defense;
	}

	public void setSpecialAttack(float specialAttack) {
		this.specialAttack = specialAttack;
	}

	public void setSpecialDefense(float specialDefense) {
		this.specialDefense = specialDefense;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setId(int id) {
		this.id = id;
	}

	

}
