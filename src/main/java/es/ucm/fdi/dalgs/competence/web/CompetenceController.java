package es.ucm.fdi.dalgs.competence.web;

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
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.Degree;

@Controller
public class CompetenceController {

	@Autowired
	private CompetenceService serviceCompetence;

//	@Autowired
//	private SubjectService serviceSubject;

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

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.GET)
	public String getAddNewCompetenceForm2(Model model,
			@PathVariable("degreeId") Long id) {
		Competence newCompetence = new Competence();

		model.addAttribute("addcompetence", newCompetence);
		return "competence/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewCompetence2(
			@ModelAttribute("addcompetence") Competence newCompetence,
			@PathVariable("degreeId") Long id_degree,
			Model model) {
		
		ResultClass<Boolean> result = serviceCompetence.addCompetence(newCompetence, id_degree);
		if (!result.hasErrors())
			return "redirect:/degree/" + id_degree + ".htm";
		else{
			model.addAttribute("addcompetence", newCompetence);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", result.isElementDeleted()); 
			model.addAttribute("errors", result.getErrorsList());
			return "competence/add";
		}
			
	
	}

	/**
	 * Methods for delete competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence)
			throws ServletException {

		if (serviceCompetence.deleteCompetence(id_competence)) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	
	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("addcompetence") Competence competence, Model model) {
		
		ResultClass<Boolean> result = serviceCompetence.unDeleteCompetence(competence, id_degree);
		
		if (!result.hasErrors())
			return "redirect:/degree/" + id_degree + ".htm";
		else{
			model.addAttribute("addcompetence", competence);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "competence/add";
		}
	}
	/**
	 * Methods for modify competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.POST)
	public String formModifyCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@ModelAttribute("modifyCompetence") Competence modify,
			Model model)

	{
		ResultClass<Boolean> result = serviceCompetence.addCompetence(modify, id_competence);
		if (!result.hasErrors())
			return "redirect:/degree/" + id_degree + ".htm";
			else{
				model.addAttribute("modifyCompetence", modify);
				if (result.isElementDeleted()){
					model.addAttribute("addcompetence", modify);
					model.addAttribute("unDelete", true); 
					model.addAttribute("errors", result.getErrorsList());
					return "competence/add";
				}	
				model.addAttribute("errors", result.getErrorsList());
				return "competence/modify";
			}
		
	

	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, Model model)
			throws ServletException {

		Competence p = serviceCompetence.getCompetence(id_competence);
		model.addAttribute("modifyCompetence", p);
		return "/competence/modify";

	}

//	/**
//	 * Delete Subject of a Competence
//	 */
//
//	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/subject/{subjectId}/delete.htm", method = RequestMethod.GET)
//	public String formDeleteSubjectForCompetence(
//			@PathVariable("degreeId") Long id_degree,
//			@PathVariable("competenceId") Long id_competence,
//			@PathVariable("subjectId") Long id_subject) throws ServletException {
//
//		if (serviceCompetence.deleteCompetenceFromSubject(id_competence,
//				id_subject)) {
//			return "redirect:/degree/" + id_degree + "/competence/"
//					+ id_competence + ".htm";
//		} else
//			return "redirect:/error.htm";
//	}

	/**
	 * Methods for view competence
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewSubject(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

//		Competence p = serviceCompetence.getCompetence(id_competence);
		
		Competence p = serviceCompetence.getCompetenceAll(id_competence);

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForCompetence(id_competence);

		myModel.put("competence", p);
		// if(subjects != null)
		myModel.put("learningGoals", p.getLearningGoals());

		return new ModelAndView("competence/view", "model", myModel);
	}

}