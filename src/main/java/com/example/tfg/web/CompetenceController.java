package com.example.tfg.web;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.SubjectService;

@Controller
public class CompetenceController {

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private SubjectService serviceSubject;
	
	@Autowired
	private DegreeService serviceDegree;

	/*
	 * private static final Logger logger = LoggerFactory
	 * .getLogger(CompetenceController.class);
	 */
	@ModelAttribute("degrees")
	public List<Degree> degrees() {
		return serviceDegree.getAll();
	}

	/**
	 * Methods for adding competences
	 */
	@RequestMapping(value = "/competence/add.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm(Model model) {
		Competence addcompetence = new Competence();
		addcompetence.setCode(serviceCompetence.getNextCode());

		model.addAttribute("addcompetence", addcompetence);
		return "competence/addChoose";
	}

	@RequestMapping(value = "/competence/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@ModelAttribute("addcompetence") Competence newCompetence,
			BindingResult result, Model model) {

		if(newCompetence.getDegree() == null)
			return "redirect:/competence/add.htm";
		
		if (!result.hasErrors()) {
			boolean created = serviceCompetence.addCompetence(newCompetence);
			if (created)
				return "redirect:/competence/list.htm";
			else
				return "redirect:/competence/add.htm";
		}
		return "redirect:/error.htm";
	}


	/**
	 * Methods for listing competences
	 */

	@RequestMapping(value = "/competence/list.htm")
	public ModelAndView handleRequestCompetenceList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<Competence> result = serviceCompetence.getAll();
		myModel.put("competences", result);

		return new ModelAndView("competence/list", "model", myModel);
	}

	/**
	 * Methods for modifying competences
	 */
	@RequestMapping(value = "/competence/modifyChoose/{competenceId}.htm", method = RequestMethod.POST)
	public String formModifyCompetence(@PathVariable("competenceId") long id,
			@ModelAttribute("modifyCompetence") Competence modify,
			BindingResult result, Model model)

	{
		if(modify.getDegree() == null)
			return "redirect://competence/modifyChoose/"+id+".htm";
		
		if (!result.hasErrors()) {
			modify.setId(id);
			boolean success = serviceCompetence.modifyCompetence(modify);
			if (success)
				return "redirect:/competence/list.htm";
		}

		return "redirect:/error.htm";

	}

	@RequestMapping(value = "/competence/modifyChoose/{competenceId}.htm", method = RequestMethod.GET)
	protected String formModifyCompetences(
			@PathVariable("competenceId") long id, Model model)
			throws ServletException {

		Competence p = serviceCompetence.getCompetence(id);
		model.addAttribute("idDegree",p.getDegree().getId());
		//p.setDegree(null);

		model.addAttribute("modifyCompetence", p);
		return "competence/modifyChoose";
	}


	/**
	 * Delete Competence
	 */
	@RequestMapping(value = "/competence/delete/{competenceId}.htm", method = RequestMethod.GET)
	public String formDeleteCompetences(@PathVariable("competenceId") long id)
			throws ServletException {

		if (serviceCompetence.deleteCompetence(id)) {
			return "redirect:/competence/list.htm";
		} else
			return "redirect:/error.htm";
	}
	/**
	 * Delete Subject of a Competence
	 */
	@RequestMapping(value = "/competence/subject/delete/{competenceId}/{subjectId}.htm", method = RequestMethod.GET)
	public String formDeleteSubjectForCompetence(@PathVariable("competenceId") long id_competence,@PathVariable("subjectId") long id_subject)
			throws ServletException {

		if (serviceCompetence.deleteCompetenceFromSubject(id_competence, id_subject)) {
			return "redirect:/competence/view/"+id_competence+".htm";
		} else
			return "redirect:/error.htm";
	}


	/**
	 * Methods for view competence
	 */
	@RequestMapping(value="/competence/view/{competenceId}.htm",method=RequestMethod.GET)
	protected ModelAndView formViewSubject(@PathVariable("competenceId") long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		//ModelAndView model = new ModelAndView();
		Competence p= serviceCompetence.getCompetence(id);
		myModel.put("competence",p);

		/*    	Collection<Competence> c = p.getCompetences();
    	if(p.getCompetences() != null)
    		model.addObject("competences",p.getCompetences());
    	if(p.getActivities() != null)
    		model.addObject("activities",p.getActivities());*/


		List<Subject> subjects = serviceSubject.getSubjectsForCompetence(id);

		
		if(subjects != null) myModel.put("subjects",subjects);

		//model.setViewName("subject/view");


		return new ModelAndView("competence/view", "model", myModel);
	}
	
	public CompetenceService getServiceCompetence() {
		return serviceCompetence;
	}

	public void setServiceCompetence(CompetenceService serviceCompetence) {
		this.serviceCompetence = serviceCompetence;
	}
}