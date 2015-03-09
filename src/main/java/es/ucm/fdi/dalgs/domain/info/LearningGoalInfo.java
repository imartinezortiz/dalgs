package es.ucm.fdi.dalgs.domain.info;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class LearningGoalInfo {
	
	@NotEmpty @NotNull @NotBlank
	@Size(min=1, max=20)
	@Basic(optional = false)
	@Column(name = "code_learning", nullable = false, unique=true)
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
