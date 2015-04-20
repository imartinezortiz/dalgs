package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "academicterm", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"term", "id_degree" }) })
public class AcademicTerm implements Cloneable, Copyable<AcademicTerm>,
		Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_academicterm")
	private Long id;

	@NotEmpty
	@NotNull
	@NotBlank
	@Size(min = 4, max = 20)
	@Basic(optional = false)
	@Column(name = "term", nullable = false, columnDefinition = "varchar(32) default '2014/2015'")
	private String term;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	@NotNull(message = "field null")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_degree")
	@JsonBackReference
	private Degree degree;

	@OneToMany(mappedBy = "academicTerm", cascade = CascadeType.ALL)
	@JsonManagedReference
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((degree == null) ? 0 : degree.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
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
		AcademicTerm other = (AcademicTerm) obj;
		if (degree == null) {
			if (other.degree != null)
				return false;
		} else if (!degree.equals(other.degree))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	public AcademicTerm depth_copy() {
		AcademicTerm copy = this.shallow_copy();
		copy.id = null;
		copy.courses = new ArrayList<Course>();
		copy.isDeleted = false;

		for (Course c : this.courses) {
			Course course = c.depth_copy();
			course.setAcademicTerm(copy);
			copy.courses.add(course);
		}
		return copy;
	}

	public AcademicTerm shallow_copy() {
		try {
			return (AcademicTerm) super.clone();

		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
