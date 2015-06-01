package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="messages")
public class MessageBox implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_message")
	private Long id;
	
	@Column(name = "code", length = 200, nullable=false)
	private String code;
	
	
	@Column(name = "_from", length = 200, nullable=false)
	private String from;
	
	@Column(name = "_to", length = 200, nullable=false)
	private String to;
	
	@Column(name = "subject", length = 100, nullable=false)
	private String subject;
	
	@Column(name = "file", length = 400, nullable=false)
	private String file;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="parent")
	private Collection<MessageBox> replies;
	
	@ManyToOne
	private MessageBox parent;
	
	public MessageBox (){
		replies = new ArrayList<>();
	}
	
	public void addReply(MessageBox reply){
		reply.setParent(this);
		replies.add(reply);
		
	}
	
	public Collection<MessageBox> getReplies() {
		return replies;
	}

	public void setReplies(Collection<MessageBox> replies) {
		this.replies = replies;
	}

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

	public MessageBox getParent() {
		return parent;
	}

	public void setParent(MessageBox parent) {
		this.parent = parent;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
