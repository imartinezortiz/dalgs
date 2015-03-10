package es.ucm.fdi.dalgs.degree.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Degree;

@Controller
public class DegreeController {

	@Autowired
	private DegreeService serviceDegree;

	private Boolean showAll;
	
	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}
	
	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.GET)
	public String addDegreeGET(Model model) {
		Degree newDegree = new Degree();
		// newDegree.setCode(serviceDegree.getNextCode());
		model.addAttribute("Degree", newDegree);
		model.addAttribute("valueButton", "Add" );
		return "degree/add";
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String addDegreePOST(
			@ModelAttribute("addDegree") Degree newDegree, Model model) {
		ResultClass<Boolean> result = serviceDegree.addDegree(newDegree);
		if (!result.hasErrors())
//		if (created)
			return "redirect:/degree/page/0.htm?showAll="+showAll;
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
			return "redirect:/degree/page/0.htm?showAll="+showAll;
		else{
			model.addAttribute("addDegree", degree);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "degree/add";
		}
	}

	
	@RequestMapping(value = "/degree/{id_degree}/restore.htm")
	// Every Post have to return redirect
	public String restoreDegree(@PathVariable("id_degree") Long id_degree) {
		ResultClass<Boolean> result = serviceDegree.unDeleteDegree(serviceDegree.getDegree(id_degree).getE());
		if (!result.hasErrors())
//			if (created)
				return "redirect:/degree/page/0.htm?showAll="+showAll;
			else{
				return "redirect:/error.htm";

			}
		
	}
	
	/**
	 * Methods for listing degrees
	 */

	@RequestMapping(value = "/degree/page/{pageIndex}.htm")
	public ModelAndView degreesGET(
			@PathVariable("pageIndex") Integer pageIndex, @RequestParam(value = "showAll", defaultValue="false") Boolean showAll) throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

//		List<Degree> result = serviceDegree.getAll().getE();
//		myModel.put("degrees", result);
		List<Degree> result = serviceDegree.getDegrees(pageIndex, showAll).getE();
		Integer numberOfPages = serviceDegree.numberOfPages(showAll);
		myModel.put("showAll", showAll);

		myModel.put("numberOfPages",numberOfPages );
		myModel.put("currentPage", pageIndex);
		myModel.put("degrees", result);		
		
		setShowAll(showAll);
		
		return new ModelAndView("degree/list", "model", myModel);
	}

	/**
	 * Methods for modify degrees
	 */
	
	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.POST)
	public String modifyDegreePOST(@PathVariable("degreeId") Long id,
			@ModelAttribute("modifyDegree") Degree modify, Model model)

	{
		modify.setId(id);
		ResultClass<Boolean> result = serviceDegree.modifyDegree(modify, id);
		if (!result.hasErrors())
//			if (created)
				return "redirect:/degree/page/0.htm?showAll="+showAll;
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
	protected String modifyDegreeGET(@PathVariable("degreeId") Long id, Model model)
			throws ServletException {
//		ModelAndView model = new ModelAndView();
		Degree p = serviceDegree.getDegree(id).getE();
		model.addAttribute("Degree", p);
//		model.setViewName("degree/modify");
		
		model.addAttribute("valueButton", "Modify");
//		return model;
		return "degree/add";
	}

	/**
	 * Methods for delete degrees
	 */

	@RequestMapping(value = "/degree/{degreeId}/delete.htm", method = RequestMethod.GET)
	public String deleteDegreeGET(@PathVariable("degreeId") Long id)
			throws ServletException {

		if (serviceDegree.deleteDegree(id).getE()) {
			return "redirect:/degree/page/0.htm?showAll="+showAll;
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view degrees
	 */
	@RequestMapping(value = "/degree/{degreeId}.htm", method = RequestMethod.GET)
	protected ModelAndView degreeGET(@PathVariable("degreeId") Long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Degree p = serviceDegree.getDegree(id).getE();

		myModel.put("degree", p);
		if (p.getModules() != null)
			myModel.put("modules", p.getModules());
		if (p.getCompetences() != null)
			myModel.put("competences", p.getCompetences());
		
		
		return new ModelAndView("degree/view", "model", myModel);
	}

}
