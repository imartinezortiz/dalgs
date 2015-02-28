package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
import com.example.tfg.repository.CompetenceDao;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.LearningGoalService;
import com.example.tfg.service.SubjectService;

@Service
public class CompetenceServiceImp implements CompetenceService {
	@Autowired
	private CompetenceDao daoCompetence;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired 
	private LearningGoalService serviceLearning;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCompetence(Competence competence, Long id_degree) {
		competence.setDegree(serviceDegree.getDegree(id_degree));
		
		Competence competenceExists = daoCompetence.existByCode(competence.getInfo().getCode(), competence.getDegree());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( competenceExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (competenceExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			
			boolean r = daoCompetence.addCompetence(competence);
			if (r) 
				result.setE(true);
		}
		return result;	
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Competence> getAll() {
		return daoCompetence.getAll();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Competence getCompetence(Long id) {
		return daoCompetence.getCompetence(id);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Competence getCompetenceByName(String name) {
		return daoCompetence.getCompetenceByName(name);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetence(Long id) {
		Competence competence = daoCompetence.getCompetence(id);
		Collection <Competence> competences= new ArrayList<Competence>();
		competences.add(competence);
		if(serviceLearning.deleteLearningGoalForCompetences(competences))
			return daoCompetence.deleteCompetence(id);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public List<Competence> getCompetencesForSubject(Long id_subject) {

		return daoCompetence.getCompetencesForSubject(id_subject);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Competence> getCompetencesForDegree(Long id_degree) {
		// TODO Auto-generated method stub
		return daoCompetence.getCompetencesForDegree(id_degree);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCompetence(Competence competence, Long id_competence, Long id_degree) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		
		competence.setDegree(serviceDegree.getDegree(id_degree));
		Competence modifyCompetence = daoCompetence.getCompetence(id_competence);
		
		Competence competenceExists = daoCompetence.existByCode(competence.getInfo().getCode(),competence.getDegree() );
		
		if(!competence.getInfo().getCode().equalsIgnoreCase(modifyCompetence.getInfo().getCode()) && 
				competenceExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (competenceExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifyCompetence.setInfo(competence.getInfo());
			boolean r = daoCompetence.saveCompetence(modifyCompetence);
			if (r) 
				result.setE(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetenceFromSubject(Long id_competence,
			Long id_subject) {
		// Subject subject = daoSubject.getSubject(id);

		Collection<Competence> c = serviceSubject.getSubject(id_subject)
				.getCompetences();
		try {
			c.remove(daoCompetence.getCompetence(id_competence));
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public boolean deleteCompetencesForDegree(Degree degree) {
		if(serviceLearning.deleteLearningGoalForCompetences(degree.getCompetences()))
			return daoCompetence.deleteCompetencesForDegree(degree);
		else return false;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public boolean modifyCompetence(Competence competence) {
		return daoCompetence.saveCompetence(competence);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Competence getCompetenceAll(Long id_competence) {
		Competence competence = daoCompetence.getCompetence(id_competence);
		//		Competence c = daoCompetence.getCompetenceAll(id_competence);
		competence.setLearningGoals(serviceLearning.getLearningGoalsFromCompetence(competence));

		return competence;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteCompetence(Competence competence, Long id_degree) {
		
		competence.setDegree(serviceDegree.getDegree(id_degree));
		Competence c = daoCompetence.existByCode(competence.getInfo().getCode(), competence.getDegree());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(c == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!c.isDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			c.setDeleted(false);
			c.setInfo(competence.getInfo());
			boolean r = daoCompetence.saveCompetence(c);
			if(r) 
				result.setE(true);	

		}
		return result;
	}



}
