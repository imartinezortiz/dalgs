package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.LearningGoalStatus;
import com.example.tfg.repository.CompetenceDao;

@Component
public class LearningGoalStatusFormatter implements Formatter<LearningGoalStatus> {

	@Autowired
	private CompetenceDao competenceDao;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(LearningGoalStatus competencestatus, Locale arg1) {
		return competencestatus.getCompetence().getName();
	}

	public LearningGoalStatus parse(String competenceId, Locale arg1)
			throws ParseException {
		LearningGoalStatus competencestatus = new LearningGoalStatus();
		competencestatus.setCompetence(competenceDao.getCompetence(Long
				.parseLong(competenceId)));
		return competencestatus;
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
