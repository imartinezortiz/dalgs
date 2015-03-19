package es.ucm.fdi.dalgs.degree.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		//		Degree newDegree = new Degree();
		// newDegree.setCode(serviceDegree.getNextCode());
		if (model.containsAttribute("degree"))
			model.addAttribute("degree", new Degree());
		model.addAttribute("valueButton", "Add" );
		return "degree/form";
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String addDegreePOST(
			@ModelAttribute("addDegree") Degree newDegree, 
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){
			ResultClass<Degree> result = serviceDegree.addDegree(newDegree);
			if (!result.hasErrors())
				//		if (created)
				return "redirect:/degree/page/0.htm?showAll="+showAll;
			else{

				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("degree", result.getSingleElement());
				}
				else attr.addAttribute("degree", newDegree);
				attr.addAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.degree",
					resultBinding);
			attr.addFlashAttribute("degree", newDegree);

		}
		return "redirect:/degree/add.htm";
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addDegree") Degree degree,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){
			ResultClass<Degree> result = serviceDegree.unDeleteDegree(degree);

			if (!result.hasErrors())
				//		if (created)
				return "redirect:/degree/" + result.getSingleElement().getId() + "/modify.htm";
			else{
				attr.addFlashAttribute("degree", degree);
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.degree",
					resultBinding);
			attr.addFlashAttribute("degree", degree);

		}
		return "redirect:/degree/add.htm";
	}


	@RequestMapping(value = "/degree/{id_degree}/restore.htm")
	// Every Post have to return redirect
	public String restoreDegree(@PathVariable("id_degree") Long id_degree) {
		ResultClass<Degree> result = serviceDegree.unDeleteDegree(serviceDegree.getDegree(id_degree).getSingleElement());
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
		ResultClass<Degree> result = serviceDegree.getDegrees(pageIndex, showAll);
		ResultClass<Integer> numberOfPages = serviceDegree.numberOfPages(showAll);
		myModel.put("showAll", showAll);

		myModel.put("numberOfPages",numberOfPages.getSingleElement() );
		myModel.put("currentPage", pageIndex);
		myModel.put("degrees", result);		

		setShowAll(showAll);

		return new ModelAndView("degree/list", "model", myModel);
	}

	/**
	 * Methods for modify degrees
	 */

	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.POST)
	public String modifyDegreePOST(@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("degree") Degree modify,
			BindingResult resultBinding, RedirectAttributes attr)

	{

		if (!resultBinding.hasErrors()){
			ResultClass<Boolean> result = serviceDegree.modifyDegree(modify, id_degree);
			if (!result.hasErrors())
				//			if (created)
				return "redirect:/degree/page/0.htm?showAll="+showAll;
			else{

				//			if (result.isElementDeleted()){
				
				//				attr.addFlashAttribute("unDelete", result.getSingleElement()); 
				//			}else 
				attr.addFlashAttribute("errors", result.getErrorsList());

				//			}	
				attr.addFlashAttribute("errors", result.getErrorsList());
			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.degree",
					resultBinding);
			
			
		}
		attr.addFlashAttribute("degree", modify);
		return "redirect:/degree/"+id_degree+"/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.GET)
	protected String modifyDegreeGET(@PathVariable("degreeId") Long id, Model model)
			throws ServletException {
		//		ModelAndView model = new ModelAndView();
	
		if(model.containsAttribute("degree")){
			Degree p = serviceDegree.getDegree(id).getSingleElement();
			model.addAttribute("Degree", p);
		//		model.setViewName("degree/modify");
		}
		model.addAttribute("valueButton", "Modify");
		//		return model;
		return "degree/form";
	}

	/**
	 * Methods for delete degrees
	 */

	@RequestMapping(value = "/degree/{degreeId}/delete.htm", method = RequestMethod.GET)
	public String deleteDegreeGET(@PathVariable("degreeId") Long id_degree)
			throws ServletException {

		if (serviceDegree.deleteDegree(serviceDegree.getDegree(id_degree).getSingleElement()).getSingleElement()) {
			return "redirect:/degree/page/0.htm?showAll="+showAll;
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view degrees
	 */
	@RequestMapping(value = "/degree/{degreeId}.htm", method = RequestMethod.GET)
	protected ModelAndView degreeGET(@PathVariable("degreeId") Long id_degree,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Degree p = serviceDegree.getDegreeAll(id_degree, show).getSingleElement();
		myModel.put("showAll", show);
		myModel.put("degree", p);
		if (p.getModules() != null)
			myModel.put("modules", p.getModules());
		if (p.getCompetences() != null)
			myModel.put("competences", p.getCompetences());


		return new ModelAndView("degree/view", "model", myModel);
	}

}
