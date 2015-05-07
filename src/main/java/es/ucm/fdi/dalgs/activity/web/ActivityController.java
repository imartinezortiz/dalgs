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
package es.ucm.fdi.dalgs.activity.web;

import java.io.IOException;
import java.security.acl.NotOwnerException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.FileUpload;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.domain.LearningGoalStatus;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService serviceActivity;

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
			.getLogger(ActivityController.class);

	/**
	 * --------------------COURSE-----------------------
	 */

	/**
	 * Methods for adding activities
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_PROFESSOR" })
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.GET)
	public String addActivityCourseGET(
			@PathVariable("academicId") Long id_Long,
			@PathVariable("courseId") Long id_course, Model model) {

		if (!model.containsAttribute("addactivity")) {

			Activity newActivity = new Activity();
			model.addAttribute("addactivity", newActivity);
		}
		return "activity/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String addActivityCoursePOST(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addactivity") @Valid Activity newactivity,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)
			throws NotOwnerException {

		if (!resultBinding.hasErrors()) {
			Course course = serviceCourse.getCourse(id_course, id_academicTerm)
					.getSingleElement();

			ResultClass<Activity> result = serviceActivity.addActivityCourse(
					course, newactivity, id_course, id_academicTerm, locale);
			if (!result.hasErrors())
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + "/activity/"
						+ result.getSingleElement().getId() + "/modify.htm";
			else {

				if (result.isElementDeleted()) {
					attr.addFlashAttribute("unDelete",
							result.isElementDeleted());
					attr.addFlashAttribute("addactivity",
							result.getSingleElement());
				} else
					attr.addFlashAttribute("addactivity",
							result.getSingleElement());
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addAcademicTerm",
					resultBinding);
			attr.addFlashAttribute("addactivity", newactivity);

		}

		return "redirect:/academicTerm/{academicId}/course/{courseId}/activity/add.htm";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteActivityCourse(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addActivity") @Valid Activity activity,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			Course course = serviceCourse.getCourse(id_course, id_academicTerm)
					.getSingleElement();

			ResultClass<Activity> result = serviceActivity.unDeleteActivity(
					course, null, activity, locale);

			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + "/activity/"
						+ result.getSingleElement().getId() + "/modify.htm";
			else {
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}

		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addAcademicTerm",
					resultBinding);

		}
		attr.addFlashAttribute("addActivity", activity);
		return "redirect:/academicTerm/{academicId}/course/{courseId}/activity/add.htm";

	}

	/**
	 * Methods for modifying activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", method = RequestMethod.GET)
	public String modifyActivityCourseGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity, Model model)
			throws ServletException {

		Activity p = serviceActivity.getActivity(id_activity, id_course, null,
				id_academic).getSingleElement();
		model.addAttribute("courseId", id_course);

		if (p != null) {
			if (!model.containsAttribute("modifyactivity")) {
				model.addAttribute("modifyactivity", p);

			}

			Collection<LearningGoal> lg = serviceLearningGoal
					.getLearningGoalsFromCourse(id_course, p);

			model.addAttribute("learningGoalStatus", p.getLearningGoalStatus());
			model.addAttribute("learningGoals", lg);

			LearningGoalStatus cs = new LearningGoalStatus();
			model.addAttribute("addlearningstatus", cs);
			
			FileUpload file = new FileUpload();
			model.addAttribute("addfileupload",file);

			return "activity/modifyChoose";
		}

		return "redirect:/academicTerm/{academicId}/course/{courseId}.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", method = RequestMethod.POST)
	public String modifyActivityCoursePOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity,
			@ModelAttribute("modifyactivity") @Valid Activity activity,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		if (!resultBinding.hasErrors()) {
			Course course = serviceCourse.getCourse(id_course, id_academic)
					.getSingleElement();

			ResultClass<Boolean> result = serviceActivity.modifyActivity(
					course, null, activity, id_activity, id_course,
					id_academic, locale);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + ".htm";
			else {

				attr.addFlashAttribute("errors", result.getErrorsList());

			}

		} else {

			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.modifyactivity",
					resultBinding);

		}
		attr.addFlashAttribute("modifyactivity", activity);

		return "redirect:/academicTerm/" + id_academic + "/course/" + id_course
				+ "/activity/" + id_activity + "/modify.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{idCourse}/activity/{activityId}/addLearningStatus.htm", method = RequestMethod.POST)
	public String addLearningStatusPOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("idCourse") Long id_course,
			@PathVariable("activityId") Long id,
			@ModelAttribute("addlearningstatus") @Valid LearningGoalStatus learningGoalStatus,
			BindingResult result, Model model) throws ServletException {

		// Activity p = serviceActivity.getActivity(id);
		if (!result.hasErrors()) {
			Course course = serviceCourse.getCourse(id_course, id_academic)
					.getSingleElement();

			if (serviceActivity.addLearningGoals(course, null, id,
					learningGoalStatus, id_course, id_academic)
					.getSingleElement())
				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + "/activity/" + id + "/modify.htm";
		}
		return "redirect:/academicTerm/" + id_academic + "/course/" + id_course
				+ "/activity/" + id + "/modify.htm";
	}

	/**
	 * Method for delete an activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/delete.htm", method = RequestMethod.GET)
	public String deleteActivityCourseGET(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity)
			throws ServletException {

		Course course = serviceCourse.getCourse(id_course, id_AcademicTerm)
				.getSingleElement();

		if (serviceActivity.deleteActivity(course, null, id_activity)
				.getSingleElement()) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Method for delete an competence status of activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/competenceStatus/{compStatusId}/delete.htm", method = RequestMethod.GET)
	public String deleteCompetenceStatusActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") long id_Activity,
			@PathVariable("compStatusId") Long id_learningStatus)
			throws ServletException {
		Course course = serviceCourse.getCourse(id_course, id_academic)
				.getSingleElement();

		if (serviceActivity.deleteLearningActivity(course, null,
				id_learningStatus, id_Activity, id_academic).getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/activity/" + id_Activity + "/modify.htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}.htm", method = RequestMethod.GET)
	public ModelAndView getActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") long id_activity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Activity a = serviceActivity.getActivity(id_activity, id_course, null,
				id_academic).getSingleElement();

		if (a != null) {
			model.put("activity", a);
			model.put("activityId", id_activity);
			model.put("learningStatus", a.getLearningGoalStatus());

			return new ModelAndView("activity/view", "model", model);
		}

		return new ModelAndView("exception/notFound", "model", model);
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/restore.htm")
	// Every Post have to return redirect
	public String restoreActivityCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity, Locale locale) {
		Course course = serviceCourse.getCourse(id_course, id_academic)
				.getSingleElement();

		ResultClass<Activity> result = serviceActivity.unDeleteActivity(
				course,
				null,
				serviceActivity.getActivity(id_activity, id_course, null,
						id_academic).getSingleElement(), locale);

		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";
		else {
			return "redirect:/error.htm";

		}
	}

	/**
	 * --------------------GROUP-----------------------
	 */

	/**
	 * Methods for adding activities
	 */
	@Secured({ "ROLE_ADMIN", "ROLE_PROFESSOR" })
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/add.htm", method = RequestMethod.GET)
	public String addActivityGroupGET(@PathVariable("academicId") Long id_Long,
			@PathVariable("groupId") Long id_group,
			@PathVariable("courseId") Long id_course, Model model) {

		if (!model.containsAttribute("addactivity")) {
			Activity newActivity = new Activity();
			model.addAttribute("addactivity", newActivity);
		}
		return "activity/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String addActivityGroupPOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("addactivity") @Valid Activity newactivity,
			BindingResult resultBinding, RedirectAttributes attr)
			throws NotOwnerException {

		if (!resultBinding.hasErrors()) {
			Group group = serviceGroup.getGroup(id_group, id_course,
					id_academic).getSingleElement();

			ResultClass<Activity> result = serviceActivity.addActivitytoGroup(
					group, newactivity, id_group, id_course, id_academic);
			if (!result.hasErrors())
				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + "/group/" + id_group + "/activity/"
						+ result.getSingleElement().getId() + "/modify.htm";
			else {

				if (result.isElementDeleted()) {
					attr.addFlashAttribute("unDelete",
							result.isElementDeleted());
					attr.addFlashAttribute("addactivity",
							result.getSingleElement());
				} else
					attr.addFlashAttribute("addactivity",
							result.getSingleElement());

				// attr.addFlashAttribute("addactivity", newactivity);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addAcademicTerm",
					resultBinding);
			attr.addFlashAttribute("addactivity", newactivity);

		}

		return "redirect:/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/add.htm";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("addActivity") @Valid Activity activity,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {

			Group group = serviceGroup.getGroup(id_group, id_course,
					id_academic).getSingleElement();

			ResultClass<Activity> result = serviceActivity.unDeleteActivity(
					null, group, activity, locale);

			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + "/group/" + id_group + "/activity/"
						+ result.getSingleElement().getId() + "/modify.htm";
			else {
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}

		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.activity",
					resultBinding);

		}
		attr.addFlashAttribute("addActivity", activity);
		return "redirect:/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/add.htm";

	}

	/**
	 * Methods for modifying activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/{activityId}/modify.htm", method = RequestMethod.GET)
	public String modifyActivityGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("activityId") Long id_activity, Model model)
			throws ServletException {

		Activity p = serviceActivity.getActivity(id_activity, id_course,
				id_group, id_academic).getSingleElement();

		if (p != null) {
			model.addAttribute("courseId", id_course);

			if (!model.containsAttribute("modifyactivity")) {
				model.addAttribute("modifyactivity", p);

			}

			Collection<LearningGoal> lg = serviceLearningGoal
					.getLearningGoalsFromCourse(id_course, p);

			model.addAttribute("learningGoalStatus", p.getLearningGoalStatus());
			model.addAttribute("learningGoals", lg);

			LearningGoalStatus cs = new LearningGoalStatus();
			model.addAttribute("addlearningstatus", cs);
			
			FileUpload file = new FileUpload();
			model.addAttribute("addfileupload",file);

			return "activity/modifyChoose";
		}
		return "redirect:/academicTerm/{academicId}/course/{courseId}/group/{groupId}.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/{activityId}/modify.htm", method = RequestMethod.POST)
	public String modifyActivityGroupPOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("activityId") Long id_activity,
			@ModelAttribute("modifyactivity") @Valid Activity activity,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		if (!resultBinding.hasErrors()) {
			Group group = serviceGroup.getGroup(id_group, id_course,
					id_academic).getSingleElement();

			ResultClass<Boolean> result = serviceActivity.modifyActivity(null,
					group, activity, id_activity, id_course, id_academic,
					locale);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + "/group/" + id_group + ".htm";
			else {

				attr.addFlashAttribute("errors", result.getErrorsList());

			}

		} else {

			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.modifyactivity",
					resultBinding);

		}
		attr.addFlashAttribute("modifyactivity", activity);

		return "redirect:/academicTerm/" + id_academic + "/course/" + id_course
				+ "/group/" + id_group + "/activity/" + id_activity
				+ "/modify.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{idCourse}/group/{groupId}/activity/{activityId}/addLearningStatus.htm", method = RequestMethod.POST)
	public String addLearningStatusGroupPOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("idCourse") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("activityId") Long id,
			@ModelAttribute("addlearningstatus") @Valid LearningGoalStatus learningGoalStatus,
			BindingResult result, Model model) throws ServletException {

		if (!result.hasErrors()) {
			Group group = serviceGroup.getGroup(id_group, id_course,
					id_academic).getSingleElement();

			if (serviceActivity.addLearningGoals(null, group, id,
					learningGoalStatus, id_course, id_academic)
					.getSingleElement())
				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + "/group/" + id_group + "/activity/" + id
						+ "/modify.htm";
		}
		return "redirect:/academicTerm/" + id_academic + "/course/" + id_course
				+ "/group/" + id_group + "/activity/" + id + "/modify.htm";
	}
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{idCourse}/activity/{activityId}/addFileUpload.htm", method = RequestMethod.POST)
	public String addFileCoursePOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("idCourse") Long id_course,
			@PathVariable("activityId") Long id,
			@ModelAttribute("addfileupload") @Valid FileUpload fileupload,
			BindingResult result, Model model) throws ServletException {

		if (!result.hasErrors()) {
			//Se almacena, se guarda el nombre del archivo en activity.info.url
			fileupload.getFilepath();
			
			fileupload.getFilepath().getName();
				
		}
		return "redirect:/academicTerm/" + id_academic + "/course/" + id_course
				 + "/activity/" + id + "/modify.htm";
	}
	


	/**
	 * Method for delete an activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/{activityId}/delete.htm", method = RequestMethod.GET)
	public String deleteActivityGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("activityId") Long id_activity)
			throws ServletException {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic)
				.getSingleElement();

		if (serviceActivity.deleteActivity(null, group, id_activity)
				.getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Method for delete an competence status of activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/{activityId}/competenceStatus/{compStatusId}/delete.htm", method = RequestMethod.GET)
	public String deleteCompetenceStatusActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("activityId") long id_Activity,
			@PathVariable("compStatusId") Long id_learningStatus)
			throws ServletException {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic)
				.getSingleElement();

		if (serviceActivity.deleteLearningActivity(null, group,
				id_learningStatus, id_Activity, id_academic).getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + "/activity/"
					+ id_Activity + "/modify.htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/{activityId}.htm", method = RequestMethod.GET)
	public ModelAndView getActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("activityId") long id_activity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Activity a = serviceActivity.getActivity(id_activity, id_course,
				id_group, id_academic).getSingleElement();

		if (a != null) {
			model.put("activity", a);
			model.put("activityId", id_activity);

			model.put("learningStatus", a.getLearningGoalStatus());

			return new ModelAndView("activity/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/activity/{activityId}/restore.htm")
	// Every Post have to return redirect
	public String restoreActivityGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,

			@PathVariable("activityId") Long id_activity, Locale locale) {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic)
				.getSingleElement();

		ResultClass<Activity> result = serviceActivity.unDeleteActivity(
				null,
				group,
				serviceActivity.getActivity(id_activity, id_course, id_group,
						id_academic).getSingleElement(), locale);

		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		else {
			return "redirect:/error.htm";

		}
	}

	@RequestMapping(value = "/activity/download.htm")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		serviceActivity.dowloadCSV(response);
	}
	
	
	/**
	 * For binding the courses of the activity
	 * 
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "learningGoals",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof LearningGoal) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							LearningGoal learning = serviceLearningGoal
									.getLearningGoalByName(element.toString())
									.getSingleElement();
							logger.info("Loking up {} to {}", element, learning);

							return learning;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}
}