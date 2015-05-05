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
package es.ucm.fdi.dalgs.rest.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;



import es.ucm.fdi.dalgs.domain.Activity;


public class Activity_Response implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private Activity activity;
	private Collection<String> errors;
	

	public Activity_Response() {
		super();
		this.activity = new Activity();
		this.setErrors(new ArrayList<String>());
	}

	public Activity_Response (Activity activity, Collection<String> errors){
		super();
		this.activity = activity;
		this.setErrors(errors);
	}

	
	
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Collection<String> getErrors() {
		return errors;
	}

	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}


	
	
}