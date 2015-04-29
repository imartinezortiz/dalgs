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
