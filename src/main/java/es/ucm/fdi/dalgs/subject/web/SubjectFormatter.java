package es.ucm.fdi.dalgs.subject.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.subject.repository.SubjectRepository;

@Component
public class SubjectFormatter implements Formatter<Subject> {

	@Autowired
	private SubjectRepository subjectService;

	// Some service class which can give the Actor after
	// fetching from Database

	@Override
	public String print(Subject subject, Locale arg1) {
		return subject.getInfo().getName();
	}

	@Override
	public Subject parse(String subjectId, Locale arg1) throws ParseException {

		return subjectService.getSubjectFormatter(Long.parseLong(subjectId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
