package com.example.tfg.web;

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
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Degree;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.DegreeService;

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
		return serviceDegree.getAll();
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
	public String processAddNewCompetence(
			@ModelAttribute("addacademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult result, Model model) {
		
		if(newAcademicTerm.getDegree() == null)
			return "redirect://academicTerm/add.htm";
		
		if (!result.hasErrors()) {
			

			boolean created = serviceAcademicTerm.addAcademicTerm(newAcademicTerm);
			if (created)
				return "redirect:/academicTerm.htm";
			else
				return "redirect:/academicTerm/add.htm";
		}
		return "redirect:/error.htm";
	}
	
	/*
	@RequestMapping(value = "/academicTerm/{term}/add.htm", method = RequestMethod.GET)
	protected String getAddNewAddDegree(@PathVariable("term") String term,Model model) {

		AcademicTerm newAcademicTerm = new AcademicTerm();
		newAcademicTerm.setTerm(term);
		model.addAttribute("addAcademicTerm", newAcademicTerm);
		return "academicTerm/addChoose";

	}

	@RequestMapping(value = "/academicTerm/{term}/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewDegree(@PathVariable("term") String term,
			@ModelAttribute("addacademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {
			boolean created = serviceAcademicTerm.addAcademicTerm(newAcademicTerm);
			if (created)
				return "redirect:/academicTerm/"+term+"/list.htm";
			else
				return "redirect:/academicTerm/"+term+"/add.htm";
		}
		return "redirect:/error.htm";
	}
*/

	
	/**
	 * Methods for listing 
	 */
/*
	@RequestMapping(value = "/academicTerm/terms.htm")
	public ModelAndView handleRequestAcademicTermList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<String> result = serviceAcademicTerm.getAllTerms();
		myModel.put("academicTerms", result);

		return new ModelAndView("academicTerm/terms", "model", myModel);
	}
*/

	/**
	 * Methods for list academic terms of a term
	 */
	@RequestMapping(value = "/academicTerm.htm")
	protected ModelAndView formViewAcademicTerm()
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<AcademicTerm> p = serviceAcademicTerm.getAcademicsTerm();//getAcademicsTerm(term);

		myModel.put("academicTerms", p);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}
	
	@RequestMapping(value = "/academicTerm/{academicId}.htm")
	protected ModelAndView formViewAcademicTermDegree(@PathVariable("academicId") Long id)
			throws ServletException {
		Map<String, Object> myModel = new HashMap<String, Object>();

		AcademicTerm a = serviceAcademicTerm.getAcademicTerm(id);
		myModel.put("academicTerm", a);

		List<Course> courses = serviceCourse.getCoursesByAcademicTerm(id);
		
		if (courses != null)
			myModel.put("courses", courses);
	
		return new ModelAndView("academicTerm/view", "model", myModel);
	}
	
	/**
	 * Methods for modifying a Term
	 */
	
	@RequestMapping(value = "/academicTerm/{term_id}/modify.htm", method = RequestMethod.GET)
	protected String formModifyActivities(@PathVariable("term_id") String term_id,
			Model model) throws ServletException {

		AcademicTerm aux = new AcademicTerm();
		aux.setTerm(term_id);
		model.addAttribute("generic", aux);
		

		return "academicTerm/modify";
	}
	
	@RequestMapping(value = "/academicTerm/{term_id}/modify.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("term_id") String term_id,
			@ModelAttribute("generic") AcademicTerm newTerm,
			BindingResult result, Model model)
	{	
		
		if (!result.hasErrors()) {
			boolean success = serviceAcademicTerm.modifyTerm(term_id, newTerm.getTerm());
			if (success)
				return "redirect:/academicTerm/terms.htm";
			
		}
		return "redirect:/error.htm";

	}

	
	
	
	/**
	 * Delete a Term.
	 */
	/*
	@RequestMapping(value = "/academicTerm/{term}/deleteAll.htm", method = RequestMethod.GET)
	public String formDeleteTerm(@PathVariable("term") String term)
			throws ServletException {

		if (serviceAcademicTerm.deleteTerm(term)) {
			return "redirect:/academicTerm/terms.htm";
		} else
			return "redirect:/error.htm";
	}*/
	
	/**
	 * Delete an academicTerm 
	 */
	
	@RequestMapping(value = "/academicTerm/{academicId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteAcademicTerm(@PathVariable("academicId") Long id_academic)
			throws ServletException {

		String term = serviceAcademicTerm.getAcademicTerm(id_academic).getTerm();
		if (serviceAcademicTerm.deleteAcademicTerm(id_academic)) {
			return "redirect:/academicTerm/"+term+"/list.htm";
		} else
			return "redirect:/error.htm";
	}
	
}