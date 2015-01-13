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
	@ModelAttribute("courses")
	public List<Course> courses() {
		return serviceCourse.getAll();
	}
	
	/**
	 * Methods for adding academicTerms
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.GET)
	protected String getAddNewAcademicTermForm(Model model) {

		AcademicTerm newAcademicTerm = new AcademicTerm();
		model.addAttribute("addAcademicTerm", newAcademicTerm);
		return "academicTerm/addChoose";

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
				return "redirect:/academicTerm/list.htm";
			else
				return "redirect:/academicTerm/add.htm";
		}
		return "redirect:/error.htm";
	}

	
	/**
	 * Methods for listing activities
	 */

	@RequestMapping(value = "/academicTerm/list.htm")
	public ModelAndView handleRequestAcademicTermList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String now = (new Date()).toString();
		logger.info("Returning hello view with " + now);

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<AcademicTerm> result = serviceAcademicTerm.getAll();
		myModel.put("academicTerms", result);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}

	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/academicTerm/view/{academicTermId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewAcademicTerm(@PathVariable("academicTermId") long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// ModelAndView model = new ModelAndView();
		AcademicTerm p = serviceAcademicTerm.getAcademicTerm(id);
//		Degree d = serviceDegree.getDegreeAcademicTerm(p);
//		p.setDegree(d);
		myModel.put("academicTerm", p);

		

		List<Course> courses = serviceCourse.getCoursesByAcademicTerm(p);
		

		if (courses != null)
			myModel.put("courses", courses);
		
	
		return new ModelAndView("academicTerm/view", "model", myModel);
	}
	
	/**
	 * Methods for modifying academic Term
	 */
	
	@RequestMapping(value = "/academicTerm/modifyChoose/{academicTermId}.htm", method = RequestMethod.GET)
	protected String formModifyActivities(@PathVariable("academicTermId") long id,
			Model model) throws ServletException {

		AcademicTerm p = serviceAcademicTerm.getAcademicTerm(id);
		if(p.getDegree()!= null)
			model.addAttribute("idDegree",p.getDegree().getId());
		
		
		model.addAttribute("modifyAcademicTerm", p);
	

		return "academicTerm/modifyChoose";
	}
	
	@RequestMapping(value = "/academicTerm/modifyChoose/{academicTermId}.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("academicTermId") long id,
			@ModelAttribute("modifyAcademicTerm") AcademicTerm modify,
			BindingResult result, Model model)

	{

		modify.setId(id);
		if(modify.getDegree() == null)
			return "redirect://academicTerm/modifyChoose/"+id+".htm";
		
		if (!result.hasErrors()) {
			modify.setId(id);
			boolean success = serviceAcademicTerm.modifyAcademicTerm(modify);
			if (success)
				return "redirect:/academicTerm/view/"+id+".htm";
			
		}
		return "redirect:/error.htm";

	}

	
	/**
	 * Delete an academicTerm.
	 */
	
	@RequestMapping(value = "/academicTerm/delete/{academicTermId}.htm", method = RequestMethod.GET)
	public String formDeleteAcademicTerm(@PathVariable("academicTermId") long id)
			throws ServletException {

		if (serviceAcademicTerm.deleteAcademicTerm(id)) {
			return "redirect:/academicTerm/list.htm";
		} else
			return "redirect:/error.htm";
	}
	
	/**
	 * Delete a course of an academicTerm.
	 */
	
	@RequestMapping(value = "/academicTerm/course/delete/{academicTermId}/{courseId}.htm", method = RequestMethod.GET)
	public String formDeleteCourseofAcademicTerm(@PathVariable("academicTermId") long id_academicTerm,
			@PathVariable("courseId") long id_course)
			throws ServletException {

		if (serviceCourse.deleteCourse(id_course)) {
			return "redirect:/academicTerm/view/"+id_academicTerm+".htm";
		} else
			return "redirect:/error.htm";
	}
	/**
	 * For binding the courses of the academicTerm.
	 */
	@InitBinder
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
		
		
	}
}
