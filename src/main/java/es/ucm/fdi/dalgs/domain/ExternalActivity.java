package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;
import java.util.Collection;

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

import com.fasterxml.jackson.annotation.JsonBackReference;

import es.ucm.fdi.dalgs.domain.info.ActivityInfo;

@Entity
public class ExternalActivity implements Serializable {

	
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
	private boolean isDeleted;


	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "id_group")
	@JsonBackReference
	private Group group;

	
	public ExternalActivity() {
		info = new ActivityInfo();
		isDeleted=false;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityInfo getInfo() {
		return info;
	}

	public void setInfo(ActivityInfo info) {
		this.info = info;
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

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}



	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
