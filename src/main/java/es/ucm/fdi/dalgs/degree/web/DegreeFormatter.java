package es.ucm.fdi.dalgs.degree.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.degree.repository.DegreeRepository;
import es.ucm.fdi.dalgs.domain.Degree;

@Component
public class DegreeFormatter implements Formatter<Degree> {

	@Autowired
	private DegreeRepository degreeDao;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(Degree degree, Locale arg1) {
		return degree.getInfo().getName();
	}

	public Degree parse(String degreeId, Locale arg1) throws ParseException {

		return degreeDao.getDegree(Long.parseLong(degreeId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
