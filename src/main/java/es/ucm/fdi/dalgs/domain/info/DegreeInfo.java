package es.ucm.fdi.dalgs.domain.info;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class DegreeInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty @NotNull @NotBlank
	@Size(min=1, max=20)
	@Basic(optional = false)
	@Column(name = "code_degree", nullable = false, unique = true)
	private String code;
	
	@NotEmpty @NotNull @NotBlank
	@Size(min=1, max=50)
	@Basic(optional = false)
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@NotEmpty @NotNull @NotBlank
	@Size(min=1, max=250)
	@Basic(optional = false)
	@Column(name = "description", length = 250, nullable = false)
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		DegreeInfo other = (DegreeInfo) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	
	
}
