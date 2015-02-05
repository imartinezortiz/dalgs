package com.example.tfg.domain;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "competence", uniqueConstraints = @UniqueConstraint(columnNames = {
		"code_competence", "id_degree" }))
@Where(clause = "isDeleted='false'")
public class Competence {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_competence")
	private Long id;

	@Basic(optional = false)
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Basic(optional = false)
	@Column(name = "description", length = 250, nullable = false)
	private String description;

	@ManyToMany(mappedBy = "competences", fetch = FetchType.LAZY)
	private Collection<Subject> subjects;

	@ManyToOne(fetch = FetchType.LAZY)
	// , optional = false)
	@JoinColumn(name = "id_degree")
	private Degree degree;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;

	@Column(name = "code_competence", nullable = false)
	private String code;

	public Competence() {
		super();
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Collection<Subject> subjects) {
		this.subjects = subjects;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
