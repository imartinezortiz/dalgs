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
package es.ucm.fdi.dalgs.subject.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.domain.info.SubjectInfo;

public class SubjectCSV {

	@SuppressWarnings("unused")
	public List<Subject> readCSVSubjectToBean(InputStream in,
			String charsetName, CsvPreference csvPreference, Topic topic)
			throws IOException {
		CsvBeanReader beanReader = null;
		List<Subject> subjects = new ArrayList<Subject>();
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(in,
					Charset.forName(charsetName)), csvPreference);
			// the name mapping provide the basis for bean setters
			final String[] nameMapping = new String[] { "code", "name",
					"description" , "credits", "url_doc"};
			// just read the header, so that it don't get mapped to User
			// object
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getSubjectProcessors();

			SubjectInfo info;

			while ((info = beanReader.read(SubjectInfo.class, nameMapping,
					processors)) != null) {
				Subject s = new Subject();
				s.setInfo(info);
				s.setTopic(topic);
				subjects.add(s);
			}

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return subjects;
	}

	/* CellProcessors have to correspond to the entity database fields */
	private static CellProcessor[] getSubjectProcessors() {

		final CellProcessor[] processors = new CellProcessor[] { 
				new NotNull(), // Code
				new NotNull(), // Name
				new NotNull(), // Description
				new ParseInt(),
				new Optional()
		};
		return processors;
	}
	
	public void downloadCSV(HttpServletResponse response,Collection<Subject> subjects ) throws IOException {

		 String csvFileName = "subjects.csv";
		 
	        response.setContentType("text/csv");
	 
	        // creates mock data
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                csvFileName);
	        response.setHeader(headerKey, headerValue);
	 
	        // uses the Super CSV API to generate CSV data from the model data
	        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
	                CsvPreference.STANDARD_PREFERENCE);
	         
	        String[] header = {"code", "name", "description", "credits", "url_doc"};
	 
	        csvWriter.writeHeader(header);
	 
	        for (Subject sub : subjects) {
	            csvWriter.write(sub.getInfo(), header);
	        }
	        csvWriter.close();  
	}
	

}
