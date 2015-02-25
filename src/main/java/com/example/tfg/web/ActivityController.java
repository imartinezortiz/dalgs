package com.example.tfg.web;

import java.security.acl.NotOwnerException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
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

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.LearningGoal;
import com.example.tfg.domain.LearningGoalStatus;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.LearningGoalService;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService serviceActivity;

	// @Autowired
	// private SubjectService serviceSubject;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private LearningGoalService serviceLearningGoal;
//	@Autowired
//	private CompetenceService serviceCompetence;
//
//	@Autowired
//	private AcademicTermService serviceAcademicTerm;

	private static final Logger logger = LoggerFactory
			.getLogger(ActivityController.class);

	/**
	 * Methods for adding activities
	 */
	@Secured({"ROLE_ADMIN", "ROLE_PROFESSOR"})
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(
			@PathVariable("academicId") Long id_Long,
			@PathVariable("courseId") Long id_course, Model model) {
		Activity newActivity = new Activity();
		// newActivity.setCode(serviceActivity.getNextCode());
		newActivity.setCourse(serviceCourse.getCourse(id_course));
		model.addAttribute("addactivity", newActivity);

		return "activity/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addactivity") @Valid Activity newactivity,
			BindingResult result, Model model) throws NotOwnerException {

		if (!result.hasErrors()) {
			ResultClass<Boolean> results = serviceActivity.addActivity(newactivity, id_course);
			if (!results.hasErrors())
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/activity/"+ newactivity.getId()+"/modify.htm";		
			else{
				model.addAttribute("addActivity", newactivity);
				if (results.isElementDeleted())
					model.addAttribute("unDelete", results.isElementDeleted()); 
				model.addAttribute("errors", results.getErrorsList());
				return "activity/add";

			}
		}
		return "redirect:/error.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addActivity") @Valid Activity activity, Model model) {
		
		ResultClass<Boolean> result = serviceActivity.unDeleteActivity(activity);
		
		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academicTerm + "/course/"
			+ id_course + ".htm";		
		else{
			model.addAttribute("addActivity", activity);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "group/add";
		}
	}
	
	
	
	
	
	/**
	 * Methods for modifying activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyActivities(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity, Model model)
			throws ServletException {

		Activity p = serviceActivity.getActivity(id_activity);
		model.addAttribute("courseId", id_course);
		
		Collection<LearningGoal> lg = serviceLearningGoal.getLearningGoalsFromCourse(id_course, p);
		
		model.addAttribute("learningGoalStatus", p.getLearningGoalStatus());
		model.addAttribute("modifyactivity", p);
		model.addAttribute("learningGoals", lg);

		LearningGoalStatus cs = new LearningGoalStatus();
		model.addAttribute("addlearningstatus", cs);

		return "activity/modifyChoose";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/modify.htm", method = RequestMethod.POST)
	public String formModifySystem(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity,
			@ModelAttribute("modifyactivity") @Valid Activity activity,
			BindingResult result, Model model)

	{

		if (!result.hasErrors()) {
			ResultClass<Boolean> results = serviceActivity.modifyActivity(activity, id_activity, id_course);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + ".htm";
			else{
					model.addAttribute("modifyActivity", activity);
					if (results.isElementDeleted()){
						model.addAttribute("addActivity", activity);
						model.addAttribute("unDelete", true); 
						model.addAttribute("errors", results.getErrorsList());
						return "module/add";
					}	
					model.addAttribute("errors", results.getErrorsList());
					return "module/modify";
				}
				
			}

		
		return "redirect:/error.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{idCourse}/activity/{activityId}/addLearningStatus.htm", method = RequestMethod.POST)
	protected String formModifyActivitiesCompetenceStatus(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("idCourse") Long id_course,
			@PathVariable("activityId") Long id,
			@ModelAttribute("addlearningstatus") @Valid LearningGoalStatus learningGoalStatus,
			BindingResult result, Model model) throws ServletException {

		// Activity p = serviceActivity.getActivity(id);
		if (!result.hasErrors())

			if (serviceActivity.addLearningGoals(id, learningGoalStatus))
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + "/activity/" + id + "/modify.htm";
		// if(serviceActivity.existsCompetenceStatus(id,
		// competencestatus.getCompetence().getId()))
		// return "redirect:/academicTerm/"+
		// id_academicTerm+"/course/"+id_course+"/activity/"+id+"/modify.htm";
		//
		// if( competencestatus.getPercentage() <= 0.0 ||
		// competencestatus.getPercentage() > 100.0)
		// return "redirect:/academicTerm/"+
		// id_academicTerm+"/course/"+id_course+"/activity/"+id+"/modify.htm";

		// p.getCompetenceStatus().add(competencestatus);
		// modify

		return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/activity/" + id + "/modify.htm";
	}

	/**
	 * Method for delete an activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteActivity(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity)
			throws ServletException {

		if (serviceActivity.deleteActivity(id_activity)) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Method for delete an competence status of activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/competenceStatus/{compStatusId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceStatusActivity(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") long id_Activity,
			@PathVariable("compStatusId") Long id_learningStatus)
			throws ServletException {

		if (serviceActivity.deleteLearningActivity(id_learningStatus,
				id_Activity)) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + "/activity/" + id_Activity + "/modify.htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewActivity(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") long id_activity)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Activity a = serviceActivity.getActivity(id_activity);

		model.put("activity", a);
		model.put("activityId", id_activity);

		model.put("learningStatus", a.getLearningGoalStatus());

		return new ModelAndView("activity/view", "model", model);
	}

	/**
	 * For binding the courses of the activity
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "learningGoals",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof LearningGoal) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							LearningGoal learning = serviceLearningGoal
									.getLearningGoalByName(element.toString());
							logger.info("Loking up {} to {}", element,
									learning);

							return learning;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}
}