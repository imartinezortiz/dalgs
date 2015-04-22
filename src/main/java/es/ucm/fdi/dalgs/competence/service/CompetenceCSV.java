package es.ucm.fdi.dalgs.competence.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.classes.TypeWithEnum;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.info.CompetenceInfo;

public class CompetenceCSV {

	@SuppressWarnings("unused")
	public List<Competence> readCSVCompetenceToBean(InputStream in,
			String charsetName, CsvPreference csvPreference, Degree degree)
			throws IOException {
		CsvBeanReader beanReader = null;
		List<Competence> competences = new ArrayList<Competence>();
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(in,
					Charset.forName(charsetName)), csvPreference);
			// the name mapping provide the basis for bean setters
			final String[] nameMapping = new String[] { "code", "name",
					"description", "type" };
			// just read the header, so that it don't get mapped to User
			// object
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getCompetenceProcessors();

			CompetenceInfo info;

			while ((info = beanReader.read(CompetenceInfo.class, nameMapping,
					processors)) != null) {
				Competence c = new Competence();
				c.setInfo(info);
				c.setDegree(degree);
				competences.add(c);
			}

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return competences;
	}

	/* CellProcessors have to correspond to the entity database fields */
	private static CellProcessor[] getCompetenceProcessors() {

		final CellProcessor[] processors = new CellProcessor[] { 
				new NotNull(), // Code
				new NotNull(), // Name
				new NotNull(), // Description
				new ParseEnum(TypeWithEnum.TypeOfCompetence.class, true)
				// Type

		};
		return processors;
	}
		
	public void downloadCSV(HttpServletResponse response, Collection<Competence> competences) throws IOException {

		 String csvFileName = "competences.csv";
		 
	        response.setContentType("text/csv");
	 
	        // creates mock data
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                csvFileName);
	        response.setHeader(headerKey, headerValue);
	 


	        // uses the Super CSV API to generate CSV data from the model data
	        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
	                CsvPreference.STANDARD_PREFERENCE);
	         
	        String[] header = {"code", "name", "description", "type"};
	 
	        csvWriter.writeHeader(header);
	 
	        for (Competence comp : competences) {
	            csvWriter.write(comp.getInfo(), header);
	        }
	        csvWriter.close();  
	}

}

