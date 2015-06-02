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
package es.ucm.fdi.dalgs.course.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.externalActivity.service.ExternalActivityService;
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
	private ExternalActivityService serviceExternalActivity;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private UserService serviceUser;
	
	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private AcademicTermService serviceAcademic;

	private static final Logger logger = LoggerFactory
			.getLogger(CourseController.class);

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	/**
	 * Methods for adding courses
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.GET)
	public String addCourseGET(@PathVariable("academicId") Long id_academic,
			Model model) {

		if (!model.containsAttribute("addCourse")) {

			AcademicTerm academic = serviceAcademic.getAcademicTerm(
					id_academic, false).getSingleElement();

			model.addAttribute("addCourse", new Course());
			model.addAttribute("academicTerm", academic);
			Collection<Subject> subjects = serviceSubject
					.getSubjectForDegree(academic.getDegree());
			model.addAttribute("professors",
					serviceUser.getAllByRole("ROLE_PROFESSOR"));

			model.addAttribute("subjects", subjects);
		}

		return "course/add";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST, params = "Add")
	// Every Post have to return redirect
	public String addCoursePOST(@PathVariable("academicId") Long id_academic,
			@ModelAttribute("addCourse") @Valid Course newCourse,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		validate(newCourse, resultBinding);
		if (!resultBinding.hasErrors()) {
			ResultClass<Course> resultReturned = serviceCourse.addCourse(
					newCourse, id_academic, locale);
			if (!resultReturned.hasErrors())
				return "redirect:/academicTerm/" + id_academic + ".htm";
			else {
				if (resultReturned.isElementDeleted()) {
					attr.addFlashAttribute("unDelete",
							resultReturned.isElementDeleted());
					attr.addFlashAttribute("academicTerm", resultReturned
							.getSingleElement().getAcademicTerm());
				}

						
				attr.addFlashAttribute(
						"subjects",
						serviceSubject.getSubjectForDegree(
								resultReturned.getSingleElement()
										.getAcademicTerm().getDegree()));

				attr.addFlashAttribute("errors", resultReturned.getErrorsList());
			}

		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addCourse",
					resultBinding);

		}

		attr.addFlashAttribute("addCourse", newCourse);

		if (newCourse.getSubject() != null)
			attr.addFlashAttribute("idSubject", newCourse.getSubject().getId());

		return "redirect:/academicTerm/{academicId}/course/add.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteCourse(@PathVariable("academicId") Long id_academic,
			@ModelAttribute("addCourse") @Valid Course course,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			ResultClass<Course> resultReturned = serviceCourse.unDeleteCourse(
					course, id_academic, locale);

			if (!resultReturned.hasErrors()) {
				attr.addFlashAttribute("academicTerm", resultReturned
						.getSingleElement().getAcademicTerm());
				attr.addFlashAttribute("idSubject", course.getSubject().getId());
				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ resultReturned.getSingleElement().getId()
						+ "/modify.htm";

			} else {
				if (resultReturned.isElementDeleted())
					attr.addFlashAttribute("unDelete", true);
				attr.addFlashAttribute("errors", resultReturned.getErrorsList());
			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addCourse",
					resultBinding);

		}
		AcademicTerm a = serviceAcademic.getAcademicTerm(id_academic, false)
				.getSingleElement();
		attr.addFlashAttribute("academicTerm", a);
		attr.addFlashAttribute("subjects",
				serviceSubject.getSubjectForDegree(a.getDegree())
						.getSingleElement());

		if (course.getSubject() != null)
			attr.addFlashAttribute("idSubject", course.getSubject().getId());
		return "redirect:/academicTerm/{academicId}/course/add.htm";

	}

	/**
	 * Methods for view courses
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}.htm", method = RequestMethod.GET)
	public ModelAndView getCourseGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean showAll)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Course p = serviceCourse.getCourseAll(id_course, id_academic, showAll)
				.getSingleElement();
		model.put("course", p);
		model.put("showAll", showAll);
		if (p != null) {
			if (!p.getActivities().isEmpty())
				model.put("activities", p.getActivities());

			if (!p.getGroups().isEmpty())
				model.put("groups", p.getGroups());
			if(!p.getExternal_activities().isEmpty())
				model.put("externalActivities", p.getExternal_activities());

			return new ModelAndView("course/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);

	}

	/**
	 * Methods for modify courses
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/modify.htm", method = RequestMethod.GET)
	public String modifyCourseGET(@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id, Model model)
			throws ServletException {

		if (!model.containsAttribute("modifyCourse")) {
			Course p = serviceCourse.getCourse(id, id_academic)
					.getSingleElement();

			AcademicTerm academic = serviceAcademic.getAcademicTerm(
					id_academic, false).getSingleElement();

			Collection<Subject> subjects = serviceSubject
					.getSubjectForDegree(academic.getDegree());
			model.addAttribute("idSubject", p.getSubject().getId());

			model.addAttribute("academicTerm", academic);
			model.addAttribute("subjects", subjects);
			model.addAttribute("professors",
					serviceUser.getAllByRole("ROLE_PROFESSOR"));
			if (p.getCoordinator() != null)
				model.addAttribute("idCoordinator", p.getCoordinator().getId());

			model.addAttribute("modifyCourse", p);
		}
		model.addAttribute("professors",
				serviceUser.getAllByRole("ROLE_PROFESSOR"));
		return "course/modify";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/modify.htm", method = RequestMethod.POST)
	public String modifyCoursePOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("modifyCourse") Course modify,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		// AcademicTerm y subject
		validate(modify, resultBinding);
		if (!resultBinding.hasErrors()) {

			ResultClass<Boolean> resultReturned = serviceCourse.modifyCourse(
					modify, id_academic, id_course, locale);
			if (!resultReturned.hasErrors())

				return "redirect:/academicTerm/" + id_academic + "/course/"+id_course +".htm";
			else {

				attr.addFlashAttribute("errors", resultReturned.getErrorsList());
			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.modifyCourse",
					resultBinding);

		}

		return "redirect:/academicTerm/{academicId}/course/{courseId}/modify.htm";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/restore.htm")
	// Every Post have to return redirect
	public String restoreAcademicTerm(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course, Locale locale) {
		ResultClass<Course> result = serviceCourse.unDeleteCourse(serviceCourse
				.getCourse(id_course, id_academic).getSingleElement(),
				id_academic, locale);

		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academic + ".htm";// ?showAll="
																	// +
																	// showAll;
		else {
			return "redirect:/error.htm";

		}

	}

	/**
	 * Delete a course of an academicTerm
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/delete.htm", method = RequestMethod.GET)
	public String deleteCourseGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course) throws ServletException {

		if (serviceCourse.deleteCourse(id_course, id_academic)
				.getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + ".htm";
		} else
			return "redirect:/error.htm";
	}
	
	/**
	 * SHOW MESSAGES
	 * @param model
	 * @param id_academic
	 * @param id_course
	 * @return
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/messages.htm", method = RequestMethod.GET)
	public String courseMessagesGET(Model model,
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@RequestParam(value = "messageId", defaultValue = "-1") Long id_message)
		{

		
		ResultClass<Course> result = serviceCourse.getCourseAll(id_course, id_academic, false);
		
		model.addAttribute("showReplies",id_message);
		model.addAttribute("mails", result.getSingleElement().getMessages());

	
		return "mail/list";
	}
	/**
	 * Add a coordinator //
	 */

	/**
	 * For binding subject and activities of the course.
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "subject",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Subject) {
							logger.info("Converting...{}", element);
							return element;
						}

						if (element instanceof String) {
							Subject subject = serviceSubject.getSubjectByName(
									element.toString()).getSingleElement();
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
							User user = serviceUser.findByUsername(element
									.toString()).getSingleElement();

							logger.info("Loking up {} to {}", element, user);
							return user;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}

	/**
	 * Validate a new Course
	 * 
	 * @param course
	 * @param errors
	 */
	public void validate(Course course, Errors errors) {

		if (course.getCoordinator() == null) {
			errors.rejectValue("degree", "validation.negative",
					"You must select a coordinator");
		}
		if (course.getSubject() == null) {
			errors.rejectValue("degree", "validation.negative",
					"You must select a subject");
		}

	}
}
