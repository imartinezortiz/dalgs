package es.ucm.fdi.dalgs.group.web;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Controller
public class GroupController {

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private UserService serviceUser;
	private static final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	/**
	 * Methods for adding activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.GET)
	public String addGroupGET(@PathVariable("academicId") Long id_Long,
			@PathVariable("courseId") Long id_course, Model model) {

		model.addAttribute("valueButton", "Add");
		if (!model.containsAttribute("group"))
			model.addAttribute("group", new Group());
		model.addAttribute("typeform", "form.add");
		return "group/form";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.POST, params = "Add")
	// Every Post have to return redirect
	public String addGroupPOST(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("group") @Valid Group newgroup,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			ResultClass<Group> result = serviceGroup.addGroup(newgroup,
					id_course, id_academicTerm,locale);
			if (!result.hasErrors())
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + ".htm";
			else {

				if (result.isElementDeleted()) {
					attr.addFlashAttribute("unDelete",
							result.isElementDeleted());
					attr.addFlashAttribute("group", result.getSingleElement());
				} else
					attr.addFlashAttribute("group", newgroup);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute("group", newgroup);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.group",
					resultBinding);

		}
		return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/group/add.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteGroupAdd(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addGroup") @Valid Group group,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			ResultClass<Group> result = serviceGroup.unDeleteGroup(group,id_course, locale);

			if (!result.hasErrors()) {
				attr.addFlashAttribute("group", result.getSingleElement());
				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + "/group/"
						+ result.getSingleElement().getId() + "/modify.htm";

			} else {

				if (result.isElementDeleted())
					attr.addAttribute("unDelete", true);
				attr.addAttribute("errors", result.getErrorsList());

			}
		} else {

			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.group",
					resultBinding);

		}
		attr.addAttribute("group", group);
		return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + "/group/add.htm";

	}

	/**
	 * Methods for modifying activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.GET)
	public String modifyGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Model model)
			throws ServletException {

		model.addAttribute("courseId", id_course);

		model.addAttribute("valueButton", "Modify");

		if (!model.containsAttribute("group")) {
			Group p = serviceGroup.getGroup(id_group, id_course, id_academic).getSingleElement();
			model.addAttribute("group", p);

		}
		model.addAttribute("typeform", "form.modify");
		return "group/form";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.POST)
	public String modifyGroupPOST(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("modifyGroup") @Valid Group group,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		if (!resultBinding.hasErrors()) {

			ResultClass<Boolean> result = serviceGroup.modifyGroup(group,
					id_group, id_course,id_academic,locale);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + ".htm";
			else {
				attr.addFlashAttribute("errors", result.getErrorsList());
			}

		} else {

			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.group",
					resultBinding);

		}
		attr.addFlashAttribute("group", group);
		return "redirect:/academicTerm/" + id_academic+ "/course/"
				+ id_course + "/group/" + id_group + "/modify.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/delete.htm", method = RequestMethod.GET)
	public String deleteGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group) throws ServletException {

		if (serviceGroup.deleteGroup(
				serviceGroup.getGroup(id_group, id_course, id_academic).getSingleElement())
				.getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}.htm", method = RequestMethod.GET)
	public ModelAndView groupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") long id_group,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Group a = serviceGroup.getGroup(id_group, id_course, id_academic).getSingleElement();
		model.put("showAll", show);

		if(a !=null){
			model.put("group", a);
			model.put("groupId", id_group);
	
			ResultClass<Activity> activitiesGroup = serviceActivity
					.getActivitiesForGroup(id_group, show);
	
			ResultClass<Activity> activitiesCourse = serviceActivity
					.getActivitiesForCourse(id_course, show);
			model.put("activitiesGroup", activitiesGroup);
			model.put("activitiesCourse", activitiesCourse);
			this.setShowAll(show);
			return new ModelAndView("group/view", "model", model);
		}
		return new ModelAndView("error", "model",model);
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/professor/add.htm", method = RequestMethod.GET)
	public String addProfessorToGroupGET(
			@PathVariable("groupId") Long id_group,@PathVariable("courseId") Long id_course,@PathVariable("academicId") Long id_academic, Model model) {

		Group group = serviceGroup.getGroup(id_group, id_course,id_academic).getSingleElement();
		List<String> professors = serviceUser.getAllByRole("ROLE_PROFESSOR");

		model.addAttribute("group", group);
		model.addAttribute("typeOfUser", "Proffesors");

		model.addAttribute("users", professors);

		return "group/addUsers";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/professor/add.htm", method = RequestMethod.POST, params = "AddProfessors")
	// Every Post have to return redirect
	public String addProfessorToGroupPOST(
			@PathVariable("academicId") Long academicId,
			@PathVariable("courseId") Long courseId,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("group") @Valid Group group,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()) {
			return "redirect:/academicTerm/" + academicId + "/course/"
					+ courseId + "/group/" + id_group + "/professor/add.htm";
		} else {

			ResultClass<Boolean> result = serviceGroup.setProfessors(group,
					id_group, courseId, academicId);
			if (!result.hasErrors())
				return "redirect:/academicTerm/" + academicId + "/course/"
						+ courseId + "/group/" + id_group + ".htm";
			else
				return "redirect:/academicTerm/" + academicId + "/course/"
						+ courseId + "/group/" + id_group
						+ "/professor/add.htm";
		}
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/student/add.htm", method = RequestMethod.GET)
	public String addStudentToGroupGET(@PathVariable("groupId") Long id_group,@PathVariable("courseId") Long id_course, @PathVariable("academicId") Long id_academic,
			Model model) {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic).getSingleElement();
		List<String> students = serviceUser.getAllByRole("ROLE_STUDENT");

		model.addAttribute("group", group);
		model.addAttribute("typeOfUser", "Students");
		model.addAttribute("users", students);

		return "group/addUsers";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/student/add.htm", method = RequestMethod.POST, params = "AddProfessors")
	// Every Post have to return redirect
	public String addStudentsToGroupPOST(
			@PathVariable("academicId") Long academicId,
			@PathVariable("courseId") Long courseId,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("group") @Valid Group group,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()) {
			return "redirect:/academicTerm/" + academicId + "/course/"
					+ courseId + "/group/" + id_group + "/student/add.htm";
		} else {

			ResultClass<Boolean> result = serviceGroup.setStudents(group,
					id_group, courseId, academicId);
			if (!result.hasErrors())
				return "redirect:/academicTerm/" + academicId + "/course/"
						+ courseId + "/group/" + id_group + ".htm";
			else
				return "redirect:/academicTerm/" + academicId + "/course/"
						+ courseId + "/group/" + id_group + "/student/add.htm";
		}
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/restore.htm")
	// Every Post have to return redirect
	public String restoreAcademicTerm(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Locale locale) {

		ResultClass<Group> result = serviceGroup.unDeleteGroup(serviceGroup
				.getGroup(id_group, id_course,id_academic).getSingleElement(), id_course, locale);

		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";
		else {
			return "redirect:/error.htm";

		}

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/user/{userId}/delete.htm", method = RequestMethod.GET)
	public String deleteUserGroupGET(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("userId") Long id_user, Locale locale) {

		if (serviceGroup.deleteUserGroup(id_group, id_user, id_course,
				id_AcademicTerm, locale).getSingleElement()) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		} else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/clone.htm")
	// Every Post have to return redirect
	public String copyGroup(@PathVariable("academicId") Long id_academic,@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Locale locale) {
		Group aux_group = serviceGroup.getGroup(id_group, id_course,id_academic).getSingleElement();
		ResultClass<Group> result = serviceGroup.copyGroup(aux_group, id_course, locale);
		
		if (!result.hasErrors())
			return "redirect:/academicTerm/"+id_academic+"/course/"+ id_course+".htm";
		
		return "redirect:/error.htm";
	}
	
	/**
	 * For binding the professor of the subject.
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "professors",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof User) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							User user = serviceUser.findByFullName(element
									.toString());
							logger.info("Loking up {} to {}", element, user);

							return user;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});

		binder.registerCustomEditor(Set.class, "students",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof User) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							User user = serviceUser.findByFullName(element
									.toString());
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
