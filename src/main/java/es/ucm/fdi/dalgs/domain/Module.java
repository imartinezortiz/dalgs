package es.ucm.fdi.dalgs.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import es.ucm.fdi.dalgs.domain.info.ModuleInfo;

@Entity

//@Table(name = "module")
@Table(name = "module", uniqueConstraints = @UniqueConstraint(columnNames = {
		"code_module", "id_degree" }))
public class Module {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_module")
	private Long id;

	@Embedded
	private ModuleInfo info;


	@Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "id_degree")
	private Degree degree;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module",cascade = CascadeType.ALL)
	private Collection<Topic> topics = new ArrayList<Topic>();

	public Module() {
		super();
		this.isDeleted = false;
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

	public Module clone() {
		Module clone = new Module();

		clone.setId(null);
		clone.setInfo(this.info);
		clone.setDeleted(this.isDeleted);
		
		//clone.setDegree(this.degree); clone.getDegree().setId(null);

		List<Topic> topicsClon = new ArrayList<Topic>();
		if (this.topics != null) {
			for (Topic t : this.topics) {
				topicsClon.add((Topic) t.clone());
			}
		}
		clone.setTopics(topicsClon);

		return clone;
	}

}
