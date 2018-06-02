package at.fh.swenga.jpa.model;
 
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
@Entity
@Table(name = "users")
public class User implements java.io.Serializable {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
 
 
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String userName;
 
	@Column(name = "password", nullable = false, length = 60)
	private String password;	
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;	
	
	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)//if user has roles that are not in database, they will be created(PERSIST)
	private Set<UserRole> userRoles;
	
	@OneToMany(mappedBy = "owner",fetch=FetchType.LAZY)
	private Set<EntryModel> entries;

	@OneToMany(mappedBy = "owner",fetch=FetchType.LAZY)
	private Set<TopicModel> topics;
	
	@Column(name = "firstname", length = 60)
	private String firstName;
	
	@Column(name = "lastname", length = 60)
	private String lastName;
	
	@Temporal(TemporalType.DATE)
	private Date dateOfEntry;
	
	@OneToMany(mappedBy = "owner",fetch=FetchType.LAZY)
	private List<PokemonModel> team;
	
	@OneToOne(cascade = CascadeType.ALL)
	private DocumentModel picture;
	
	
 
	public User() {
	}
 
	public User(String userName, String password, boolean enabled) {
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
	}
 
 
	public int getId() {
		return id;
	}
 
	public void setId(int id) {
		this.id = id;
	}
 
 
 
	public String getUserName() {
		return userName;
	}
 
	public void setUserName(String userName) {
		this.userName = userName;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
	public boolean isEnabled() {
		return enabled;
	}
 
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
 
 
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
 
	public void addUserRole(UserRole userRole) {
		if (userRoles==null) userRoles = new HashSet<UserRole>();
		userRoles.add(userRole);
	}
 
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
 
	public void encryptPassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		password = passwordEncoder.encode(password);		
	}

	public Set<EntryModel> getEntries() {
		return entries;
	}

	public void setEntries(Set<EntryModel> entries) {
		this.entries = entries;
	}

	public Set<TopicModel> getTopics() {
		return topics;
	}

	public void setTopics(Set<TopicModel> topics) {
		this.topics = topics;
	}
 
}