package com.example.tfg.web;

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

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
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

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm2(Model model,
			@PathVariable("degreeId") Long id) {
		Competence newCompetence = new Competence();

		model.addAttribute("addcompetence", newCompetence);
		return "competence/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence2(
			@ModelAttribute("addcompetence") Competence newCompetence,
			@PathVariable("degreeId") Long id_degree) {

		boolean created = serviceCompetence.addCompetence(newCompetence,
				id_degree);
		if (created)
			return "redirect:/degree/" + id_degree + ".htm";
		else
			return "redirect:/error.htm";
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

	/**
	 * Methods for modify competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.POST)
	public String formModifyCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@ModelAttribute("modifyCompetence") Competence modify)

	{

		modify.setId(id_competence);
		serviceCompetence.modifyCompetence(modify);
		return "redirect:/degree/" + id_degree + ".htm";
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

	/**
	 * Delete Subject of a Competence
	 */

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/subject/{subjectId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteSubjectForCompetence(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("subjectId") Long id_subject) throws ServletException {

		if (serviceCompetence.deleteCompetenceFromSubject(id_competence,
				id_subject)) {
			return "redirect:/degree/" + id_degree + "/competence/"
					+ id_competence + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view competence
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewSubject(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Competence p = serviceCompetence.getCompetence(id_competence);

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForCompetence(id_competence);

		myModel.put("competence", p);
		// if(subjects != null)
		myModel.put("subjects", p.getSubjects());

		return new ModelAndView("competence/view", "model", myModel);
	}

}