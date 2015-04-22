package es.ucm.fdi.dalgs.topic.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.domain.info.TopicInfo;

public class TopicCSV {

	@SuppressWarnings("unused")
	public List<Topic> readCSVTopicToBean(InputStream in, String charsetName,
			CsvPreference csvPreference, Module module) throws IOException {
		CsvBeanReader beanReader = null;
		List<Topic> topics = new ArrayList<Topic>();
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(in,
					Charset.forName(charsetName)), csvPreference);
			// the name mapping provide the basis for bean setters
			final String[] nameMapping = new String[] { "code", "name",
					"description" };
			// just read the header, so that it don't get mapped to User
			// object
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getTopicProcessors();

			TopicInfo info;

			while ((info = beanReader.read(TopicInfo.class, nameMapping,
					processors)) != null) {
				Topic t = new Topic();
				t.setInfo(info);
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

		final CellProcessor[] processors = new CellProcessor[] { new NotNull(), // Code
				new NotNull(), // Name
				new NotNull(), // Description

		};
		return processors;
	}
	
	public void downloadCSV(HttpServletResponse response, Collection<Topic> topics) throws IOException {

		 String csvFileName = "topics.csv";
		 
	        response.setContentType("text/csv");
	 
	        // creates mock data
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                csvFileName);
	        response.setHeader(headerKey, headerValue);
	 

	        // uses the Super CSV API to generate CSV data from the model data
	        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
	                CsvPreference.STANDARD_PREFERENCE);
	         
	        String[] header = {"code", "name", "description"};
	 
	        csvWriter.writeHeader(header);
	 
	        for (Topic top : topics) {
	            csvWriter.write(top.getInfo(), header);
	        }
	        csvWriter.close();  
	}


}
