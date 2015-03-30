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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import es.ucm.fdi.dalgs.domain.info.ModuleInfo;

@Entity

@Table(name = "module", uniqueConstraints = @UniqueConstraint(columnNames = {
		"code_module", "id_degree" }))
public class Module implements Cloneable, Copyable<Module>, Serializable {

	private static final long serialVersionUID = 1L;

	
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
	private Collection<Topic> topics;

	public Module() {
		super();
		this.isDeleted = false;
		this.topics = new ArrayList<Topic>();

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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((degree == null) ? 0 : degree.hashCode());
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
		Module other = (Module) obj;
		if (degree == null) {
			if (other.degree != null)
				return false;
		} else if (!degree.equals(other.degree))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}

	public Module depth_copy() {
		Module copy = this.shallow_copy();
		
		copy.isDeleted=false;
		copy.id=null;
		
		ModuleInfo mInfo = copy.info.depth_copy();
		copy.info = mInfo;
		
		copy.topics = new ArrayList<>();
		for (Topic t : this.topics) {
			Topic topic  = t.depth_copy();
			topic.setModule(copy);
			copy.topics.add(topic);
		}
		return copy;
	}
	public Module shallow_copy() {
		try {
			return (Module) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
