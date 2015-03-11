package es.ucm.fdi.dalgs.course.web;

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

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.subject.service.SubjectService;
import es.ucm.fdi.dalgs.user.service.UserService;

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
	private UserService serviceUser;

//	@Autowired
//	private ActivityService serviceActivity;

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

		AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic).getE();
		Collection <Subject> subjects = serviceSubject.getSubjectForDegree(academic.getDegree()).getE();
		//model.addAttribute("activities", serviceActivity.getAll());
		model.addAttribute("addcourse", newCourse);

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForDegree(academic.getDegree().getId());
		model.addAttribute("academicTerm", academic);
		model.addAttribute("subjects", subjects);
		model.addAttribute("add", true);
		return "course/add";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewCourse(
			@PathVariable("academicId") Long id_academic,
			@ModelAttribute("addcourse") Course newCourse,
			BindingResult result, Model model) {

		

		if (newCourse.getSubject() == null)
			return "redirect:/academicTerm/" + id_academic + "/course/add.htm";

		if (!result.hasErrors()) {
			ResultClass<Boolean> results = serviceCourse.addCourse(newCourse, id_academic);
			if (!results.hasErrors())
				return "redirect:/academicTerm/" + id_academic + ".htm";	
			else{
				model.addAttribute("addCourse", newCourse);
				if (results.isElementDeleted())
					model.addAttribute("unDelete", results.isElementDeleted()); 
				model.addAttribute("errors", results.getErrorsList());
				return "course/add";

			}
			
		}
		return "redirect:/error.htm";
	}

	
	
	@RequestMapping(value ="/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addCourse") @Valid Course course, Model model) {
		
		ResultClass<Boolean> result = serviceCourse.unDeleteCourse(course);
		
		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academicTerm + ".htm";		
		else{
			model.addAttribute("addCourse", course);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "course/add";
		}
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

//		List<Activity> activities = serviceActivity.getActivitiesForCourse(id);

		if (p.getActivities() != null)
			myModel.put("activities", p.getActivities());
		
		if (p.getGroups() != null)
			myModel.put("groups", p.getGroups());
		

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

		Course p = serviceCourse.getCourse(id).getE();

		AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic).getE();
		
		Collection <Subject> subjects = serviceSubject.getSubjectForDegree(academic.getDegree()).getE();
		model.addAttribute("idSubject", p.getSubject().getId());

		//Collection<Activity> activities  =serviceActivity.getAll();
		// serviceSubject.getSubjectsForDegree(academic.getDegree().getId());
		model.addAttribute("academicTerm", academic);
		model.addAttribute("subjects", subjects);
		model.addAttribute("professors", serviceUser.getAllByRole("ROLE_PROFESSOR"));

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

		//AcademicTerm y subject
		if (!result.hasErrors()) {
			Course course_aux = serviceCourse.getCourse(id_course).getE();
			course_aux.setAcademicTerm(modify.getAcademicTerm());
			course_aux.setSubject(modify.getSubject());
			
			ResultClass<Boolean> results = serviceCourse.modifyCourse(course_aux);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academic + ".htm";	
			else{
					model.addAttribute("modifyCourse", modify);
					if (results.isElementDeleted()){
						model.addAttribute("addCourse", modify);
						model.addAttribute("unDelete", true); 
						model.addAttribute("errors", results.getErrorsList());
						return "course/add";
					}	
					model.addAttribute("errors", results.getErrorsList());
					return "course/modify";
				}
				
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

		if (serviceCourse.deleteCourse(id_course).getE()) {
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
									.getSubjectByName(element.toString()).getE();
							logger.info("Loking up {} to {}", element, subject);
							return subject;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});

		binder.registerCustomEditor(Set.class, "professors",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof User) {
							logger.info("Converting...{}", element);
							return element;
						}

						if (element instanceof String) {
							User user = serviceUser.findByUsername(element.toString());
								
							logger.info("Loking up {} to {}", element, user);
							return user;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}
}
