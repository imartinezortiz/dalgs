package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.ucm.fdi.dalgs.domain.info.CompetenceInfo;

@Entity
@Table(name = "competence", uniqueConstraints = @UniqueConstraint(columnNames = {
		"code_competence", "id_degree" }))
public class Competence implements Cloneable, Copyable<Competence>, Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_competence")
	private Long id;

	@Embedded
	private CompetenceInfo info;

	@ManyToMany(mappedBy = "competences", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Collection<Subject> subjects;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "competence",cascade = CascadeType.ALL)
	private Collection<LearningGoal> learningGoals;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "id_degree")
	private Degree degree;



	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	public Competence() {
		super();
		this.isDeleted = false;
		this.subjects = new ArrayList<Subject>();
		this.learningGoals = new ArrayList<LearningGoal>();
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

	public Collection<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Collection<Subject> subjects) {
		this.subjects = subjects;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public CompetenceInfo getInfo() {
		return info;
	}

	public void setInfo(CompetenceInfo info) {
		this.info = info;
	}

	public Collection<LearningGoal> getLearningGoals() {
		return learningGoals;
	}

	public void setLearningGoals(Collection<LearningGoal> learningGoals) {
		this.learningGoals = learningGoals;
	}

	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((degree == null) ? 0 : degree.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
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
		Competence other = (Competence) obj;
		if (degree == null) {
			if (other.degree != null)
				return false;
		} else if (!degree.equals(other.degree))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}

	public Competence depth_copy() {
		Competence copy = this.shallow_copy();

		copy.isDeleted=false;
		copy.id = null;
		copy.learningGoals = new ArrayList<>();
		for (LearningGoal lg : this.learningGoals) {
			LearningGoal learningGoal = lg.depth_copy();
			learningGoal.setCompetence(copy);
			copy.learningGoals.add(learningGoal);
		}
		return copy;
	}


	public Competence shallow_copy() {
		try {
			return  (Competence) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
	}


}
