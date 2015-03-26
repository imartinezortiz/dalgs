package es.ucm.fdi.dalgs.degree.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.domain.Degree;

public class DegreeUpload {
	
	
	@SuppressWarnings("unused")
	public	 List<Degree> readCSVDegreeToBean(InputStream in,
			String charsetName, CsvPreference csvPreference) throws IOException {
		CsvBeanReader beanReader = null;
		List<Degree> degrees = new ArrayList<Degree>();
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(in,
					Charset.forName(charsetName)), csvPreference);
			// the name mapping provide the basis for bean setters
			final String[] nameMapping = new String[] { "info.code", "info.name", "info.description"};
			// just read the header, so that it don't get mapped to User
			// object
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getDegreeProcessors();

			Degree d;

			while ((d = beanReader.read(Degree.class, nameMapping, processors)) != null) {
				degrees.add(d);
			}

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return degrees;
	}

	/* CellProcessors have to correspond to the entity database fields */
	private static CellProcessor[] getDegreeProcessors() {
	
	        
		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), //Code
				new NotNull(), //Name
				new NotNull(), // Description
		};
		return processors;
	}

}
