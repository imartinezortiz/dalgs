package com.example.tfg.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.LearningGoal;

@Service
public interface LearningGoalService {

	LearningGoal getLearningGoal(Long id_learningGoal);

	ResultClass<Boolean> modifyLearningGoal(LearningGoal modify, Long id_learningGoal);

	boolean deleteLearningGoal(Long id_learningGoal);

	ResultClass<Boolean> addLearningGoal(LearningGoal newLearningGoal, Long id_competence);

	Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course, Activity p);

	LearningGoal getLearningGoalByName(String name);

	Collection<LearningGoal> getLearningGoalsFromCompetence(
			Competence competence);

	boolean deleteLearningGoalForCompetences(Collection<Competence> competences);
	
	public ResultClass<Boolean> unDeleteLearningGoal(LearningGoal learning);



}
