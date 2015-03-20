package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.ucm.fdi.dalgs.domain.info.SubjectInfo;

@Entity
@Table(name = "subject")
public class Subject implements Cloneable, Copyable<Subject> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_subject")
	private Long id;

	@Embedded
	private SubjectInfo info;


	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "id_topic")
	private Topic topic;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "subject_competence", joinColumns = { @JoinColumn(name = "id_subject") }, inverseJoinColumns = { @JoinColumn(name = "id_competence") })
	private Collection<Competence> competences = new ArrayList<Competence>();

	public Subject() {
		super();
		this.isDeleted = false;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<Competence> getCompetences() {
		return competences;
	}

	public void setCompetences(Collection<Competence> competences) {
		this.competences = competences;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public SubjectInfo getInfo() {
		return info;
	}

	public void setInfo(SubjectInfo info) {
		this.info = info;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	@Override
	public Subject copy() {
		Subject copy;
		try {
			copy = (Subject) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
		copy.id = null;
		copy.competences = new ArrayList<>();
		for (Competence c : this.competences) {
			c.getSubjects().remove(this);
			c.getSubjects().add(copy);
			copy.competences.add(c);
		}
		return copy;
	}

	
}
