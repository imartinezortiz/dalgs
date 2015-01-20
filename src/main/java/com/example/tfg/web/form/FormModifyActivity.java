package com.example.tfg.web.form;

import java.util.List;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;

public class FormModifyActivity {

	Activity activity;
	List<Competence> competences;
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public List<Competence> getCompetences() {
		return competences;
	}
	public void setCompetences(List<Competence> competences) {
		this.competences = competences;
	}
	
	
}
