package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;


import es.ucm.fdi.dalgs.domain.info.DegreeInfo;

@Entity
@Table(name = "degree")
public class Degree implements Cloneable, Copyable<Degree>, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_degree")
	private Long id;

	@Embedded
	private DegreeInfo info;

	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Collection<Module> modules;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "degree", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Collection<Competence> competences;

	public Degree() {
		super();
		this.isDeleted = false;
		this.modules = new ArrayList<Module>();
		this.competences = new ArrayList<Competence>();

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
		Degree other = (Degree) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}

	public Degree depth_copy() {
		Degree copy = this.shallow_copy();

		copy.isDeleted = false;
		copy.id = null;
		copy.modules = new ArrayList<>();

		DegreeInfo dInfo = copy.info.depth_copy();
		copy.info = dInfo;

		for (Module m : this.modules) {
			Module module = m.depth_copy();
			module.setDegree(copy);
			copy.modules.add(module);
		}

		copy.competences = new ArrayList<>();
		for (Competence c : this.competences) {
			Competence competence = c.depth_copy();
			competence.setDegree(copy);
			copy.competences.add(competence);
		}

		return copy;
	}

	public Degree shallow_copy() {
		try {
			return (Degree) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
