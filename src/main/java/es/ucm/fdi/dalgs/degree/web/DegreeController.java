package es.ucm.fdi.dalgs.degree.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Degree;

@Controller
public class DegreeController {

	@Autowired
	private DegreeService serviceDegree;

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.GET)
	public String getAddNewDegreeForm(Model model) {
		Degree newDegree = new Degree();
		// newDegree.setCode(serviceDegree.getNextCode());
		model.addAttribute("addDegree", newDegree);
		return "degree/add";
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewDegree(
			@ModelAttribute("addDegree") Degree newDegree, Model model) {
		ResultClass<Boolean> result = serviceDegree.addDegree(newDegree);
		if (!result.hasErrors())
//		if (created)
			return "redirect:/degree/list.htm";
		else{
			model.addAttribute("addDegree", newDegree);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", result.isElementDeleted()); 
			model.addAttribute("errors", result.getErrorsList());
			return "degree/add";
		}
			
	}
	
	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addDegree") Degree degree, Model model) {
		ResultClass<Boolean> result = serviceDegree.unDeleteDegree(degree);
		
		if (!result.hasErrors())
//		if (created)
			return "redirect:/degree/list.htm";
		else{
			model.addAttribute("addDegree", degree);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "degree/add";
		}
	}

	/**
	 * Methods for listing degrees
	 */

	@RequestMapping(value = "/degree/list.htm")
	public ModelAndView handleRequestDegreeList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<Degree> result = serviceDegree.getAll();
		myModel.put("degrees", result);

		return new ModelAndView("degree/list", "model", myModel);
	}

	/**
	 * Methods for modify degrees
	 */
	
	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.POST)
	public String formModifyDegree(@PathVariable("degreeId") Long id,
			@ModelAttribute("modifyDegree") Degree modify, Model model)

	{
		// modify.setId(id);
		ResultClass<Boolean> result = serviceDegree.modifyDegree(modify, id);
		if (!result.hasErrors())
//			if (created)
				return "redirect:/degree/list.htm";
			else{
				model.addAttribute("modifyDegree", modify);
				if (result.isElementDeleted()){
					model.addAttribute("addDegree", modify);
					model.addAttribute("unDelete", true); 
					model.addAttribute("errors", result.getErrorsList());
					return "degree/add";
				}	
				model.addAttribute("errors", result.getErrorsList());
				return "degree/modify";
			}
	}

	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.GET)
	protected ModelAndView formModifyDegrees(@PathVariable("degreeId") Long id)
			throws ServletException {
		ModelAndView model = new ModelAndView();
		Degree p = serviceDegree.getDegree(id);
		model.addObject("modifyDegree", p);
		model.setViewName("degree/modify");

		return model;
	}

	/**
	 * Methods for delete degrees
	 */

	@RequestMapping(value = "/degree/delete/{degreeId}.htm", method = RequestMethod.GET)
	public String formDeleteDegrees(@PathVariable("degreeId") Long id)
			throws ServletException {

		if (serviceDegree.deleteDegree(id)) {
			return "redirect:/degree/list.htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view degrees
	 */
	@RequestMapping(value = "/degree/{degreeId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewDegree(@PathVariable("degreeId") Long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Degree p = serviceDegree.getDegreeAll(id);

		myModel.put("degree", p);
		if (p.getModules() != null)
			myModel.put("modules", p.getModules());
		if (p.getCompetences() != null)
			myModel.put("competences", p.getCompetences());
		
		
		return new ModelAndView("degree/view", "model", myModel);
	}

}
