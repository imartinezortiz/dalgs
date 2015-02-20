package com.example.tfg.service.implementation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.LearningGoal;
import com.example.tfg.repository.LearningGoalDao;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.LearningGoalService;

@Service
public class LearningGoalServiceImp implements LearningGoalService {


	@Autowired
	CompetenceService serviceCompetence;

	@Autowired
	LearningGoalDao daoLearningGoal;

	@Autowired
	ActivityService serviceActivity;

	@Transactional(readOnly = false)
	public boolean addLearningGoal(LearningGoal newLearningGoal,
			Long id_competence) {

		Competence competence = serviceCompetence.getCompetence(id_competence);
		LearningGoal existLearning = daoLearningGoal.existByCode(newLearningGoal.getInfo().getCode(), competence);
		if(existLearning == null){
			newLearningGoal.setCompetence(competence);
			competence.getLearningGoals().add(newLearningGoal);
			if(daoLearningGoal.addLearningGoal(newLearningGoal))
				return serviceCompetence.modifyCompetence(competence);
		}
		else if (existLearning.isDeleted()) {
			existLearning.setCompetence(competence);
			existLearning.setDeleted(false);
			competence.getLearningGoals().add(existLearning);
			if(daoLearningGoal.saveLearningGoal(existLearning))
				return serviceCompetence.modifyCompetence(competence);

		}

		return false;
	}

	@Transactional(readOnly = true)
	public LearningGoal getLearningGoal(Long id_learningGoal) {
		return daoLearningGoal.getLearningGoal(id_learningGoal);
	}

	@Transactional(readOnly = false)
	public boolean modifyLearningGoal(LearningGoal learningGoal, Long id_learningGoal) {
		LearningGoal modifyLearningGoal = daoLearningGoal.getLearningGoal(id_learningGoal);
		modifyLearningGoal.setInfo(learningGoal.getInfo());
		return daoLearningGoal.saveLearningGoal(modifyLearningGoal);

	}

	@Transactional(readOnly = false)
	public boolean deleteLearningGoal(Long id_learningGoal) {
		boolean deleteFromActivities = serviceActivity.deleteLearningActivities(daoLearningGoal.getLearningGoal(id_learningGoal));;
		if (deleteFromActivities)
			return daoLearningGoal.deleteLearningGoal(id_learningGoal);
		return false;
	}

	public boolean deleteLearningGoalForCompetence(Competence competence) {

		return daoLearningGoal.deleteLearningGoalForCompetence(competence);
	}

	@Transactional(readOnly = false)
	public Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course, Activity activity) {
		Collection<LearningGoal> learningGoals = daoLearningGoal.getLearningGoalsFromActivity(activity);
		if(!learningGoals.isEmpty())
			return daoLearningGoal.getLearningGoalsFromCourse(id_course, learningGoals);
		else return daoLearningGoal.getLearningGoalsFromCourse(id_course);
	}

	@Transactional(readOnly = true)
	public LearningGoal getLearningGoalByName(String name) {
		return daoLearningGoal.getLearningGoalByName(name);
	}


	public Collection<LearningGoal> getLearningGoalsFromCompetence(
			Competence competence) {

		return daoLearningGoal.getLearningGoalsFromCompetence(competence);
	}


	public boolean deleteLearningGoalForCompetences(
			Collection<Competence> competences) {

		return daoLearningGoal.deleteLearningGoalsForCompetences(competences);
	}

}
