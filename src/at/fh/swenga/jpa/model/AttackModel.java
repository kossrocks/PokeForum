package at.fh.swenga.jpa.model;

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

@Entity
@Table(name = "attacks")
public class AttackModel implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", unique = true, nullable = false, length = 45)
	private String name;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private TypeModel type;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private CategoryModel category;
	
	@Column(name = "PP")
	private int powerPoints;
	
	@Column(name = "BP")
	private int basePower;
	
	@Column(name = "acc")
	private int accuracy;
	
	@Column(name = "effect")
	private String battleEffect;
	
	@ManyToMany(mappedBy = "attacks",fetch=FetchType.LAZY)
	private List<PokemonModel> pokemons;
	
	public AttackModel() {
		// TODO Auto-generated constructor stub
	}

	public AttackModel(String name, TypeModel type, CategoryModel category, int basePower, int accuracy, int powerPoints,
			String battleEffect) {
		super();
		this.name = name;
		this.type = type;
		this.category = category;
		this.powerPoints = powerPoints;
		this.basePower = basePower;
		this.accuracy = accuracy;
		this.battleEffect = battleEffect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypeModel getType() {
		return type;
	}

	public void setType(TypeModel type) {
		this.type = type;
	}

	public CategoryModel getCategory() {
		return category;
	}

	public void setCategory(CategoryModel category) {
		this.category = category;
	}

	public int getPowerPoints() {
		return powerPoints;
	}

	public void setPowerPoints(int powerPoints) {
		this.powerPoints = powerPoints;
	}

	public int getBasePower() {
		return basePower;
	}

	public void setBasePower(int basePower) {
		this.basePower = basePower;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public String getBattleEffect() {
		return battleEffect;
	}

	public void setBattleEffect(String battleEffect) {
		this.battleEffect = battleEffect;
	}

	public int getId() {
		return id;
	}

	public List<PokemonModel> getPokemons() {
		return pokemons;
	}

	public void setPokemons(List<PokemonModel> pokemons) {
		this.pokemons = pokemons;
	}
	
	
}
