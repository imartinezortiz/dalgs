package com.example.tfg.repository;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.LearningGoal;

@Repository
public interface LearningGoalDao {

	boolean addLearningGoal(LearningGoal newLearningGoal);

	boolean saveLearningGoal(LearningGoal existLearning);

	LearningGoal existByCode(String code);

	LearningGoal getLearningGoal(Long id_learningGoal);

	boolean deleteLearningGoal(Long id_learningGoal);

	boolean deleteLearningGoalForCompetence(Competence competence);

	Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course, Collection<LearningGoal> learningGoals);

	LearningGoal getLearningGoalByName(String name);

	Collection<LearningGoal> getLearningGoalsFromActivity(Activity activity);

	Collection<LearningGoal> getLearningGoalsFromCourse(Long id_course);

	Collection<LearningGoal> getLearningGoalsFromCompetence(
			Competence competence);

	boolean deleteLearningGoalsForCompetences(Collection<Competence> competences);

}
