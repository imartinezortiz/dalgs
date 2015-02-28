package com.example.tfg.domain;

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

import com.example.tfg.domain.info.SubjectInfo;


@Entity
@Table(name = "subject")
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_subject")
	private Long id;

//	@Basic(optional = false)
//	@Column(name = "name", length = 50, nullable = false)
//	private String name;
//
//	@Basic(optional = false)
//	@Column(name = "description", length = 250, nullable = false)
//	private String description;
	@Embedded
	private SubjectInfo info;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;

//	@ManyToOne(fetch = FetchType.LAZY)
//	// , optional = false)//cascade= CascadeType.ALL)
//	@JoinColumn(name = "id_degree")
//	private Degree degree;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_topic")
	private Topic topic;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "subject_competence", joinColumns = { @JoinColumn(name = "id_subject") }, inverseJoinColumns = { @JoinColumn(name = "id_competence") })
	private Collection<Competence> competences = new ArrayList<Competence>();

//	@Column(name = "code_subject", nullable = false)
//	private String code;

	public Subject() {
		super();
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

	public boolean isDeleted() {
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

//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}

	
}
