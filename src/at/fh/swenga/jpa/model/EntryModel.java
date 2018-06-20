package at.fh.swenga.jpa.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "entries")
public class EntryModel implements java.io.Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne (cascade = CascadeType.PERSIST)
	private User owner;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	public Date dateOfCreation;
	
	@Column(name = "content", length = 1000)
	private String content;
	
	@Column(name = "edited")
	private boolean edited;
	
	@ManyToOne (cascade = CascadeType.PERSIST)
	private TopicModel topic;
	
	@Version
	long version;
	
	
	public EntryModel() {
		// TODO Auto-generated constructor stub
	}



	public EntryModel(User owner, String content, TopicModel topic, boolean edited) {
		super();
		this.owner = owner;
		this.dateOfCreation = new Date();
		this.content = content;
		this.topic = topic;
		this.edited = edited;
	}



	public User getOwner() {
		return owner;
	}



	public void setOwner(User owner) {
		this.owner = owner;
	}



	public Date getDayOfCreation() {
		return dateOfCreation;
	}



	public void setDayOfCreation(Date dayOfCreation) {
		this.dateOfCreation = dayOfCreation;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public boolean isEdited() {
		return edited;
	}



	public void setEdited(boolean edited) {
		this.edited = edited;
	}



	public TopicModel getTopic() {
		return topic;
	}



	public void setTopic(TopicModel topic) {
		this.topic = topic;
	}



	public int getId() {
		return id;
	}
	
	
	
	
}
