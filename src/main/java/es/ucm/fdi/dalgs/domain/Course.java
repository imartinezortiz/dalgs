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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "course", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"id_subject", "id_academicterm" }) })
public class Course implements Cloneable, Copyable<Course>, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_course")
	private Long id;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_subject")
	@JsonBackReference
	private Subject subject;
	
	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "course_activities", joinColumns ={@JoinColumn(name = "id_course")},
    inverseJoinColumns ={@JoinColumn(name = "id_activity")})
//	@Where(clause="isDeleted = 'false'")
	@JsonManagedReference
	private Collection<Activity> activities;
	
	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "course_external", joinColumns ={@JoinColumn(name = "id_course")},
    inverseJoinColumns ={@JoinColumn(name = "id_activity")})
	@JsonManagedReference
	private Collection<Activity> external_activities;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Collection<Group> groups;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_academicterm")
	@JsonBackReference
	private AcademicTerm academicTerm;

	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_coordinator")
	@JsonBackReference
	private User coordinator;

	public Course() {
		super();
		this.isDeleted = false;
		this.activities = new ArrayList<Activity>();
		this.external_activities = new ArrayList<Activity>();
		this.groups = new ArrayList<Group>();

	}

	public User getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(User coordinator) {
		this.coordinator = coordinator;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public AcademicTerm getAcademicTerm() {
		return academicTerm;
	}

	public void setAcademicTerm(AcademicTerm academicTerm) {
		this.academicTerm = academicTerm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Collection<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Collection<Activity> activities) {
		this.activities.clear();
		this.activities.addAll(activities);
	}

	public Collection<Group> getGroups() {
		return groups;
	}

	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}
	

	public Collection<Activity> getExternal_activities() {
		return external_activities;
	}

	public void setExternal_activities(Collection<Activity> external_activities) {
		this.external_activities.clear();		
		this.external_activities.addAll(external_activities);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((academicTerm == null) ? 0 : academicTerm.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Course other = (Course) obj;
		if (academicTerm == null) {
			if (other.academicTerm != null)
				return false;
		} else if (!academicTerm.equals(other.academicTerm))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	public Course depth_copy() {
		Course copy = this.shallow_copy();

		copy.id = null;
		copy.groups = new ArrayList<Group>();
		copy.isDeleted = false;
		copy.activities = new ArrayList<Activity>();

		for (Group g : this.groups) {
			Group group = g.depth_copy();
			group.setCourse(copy);
			copy.getGroups().add(group);
		}

		// copy.activities = new ArrayList<Activity>();
		for (Activity a : this.activities) {
			Activity activity = a.depth_copy();
			activity.setCourse(copy);
			activity.setGroup(null);
			copy.getActivities().add(activity);
		}

		copy.setCoordinator(null);

		return copy;

	}

	public Course shallow_copy() {
		try {
			return (Course) super.clone();

		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
