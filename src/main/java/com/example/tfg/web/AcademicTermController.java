package com.example.tfg.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	

	private static final Logger logger = LoggerFactory
			.getLogger(AcademicTermController.class);

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
		newAcademicTerm.setDegree(null);
		model.addAttribute("addAcademicTerm", newAcademicTerm);
		return "academicTerm/add";

	}

	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@ModelAttribute("addacademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult result, Model model) {
		
	///	if(newAcademicTerm.getDegree() == null)
	//		return "redirect://academicTerm/add.htm";
		
		if (!result.hasErrors()) {
			newAcademicTerm.setDegree(null);

			boolean created = serviceAcademicTerm.addAcademicTerm(newAcademicTerm);
			if (created)
				return "redirect:/academicTerm/list.htm";
			else
				return "redirect:/academicTerm/add.htm";
		}
		return "redirect:/error.htm";
	}
	
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
		
	///	if(newAcademicTerm.getDegree() == null)
	//		return "redirect://academicTerm/add.htm";
		
		if (!result.hasErrors()) {
			boolean created = serviceAcademicTerm.addAcademicTerm(newAcademicTerm);
			if (created)
				return "redirect:/academicTerm/"+term+"/listTermDegree.htm";
			else
				return "redirect:/academicTerm/"+term+"add.htm";
		}
		return "redirect:/error.htm";
	}


	
	/**
	 * Methods for listing 
	 */

	@RequestMapping(value = "/academicTerm/list.htm")
	public ModelAndView handleRequestAcademicTermList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<String> result = serviceAcademicTerm.getAllTerms();
		myModel.put("academicTerms", result);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}
	
/*	@RequestMapping(value = "/academicTerm/{term}/listDegrees.htm")
	public ModelAndView handleRequestAcademicTermDegreeList(@PathVariable("term") String term,
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<Course> result = serviceCourse.getCoursesByAcademicTerm(term);
		myModel.put("courses", result);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}*/

	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/academicTerm/{term}/listTermDegree.htm")
	protected ModelAndView formViewAcademicTerm(@PathVariable("term") String term)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// ModelAndView model = new ModelAndView();
		List<AcademicTerm> p = serviceAcademicTerm.getAcademicsTerm(term);
//		Degree d = serviceDegree.getDegreeAcademicTerm(p);
//		p.setDegree(d);
		myModel.put("academicTerms", p);

		

		//List<Course> courses = serviceCourse.getCoursesByAcademicTerm(p);
		

		//if (courses != null)
		//	myModel.put("courses", courses);
		
	
		return new ModelAndView("academicTerm/listDegreesOfTerm", "model", myModel);
	}
	
	@RequestMapping(value = "/academicTerm/{term}/degree/{idDegree}/view.htm")
	protected ModelAndView formViewAcademicTermDegree(@PathVariable("term") String term,
			@PathVariable("idDegree") Long id_degree)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// ModelAndView model = new ModelAndView();
		AcademicTerm a = serviceAcademicTerm.getAcademicTermDegree(term,id_degree);
		//Degree d = serviceDegree.getDegreeAcademicTerm(p);
//		p.setDegree(d);
		
		myModel.put("academicTerm", a);

		

		List<Course> courses = serviceCourse.getCoursesByAcademicTermDegree(term, id_degree);
		

		if (courses != null)
			myModel.put("courses", courses);
		
	
		return new ModelAndView("academicTerm/view", "model", myModel);
	}
	/**
	 * Methods for modifying academic Term
	 */
	@RequestMapping(value = "/academicTerm/{term}/modifyChoose.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("term") String term,
			@ModelAttribute("modifyAcademicTerm") AcademicTerm modify,
			BindingResult result, Model model)

	{

		//modify.setId(id);
		if(modify.getDegree() == null)
			return "redirect://academicTerm/"+term+"/modifyChoose.htm";
		
		if (!result.hasErrors()) {
			//modify.setId(id);
			boolean success = serviceAcademicTerm.modifyAcademicTerm(modify);
			if (success)
				return "redirect:/academicTerm/"+ term +"/view.htm";
			
		}
		return "redirect:/error.htm";

	}

	/*@RequestMapping(value = "/academicTerm/{term}/modifyChoose.htm", method = RequestMethod.GET)
	protected String formModifyActivities(@PathVariable("term") String term,
			Model model) throws ServletException {

		AcademicTerm p = serviceAcademicTerm.getAcademicTerm(term);
		model.addAttribute("idDegree",p.getDegree().getId());
		model.addAttribute("modifyAcademicTerm", p);

		return "academicTerm/modifyChoose";
	}
	*/
	
	/**
	 * Delete an academicTerm.
	 */
	
	@RequestMapping(value = "/academicTerm/{term}/delete.htm", method = RequestMethod.GET)
	public String formDeleteAcademicTerm(@PathVariable("term") String term)
			throws ServletException {

		if (serviceAcademicTerm.deleteAcademicTerm(term)) {
			return "redirect:/academicTerm/list.htm";
		} else
			return "redirect:/error.htm";
	}
	
	/**
	 * Delete a course of an academicTerm.
	 */
	
	@RequestMapping(value = "/academicTerm/{term}/course/{courseId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteCourseofAcademicTerm(@PathVariable("term") String term,
			@PathVariable("courseId") Long id_course)
			throws ServletException {

		if (serviceCourse.deleteCourse(id_course)) {
			return "redirect:/academicTerm/"+term+"/view.htm";
		} else
			return "redirect:/error.htm";
	}
	
	/**
	 * For binding the courses of the academicTerm.
	 */
	/*@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "courses",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Course) {
							logger.info("Converting...{}", element);
							return element;
						}
					
						if (element instanceof String) {
							Course course = serviceCourse.getCourseByName(element.toString());
							logger.info("Loking up {} to {}", element,course);
							return course;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
		
		
	}*/
}
