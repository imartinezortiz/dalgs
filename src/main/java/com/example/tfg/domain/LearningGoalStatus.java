package com.example.tfg.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class LearningGoalStatus {

	@ManyToOne
	private LearningGoal learningGoal;

	@Basic
	@Column(name = "percentage", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer percentage;



	public LearningGoal getLearningGoal() {
		return learningGoal;
	}

	public void setLearningGoal(LearningGoal learningGoal) {
		this.learningGoal = learningGoal;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

}
