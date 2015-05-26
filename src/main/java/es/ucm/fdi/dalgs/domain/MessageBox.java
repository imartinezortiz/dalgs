package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message_box")
public class MessageBox implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_message_box")
	Long id;
	
	@Column(name = "from", length = 100, nullable = false)
	String from;
	
	@Column(name = "to", length = 50, nullable = false)
	String to;
	
	@Column(name = "subject", length = 100, nullable = false)
	String subject;
	
	@Column(name = "file", length = 400, nullable = false)
	String file;
	
//	@OneToOne
//	MessageBox parent;
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

//	public MessageBox getParent() {
//		return parent;
//	}
//
//	public void setParent(MessageBox parent) {
//		this.parent = parent;
//	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
}
