package es.ucm.fdi.dalgs.learningGoal.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.learningGoal.repository.LearningGoalRepository;

@Service
public class LearningGoalService {

	@Autowired
	CompetenceService serviceCompetence;

	@Autowired
	LearningGoalRepository daoLearningGoal;

	@Autowired
	ActivityService serviceActivity;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addLearningGoal(LearningGoal learningGoal,
			Long id_competence) {

		LearningGoal learningExists = daoLearningGoal.existByCode(learningGoal.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( learningExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (learningExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setSingleElement(false);
			result.setErrorsList(errors);
		}
		else{
			learningGoal.setCompetence(serviceCompetence.getCompetence(id_competence).getSingleElement());
			boolean r = daoLearningGoal.addLearningGoal(learningGoal);
			if (r) 
				result.setSingleElement(true);
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<LearningGoal> getLearningGoal(Long id_learningGoal) {
		ResultClass<LearningGoal> result = new ResultClass<LearningGoal>();
		result.setSingleElement(daoLearningGoal.getLearningGoal(id_learningGoal));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyLearningGoal(LearningGoal learningGoal, Long id_learningGoal) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		LearningGoal modifyLearning = daoLearningGoal.getLearningGoal(id_learningGoal);
		
		LearningGoal learningExists = daoLearningGoal.existByCode(learningGoal.getInfo().getCode());
		
		if(!learningGoal.getInfo().getCode().equalsIgnoreCase(modifyLearning.getInfo().getCode()) && 
				learningExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (learningExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifyLearning.setInfo(learningGoal.getInfo());
			boolean r = daoLearningGoal.saveLearningGoal(modifyLearning);
			if (r) 
				result.setSingleElement(true);
		}
		return result;

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteLearningGoal(Long id_learningGoal) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoLearningGoal.deleteLearningGoal(id_learningGoal));
		return result;

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public ResultClass<Boolean> deleteLearningGoalForCompetence(Competence competence) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoLearningGoal.deleteLearningGoalForCompetence(competence));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Collection<LearningGoal>> getLearningGoalsFromCourse(Long id_course, Activity activity) {
		ResultClass<Collection<LearningGoal>> result = new ResultClass<Collection<LearningGoal>>();
		Collection<LearningGoal> learningGoals = daoLearningGoal.getLearningGoalsFromActivity(activity);
		if(!learningGoals.isEmpty()){
			result.setSingleElement(daoLearningGoal.getLearningGoalsFromCourse(id_course, learningGoals));
			return result;
		}
			
		else{
			result.setSingleElement(daoLearningGoal.getLearningGoalsFromCourse(id_course));
			return result;
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<LearningGoal> getLearningGoalByName(String name) {
		ResultClass<LearningGoal> result = new ResultClass<LearningGoal>();
		result.setSingleElement(daoLearningGoal.getLearningGoalByName(name));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public ResultClass<Collection<LearningGoal>> getLearningGoalsFromCompetence(
			Competence competence) {
		ResultClass<Collection<LearningGoal>> result = new ResultClass<Collection<LearningGoal>>();
		result.setSingleElement(daoLearningGoal.getLearningGoalsFromCompetence(competence));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public ResultClass<Boolean> deleteLearningGoalForCompetences(
			Collection<Competence> competences) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoLearningGoal.deleteLearningGoalsForCompetences(competences));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteLearningGoal(LearningGoal learning) {
		LearningGoal l = daoLearningGoal.existByCode(learning.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(l == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!l.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			l.setDeleted(false);
			l.setInfo(learning.getInfo());
			boolean r = daoLearningGoal.saveLearningGoal(l);
			if(r) 
				result.setSingleElement(true);	

		}
		return result;
	}


}
