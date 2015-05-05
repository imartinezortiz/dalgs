package es.ucm.fdi.dalgs.externalActivity.web;

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

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.ExternalActivity;
import es.ucm.fdi.dalgs.domain.Group;
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

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/externalActivity/{externalActivityId}/delete.htm", method = RequestMethod.GET)
	public String deleteExternalActivityCourseGET(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("externalActivityId") Long id_externalActivity)
			throws ServletException {

		Course course = serviceCourse.getCourse(id_course, id_AcademicTerm)
				.getSingleElement();

		if (serviceExternalActivity.deleteExternalActivity(course, null, id_externalActivity)
				.getSingleElement()!=null) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/externalActivity/{externalActivityId}.htm", method = RequestMethod.GET)
	public ModelAndView getExternalActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("externalActivityId") long id_externalActivity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		ExternalActivity a = serviceExternalActivity.getExternalActivity(id_externalActivity, id_course, null,
				id_academic).getSingleElement();

		if (a != null) {
			model.put("externalEexternalActivity", a);
			model.put("externalActivityId", id_externalActivity);
//			model.put("learningStatus", a.getLearningGoalStatus());

			return new ModelAndView("externalActivity/view", "model", model);
		}

		return new ModelAndView("exception/notFound", "model", model);
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/externalActivity/{externalActivityId}/restore.htm")
	// Every Post have to return redirect
	public String restoreExternalActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("externalActivityId") Long id_externalActivity, Locale locale) {
		Course course = serviceCourse.getCourse(id_course, id_academic)
				.getSingleElement();

		ResultClass<ExternalActivity> result = serviceExternalActivity.unDeleteExternalActivity(
				course,
				null,
				serviceExternalActivity.getExternalActivity(id_externalActivity, id_course, null,
						id_academic).getSingleElement(), locale);

		if (!result.hasErrors())

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

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/externalActivity/{externalActivityId}/delete.htm", method = RequestMethod.GET)
	public String deleteExternalActivityGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("externalActivityId") Long id_externalActivity)
			throws ServletException {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic)
				.getSingleElement();

		if (serviceExternalActivity.deleteExternalActivity(null, group, id_externalActivity)
				.getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		} else
			return "redirect:/error.htm";
	}
	
	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/externalActivity/{externalActivityId}.htm", method = RequestMethod.GET)
	public ModelAndView getExternalActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("externalActivityId") long id_externalActivity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		ExternalActivity a = serviceExternalActivity.getExternalActivity(id_externalActivity, id_course,
				id_group, id_academic).getSingleElement();

		if (a != null) {
			model.put("externalActivity", a);
			model.put("externalActivityId", id_externalActivity);

//			model.put("learningStatus", a.getLearningGoalStatus());

			return new ModelAndView("externalActivity/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/externalActivity/{externalActivityId}/restore.htm")
	// Every Post have to return redirect
	public String restoreExternalActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,

			@PathVariable("externalActivityId") Long id_externalActivity, Locale locale) {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic)
				.getSingleElement();

		ResultClass<ExternalActivity> result = serviceExternalActivity.unDeleteExternalActivity(
				null,
				group,
				serviceExternalActivity.getExternalActivity(id_externalActivity, id_course, id_group,
						id_academic).getSingleElement(), locale);

		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		else {
			return "redirect:/error.htm";

		}
	}
}
