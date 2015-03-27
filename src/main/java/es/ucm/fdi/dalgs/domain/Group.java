package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "_group" , uniqueConstraints = { @UniqueConstraint(columnNames = {"id_course", "name" }) })
public class Group implements Cloneable, Copyable<Group>, Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_group")
	private Long id;
	
	@NotEmpty @NotNull @NotBlank
	@Size(min=5, max=50)
	@Basic(optional = false)
	@Column(name = "name", length = 50, nullable = false)//, unique=true)
	private String name;
	
	//@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
	@JoinColumn(name = "id_course")
	private Course course;
	
	//@NotNull
	//@Valid
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "group_professor", joinColumns = { @JoinColumn(name = "id_group") }, inverseJoinColumns = { @JoinColumn(name = "id_user") })
	private Collection<User> professors;
	
	//@NotNull
	//@Valid
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "group_student", joinColumns = { @JoinColumn(name = "id_group") }, inverseJoinColumns = { @JoinColumn(name = "id_user") })
	private Collection<User> students;
	

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private Collection<Activity> activities;
	
	
	public Group() {
		super();
		this.isDeleted=false;
		this.professors = new ArrayList<User>();
		this.students = new ArrayList<User>();
		this.activities = new ArrayList<Activity>();
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
	
	public Collection<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	public Group depth_copy() {
		Group copy = this.shallow_copy();
		
		copy.id = null;
		copy.activities = new ArrayList<Activity>();
		copy.isDeleted=false;
		
//		for(Activity a: this.activities){
//			Activity activity = new Activity();
//				activity =  a.depth_copy();
//				activity.setGroup(copy);
//				copy.activities.add(activity);	
//		}
		
		copy.students = new ArrayList<User>();
		copy.professors = new ArrayList<User>();
		
		return copy;
	}


	public Group shallow_copy() {
		try {
			return (Group) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}

