package com.example.tfg.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.DegreeService;
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
	private DegreeService serviceDegree;
	
	@Autowired
	private AcademicTermService serviceAcademicTerm;


	//@Autowired
	//private AcademicTermService serviceAcademicTerm;
	
	private static final Logger logger = LoggerFactory
			.getLogger(CourseController.class);
	
	/*@ModelAttribute("degrees")
	public List<Degree> degrees() {
		return serviceDegree.getAll();
	}
	*/
	
	/*s
	@ModelAttribute("academicTerms")
	public List<String> academicTerms() {
		return serviceAcademicTerm.getAllTerms();
	}
	*/
	
	@ModelAttribute("subjects")
	public List<Subject> subjects() {
		return serviceSubject.getAll();
	}
	/**
	 * Methods for adding courses
	 */
	@RequestMapping(value = "/course/add.htm", method = RequestMethod.GET)
	protected String getAddNewCourseForm(Model model) {
		Course newCourse = new Course();
		
	
		model.addAttribute("activities",serviceActivity.getAll());
		model.addAttribute("addcourse", newCourse);
		return "course/add";
	}

	@RequestMapping(value = "/course/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCourse(
			@ModelAttribute("addcourse") Course newCourse,
			BindingResult result, Model model) {

		
		//if( (newCourse.getActivities() == null) || (newCourse.getSubjects() == null) || (newCourse.getAcademicTerm() == null))
		if(newCourse.getAcademicTerm() == null)	
				return "redirect:/course/add.htm";
		
		if (!result.hasErrors()) {
			boolean created = serviceCourse.addCourse(newCourse);
			
		

			if (created)
				return "redirect:/course/list.htm";
			else
				return "redirect:/course/add.htm";
		}
		return "redirect:/error.htm";
	}



	/**
	 * Methods for listing courses
	 */

	@RequestMapping(value = "/academicTerm/{term}/course/list.htm")
	public ModelAndView handleRequestCourseList(@PathVariable("term") String term, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<Course> result = serviceCourse.getCoursesByAcademicTerm(term);
		myModel.put("courses", result);

		return new ModelAndView("course/list", "model", myModel);
	}

	/**
	 * Methods for modify courses
	 */
	

	@RequestMapping(value = "/academicTerm/{term}/course/{courseId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyCourses(@PathVariable("term") String term, @PathVariable("courseId") Long id,
			Model model) throws ServletException {
		Course p = serviceCourse.getCourse(id);
		
		model.addAttribute("idAcademicTerm",term);
		model.addAttribute("idSubject", p.getSubject().getId());
	
		model.addAttribute("activities", serviceActivity.getAll());
		model.addAttribute("modifyCourse", p);
		return "course/modify";

	}


	@RequestMapping(value = "/course/modify/{courseId}.htm", method = RequestMethod.POST)
	public String formModifyCourse(@PathVariable("courseId") Long id,
			@ModelAttribute("modifyCourse") Course modify,
			BindingResult result, Model model)

	{
		if(modify.getAcademicTerm() == null)
			return "redirect:/course/modify/"+id+".htm";
		
		if (!result.hasErrors()) {
			modify.setId(id);
			boolean success = serviceCourse.modifyCourse(modify);
			if (success)
				return "redirect:/course/view/" + id + ".htm";
		}

		return "redirect:/error.htm";

	}

	/**
	 * Methods for delete courses
	 */

	@RequestMapping(value = "/course/delete/{courseId}.htm", method = RequestMethod.GET)
	public String formDeleteCourses(@PathVariable("courseId") Long id)
			throws ServletException {

		if (serviceCourse.deleteCourse(id)) {
			return "redirect:/course/list.htm";
		} else
			return "redirect:/error.htm";

	}

	/**
	 * Methods for view courses
	 */
	@RequestMapping(value = "/course/view/{courseId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewCourse(@PathVariable("courseId") Long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Course p = serviceCourse.getCourse(id);

		p.setSubject(serviceSubject.getSubjectForCourse(id));
		myModel.put("course", p);

		List<Activity> activities = serviceActivity.getActivitiesForCourse(id);

		if (activities != null)
			myModel.put("activities", activities);

		return new ModelAndView("course/view", "model", myModel);
	}
	
	/**
	 * Methods for manage activities of a course
	 */
	@RequestMapping(value = "/course/activity/delete/{courseId}/{activityId}.htm", method = RequestMethod.GET)
	public String formDeleteActivityFromCourse(
			@PathVariable("courseId") Long id_course,
			@PathVariable("activityId") Long id_activity)
			throws ServletException {

		if (serviceActivity.deleteActivity(	id_activity)) {
			return "redirect:/course/view/" + id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

/**
	@RequestMapping(value = "/course/addCompetences/{courseId}.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm(
			@PathVariable("courseId") Long id_course, Model model) {

		Course s = serviceCourse.getCourse(id_course);
		Collection<Competence> competences = serviceCompetence
				.getCompetencesForDegree(s.getDegree().getId());

		model.addAttribute("course", s);
		model.addAttribute("competences", competences);
		//model.addAttribute("activities", s.getActivities());

		return "course/addCompetences";
	}

	@RequestMapping(value = "/course/addCompetences/{courseId}.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("course") Course course, BindingResult result,
			Model model) {

		Course aux = serviceCourse.getCourse(id_course);
		//course.setActivities(aux.getActivities());
		course.setId(id_course);
		course.setDegree(aux.getDegree());

		try {
			serviceCourse.modifyCourse(course);// getCourse(id_course).getCompetences().addAll(competences);
			return "redirect:/course/view/" + id_course + ".htm";
		} catch (Exception e) {
			return "redirect:/competence/add.htm";
		}

	}
*/
	
	
	/**
	 * For binding the competences of the course.
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
							Subject subject = serviceSubject.getSubjectByName(element.toString());
							logger.info("Loking up {} to {}", element,subject);
							return subject;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
		
		binder.registerCustomEditor(Set.class, "activities",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Activity) {
							logger.info("Converting...{}", element);
							return element;
						}
					
						if (element instanceof String) {
							Activity activity = serviceActivity.getActivityByName(element.toString());
							logger.info("Loking up {} to {}", element,activity);
							return activity;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}

}
