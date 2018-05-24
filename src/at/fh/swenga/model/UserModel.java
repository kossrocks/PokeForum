package at.fh.swenga.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	
	private String firstName;
	
	private String lastName;
	
	private Date dayOfBirth;
	
	private String topic;
	
	
	
}
