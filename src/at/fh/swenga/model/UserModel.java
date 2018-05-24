package at.fh.swenga.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import at.fh.swenga.jpa.model.UserRole;

@Entity
@Table(name = "users")
public class UserModel implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //value is set by table itself that should be unique
	private int id;
	
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String userName;
	
	@Column(name = "password", nullable = false, length = 60)
	private String password;
	
	@Column(name = "disabled", nullable = false)
	private boolean disabled;
	
	@Column(name = "firstname", nullable = false, length = 60)
	private String firstName;
	
	@Column(name = "lastname", nullable = false, length = 60)
	private String lastName;
	
	@Column(name = "dayofbirth", nullable = false)
	private Date dayOfBirth;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	private List<TopicModel> topics;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	private List<EntryModel> entries;
	
	@Column(name = "dayofentry", nullable = false)
	private Date dayOfEntry;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] profilePicture;
	
	private String contentType;
	private Date created;
	
	@ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST) //Cascade.Type = 
	private Set<UserRole> userRoles;
	
	public UserModel() {
		// TODO Auto-generated constructor stub
	}

	public UserModel(String userName, String password, boolean disabled, String firstName, String lastName,
			Date dayOfBirth, List<TopicModel> topics, List<EntryModel> entries, Date dayOfEntry, byte[] profilePicture,
			String contentType, Date created, Set<at.fh.swenga.model.UserRole> userRoles) {
		super();
		this.userName = userName;
		this.password = password;
		this.disabled = disabled;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dayOfBirth = dayOfBirth;
		this.topics = topics;
		this.entries = entries;
		this.dayOfEntry = dayOfEntry;
		this.profilePicture = profilePicture;
		this.contentType = contentType;
		this.created = created;
		this.userRoles = userRoles;
	}

	public int getId() {
		return id;
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(Date dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public List<TopicModel> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicModel> topics) {
		this.topics = topics;
	}

	public List<EntryModel> getEntries() {
		return entries;
	}

	public void setEntries(List<EntryModel> entries) {
		this.entries = entries;
	}

	public Date getDayOfEntry() {
		return dayOfEntry;
	}

	public void setDayOfEntry(Date dayOfEntry) {
		this.dayOfEntry = dayOfEntry;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((dayOfBirth == null) ? 0 : dayOfBirth.hashCode());
		result = prime * result + ((dayOfEntry == null) ? 0 : dayOfEntry.hashCode());
		result = prime * result + (disabled ? 1231 : 1237);
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + Arrays.hashCode(profilePicture);
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (dayOfBirth == null) {
			if (other.dayOfBirth != null)
				return false;
		} else if (!dayOfBirth.equals(other.dayOfBirth))
			return false;
		if (dayOfEntry == null) {
			if (other.dayOfEntry != null)
				return false;
		} else if (!dayOfEntry.equals(other.dayOfEntry))
			return false;
		if (disabled != other.disabled)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (!Arrays.equals(profilePicture, other.profilePicture))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public void addUserRole(UserRole userRole) {
		if (userRoles==null) userRoles = new HashSet<UserRole>();
		userRoles.add(userRole);
	}

}
