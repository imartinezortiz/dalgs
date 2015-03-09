package es.ucm.fdi.dalgs.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class LearningGoalStatus {

	@NotNull
	@ManyToOne
	private LearningGoal learningGoal;

	@NotNull
    @Min(0)
	@Max(100)
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
