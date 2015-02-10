package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.Subject;
import com.example.tfg.repository.SubjectDao;

@Component
public class SubjectFormatter implements Formatter<Subject> {

	@Autowired
	private SubjectDao subjectService;

	// Some service class which can give the Actor after
	// fetching from Database

	@Override
	public String print(Subject subject, Locale arg1) {
		return subject.getInfo().getName();
	}

	@Override
	public Subject parse(String subjectId, Locale arg1) throws ParseException {

		return subjectService.getSubject(Long.parseLong(subjectId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
