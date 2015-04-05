package es.ucm.fdi.dalgs.learningGoal.web;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.learningGoal.repository.LearningGoalRepository;

@Component
public class LearningGoalFormatter implements Formatter<LearningGoal>{

	@Autowired
	private LearningGoalRepository daoLearningGoal;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(LearningGoal learningGoal, Locale arg1) {
		return learningGoal.getInfo().getName();
	}

	public LearningGoal parse(String learningGoalId, Locale arg1) throws ParseException {

		return daoLearningGoal.getLearningGoalFormatter(Long.parseLong(learningGoalId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
