package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.ucm.fdi.dalgs.domain.info.ActivityInfo;


@Entity
@Table(name = "activity")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_activity")
	private Long id;

	
	@Embedded
	private ActivityInfo info;
	


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_course")
	private Course course;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;


	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "activity_learninggoalstatus", joinColumns = @JoinColumn(name = "id_activity"))
	@Column(nullable = false)
	private Collection<LearningGoalStatus> learningGoalStatus =  new ArrayList<LearningGoalStatus>();

	public Activity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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


	public Collection<LearningGoalStatus> getLearningGoalStatus() {
		return learningGoalStatus;
	}

	public void setLearningGoalStatus(
			Collection<LearningGoalStatus> learningGoalStatus) {
		this.learningGoalStatus = learningGoalStatus;
	}

	public ActivityInfo getInfo() {
		return info;
	}

	public void setInfo(ActivityInfo info) {
		this.info = info;
	}

}
