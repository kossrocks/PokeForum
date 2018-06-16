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
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "goodAgainst")
	private List<TypeModel> goodAgainst;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "weakAgainst")
	private List<TypeModel> weakAgainst;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "noDamageAgainst")
	private List<TypeModel> noDamageAgainst;
	
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

	public List<TypeModel> getGoodAgainst() {
		return goodAgainst;
	}

	public void setGoodAgainst(List<TypeModel> goodAgainst) {
		this.goodAgainst = goodAgainst;
	}

	public List<TypeModel> getWeakAgainst() {
		return weakAgainst;
	}

	public void setWeakAgainst(List<TypeModel> weakAgainst) {
		this.weakAgainst = weakAgainst;
	}

	public List<TypeModel> getNoDamageAgainst() {
		return noDamageAgainst;
	}

	public void setNoDamageAgainst(List<TypeModel> noDamageAgainst) {
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
	
	public void addGoodAgainst(TypeModel type) {
		if (goodAgainst== null) {
			goodAgainst= new ArrayList<TypeModel>();
		}
		goodAgainst.add(type);
	}
	
	public void addWeakAgainst(TypeModel type) {
		if (weakAgainst== null) {
			weakAgainst= new ArrayList<TypeModel>();
		}
		weakAgainst.add(type);
	}
	
	public void addNoDamageAgainst(TypeModel type) {
		if (noDamageAgainst== null) {
			noDamageAgainst= new ArrayList<TypeModel>();
		}
		noDamageAgainst.add(type);
	}
	
	
}
