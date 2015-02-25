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

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.CompetenceService;
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

//	@Autowired
//	private ActivityService serviceActivity;

//	@Autowired
//	private DegreeService serviceDegree;

//	@Autowired
//	private AcademicTermService serviceAcademicTerm;


	private static final Logger logger = LoggerFactory
			.getLogger(SubjectController.class);

	/**
	 * Methods for delete subjects
	 */

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/{subjectId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteSubjectFromTopic(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic,
			@PathVariable("subjectId") Long id_subject) throws ServletException {

		if (serviceSubject.deleteSubject(id_subject)) {
			return "redirect:/degree/" + id_degree + "/module/"+ id_module + "/topic/" + id_topic + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for adding subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(Model model,
			@PathVariable("degreeId") Long id_degree) {
		Subject newSubject = new Subject();
		// newSubject.setCode(serviceSubject.getNextCode());

		// newSubject.setDegree(serviceDegree.getDegree(id));
		model.addAttribute("addsubject", newSubject);
		return "subject/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/add", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewSubject(
			@ModelAttribute("addsubject") Subject newSubject,
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic,
			Model model) {

		ResultClass<Boolean> result = serviceSubject.addSubject(newSubject, id_topic);
		if (!result.hasErrors())
			//		if (created)
			return "redirect:/degree/" + id_degree + "/module/"+ id_module + "/topic/" + id_topic + ".htm";
		else{
			model.addAttribute("addsubject", newSubject);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", result.isElementDeleted()); 
			model.addAttribute("errors", result.getErrorsList());
			return "subject/add";

		}
	}

	
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/add", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addsubject") Subject subject, Model model,
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic) {
		
		ResultClass<Boolean> result = serviceSubject.unDeleteSubject(subject);
		
		if (!result.hasErrors())
//		if (created)
			return "redirect:/degree/" + id_degree + "/module/"+ id_module + "/topic/" + id_topic + ".htm";
		else{
			model.addAttribute("addsubject", subject);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "subject/add";
		}
	}
		/**
		 * Methods for modify subjects
		 */
		@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/{subjectId}/modify.htm", method = RequestMethod.POST)
		public String formModifySubjectFromDegree(
				@PathVariable("degreeId") Long id_degree,
				@PathVariable("moduleId") Long id_module,
				@PathVariable("topicId") Long id_topic,
				@PathVariable("subjectId") Long id_subject,
				@ModelAttribute("modifySubject") Subject modify,
				Model model)

		{
			ResultClass<Boolean> result = serviceSubject.modifySubject(modify, id_subject);
			if (!result.hasErrors())
//				if (created)
				return "redirect:/degree/" + id_degree + "/module/"+ id_module + "/topic/" + id_topic + ".htm";
				else{
					model.addAttribute("modifySubject", modify);
					if (result.isElementDeleted()){
						model.addAttribute("addsubject", modify);
						model.addAttribute("unDelete", true); 
						model.addAttribute("errors", result.getErrorsList());
						return "subject/add";
					}	
					model.addAttribute("errors", result.getErrorsList());
					return "subject/modify";
				}

			

		}

		@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/{subjectId}/modify.htm", method = RequestMethod.GET)
		protected ModelAndView formModifySubjectFromDegree(
				@PathVariable("degreeId") Long id_degree,
				@PathVariable("moduleId") Long id_module,
				@PathVariable("topicId") Long id_topic,
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
		@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/{subjectId}.htm", method = RequestMethod.GET)
		protected ModelAndView formViewSubject(
				@PathVariable("degreeId") Long id_degree,
				@PathVariable("subjectId") Long id_subject) throws ServletException {

			Map<String, Object> myModel = new HashMap<String, Object>();

			Subject p = serviceSubject.getSubjectAll(id_subject);

			myModel.put("subject", p);
			myModel.put("topic", p.getTopic());

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
				@PathVariable("moduleId") Long id_module,
				@PathVariable("moduleId") Long id_topic,
				@PathVariable("subjectId") Long id_subject,
				@PathVariable("competenceId") Long id_competence)
						throws ServletException {

			if (serviceCompetence.deleteCompetenceFromSubject(id_competence,
					id_subject)) {
				return "redirect:/degree/" + id_degree + "/module/"+ id_module + "/topic/" + id_topic + ".htm";
			} else
				return "redirect:/error.htm";
		}

		@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/{subjectId}/addCompetences.htm", method = RequestMethod.GET)
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

		@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/subject/{subjectId}/addCompetences.htm", method = RequestMethod.POST)
		// Every Post have to return redirect
		public String processAddNewCompetence(
				@PathVariable("degreeId") Long id_degree,
				@PathVariable("moduleId") Long id_module,
				@PathVariable("moduleId") Long id_topic,
				@PathVariable("subjectId") Long id_subject,
				@ModelAttribute("subject") Subject subject, BindingResult result,
				Model model) {

			// Subject aux = serviceSubject.getSubject(id_subject);

			// subject.setId(id_subject);

			// subject.setDegree(aux.getDegree());

			if (!result.hasErrors()){
				try {
					serviceSubject.addCompetences(subject, id_subject);
					return "redirect:/degree/" + id_degree + "/module/"+ id_module + "/topic/" + id_topic + ".htm";
				} catch (Exception e) {
					return "redirect:/competence/add.htm";
				}
			}
			else return "redirect:/error.htm";

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
