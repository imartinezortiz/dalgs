package es.ucm.fdi.dalgs.rest.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.dalgs.domain.Activity;

public class Activity_Response implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private String code;
	private long id;
	private Collection<String> errors;
	

	public Activity_Response() {
		super();
		this.setErrors(new ArrayList<String>());
	}

	public Activity_Response (Activity activity, Collection<String> errors){
		super();
		this.code = activity.getInfo().getCode();
		this.description = activity.getInfo().getDescription();
		this.id = activity.getId();
		this.name = activity.getInfo().getName();
		this.setErrors(errors);
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


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	
	public Collection<String> getErrors() {
		return errors;
	}

	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}


	
	
}