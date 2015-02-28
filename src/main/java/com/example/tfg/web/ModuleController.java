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

import com.example.tfg.classes.ResultClass;
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

	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewModule(
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("addModule") Module newModule,
			Model model) {
		
		ResultClass<Boolean> result = serviceModule.addModule(newModule, id_degree);
		if (!result.hasErrors())
			//		if (created)
			return "redirect:/degree/" +id_degree+ ".htm";		
		else{
			model.addAttribute("addModule", newModule);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", result.isElementDeleted()); 
			model.addAttribute("errors", result.getErrorsList());
			return "module/add";

		}
		
	}


	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addModule") Module module, Model model,
			@PathVariable("degreeId") Long id_degree) {
		
		ResultClass<Boolean> result = serviceModule.unDeleteModule(module);
		
		if (!result.hasErrors())

			return "redirect:/degree/" + id_degree + ".htm";
		else{
			model.addAttribute("addModule", module);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "module/add";
		}
	}
	

	/**
	 * Methods for modify modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.POST)
	public String formModifyModule(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@ModelAttribute("modifyModule") Module modify,Model model)

	{
		
		ResultClass<Boolean> result = serviceModule.modifyModule(modify, id_module);
		if (!result.hasErrors())

			return "redirect:/degree/" + id_degree + ".htm";
		else{
				model.addAttribute("modifyModule", modify);
				if (result.isElementDeleted()){
					model.addAttribute("addModule", modify);
					model.addAttribute("unDelete", true); 
					model.addAttribute("errors", result.getErrorsList());
					return "module/add";
				}	
				model.addAttribute("errors", result.getErrorsList());
				return "module/modify";
			}
		
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
