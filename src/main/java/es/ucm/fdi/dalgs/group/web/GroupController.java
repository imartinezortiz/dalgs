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
package es.ucm.fdi.dalgs.group.web;

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
import org.springframework.validation.ObjectError;
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
import es.ucm.fdi.dalgs.classes.CharsetString;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.externalActivity.service.ExternalActivityService;
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
	private ExternalActivityService serviceExternalActivity;

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
	
	private Long id_message;
	

	public Long getId_message() {
		return id_message;
	}

	public void setId_message(Long id_message) {
		this.id_message = id_message;
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
					id_course, id_academicTerm, locale);
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
			ResultClass<Group> result = serviceGroup.unDeleteGroup(group,
					id_course, locale);

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
	public String modifyGroupGET(@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Model model)
			throws ServletException {

		model.addAttribute("courseId", id_course);

		model.addAttribute("valueButton", "Modify");

		if (!model.containsAttribute("group")) {
			Group p = serviceGroup.getGroup(id_group, id_course, id_academic, false)
					.getSingleElement();
			model.addAttribute("group", p);

		}
		model.addAttribute("typeform", "form.modify");
		return "group/form";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.POST)
	public String modifyGroupPOST(@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("modifyGroup") @Valid Group group,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		if (!resultBinding.hasErrors()) {

			ResultClass<Boolean> result = serviceGroup.modifyGroup(group,
					id_group, id_course, id_academic, locale);
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
		return "redirect:/academicTerm/" + id_academic + "/course/" + id_course
				+ "/group/" + id_group + "/modify.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/delete.htm", method = RequestMethod.GET)
	public String deleteGroupGET(@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group) throws ServletException {

		if (serviceGroup.deleteGroup(
				serviceGroup.getGroup(id_group, id_course, id_academic, false)
						.getSingleElement()).getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}.htm", method = RequestMethod.GET)
	public ModelAndView getGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") long id_group,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Group a = serviceGroup.getGroup(id_group, id_course, id_academic, show)
				.getSingleElement();
		model.put("showAll", show);

		if (a != null) {
			model.put("group", a);
			model.put("groupId", id_group);
			
//			serviceActivity.getActivitiesForGroup(id_group, show)
			model.put("activitiesGroup", a.getActivities());
			model.put("activitiesCourse", a.getCourse().getActivities());
			model.put("externalActivities", a.getExternal_activities());
			this.setShowAll(show);
			return new ModelAndView("group/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);
	}

	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/{typeOfUser}/view.htm", method = RequestMethod.GET)
	public ModelAndView getUserGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") long id_group,
			@PathVariable("typeOfUser") String typeOfUser,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Group a = serviceGroup.getGroup(id_group, id_course, id_academic, false)
				.getSingleElement();
		model.put("showAll", show);

		if (a != null) {
			model.put("group", a);
			model.put("groupId", id_group);
			model.put("typeOfUser", typeOfUser);
			this.setShowAll(show);
			return new ModelAndView("group/viewUser", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);
	}
	
	

	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/restore.htm")
	// Every Post have to return redirect
	public String restoreAcademicTerm(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Locale locale) {

		ResultClass<Group> result = serviceGroup.unDeleteGroup(serviceGroup
				.getGroup(id_group, id_course, id_academic, false).getSingleElement(),
				id_course, locale);

		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";
		else {
			return "redirect:/error.htm";

		}

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/user/{userId}/delete.htm", method = RequestMethod.GET)
	public String deleteUserGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("userId") Long id_user, Locale locale) {

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic, false).getSingleElement();
		if (group !=null && serviceGroup.deleteUserGroup(group, id_group, id_user, id_course,
				id_academic, locale).getSingleElement()) {
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + "/group/" + id_group + ".htm";
		} else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/clone.htm")
	// Every Post have to return redirect
	public String copyGroup(@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Locale locale) {
		Group aux_group = serviceGroup.getGroup(id_group, id_course,
				id_academic, false).getSingleElement();
		ResultClass<Group> result = serviceGroup.copyGroup(aux_group,
				id_course, locale);

		if (!result.hasErrors())
			return "redirect:/academicTerm/" + id_academic + "/course/"
					+ id_course + ".htm";

		return "redirect:/error.htm";
	}

	// /academicTerm/${academicId}/course/${courseId}/group/${groupId}/student/upload.htm
	/** Method method to insert users massively */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/{typeOfUser}/upload.htm", method = RequestMethod.GET)
	public String uploadUserGet(Model model,
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("typeOfUser") String typeOfUser) {
		CharsetString charsets = new CharsetString();

		model.addAttribute("className", "User");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("User"));
		model.addAttribute("typeOfUser", typeOfUser);
		return "upload";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/{typeOfUser}/upload.htm", method = RequestMethod.POST)
	public String uploadUserPost(
			@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult resultBinding, Model model,
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@PathVariable("typeOfUser") String typeOfUser,
			Locale locale,
			RedirectAttributes attr) {

		logger.info("Upload POST USERS: " + typeOfUser);

		if (resultBinding.hasErrors() || upload.getCharset().isEmpty()) {
			for (ObjectError error : resultBinding.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}

		Group group = serviceGroup.getGroup(id_group, id_course, id_academic, false)
				.getSingleElement();
		
		boolean success = !serviceGroup.removeUsersFromGroup(group, typeOfUser, id_academic,id_course).hasErrors();
		if (success){
			ResultClass<Boolean> result = serviceGroup.uploadUserCVS(group, upload, typeOfUser, locale);
		
			
			if (!result.hasErrors()) {
				return "redirect:/academicTerm/" + id_academic + "/course/"
						+ id_course + "/group/" + id_group + ".htm";
			} else
				attr.addFlashAttribute("errors", result.getErrorsList());
				return "redirect:/academicTerm/" + id_academic + "/course/"
				+ id_course + "/group/" + id_group + "/"+ typeOfUser +"/upload.htm";
		}else{
			
			return "redirect:/academicTerm/" + id_academic + "/course/"
			+ id_course + "/group/" + id_group + "/"+ typeOfUser +"/upload.htm";
		}
	}
	
	/**
	 * SHOW MESSAGES
	 * @param model
	 * @param id_academic
	 * @param id_course
	 * @param id_group
	 * @return 
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/messages.htm", method = RequestMethod.GET)
	public String groupMessagesGET(Model model,
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@RequestParam(value = "messageId", defaultValue = "-1") Long id_message)
		{

		
		ResultClass<Group> result = serviceGroup.getGroup(id_group, id_course, id_academic, false);
		
		model.addAttribute("showReplies",id_message);
		model.addAttribute("mails", result.getSingleElement().getMessages());

	
		return "mail/list";
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
									.toString()).getSingleElement();
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

}
