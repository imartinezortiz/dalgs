package com.example.tfg.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.LearningGoal;

@Service
public interface LearningGoalService {

	LearningGoal getLearningGoal(Long id_learningGoal);

	boolean modifyLearningGoal(LearningGoal modify, Long id_learningGoal);

	boolean deleteLearningGoal(Long id_learningGoal);

	boolean addLearningGoal(LearningGoal newLearningGoal, Long id_competence);

	Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course, Activity p);

	LearningGoal getLearningGoalByName(String name);

	Collection<LearningGoal> getLearningGoalsFromCompetence(
			Competence competence);



}
