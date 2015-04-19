package es.ucm.fdi.dalgs.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class LearningGoalStatus implements Cloneable,
		Copyable<LearningGoalStatus> {

	@NotNull
	@ManyToOne
	private LearningGoal learningGoal;

	@NotNull
	@Min(0)
	@Max(100)
	@Basic
	@Column(name = "weight", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer weight;

	public LearningGoalStatus() {
		super();
	}

	public LearningGoal getLearningGoal() {
		return learningGoal;
	}

	public void setLearningGoal(LearningGoal learningGoal) {
		this.learningGoal = learningGoal;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public LearningGoalStatus depth_copy() {
		LearningGoalStatus copy = this.shallow_copy();

		return copy;
	}

	public LearningGoalStatus shallow_copy() {
		try {
			return (LearningGoalStatus) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
