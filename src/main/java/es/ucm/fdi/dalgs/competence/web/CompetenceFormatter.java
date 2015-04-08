package es.ucm.fdi.dalgs.competence.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.competence.repository.CompetenceRepository;
import es.ucm.fdi.dalgs.domain.Competence;

@Component
public class CompetenceFormatter implements Formatter<Competence> {

	@Autowired
	private CompetenceRepository competenceDao;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(Competence competence, Locale arg1) {
		return competence.getInfo().getName();
	}

	public Competence parse(String competenceId, Locale arg1)
			throws ParseException {

		return competenceDao.getCompetenceFormatter(Long
				.parseLong(competenceId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
