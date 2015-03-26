package es.ucm.fdi.dalgs.topic.service;

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

import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.domain.Topic;

public class TopicUpload {
	
	
	@SuppressWarnings("unused")
	public	 List<Topic> readCSVTopicToBean(InputStream in,
			String charsetName, CsvPreference csvPreference, Module module) throws IOException {
		CsvBeanReader beanReader = null;
		List<Topic> topics = new ArrayList<Topic>();
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(in,
					Charset.forName(charsetName)), csvPreference);
			// the name mapping provide the basis for bean setters
			final String[] nameMapping = new String[] {"info.code", "info.name", "info.description"};
			// just read the header, so that it don't get mapped to User
			// object
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getTopicProcessors();

			Topic t;

			while ((t = beanReader.read(Topic.class, nameMapping, processors)) != null) {
				t.setModule(module);
				topics.add(t);
			}

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return topics;
	}

	/* CellProcessors have to correspond to the entity database fields */
	private static CellProcessor[] getTopicProcessors() {

	        
		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), //Code
				new NotNull(), //Name
				new NotNull(), // Description
				
		};
		return processors;
	}

}
