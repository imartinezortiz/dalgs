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
package es.ucm.fdi.dalgs.rest.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.ExternalActivity;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.rest.classes.Activity_Request;
import es.ucm.fdi.dalgs.rest.classes.Activity_Response;
import es.ucm.fdi.dalgs.rest.classes.ExternalActivity_Response;
import es.ucm.fdi.dalgs.rest.service.WebhookService;

@RestController
public class WebhookController {

	@Autowired
	private WebhookService service_rest;

	/*
	 * @ResponseBody annotation is used to map the response object in the
	 * response body. Once the response object is returned by the handler
	 * method, MappingJackson2HttpMessageConverter kicks in and convert it to
	 * JSON response.
*/



	@RequestMapping(value = WebhookUriConstants.GET_ACTIVITY, headers = { "Accept=text/xml, application/json" }, produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody Activity_Response get(
			@PathVariable("id") Long activity_id) {
		ResultClass<Activity> result = new ResultClass<Activity>();

		result = service_rest.getActivityREST(activity_id);
		return new Activity_Response(result.getSingleElement(),
				result.getErrorsList());
	}

	@RequestMapping(value = WebhookUriConstants.POST_ACTIVITY, headers = {"Accept=text/xml, application/json"}, produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody ExternalActivity_Response externalActivity(
			@Valid @RequestBody Activity_Request activity_rest) {

		ExternalActivity act = new ExternalActivity();
		act.getInfo().setName(activity_rest.getName());
		act.getInfo().setDescription(activity_rest.getDescription());
		act.getInfo().setCode(activity_rest.getCode());
		ResultClass<ExternalActivity> result = new ResultClass<ExternalActivity>();

		Course course = service_rest.getCourseREST(activity_rest.getId_course())
				.getSingleElement();
		Group group = service_rest.getGroupREST(activity_rest.getId_group())
				.getSingleElement();
		if (course != null && group != null) {
			result = service_rest.addActivitytoGroupREST(group, act, group
					.getId(), course.getId(), course.getAcademicTerm().getId());
		} else if (course != null) {
			result = service_rest
					.addActivityCourseREST(course, act, course.getId(), course
							.getAcademicTerm().getId(), Locale.UK);
		}
	
		
		return new ExternalActivity_Response(result.getSingleElement(),
				result.getErrorsList());
	}

	  
    /** 
     * Error if user try to access to any other url. 
     */  
    @RequestMapping(value = "/api/**", method = RequestMethod.GET)  
    public void notExistsUrlHandler(HttpServletRequest request) {  
        throw new IllegalArgumentException("Requested url not exists: " + request.getRequestURI());  
    }  

     
}