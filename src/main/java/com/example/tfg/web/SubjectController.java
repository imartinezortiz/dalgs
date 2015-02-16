package com.example.tfg.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

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

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.SubjectService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SubjectController {

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	private static final Logger logger = LoggerFactory
			.getLogger(SubjectController.class);

	/**
	 * Methods for delete subjects
	 */

	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteSubjectFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject) throws ServletException {

		if (serviceSubject.deleteSubject(id_subject, id_degree)) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for adding subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(Model model,
			@PathVariable("degreeId") Long id) {
		Subject newSubject = new Subject();
		// newSubject.setCode(serviceSubject.getNextCode());

		// newSubject.setDegree(serviceDegree.getDegree(id));
		model.addAttribute("addsubject", newSubject);
		return "subject/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/add", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewSubject(
			@ModelAttribute("addsubject") Subject newSubject,
			@PathVariable("degreeId") Long id_degree) {

		boolean created = serviceSubject.addSubject(newSubject, id_degree);

		if (created)
			return "redirect:/degree/" + id_degree + ".htm";
		else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for modify subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/modify.htm", method = RequestMethod.POST)
	public String formModifySubjectFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject,
			@ModelAttribute("modifySubject") Subject modify)

	{
		// modify.setId(id_subject);

		if (serviceSubject.modifySubject(modify, id_subject))
			return "redirect:/degree/" + id_degree + ".htm";
		else
			return "redirect:/error.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/modify.htm", method = RequestMethod.GET)
	protected ModelAndView formModifySubjectFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject) throws ServletException {
		ModelAndView model = new ModelAndView();
		Subject p = serviceSubject.getSubject(id_subject);

		model.addObject("modifySubject", p);
		model.addObject("competences", p.getCompetences());
		model.setViewName("/subject/modify");

		return model;
	}

	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewSubject(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject) throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Subject p = serviceSubject.getSubjectAndDegree(id_subject, id_degree);

		myModel.put("subject", p);
		myModel.put("degree", p.getDegree());

		if (p.getCompetences() != null)
			myModel.put("competences", p.getCompetences());

		return new ModelAndView("subject/view", "model", myModel);
	}

	/**
	 * Method for manage competences of a subject
	 */

	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/competence/{competenceId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceFromSubject(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject,
			@PathVariable("competenceId") Long id_competence)
			throws ServletException {

		if (serviceCompetence.deleteCompetenceFromSubject(id_competence,
				id_subject)) {
			return "redirect:/degree/" + id_degree + "/subject/" + id_subject
					+ ".htm";
		} else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/addCompetences.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject, Model model) {

		Subject s = serviceSubject.getSubject(id_subject);
		Collection<Competence> competences = serviceCompetence
				.getCompetencesForDegree(id_degree);

		model.addAttribute("subject", s);
		model.addAttribute("competences", competences);

		return "subject/addCompetences";
	}

	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/addCompetences.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("subjectId") Long id_subject,
			@ModelAttribute("subject") Subject subject, BindingResult result,
			Model model) {

		// Subject aux = serviceSubject.getSubject(id_subject);

		// subject.setId(id_subject);

		// subject.setDegree(aux.getDegree());

		try {
			serviceSubject.addCompetences(subject, id_subject);
			return "redirect:/degree/" + id_degree + "/subject/" + id_subject
					+ ".htm";
		} catch (Exception e) {
			return "redirect:/competence/add.htm";
		}

	}

	/**
	 * For binding the competences of the subject.
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "competences",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Competence) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							Competence competence = serviceCompetence
									.getCompetenceByName(element.toString());
							logger.info("Loking up {} to {}", element,
									competence);

							return competence;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}

}
