package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;

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
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;

import es.ucm.fdi.dalgs.domain.info.ModuleInfo;

@Entity
@Table(name = "module")
public class Module {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_module")	
	private Long id;
	
	@Embedded
	private ModuleInfo info;
	
	@AssertFalse
	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_degree")
	private Degree degree;
	
	@NotNull
	@Valid
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	private Collection<Topic> topics = new ArrayList<Topic>();

	
	public Module() {
		super();
		this.isDeleted=false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModuleInfo getInfo() {
		return info;
	}

	public void setInfo(ModuleInfo info) {
		this.info = info;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}
	
	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Collection<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Collection<Topic> topics) {
		this.topics = topics;
	}
	
	
}
