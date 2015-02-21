package com.example.tfg.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.Module;
import com.example.tfg.service.ModuleService;

@Controller
public class ModuleController {
	@Autowired
	private ModuleService serviceModule;

	
	
	

	
	/**
	 * methods for adding modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.GET)
	public String getAddNewModuleForm(Model model, @PathVariable("degreeId") Long id_degree) {
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
			return "redirect:/degree/" +id_degree+ ".htm";
		else
			return "redirect:/error.htm";
	}



	/**
	 * Methods for modify modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.POST)
	public String formModifyModule(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@ModelAttribute("modifyModule") Module modify)

	{
		// modify.setId(id);
		boolean modified = serviceModule.modifyModule(modify, id_module);
		if (modified)
			return "redirect:/degree/" + id_degree + ".htm";
		else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.GET)
	protected ModelAndView formModifyModules(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module)
			throws ServletException {
		
		ModelAndView model = new ModelAndView();
		Module p = serviceModule.getModule(id_module);
		model.addObject("modifyModule", p);
		model.setViewName("module/modify");

		return model;
	}

	/**
	 * Methods for delete modules
	 */

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteModules(@PathVariable("moduleId") Long id_module,
						@PathVariable("degreeId") Long id_degree)
						throws ServletException {

		if (serviceModule.deleteModule(id_module)) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewModule(@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Module p = serviceModule.getModuleAll(id_module, id_degree);

		myModel.put("module", p);
		if (p.getTopics() != null)
			myModel.put("topics", p.getTopics());
//		myModel.put("moduleId", p.getId());

		return new ModelAndView("module/view", "model", myModel);
	}

}
