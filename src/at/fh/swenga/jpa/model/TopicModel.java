package at.fh.swenga.jpa.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "entries")
public class TopicModel implements java.io.Serializable{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", unique = true, nullable = false, length = 100)
	private String title;
	
	@ManyToOne (cascade = CascadeType.PERSIST)
	private User owner;
	
	@Temporal(TemporalType.DATE)
	private Date lastEdited;
	
	@OneToMany(mappedBy = "topic",fetch=FetchType.EAGER)
	private Set<EntryModel> entries;
	
	@Version
	long version;
	
	public TopicModel() {
		// TODO Auto-generated constructor stub
	}

	public TopicModel(String title, User owner, Date lastEdited, Set<EntryModel> entries) {
		super();
		this.title = title;
		this.owner = owner;
		this.lastEdited = lastEdited;
		this.entries = entries;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public Set<EntryModel> getEntries() {
		return entries;
	}

	public void setEntries(Set<EntryModel> entries) {
		this.entries = entries;
	}

	public int getId() {
		return id;
	}

	
	
	
	
}
