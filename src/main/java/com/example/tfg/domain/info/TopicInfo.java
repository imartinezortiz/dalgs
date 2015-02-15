package com.example.tfg.domain.info;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TopicInfo {

	@Basic(optional = false)
	@Column(name = "code_topic", nullable = false, unique=true)
	private String code;
	
	@Basic(optional = false)
	@Column(name = "name", length = 50, nullable = false)
	private String name;

//	@Basic(optional = false)
//	@Column(name = "description", length = 250, nullable = false)
//	private String description;

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

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
	
	
}
