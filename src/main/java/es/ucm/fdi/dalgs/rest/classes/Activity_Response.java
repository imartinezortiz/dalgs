package es.ucm.fdi.dalgs.rest.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.dalgs.domain.Activity;

public class Activity_Response implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Activity activity;
	private Collection<String> errors;
	

	public Activity_Response() {
		super();
		this.activity = new Activity();
		this.setErrors(new ArrayList<String>());
	}

	public Activity_Response (Activity activity, Collection<String> errors){
		super();
		this.activity = activity;
		this.setErrors(errors);
	}

	
	
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Collection<String> getErrors() {
		return errors;
	}

	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}


	
	
}