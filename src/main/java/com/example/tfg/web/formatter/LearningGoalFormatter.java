package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.LearningGoal;
import com.example.tfg.repository.LearningGoalDao;

@Component
public class LearningGoalFormatter implements Formatter<LearningGoal>{

	@Autowired
	private LearningGoalDao daoLearningGoal;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(LearningGoal learningGoal, Locale arg1) {
		return learningGoal.getInfo().getName();
	}

	public LearningGoal parse(String learningGoalId, Locale arg1) throws ParseException {

		return daoLearningGoal.getLearningGoal(Long.parseLong(learningGoalId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
