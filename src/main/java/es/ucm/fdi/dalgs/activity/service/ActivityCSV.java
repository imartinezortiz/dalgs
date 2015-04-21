package es.ucm.fdi.dalgs.activity.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;


import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.domain.Activity;


public class ActivityCSV {
	


	public void dowloadCSV(HttpServletResponse response, Collection<Activity> activities) throws IOException{
			 String csvFileName = "activities.csv";
			 
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
		 
		        for (Activity act : activities) {
		            csvWriter.write(act.getInfo(), header);
		        }
		        csvWriter.close();  
	}

}
