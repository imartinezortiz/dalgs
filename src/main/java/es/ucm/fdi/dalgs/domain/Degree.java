package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class Degree implements Cloneable {

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

	public Degree clone(Boolean cloneOwner) {
		Degree clone = new Degree();
		clone.setId(null);


		clone.setInfo(this.info);

		// If the clone root is Degree, it needs a new id, otherwise its value
		// be obtained by inheritance
		if (cloneOwner) {
			clone.getInfo().setCode(this.info.getCode() + "(copy)");
		}
		
		clone.setDeleted(this.isDeleted);

		List<Module> modulesClon = new ArrayList<Module>();
		if (this.modules != null) {
			for (Module m : this.modules) {
				modulesClon.add((Module) m.clone());
			}
		}
		clone.setModules(modulesClon);

		List<Competence> competencesClon = new ArrayList<Competence>();
		if (this.competences != null) {
			for (Competence c : this.competences) {
				competencesClon.add((Competence) c.clone());
			}
		}
		clone.setCompetences(competencesClon);

		return clone;
	}

}
