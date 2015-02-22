package com.example.tfg.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.Group;
import com.example.tfg.service.GroupService;

@Controller
public class GroupController {

	@Autowired
	private GroupService serviceGroup;
	
	/**
	 * Methods for adding activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.GET)
	protected String getAddNewGroupForm(
			@PathVariable("academicId") Long id_Long,
			@PathVariable("courseId") Long id_course, Model model) {
		Group newGroup = new Group();
		// newGroup.setCode(serviceGroup.getNextCode());
		
		model.addAttribute("addGroup", newGroup);

		return "group/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addGroup") @Valid Group newgroup,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {
			// newgroup.setCourse(serviceCourse.getCourse(id_course));

			boolean created = serviceGroup.addGroup(newgroup,
					id_course);
			if (created) {

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + ".htm";
			} else
				return "redirect:/group/add.htm";
		}
		return "redirect:/error.htm";
	}

	/**
	 * Methods for modifying activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyActivities(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Model model)
			throws ServletException {

		Group p = serviceGroup.getGroup(id_group);
		model.addAttribute("courseId", id_course);
		
		
		model.addAttribute("modifyGroup", p);
		

		return "group/modify";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.POST)
	public String formModifySystem(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("modifyGroup") @Valid Group group,
			BindingResult result, Model model)

	{

		if (!result.hasErrors()) {
			// group.setId(id_group);
			// group.setCourse(serviceCourse.getCourse(id_course));
			// group.setCompetenceStatus(serviceGroup.getGroup(id_group).getCompetenceStatus());
			boolean success = serviceGroup.modifyGroup(group,
					id_group);
			if (success) {

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
						+ id_course + ".htm";
			}
		}
		return "redirect:/error.htm";
	}



	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteGroup(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group)
			throws ServletException {

		if (serviceGroup.deleteGroup(id_group)) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}



	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewGroup(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") long id_group)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Group a = serviceGroup.getGroup(id_group);

		model.put("group", a);
		model.put("groupId", id_group);


		return new ModelAndView("group/view", "model", model);
	}
}
