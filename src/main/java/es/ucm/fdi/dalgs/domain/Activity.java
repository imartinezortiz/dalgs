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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.CascadeType;
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

import org.codehaus.jackson.annotate.JsonManagedReference;

import com.fasterxml.jackson.annotation.JsonBackReference;

import es.ucm.fdi.dalgs.domain.info.ActivityInfo;

@Entity
@Table(name = "Activity", uniqueConstraints = @UniqueConstraint(columnNames = {
		"code_activity", "id_course", "id_group" }))

public class Activity implements Cloneable, Copyable<Activity>, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_activity")
	private Long id;

	@Embedded
	private ActivityInfo info;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "id_course")
	@JsonBackReference
	private Course course;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "activity_learninggoalstatus", joinColumns = @JoinColumn(name = "id_activity"))
	@Column(nullable = false)
	private Collection<LearningGoalStatus> learningGoalStatus;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "id_group")
	@JsonBackReference
	private Group group;
	
	

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name="activity_attachments", joinColumns={@JoinColumn(name="id_activity")}, inverseJoinColumns = { @JoinColumn(name = "id_attachment") })
	@JsonManagedReference
	private Collection<Attachment> attachments;
	
	

	public Activity() {
		super();
		this.isDeleted = false;
		this.learningGoalStatus = new ArrayList<LearningGoalStatus>();
		this.info = new ActivityInfo();
		this.attachments = new ArrayList<Attachment>();
	}
	

	public Collection<Attachment> getAttachments() {
		return attachments;
	}


	public void setAttachments(Collection<Attachment> attachments) {
		this.attachments = attachments;
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

	public Boolean getIsDeleted() {
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Activity other = (Activity) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}

	public Activity depth_copy() {
		Activity copy = this.shallow_copy();

		copy.id = null;
		copy.isDeleted = false;
	
		ActivityInfo aInfo = this.getInfo().depth_copy();
		copy.info = aInfo;
		copy.learningGoalStatus = new ArrayList<LearningGoalStatus>();
		copy.attachments = new ArrayList<Attachment>();
		
	

		return copy;

	}

	public Activity shallow_copy() {
		// Same reference
		try {
			return (Activity) super.clone();

		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
