package es.ucm.fdi.dalgs.academicTerm.web;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Degree;

@Controller
public class AcademicTermController {

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private CourseService serviceCourse;

	@ModelAttribute("degrees")
	public List<Degree> degree() {
		return serviceDegree.getAll().getE();
	}
	
	private Boolean showAll;
	
	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}


	/**
	 * Methods for adding academicTerms
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.GET)
	protected String getAddNewAcademicTermForm(Model model) {

		AcademicTerm newAcademicTerm = new AcademicTerm();
		model.addAttribute("addAcademicTerm", newAcademicTerm);
		return "academicTerm/add";

	}

	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewAcademicTerm(
			@ModelAttribute("addacademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult result, Model model) {

		
		if (newAcademicTerm.getDegree() == null)
			return "redirect://academicTerm/add.htm";

		if (!result.hasErrors()) {

			ResultClass<Boolean> resultReturned = serviceAcademicTerm.addAcademicTerm(newAcademicTerm);
			if (!result.hasErrors())
				model.addAttribute("addacademicTerm", newAcademicTerm);
			if (resultReturned.isElementDeleted())
				model.addAttribute("unDelete", resultReturned.isElementDeleted()); 
			model.addAttribute("errors", resultReturned.getErrorsList());
			return "redirect:/academicTerm/page/0.htm?showAll";
			//			boolean created = serviceAcademicTerm
			//					.addAcademicTerm(newAcademicTerm).getE();
			//			if (created)
			//				return "redirect:/academicTerm/page/0.htm?showAll";
			//			else
			//				return "redirect:/academicTerm/add.htm";
		}
		else{
			// Write the binding result errors on the view
			
			// ----
			
			return "redirect:/academicTerm/add.htm";
		}
		//return "redirect:/error.htm";
	}

	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addacademicTerm") AcademicTerm academicTerm, Model model) {
		ResultClass<Boolean> result = serviceAcademicTerm.undeleteAcademic(academicTerm);

		if (!result.hasErrors())
			//		if (created)
			return "redirect:/aca/list.htm";
		else{
			model.addAttribute("addacademicTerm", academicTerm);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "degree/add";
		}
	}

	/**
	 * Methods for list academic terms of a term
	 */
	@RequestMapping(value = "/academicTerm/page/{pageIndex}.htm")
	protected ModelAndView formViewAcademicTerm(@PathVariable("pageIndex") Integer pageIndex, 
			@RequestParam(value = "showAll", defaultValue="false") Boolean showAll)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		/*if (showAll== false) showAll=true;
		else showAll= false;*/

		List<AcademicTerm> p = serviceAcademicTerm.getAcademicsTerm(pageIndex, showAll).getE();
		myModel.put("showAll", showAll);
		myModel.put("academicTerms", p);
		Integer numberOfPages = serviceAcademicTerm.numberOfPages(showAll).getE();
		myModel.put("numberOfPages",numberOfPages );
		myModel.put("currentPage", pageIndex);
		
		setShowAll(showAll);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}

	@RequestMapping(value = "/academicTerm/{academicId}.htm", method = RequestMethod.GET)
	protected ModelAndView academicTermGET(
			@PathVariable("academicId") Long id_academic)
					throws ServletException {
		Map<String, Object> myModel = new HashMap<String, Object>();

		AcademicTerm a = serviceAcademicTerm.getAcademicTerm(id_academic).getE();
		myModel.put("academicTerm", a);

		// List<Course> courses =
		// serviceCourse.getCoursesByAcademicTerm(id_degree);

		// if (courses != null)
		myModel.put("courses", a.getCourses());

		return new ModelAndView("academicTerm/view", "model", myModel);
	}

	/**
	 * Methods for modifying a Term
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.GET)
	protected String modifyAcademictermGET(
			@PathVariable("academicId") Long id_academic, Model model)
					throws ServletException {

		AcademicTerm aT = serviceAcademicTerm.getAcademicTerm(id_academic).getE();
		model.addAttribute("academicTerm", aT);
		//		model.addAttribute("degree", aT.getDegree());

		return "academicTerm/modify";
	}

	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("academicId") Long academicId,
			@ModelAttribute("academicTerm") AcademicTerm newTerm,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {
			//			AcademicTerm at = serviceAcademicTerm.getAcademicTerm(academicId).getE();
			//			at.setTerm(newTerm.getTerm());
			ResultClass<Boolean> resultReturned = serviceAcademicTerm.modifyAcademicTerm(newTerm, academicId);
			if (!resultReturned.hasErrors())
				return "redirect:/academicTerm/page/0.htm?showAll";
			else{
				model.addAttribute("academicTerm", newTerm);
				if (resultReturned.isElementDeleted()){
					model.addAttribute("addAcademicTerm", newTerm);
					model.addAttribute("unDelete", true); 
					model.addAttribute("errors", resultReturned.getErrorsList());
					return "academicTerm/add";
				}	
				model.addAttribute("errors", resultReturned.getErrorsList());
				return "academicTerm/modify";
			}

		}
		return "redirect:/error.htm";

	}

	/**
	 * Delete an academicTerm
	 */

	@RequestMapping(value = "/academicTerm/{academicId}/delete.htm", method = RequestMethod.GET)
	public String deleteAcademicTermGET(
			@PathVariable("academicId") Long id_academic)
					throws ServletException {

		if (serviceAcademicTerm.deleteAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic).getE()).getE()) {
			return "redirect:/academicTerm/page/0.htm?showAll="+showAll;
		} else
			return "redirect:/error.htm";
	}

}