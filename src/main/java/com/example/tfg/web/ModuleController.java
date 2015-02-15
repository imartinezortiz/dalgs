package com.example.tfg.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.Module;
import com.example.tfg.service.ModuleService;

public class ModuleController {
	@Autowired
	private ModuleService serviceModule;
//	/degree/${degreeId}/module/add.htm
	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.GET)
	public String getAddNewModuleForm(Model model, @PathVariable("degreeId") Long id) {
		Module newModule = new Module();
		// newDegree.setCode(serviceDegree.getNextCode());

		model.addAttribute("addModule", newModule);
		return "module/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewModule(
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("addModule") Module newModule) {
		boolean created = serviceModule.addModule(newModule, id_degree);
		if (created)
			return "redirect:/module/list.htm";
		else
			return "redirect:/module/add.htm";
	}

	/**
	 * Methods for listing modules
	 */

	@RequestMapping(value = "/module/list.htm")
	public ModelAndView handleRequestModuleList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<Module> result = serviceModule.getAll();
		myModel.put("modules", result);

		return new ModelAndView("module/list", "model", myModel);
	}

	/**
	 * Methods for modify modules
	 */
	@RequestMapping(value = "/module/{moduleId}/modify.htm", method = RequestMethod.POST)
	public String formModifyModule(@PathVariable("moduleId") Long id,
			@ModelAttribute("modifyModule") Module modify)

	{
		// modify.setId(id);
		boolean modified = serviceModule.modifyModule(modify, id);
		if (modified)
			return "redirect:/module/list.htm";
		else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/module/{moduleId}/modify.htm", method = RequestMethod.GET)
	protected ModelAndView formModifyModules(@PathVariable("moduleId") Long id)
			throws ServletException {
		ModelAndView model = new ModelAndView();
		Module p = serviceModule.getModule(id);
		model.addObject("modifyModule", p);
		model.setViewName("module/modify");

		return model;
	}

	/**
	 * Methods for delete modules
	 */

	@RequestMapping(value = "/module/delete/{moduleId}.htm", method = RequestMethod.GET)
	public String formDeleteModules(@PathVariable("moduleId") Long id)
			throws ServletException {

		if (serviceModule.deleteModule(id)) {
			return "redirect:/module/list.htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view modules
	 */
	@RequestMapping(value = "/module/{moduleId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewModule(@PathVariable("moduleId") Long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Module p = serviceModule.getModuleAll(id);

		myModel.put("module", p);
		if (p.getSubjects() != null)
			myModel.put("subjects", p.getSubjects());
		if (p.getCompetences() != null)
			myModel.put("competences", p.getCompetences());

		return new ModelAndView("module/view", "model", myModel);
	}

}
