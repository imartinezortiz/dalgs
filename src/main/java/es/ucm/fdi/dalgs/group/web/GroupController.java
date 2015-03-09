package es.ucm.fdi.dalgs.group.web;

import java.util.HashMap;
import java.util.List;
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

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Controller
public class GroupController {

	@Autowired
	private GroupService serviceGroup;
	
	@Autowired
	private UserService serviceUser;
	private static final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	/**
	 * Methods for adding activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.GET)
	protected String addGroupGET(
			@PathVariable("academicId") Long id_Long,
			@PathVariable("courseId") Long id_course, Model model) {
		Group newGroup = new Group();
		// newGroup.setCode(serviceGroup.getNextCode());
		
		model.addAttribute("addGroup", newGroup);

		return "group/add";

	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String addGroupPOST(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addGroup") @Valid Group newgroup,
			BindingResult result, Model model) {

		
		
		ResultClass<Boolean> results = serviceGroup.addGroup(newgroup, id_course);
		if (!results.hasErrors())
			return "redirect:/academicTerm/" + id_academicTerm + "/course/"
			+ id_course + ".htm";		
		else{
			model.addAttribute("addGroup", newgroup);
			if (results.isElementDeleted())
				model.addAttribute("unDelete", results.isElementDeleted()); 
			model.addAttribute("errors", results.getErrorsList());
			return "group/add";

		}

		
	}
	
	
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteGroupAdd(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@ModelAttribute("addGroup") @Valid Group group, Model model) {
		
		ResultClass<Boolean> result = serviceGroup.unDeleteGroup(group);
		
		if (!result.hasErrors())

			return "redirect:/academicTerm/" + id_academicTerm + "/course/"
			+ id_course + ".htm";		
		else{
			model.addAttribute("addGroup", group);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "group/add";
		}
	}

	/**
	 * Methods for modifying activities
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.GET)
	protected String modifyGroupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group, Model model)
			throws ServletException {

		Group p = serviceGroup.getGroup(id_group).getE();
		model.addAttribute("courseId", id_course);
		
		
		model.addAttribute("modifyGroup", p);
		

		return "group/modify";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/modify.htm", method = RequestMethod.POST)
	public String modifyGroupPOST(
			@PathVariable("academicId") Long id_academicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("modifyGroup") @Valid Group group,
			BindingResult result, Model model)

	{

		if (!result.hasErrors()) {
			Group aux = serviceGroup.getGroup(id_group).getE();
			aux.setName(group.getName());
			
			ResultClass<Boolean> results = serviceGroup.modifyGroup(aux);
			if (!result.hasErrors())

				return "redirect:/academicTerm/" + id_academicTerm + "/course/"
				+ id_course + ".htm";
			else{
					model.addAttribute("modifyGroup", aux);
					if (results.isElementDeleted()){
						model.addAttribute("addModule", aux);
						model.addAttribute("unDelete", true); 
						model.addAttribute("errors", results.getErrorsList());
						return "module/add";
					}	
					model.addAttribute("errors", results.getErrorsList());
					return "module/modify";
				}
				
			}
		else{
			
			return "group/add";
		}
		
	}


	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/delete.htm", method = RequestMethod.GET)
	public String deleteGroupGET(
			@PathVariable("academicId") Long id_AcademicTerm,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") Long id_group)
			throws ServletException {

		if (serviceGroup.deleteGroup(id_group).getE()) {
			return "redirect:/academicTerm/" + id_AcademicTerm + "/course/"
					+ id_course + ".htm";
		} else
			return "redirect:/error.htm";
	}



	/**
	 * Methods for view activities
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}.htm", method = RequestMethod.GET)
	protected ModelAndView groupGET(
			@PathVariable("academicId") Long id_academic,
			@PathVariable("courseId") Long id_course,
			@PathVariable("groupId") long id_group)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Group a = serviceGroup.getGroup(id_group).getE();

		model.put("group", a);
		model.put("groupId", id_group);


		return new ModelAndView("group/view", "model", model);
	}
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/professor/add.htm", method = RequestMethod.GET)
	public String addProfessorToGroupGET(@PathVariable("groupId") Long id_group, Model model) {
		
		Group group = serviceGroup.getGroup(id_group).getE();
		List<String> professors = serviceUser.getAllByRole("ROLE_PROFESSOR");
		
		model.addAttribute("group",group);
		model.addAttribute("professors", professors);
		
		return "group/addUsers";

	}
	
	@RequestMapping(value = "/academicTerm/{academicId}/course/{courseId}/group/{groupId}/professor/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String addProfessorToGroupPOST(
			@PathVariable("academicId") Long academicId,
			@PathVariable("courseId") Long courseId,
			@PathVariable("groupId") Long id_group,
			@ModelAttribute("group") @Valid Group group,
			BindingResult result, Model model) {

		if (!result.hasErrors()){
			return "redirect:/academicTerm/" + academicId + "/course/"
			+ courseId + "/group/"+ id_group + "/professor/add.htm";		
		}
		else{
			Group aux = serviceGroup.getGroup(id_group).getE();
			aux.setProfessors(group.getProfessors());
			
			serviceGroup.modifyGroup(aux);
			
			return "redirect:/academicTerm/" + academicId + "/course/"
			+ courseId + "/group/"+ id_group + ".htm";

		}

		
	}
	/**
	 * For binding the professor of the subject.
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "professors",
				new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element instanceof User) {
					logger.info("Converting...{}", element);
					return element;
				}
				if (element instanceof String) {
					User user = serviceUser.findByUsername(element.toString());
					logger.info("Loking up {} to {}", element,
							user);

					return user;
				}
				System.out.println("Don't know what to do with: "
						+ element);
				return null;
			}
		});
	}

}
