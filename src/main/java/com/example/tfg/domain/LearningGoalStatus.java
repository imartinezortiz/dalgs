package com.example.tfg.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class LearningGoalStatus {

	@ManyToOne
	private Competence competence;

	@Basic
	@Column(name = "percentage", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer percentage;

	public Competence getCompetence() {
		return competence;
	}

	public void setCompetence(Competence competence) {
		this.competence = competence;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

}
