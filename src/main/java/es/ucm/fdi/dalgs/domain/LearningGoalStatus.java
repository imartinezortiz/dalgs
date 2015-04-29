/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
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
//	@JsonBackReference
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
