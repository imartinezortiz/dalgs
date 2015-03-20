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

import es.ucm.fdi.dalgs.domain.info.DegreeInfo;

@Entity
@Table(name = "degree")
public class Degree implements Cloneable ,Copyable<Degree>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_degree")
	private Long id;

	@Embedded
	private DegreeInfo info;



	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
	private Collection<Module> modules = new ArrayList<Module>();

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree")
	private Collection<Competence> competences = new ArrayList<Competence>();

	public Degree() {
		super();
		this.isDeleted = false;
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

	
	public Degree copy() {
		Degree copy;
		try {
			copy = (Degree) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
		copy.id = null;
		copy.modules = new ArrayList<>();
		for (Module m : this.modules) {
			Module module  = m.copy();
			module.setDegree(copy);
			copy.modules.add(m.copy());
		}
		
		copy.competences = new ArrayList<>();
		for (Competence c : this.competences) {
			Competence competence = c.copy();
			competence.setDegree(copy);
			copy.competences.add(c.copy());
		}
		return copy;
	}
	
	
}
