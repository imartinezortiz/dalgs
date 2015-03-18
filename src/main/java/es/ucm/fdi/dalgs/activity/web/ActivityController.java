package es.ucm.fdi.dalgs.activity.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.domain.LearningGoalStatus;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private CourseService serviceCourse;

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
	 * Methods for adding activities
	 */
	@Secured({"ROLE_ADMIN", "ROLE_PROFESSOR"})
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(
			@PathVariable("academicId") Long id_Long,
			@PathVariable("courseId") Long id_course, Model model) {

		if(!model.containsAttribute("addactivity")){

			Activity newActivity = new Activity();
			// newActivity.setCode(serviceActivity.getNextCode());
			//			newActivity.setCourse(serviceCourse.getCourse(id_course).getSingleElement());
			model.addAttribute("addactivity", newActivity);
		}
		return "activity/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addactivity") @Valid Activity newactivity,
			BindingResult resultBinding,
			RedirectAttributes attr) throws NotOwnerException {

		if (!resultBinding.hasErrors()) {
			ResultClass<Activity> result = serviceActivity.addActivity(newactivity, id_course);
			if (!result.hasErrors())
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/activity/"+ newactivity.getId()+"/modify.htm";		
			else{

				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("addactivity", result.getSingleElement());
				}else attr.addFlashAttribute("addactivity", newactivity);
				attr.addFlashAttribute("errors", result.getErrorsList());


			}
		}
		else{
			attr.addFlashAttribute("org.springframework.validation.BindingResult.addAcademicTerm", resultBinding);
			attr.addFlashAttribute("addactivity", newactivity);


		}


		return "redirect:/academicTerm/{academicId}/course/{courseId}/activity/add.htm";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addActivity") @Valid Activity activity,
			BindingResult resultBinding,
			RedirectAttributes attr) {


		if(!resultBinding.hasErrors()){

			ResultClass<Activity> result = serviceActivity.unDeleteActivity(activity);

			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/activity/"+ result.getSingleElement().getId()+"/modify.htm";			
			else{
				//			attr.addFlashAttribute("addActivity", activity);
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());

			}

		} else{
			attr.addFlashAttribute("org.springframework.validation.BindingResult.addAcademicTerm", resultBinding);


		}
		attr.addFlashAttribute("addActivity", activity);
		return "redirect:/academicTerm/{academicId}/course/{courseId}/activity/add.htm";

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


		Activity p = serviceActivity.getActivity(id_activity).getSingleElement();
		model.addAttribute("courseId", id_course);

		if(!model.containsAttribute("modifyactivity")){
				model.addAttribute("modifyactivity", p);

		}


			Collection<LearningGoal> lg = serviceLearningGoal.getLearningGoalsFromCourse(id_course, p).getSingleElement();

			model.addAttribute("learningGoalStatus", p.getLearningGoalStatus());
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
			BindingResult resultBinding, RedirectAttributes attr)

	{

		if (!resultBinding.hasErrors()) {
			ResultClass<Boolean> result = serviceActivity.modifyActivity(activity, id_activity, id_course);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + ".htm";
			else{
	
				attr.addFlashAttribute("errors", result.getErrorsList());

			}

		}	else {

			attr.addFlashAttribute("org.springframework.validation.BindingResult.modifyactivity", resultBinding);

		}
		attr.addFlashAttribute("modifyactivity", activity);

		return "redirect:/academicTerm/"+ id_academicTerm +"/course/" +id_course+"/activity/"+id_activity + "/modify.htm";
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

			if (serviceActivity.addLearningGoals(id, learningGoalStatus).getSingleElement())
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/activity/" + id + "/modify.htm";



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

		if (serviceActivity.deleteActivity(id_activity).getSingleElement()) {
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
				id_Activity).getSingleElement()) {
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

		Activity a = serviceActivity.getActivity(id_activity).getSingleElement();

		model.put("activity", a);
		model.put("activityId", id_activity);

		model.put("learningStatus", a.getLearningGoalStatus());

		return new ModelAndView("activity/view", "model", model);
	}


	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/activity/{activityId}/restore.htm")
	// Every Post have to return redirect
	public String restoreAcademicTerm(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity) {
		ResultClass<Activity> result = serviceActivity.unDeleteActivity(serviceActivity.getActivity(id_activity).getSingleElement());

		if (!result.hasErrors())

			return "redirect:/academicTerm/"+ id_academic +"/course/" +  id_course  +".htm";
		else {
			return "redirect:/error.htm";

		}

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
							.getLearningGoalByName(element.toString()).getSingleElement();
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