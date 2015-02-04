package com.example.tfg.domain;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "academicterm", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"term", "id_degree" }) })
@Where(clause = "isDeleted ='false'")
public class AcademicTerm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_academicterm")
	private Long id;

	@Basic(optional = false)
	@Column(name = "term", nullable = false, columnDefinition = "varchar(32) default '2014/2015'")
	private String term;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;

	@ManyToOne
	@JoinColumn(name = "id_degree")
	private Degree degree;

	@OneToMany(mappedBy = "academicTerm", cascade = CascadeType.ALL)
	private Collection<Course> courses;

	public AcademicTerm() {
		super();
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public void setCourses(Collection<Course> courses) {
		this.courses = courses;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

}
