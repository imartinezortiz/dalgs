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