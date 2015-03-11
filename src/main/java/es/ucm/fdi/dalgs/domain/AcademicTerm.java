package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;



@Entity
@Table(name = "academicterm", uniqueConstraints = { @UniqueConstraint(columnNames = {"term", "id_degree" }) })
public class AcademicTerm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_academicterm")
	private Long id;

	@NotEmpty @NotNull @NotBlank
	@Size(min=4, max=20)
	@Basic(optional = false)
	@Column(name = "term", nullable = false, columnDefinition = "varchar(32) default '2014/2015'")
	private String term;

	
	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	//@NotNull 
	@ManyToOne
	@JoinColumn(name = "id_degree")
	private Degree degree;

	
	//@NotNull
	//@Valid
	@OneToMany(mappedBy = "academicTerm", cascade = CascadeType.ALL)
	private Collection<Course> courses = new ArrayList<Course>();

	
	public AcademicTerm() {
		super();
		this.isDeleted = false;
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

	
	public Boolean getIsDeleted() {
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
