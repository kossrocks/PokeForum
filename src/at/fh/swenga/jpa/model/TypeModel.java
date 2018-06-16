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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "types")
public class TypeModel implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", unique = true, nullable = false, length = 45)
	private String name;
	
	
	private String goodAgainst;

	
	private String weakAgainst;
	
	
	private String noDamageAgainst;
	
	@OneToMany(mappedBy = "type",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<AttackModel> attacks;
	
	@ManyToMany(mappedBy = "types",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<SpeciesModel> specieses;
	
	@ManyToMany(mappedBy = "types",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<PokemonModel> pokemons;
	
	@Version
	long version;
	
	public TypeModel() {
		// TODO Auto-generated constructor stub
	}
	
	public TypeModel(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoodAgainst() {
		return goodAgainst;
	}

	public void setGoodAgainst(String goodAgainst) {
		this.goodAgainst = goodAgainst;
	}

	public String getWeakAgainst() {
		return weakAgainst;
	}

	public void setWeakAgainst(String weakAgainst) {
		this.weakAgainst = weakAgainst;
	}

	public String getNoDamageAgainst() {
		return noDamageAgainst;
	}

	public void setNoDamageAgainst(String noDamageAgainst) {
		this.noDamageAgainst = noDamageAgainst;
	}

	public List<AttackModel> getAttacks() {
		return attacks;
	}

	public List<SpeciesModel> getSpecieses() {
		return specieses;
	}


	public List<PokemonModel> getPokemons() {
		return pokemons;
	}

	public void setAttacks(List<AttackModel> attacks) {
		this.attacks = attacks;
	}

	public void setSpecieses(List<SpeciesModel> specieses) {
		this.specieses = specieses;
	}

	public void setPokemons(List<PokemonModel> pokemons) {
		this.pokemons = pokemons;
	}
	
	public void addGoodAgainst(String type) {
		if (goodAgainst== null) {
			goodAgainst= new String();
		}
		goodAgainst += "," + type;
	}
	
	public void addWeakAgainst(String type) {
		if (weakAgainst== null) {
			weakAgainst= new String();
		}
		weakAgainst += "," + type;
	}
	
	public void addNoDamageAgainst(String type) {
		if (noDamageAgainst== null) {
			noDamageAgainst= new String();
		}
		noDamageAgainst += "," + type;
	}
	
	
}
