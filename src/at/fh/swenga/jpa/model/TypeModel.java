package at.fh.swenga.jpa.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "types")
public class TypeModel implements java.io.Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", unique = true, nullable = false, length = 45)
	private String name;
	
	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
	private Set<TypeModel> goodAgainst;

	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
	private Set<TypeModel> weakAgainst;
	
	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
	private Set<TypeModel> noDamageAgainst;
	
	@OneToMany(mappedBy = "type",fetch=FetchType.LAZY)
	private Set<AttackModel> attacks;
	
	@ManyToMany(mappedBy = "types")
	private Set<SpeciesModel> specieses;
	
	@ManyToMany(mappedBy = "types",fetch=FetchType.EAGER)
	private List<PokemonModel> pokemons;
	
	public TypeModel() {
		// TODO Auto-generated constructor stub
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

	public Set<TypeModel> getGoodAgainst() {
		return goodAgainst;
	}

	public void setGoodAgainst(Set<TypeModel> goodAgainst) {
		this.goodAgainst = goodAgainst;
	}

	public Set<TypeModel> getWeakAgainst() {
		return weakAgainst;
	}

	public void setWeakAgainst(Set<TypeModel> weakAgainst) {
		this.weakAgainst = weakAgainst;
	}

	public Set<TypeModel> getNoDamageAgainst() {
		return noDamageAgainst;
	}

	public void setNoDamageAgainst(Set<TypeModel> noDamageAgainst) {
		this.noDamageAgainst = noDamageAgainst;
	}

	public Set<AttackModel> getAttacks() {
		return attacks;
	}

	public Set<SpeciesModel> getSpecieses() {
		return specieses;
	}


	public List<PokemonModel> getPokemons() {
		return pokemons;
	}

	public void setAttacks(Set<AttackModel> attacks) {
		this.attacks = attacks;
	}

	public void setSpecieses(Set<SpeciesModel> specieses) {
		this.specieses = specieses;
	}

	public void setPokemons(List<PokemonModel> pokemons) {
		this.pokemons = pokemons;
	}
	
	
}
