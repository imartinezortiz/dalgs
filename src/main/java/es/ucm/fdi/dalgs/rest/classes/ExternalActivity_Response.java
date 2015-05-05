package es.ucm.fdi.dalgs.rest.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.dalgs.domain.ExternalActivity;

public class ExternalActivity_Response implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private ExternalActivity externalActivity;
	private Collection<String> errors;
	

	public ExternalActivity_Response() {
		super();
		this.externalActivity = new ExternalActivity();
		this.setErrors(new ArrayList<String>());
	}

	public ExternalActivity_Response (ExternalActivity externalActivity, Collection<String> errors){
		super();
		this.externalActivity = externalActivity;
		this.setErrors(errors);
	}

	
	
	public ExternalActivity getExternalActivity() {
		return externalActivity;
	}

	public void setExternalActivity(ExternalActivity externalActivity) {
		this.externalActivity = externalActivity;
	}

	public Collection<String> getErrors() {
		return errors;
	}

	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}


	
}
