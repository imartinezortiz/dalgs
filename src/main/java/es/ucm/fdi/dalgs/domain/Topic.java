package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.ucm.fdi.dalgs.domain.info.TopicInfo;

@Entity
@Table(name = "topic", uniqueConstraints = @UniqueConstraint(columnNames = {
		"code_topic", "id_module" }))
public class Topic implements Cloneable, Copyable<Topic>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_topic")
	private Long id;

	@Embedded
	private TopicInfo info;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "id_module")
	private Module module;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "topic",cascade = CascadeType.ALL)
	private Collection<Subject> subjects = new ArrayList<Subject>();

	public Topic() {
		super();
		this.isDeleted = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TopicInfo getInfo() {
		return info;
	}

	public void setInfo(TopicInfo info) {
		this.info = info;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Collection<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Collection<Subject> subjects) {
		this.subjects = subjects;
	}
	
	public Topic copy() {
		Topic copy;
		try {
			copy = (Topic) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
		copy.id = null;
		copy.subjects = new ArrayList<>();
		for (Subject s : this.subjects) {
			Subject subject  = s.copy();
			subject.setTopic(copy);
		}
		return copy;
	}

}
