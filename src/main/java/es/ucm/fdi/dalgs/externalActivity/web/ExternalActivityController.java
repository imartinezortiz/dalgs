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

package es.ucm.fdi.dalgs.externalActivity.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.externalActivity.service.ExternalActivityService;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;

@Controller
public class ExternalActivityController {

	@Autowired
	private ExternalActivityService serviceExternalActivity;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private LearningGoalService serviceLearningGoal;

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ExternalActivityController.class);
	/**
	 *COURSE 
	 */
	
	/**
	 * Method for delete an activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/externalactivity/{externalActivityId}/delete.htm", method = RequestMethod.GET)
	public String deleteExternalActivityCourseGET(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("externalActivityId") Long id_externalActivity)
			throws ServletException {

//		Course course = serviceCourse.getCourse(id_course, id_AcademicTerm)
//				.getSingleElement();

		if (serviceExternalActivity.deleteExternalActivity(id_AcademicTerm,id_course, id_externalActivity)
				.getSingleElement()!=null) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/externalactivity/{externalActivityId}.htm", method = RequestMethod.GET)
	public ModelAndView getExternalActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("externalActivityId") Long id_externalActivity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Activity a = serviceExternalActivity.getExternalActivity(id_externalActivity, id_course, null,
				id_academic).getSingleElement();

		if (a != null) {
			model.put("activity", a);
			model.put("externalActivityId", id_externalActivity);
//			model.put("learningStatus", a.getLearningGoalStatus());

			return new ModelAndView("externalActivity/view", "model", model);
		}

		return new ModelAndView("exception/notFound", "model", model);
	}


	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/externalactivity/{externalActivityId}/move.htm", method=RequestMethod.GET)
	// Every Post have to return redirect
	public String moveExternalActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("externalActivityId") Long id_externalActivity, Locale locale) {

		if (serviceExternalActivity.moveCourse(id_externalActivity, id_academic, id_course, locale).getSingleElement())
		
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";
		else {
			return "redirect:/error.htm";

		}
	}
	
	/**
	 * GROUP
	 * 
	 */
	
	/**
	 * Method for delete an activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/externalactivity/{externalActivityId}/delete.htm", method = RequestMethod.GET)
	public String deleteExternalActivityGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("externalActivityId") Long id_externalActivity)
			throws ServletException {

//		Group group = serviceGroup.getGroup(id_group, id_course, id_academic)
//				.getSingleElement();

		if (serviceExternalActivity.deleteExternalActivity(id_academic, id_course, id_group, id_externalActivity)
				.getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		} else
			return "redirect:/error.htm";
	}
	
	/**
	 * Methods for view activities
	 */
	

	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/externalactivity/{externalActivityId}.htm", method = RequestMethod.GET)
	public ModelAndView getExternalActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("externalActivityId") Long id_externalActivity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Activity a = serviceExternalActivity.getExternalActivity(id_externalActivity, id_course,
				id_group, id_academic).getSingleElement();

		if (a != null) {
			model.put("activity", a);
			model.put("externalActivityId", id_externalActivity);

//			model.put("learningStatus", a.getLearningGoalStatus());

			return new ModelAndView("externalActivity/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);
	}


	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/externalactivity/{externalActivityId}/move.htm", method=RequestMethod.GET)
	// Every Post have to return redirect
	public String moveExternalActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("externalActivityId") Long id_externalActivity, Locale locale) {

		if (serviceExternalActivity.moveGroup(id_externalActivity, id_academic, id_course, id_group).getSingleElement())
		
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		else {
			return "redirect:/error.htm";

		}
	}

	
	@RequestMapping(value = "/externalActivities.htm", method = RequestMethod.GET)
	public ModelAndView getExternalActivityList()
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();
		Collection<Activity> externals = serviceExternalActivity.getExternalActivitiesAll();

		if (externals != null) {
			model.put("externalActivities", externals);

			return new ModelAndView("externalActivity/list", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);
	}
	
	public static Logger getLogger() {
		return logger;
	}
}
