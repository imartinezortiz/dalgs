package es.ucm.fdi.dalgs.domain.info;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ActivityInfo {

	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "code_activity", nullable = false, unique = true)
	private String code;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
