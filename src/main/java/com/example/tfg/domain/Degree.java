package com.example.tfg.domain;

import java.util.Collection;


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.tfg.domain.info.DegreeInfo;


@Entity
@Table(name = "degree")
public class Degree {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_degree")
	private Long id;

//	@Basic(optional = false)
//	@Column(name = "name", length = 50, nullable = false)
//	private String name;
//
//	@Basic(optional = false)
//	@Column(name = "description", length = 250, nullable = false)
//	private String description;
	
	@Embedded
	private DegreeInfo info;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
//	// , cascade= CascadeType.ALL)//, orphanRemoval=true)
//	private Collection<Subject> subjects;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
	// , cascade= CascadeType.ALL)//, orphanRemoval=true)
	private Collection<Module> modules;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
	// , cascade= CascadeType.ALL)//, orphanRemoval=true)
	private Collection<Competence> competences;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;

//	@Column(name = "code_degree", nullable = false)
//	private String code;

	public Degree() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

	public DegreeInfo getInfo() {
		return info;
	}

	public void setInfo(DegreeInfo infoDegree) {
		this.info = infoDegree;
	}

//	public Collection<Subject> getSubjects() {
//		return subjects;
//	}
//
//	public void setSubjects(Collection<Subject> subjects) {
//		this.subjects = subjects;
//	}

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

//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}

}
