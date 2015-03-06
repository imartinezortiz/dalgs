package es.ucm.fdi.dalgs.learningGoal.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.LearningGoalStatus;
import es.ucm.fdi.dalgs.learningGoal.repository.LearningGoalRepository;

@Component
public class LearningGoalStatusFormatter implements Formatter<LearningGoalStatus> {

	@Autowired
	private LearningGoalRepository daoLearningGoal;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(LearningGoalStatus learningGoalStatus, Locale arg1) {
		return learningGoalStatus.getLearningGoal().getInfo().getName();
	}

	public LearningGoalStatus parse(String learningGoalId, Locale arg1)
			throws ParseException {
		LearningGoalStatus learningGoalStatus = new LearningGoalStatus();
		learningGoalStatus.setLearningGoal(daoLearningGoal.getLearningGoal(Long
				.parseLong(learningGoalId)));
		return learningGoalStatus;
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
