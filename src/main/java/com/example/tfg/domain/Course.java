package com.example.tfg.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "course")
@Where(clause = "isDeleted='false'")
public class Course {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_course")
	private Long id;
	
	
	@Column(name = "isDeleted", nullable=false, columnDefinition="boolean default false")
	private boolean isDeleted;
	
	
	@JoinColumn(name = "id_subject")
	private Subject subject;
	

	@OneToMany(mappedBy="course", cascade= CascadeType.ALL)//, orphanRemoval=true)
	private Collection<Activity> activities;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_academicterm", insertable=false, updatable=false)
	private AcademicTerm academicTerm;
	
	public Course() {
		super();
	}
	
	public AcademicTerm getAcademicTerm() {
		return academicTerm;
	}

	public void setAcademicTerm(AcademicTerm academicTerm) {
		this.academicTerm = academicTerm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Collection<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}


}
