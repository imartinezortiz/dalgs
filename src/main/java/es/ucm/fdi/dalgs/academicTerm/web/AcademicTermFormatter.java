/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.academicTerm.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.academicTerm.repository.AcademicTermRepository;
import es.ucm.fdi.dalgs.domain.AcademicTerm;

@Component
public class AcademicTermFormatter implements Formatter<AcademicTerm> {

	@Autowired
	private AcademicTermRepository academicTermDao;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(AcademicTerm academicTerm, Locale arg1) {
		return academicTerm.getTerm();
	}

	public AcademicTerm parse(String academicTermId, Locale arg1)
			throws ParseException {

		return (AcademicTerm) academicTermDao.getAcademicTermById(Long
				.parseLong(academicTermId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
