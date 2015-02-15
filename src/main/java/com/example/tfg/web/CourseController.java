package com.example.tfg.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
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

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.SubjectService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CourseController {

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private AcademicTermService serviceAcademic;

	private static final Logger logger = LoggerFactory
			.getLogger(CourseController.class);

	/**
	 * Methods for adding courses
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.GET)
	protected String getAddNewCourseForm(
			@PathVariable("academicId") Long id_academic, Model model) {
		Course newCourse = new Course();

		AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic);

		//model.addAttribute("activities", serviceActivity.getAll());
		model.addAttribute("addcourse", newCourse);

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForDegree(academic.getDegree().getId());
		model.addAttribute("academicTerm", academic);
	//__	model.addAttribute("subjects", academic.getDegree());
		return "course/add";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCourse(
			@PathVariable("academicId") Long id_academic,
			@ModelAttribute("addcourse") Course newCourse,
			BindingResult result, Model model) {

		// AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic);
		// academic.setId(id_academic);
		// newCourse.setAcademicTerm(academic);

		if (newCourse.getSubject() == null)
			return "redirect:/academicTerm/" + id_academic + "/course/add.htm";

		if (!result.hasErrors()) {
			boolean created = serviceCourse.addCourse(newCourse, id_academic);

			if (created)
				return "redirect:/academicTerm/" + id_academic + ".htm";
			else
				return "redirect:/academicTerm/" + id_academic
						+ "/course/add.htm";
		}
		return "redirect:/error.htm";
	}

	/**
	 * Methods for view courses
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id) throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Course p = serviceCourse.getCourseAll(id);
		myModel.put("course", p);

		List<Activity> activities = serviceActivity.getActivitiesForCourse(id);

		if (activities != null)
			myModel.put("activities", p.getActivities());

		return new ModelAndView("course/view", "model", myModel);
	}

	/**
	 * Methods for modify courses
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyCourses(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id, Model model)
			throws ServletException {

		Course p = serviceCourse.getCourse(id);

		AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic);
		model.addAttribute("idSubject", p.getSubject().getId());

		//Collection<Activity> activities  =serviceActivity.getAll();
		// serviceSubject.getSubjectsForDegree(academic.getDegree().getId());
		model.addAttribute("academicTerm", academic);
		model.addAttribute("subjects", academic.getDegree().getSubjects());

		//model.addAttribute("activities", activities);
		model.addAttribute("modifyCourse", p);
		return "course/modify";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/modify.htm", method = RequestMethod.POST)
	public String formModifyCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("modifyCourse") Course modify,
			BindingResult result, Model model)

	{
		// if(modify.getAcademicTerm() == null)
		// return
		// "redirect:/academicTerm/"+id_academic+"/course/"+id_course+"/modify.htm";

		if (!result.hasErrors()) {
//			modify.setId(id_course);
			// modify.setAcademicTerm(serviceAcademic.getAcademicTerm(id_academic));
			boolean success = serviceCourse.modifyCourse(modify, id_academic, id_course);
			if (success)
				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + ".htm";
		}

		return "redirect:/error.htm";

	}

	/**
	 * Delete a course of an academicTerm
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteAcademicTermCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course) throws ServletException {

		if (serviceCourse.deleteCourse(id_course)) {
			return "redirect:/academicTerm/" + id_academic + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * For binding subject and activities of the course.
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "subject",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Subject) {
							logger.info("Converting...{}", element);
							return element;
						}

						if (element instanceof String) {
							Subject subject = serviceSubject
									.getSubjectByName(element.toString());
							logger.info("Loking up {} to {}", element, subject);
							return subject;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});

		/*binder.registerCustomEditor(Set.class, "activities",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Activity) {
							logger.info("Converting...{}", element);
							return element;
						}

						if (element instanceof String) {
							Activity activity = serviceActivity
									.getActivityByName(element.toString());
							logger.info("Loking up {} to {}", element, activity);
							return activity;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});*/
	}
}
