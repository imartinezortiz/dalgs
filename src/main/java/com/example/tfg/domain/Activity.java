package com.example.tfg.domain;



import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name="activity")
@Where(clause = "isDeleted='false'")
public class Activity {
	
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_activity")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_course")//, insertable=false, updatable=false)
	private Course course;
	
	@Column(name = "isDeleted", nullable=false, columnDefinition="boolean default false")
	private boolean isDeleted;
	

	@Column(name = "code_activity", nullable=false)
	private String code;
	
	
	 @ElementCollection(fetch = FetchType.LAZY)
	 @CollectionTable(
	        name="activity_competencestatus",
	        joinColumns=@JoinColumn(name="id_activity")
	  )
	 @Column(nullable = false)

	// @OrderColumn(insertable=true,updatable=true,name="competencestatus")
	private Collection<CompetenceStatus> competenceStatus;
	
	public Activity() {
		super();
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
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
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

	public Collection<CompetenceStatus> getCompetenceStatus() {
		return competenceStatus;
	}

	public void setCompetenceStatus(Collection<CompetenceStatus> competenceStatus) {
		this.competenceStatus = competenceStatus;
	}
	
	@PostLoad
	public void postLoad(){
	    try {
	        if(getCourse() != null && getCourse().getId() == 0){
	            setCourse(null);
	        }
	    }
	    catch (EntityNotFoundException e){
	        setCourse(null);
	    }
	} 

}
