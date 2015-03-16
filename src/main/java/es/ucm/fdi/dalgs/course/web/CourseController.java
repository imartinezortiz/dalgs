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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


		if(!model.containsAttribute("addCourse")){

			AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic).getSingleElement();



			model.addAttribute("addCourse", new Course());
			model.addAttribute("academicTerm", academic);
			Collection <Subject> subjects = serviceSubject.getSubjectForDegree(academic.getDegree()).getSingleElement();
			
			model.addAttribute("subjects", subjects);
		}
		

		//		model.addAttribute("add", true);

		return "course/add";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewCourse(
			@PathVariable("academicId") Long id_academic,
			@ModelAttribute("addCourse") @Valid Course newCourse,
			BindingResult resultBinding,
			RedirectAttributes attr) {



		//		if (newCourse.getSubject() == null)
		//			return "redirect:/academicTerm/" + id_academic + "/course/add.htm";

		if (!resultBinding.hasErrors()) {
			ResultClass<Course> resultReturned = serviceCourse.addCourse(newCourse, id_academic);
			if (!resultReturned.hasErrors())
				return "redirect:/academicTerm/" + id_academic + ".htm";	
			else{
				//				AcademicTerm a = serviceAcademic.getAcademicTerm(id_academic).getSingleElement();
				if (resultReturned.isElementDeleted()){
					attr.addFlashAttribute("unDelete", resultReturned.isElementDeleted()); 
					attr.addFlashAttribute("academicTerm", resultReturned.getSingleElement().getAcademicTerm());
				}
				//				else attr.addFlashAttribute("academicTerm", a);
				attr.addFlashAttribute("subjects",serviceSubject.getSubjectForDegree(
						resultReturned.getSingleElement().getAcademicTerm().getDegree()).getSingleElement());

				attr.addFlashAttribute("errors", resultReturned.getErrorsList());
					


			}

		}else{
			attr.addFlashAttribute("org.springframework.validation.BindingResult.addAcademicTerm", resultBinding);
//			AcademicTerm a = serviceAcademic.getAcademicTerm(id_academic).getSingleElement();
//			attr.addFlashAttribute("academicTerm", a);
//			attr.addFlashAttribute("subjects",serviceSubject.getSubjectForDegree(a.getDegree()).getSingleElement());


			//			return "redirect:/academicTerm/{academicId}/course/add.htm";
		}

		
		attr.addFlashAttribute("addCourse", newCourse );

		if (newCourse.getSubject() != null)
			attr.addFlashAttribute("idSubject", newCourse.getSubject().getId());

		return "redirect:/academicTerm/{academicId}/course/add.htm";
	}



	@RequestMapping(value ="/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteCourse(
			@PathVariable("academicId") Long id_academic,
			@ModelAttribute("addCourse") @Valid Course course,
			BindingResult resultBinding,
			RedirectAttributes attr) {



		if(!resultBinding.hasErrors()){
			ResultClass<Course> resultReturned = serviceCourse.unDeleteCourse(course, id_academic);
			
			if (!resultReturned.hasErrors()){
				attr.addFlashAttribute("addCourse", resultReturned.getSingleElement());
				return "redirect:/academicTerm/" + id_academic + "/course/"+ resultReturned.getSingleElement().getId() +"/modify.htm";		

			}else{
				//				attr.addFlashAttribute("addCourse", course);
				if (resultReturned.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", resultReturned.getErrorsList());
				//				attr.addFlashAttribute("addCourse", course.getSubject().getId());


//				return "redirect:/academicTerm/{academicId}/course/add.htm";
			}
		}else{
			attr.addFlashAttribute("org.springframework.validation.BindingResult.addAcademicTerm", resultBinding);
			//			attr.addFlashAttribute("addCourse", course);

		}
		AcademicTerm a = serviceAcademic.getAcademicTerm(id_academic).getSingleElement();
		attr.addFlashAttribute("academicTerm", a);
		attr.addFlashAttribute("subjects",serviceSubject.getSubjectForDegree(a.getDegree()).getSingleElement());

		if (course.getSubject() != null)
			attr.addFlashAttribute("idSubject", course.getSubject().getId());
		return "redirect:/academicTerm/{academicId}/course/add.htm";


	}
	/**
	 * Methods for view courses
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewCourse(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id) throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Course p = serviceCourse.getCourseAll(id).getSingleElement();
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

		if(!model.containsAttribute("modifyCourse")){
			Course p = serviceCourse.getCourse(id).getSingleElement();

			AcademicTerm academic = serviceAcademic.getAcademicTerm(id_academic).getSingleElement();

			Collection <Subject> subjects = serviceSubject.getSubjectForDegree(academic.getDegree()).getSingleElement();
			model.addAttribute("idSubject", p.getSubject().getId());

			//Collection<Activity> activities  =serviceActivity.getAll();
			// serviceSubject.getSubjectsForDegree(academic.getDegree().getId());
			model.addAttribute("academicTerm", academic);
			model.addAttribute("subjects", subjects);
			

			//model.addAttribute("activities", activities);
			model.addAttribute("modifyCourse", p);
		}
		model.addAttribute("professors", serviceUser.getAllByRole("ROLE_PROFESSOR"));
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
			//			Course course_aux = serviceCourse.getCourse(id_course).getSingleElement();
			//			course_aux.setAcademicTerm(modify.getAcademicTerm());
			//			course_aux.setSubject(modify.getSubject());

			ResultClass<Boolean> results = serviceCourse.modifyCourse(modify, id_academic, id_course);
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

		if (serviceCourse.deleteCourse(id_course).getSingleElement()) {
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
							.getSubjectByName(element.toString()).getSingleElement();
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
