package es.ucm.fdi.dalgs.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.ucm.fdi.dalgs.domain.info.LearningGoalInfo;

@Entity
@Table(name = "learninggoal")
public class LearningGoal implements Cloneable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_learninggoal")
	private Long id;
	
	@Embedded
	private LearningGoalInfo info;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_competence")
	private Competence competence;
	
	
	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	
	public LearningGoal() {
		super();
		this.isDeleted=false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LearningGoalInfo getInfo() {
		return info;
	}

	public void setInfo(LearningGoalInfo info) {
		this.info = info;
	}

	public Competence getCompetence() {
		return competence;
	}

	public void setCompetence(Competence competence) {
		this.competence = competence;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	public LearningGoal clone(){
		LearningGoal clone = new LearningGoal();
		
		clone.setId(null);
		
		//clone.setCompetence(this.competence); clone.getCompetence().setId(null);
		
		clone.setDeleted(this.isDeleted);
		clone.setInfo(this.info);
		
		return clone;
	}
		
}
