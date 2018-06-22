package at.fh.swenga.jpa.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "pokemons")
public class PokemonModel implements java.io.Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", length = 45)
	private String name;

	@Column(name = "species", length = 45)
	private String species;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<TypeModel> types;

	@Column(name = "level")
	private float level;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<AttackModel> attacks;

	@Column(name = "HP")
	private float healthPoints = 0;

	@Column(name = "ATK")
	private float attack = 0;

	@Column(name = "DEF")
	private float defense = 0;

	@Column(name = "SPATK")
	private float specialAttack = 0;

	@Column(name = "SPDEF")
	private float specialDefense = 0;

	@Column(name = "SPE")
	private float speed = 0;

	@Column(name = "gender")
	private String gender;

	private boolean shiny;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private User owner;

	private float baseHP;
	private float baseATK;
	private float baseDEF;
	private float baseSPATK;
	private float baseSPDEF;
	private float baseSPE;

	@Version
	long version;

	public PokemonModel() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public float getLevel() {
		return level;
	}

	public void setLevel(float level) {
		this.level = level;
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
		if (attacks == null) {
			attacks = new ArrayList<AttackModel>();
		}
		attacks.add(type);
	}

	public void addType(TypeModel type) {
		if (types == null) {
			types = new ArrayList<TypeModel>();
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

	public float getBaseHP() {
		return baseHP;
	}

	public void setBaseHP(float baseHP) {
		this.baseHP = baseHP;
	}

	public float getBaseATK() {
		return baseATK;
	}

	public void setBaseATK(float baseATK) {
		this.baseATK = baseATK;
	}

	public float getBaseDEF() {
		return baseDEF;
	}

	public void setBaseDEF(float baseDEF) {
		this.baseDEF = baseDEF;
	}

	public float getBaseSPATK() {
		return baseSPATK;
	}

	public void setBaseSPATK(float baseSPATK) {
		this.baseSPATK = baseSPATK;
	}

	public float getBaseSPDEF() {
		return baseSPDEF;
	}

	public void setBaseSPDEF(float baseSPDEF) {
		this.baseSPDEF = baseSPDEF;
	}

	public float getBaseSPE() {
		return baseSPE;
	}

	public void setBaseSPE(float baseSPE) {
		this.baseSPE = baseSPE;
	}

	public void recalculateStats() {
		this.healthPoints = (2f * baseHP * level / 100f) + level + 10;
		this.attack = (2f * baseATK * level / 100f) + 5;
		this.defense = (2f * baseDEF * level / 100f) + 5;
		this.specialAttack = (2f * baseSPATK * level / 100f) + 5;
		this.specialDefense = (2f * baseSPDEF * level / 100f) + 5;
		this.speed = (2f * baseSPE * level / 100f) + 5;
	}

}
