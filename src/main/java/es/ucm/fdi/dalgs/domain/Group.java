package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "_group")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_group")
	private Long id;
	
	@NotEmpty @NotNull @NotBlank
	@Size(min=5, max=50)
	@Basic(optional = false)
	@Column(name = "name", length = 50, nullable = false, unique=true)
	private String name;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_course")
	private Course course;
	
	@NotNull
	@Valid
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "group_professor", joinColumns = { @JoinColumn(name = "id_group") }, inverseJoinColumns = { @JoinColumn(name = "id_user") })
	private Collection<User> professors = new ArrayList<User>();
	
	@NotNull
	@Valid
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "group_student", joinColumns = { @JoinColumn(name = "id_group") }, inverseJoinColumns = { @JoinColumn(name = "id_user") })
	private Collection<User> students = new ArrayList<User>();
	
	@AssertFalse
	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	
	public Group() {
		super();
		this.isDeleted=false;
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

	public Collection<User> getProfessors() {
		return professors;
	}

	public void setProfessors(Collection<User> professors) {
		this.professors = professors;
	}

	public Collection<User> getStudents() {
		return students;
	}

	public void setStudents(Collection<User> students) {
		this.students = students;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	
	
}
