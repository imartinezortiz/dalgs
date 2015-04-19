package es.ucm.fdi.dalgs.subject.service;

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

import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.domain.info.SubjectInfo;

public class SubjectUpload {

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
					"description" };
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

		final CellProcessor[] processors = new CellProcessor[] { new NotNull(), // Code
				new NotNull(), // Name
				new NotNull(), // Description
		};
		return processors;
	}

}
