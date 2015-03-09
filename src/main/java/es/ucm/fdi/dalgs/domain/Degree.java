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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;

import es.ucm.fdi.dalgs.domain.info.DegreeInfo;


@Entity
@Table(name = "degree")
public class Degree {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_degree")
	private Long id;

	
	@Embedded
	private DegreeInfo info;

	
	@AssertFalse
	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	@NotNull
	@Valid
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
	private Collection<Module> modules = new ArrayList<Module>();

	@NotNull
	@Valid
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
	private Collection<Competence> competences = new ArrayList<Competence>();

	public Degree() {
		super();
		this.isDeleted=false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DegreeInfo getInfo() {
		return info;
	}

	public void setInfo(DegreeInfo infoDegree) {
		this.info = infoDegree;
	}



	public Collection<Competence> getCompetences() {
		return competences;
	}

	public void setCompetences(Collection<Competence> competences) {
		this.competences = competences;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Collection<Module> getModules() {
		return modules;
	}

	public void setModules(Collection<Module> modules) {
		this.modules = modules;
	}

}
