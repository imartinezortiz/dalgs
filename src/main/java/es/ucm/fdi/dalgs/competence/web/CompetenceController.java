package es.ucm.fdi.dalgs.competence.web;

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
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Competence;

@Controller
public class CompetenceController {

	@Autowired
	private CompetenceService serviceCompetence;

	//	@Autowired
	//	private SubjectService serviceSubject;

	@Autowired
	private DegreeService serviceDegree;

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}
	/*
	 * private static final Logger logger = LoggerFactory
	 * .getLogger(CompetenceController.class);
	 */
	//	@ModelAttribute("degrees")
	//	public List<Degree> degrees() {
	//		return serviceDegree.getAll().getE();
	//	}

	/**
	 * Methods for adding competences
	 */

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.GET)
	public String getAddNewCompetenceForm2(Model model,
			@PathVariable("degreeId") Long id) {
		//		Competence newCompetence = new Competence();

		if(!model.containsAttribute("competence"))
			model.addAttribute("competence", new Competence());

		model.addAttribute("valueButton", "Add");

		return "competence/form";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewCompetence2(
			@ModelAttribute("competence") Competence newCompetence,
			@PathVariable("degreeId") Long id_degree,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){
			ResultClass<Competence> result = serviceCompetence.addCompetence(newCompetence, id_degree);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + ".htm";
			else{

				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("competence", result.getSingleElement());

				}else attr.addFlashAttribute("competence", newCompetence);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute("competence", newCompetence);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.competence",
					resultBinding);

		}
		return "redirect:/degree/"+id_degree+"/competence/add.htm";

	}

	/**
	 * Methods for delete competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence)
					throws ServletException {

		if (serviceCompetence.deleteCompetence(serviceCompetence.getCompetence(id_competence).getSingleElement()).getSingleElement()) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}


	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("competence") Competence competence,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){

			ResultClass<Competence> result = serviceCompetence.unDeleteCompetence(competence, id_degree);

			if (!result.hasErrors()){
				attr.addFlashAttribute("competence", result.getSingleElement());
				return "redirect:/degree/" + id_degree +  "/competence/" + result.getSingleElement().getId() + "/modify.htm";
			}
			else{

				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.competence",
					resultBinding);

		}
		attr.addFlashAttribute("competence", competence);
		return "redirect:/degree/"+id_degree+"/competence/add.htm";
	}
	/**
	 * Methods for modify competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.POST)
	public String formModifyCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@ModelAttribute("competence") Competence modify,
			BindingResult resultBinding, RedirectAttributes attr)

	{
		if (!resultBinding.hasErrors()){

			ResultClass<Boolean> result = serviceCompetence.modifyCompetence(modify, id_competence, id_degree);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + ".htm";
			else{
				//			model.addAttribute("modifyCompetence", modify);
				//			if (result.isElementDeleted()){
				//				model.addAttribute("addcompetence", modify);
				//				model.addAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());

			}	

		}
		else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.competence",
					resultBinding);

		}
		attr.addFlashAttribute("module", modify);
		return "redirect:/degree/"+id_degree+"/competence/"+id_competence+"/modify.htm";


	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyCompetenceFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, Model model)
					throws ServletException {
		if (!model.containsAttribute("competence")){

			Competence p = serviceCompetence.getCompetence(id_competence).getSingleElement();
			model.addAttribute("competence", p);
		}
		model.addAttribute("valueButton", "Modify");

		return "/competence/form";

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
			@PathVariable("competenceId") Long id_competence,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		//		Competence p = serviceCompetence.getCompetence(id_competence);

		Competence p = serviceCompetence.getCompetenceAll(id_competence, show).getSingleElement();

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForCompetence(id_competence);
		myModel.put("showAll", show);
		myModel.put("competence", p);
		// if(subjects != null)
		myModel.put("learningGoals", p.getLearningGoals());

		return new ModelAndView("competence/view", "model", myModel);
	}
	
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/restore.htm")
	// Every Post have to return redirect
	public String restoreCompetence(@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence) {
		ResultClass<Competence> result = serviceCompetence.unDeleteCompetence(serviceCompetence.getCompetence(id_competence).getSingleElement(), id_degree);
		if (!result.hasErrors())
			//			if (created)
			return "redirect:/degree/"+id_degree+".htm";
		else{
			return "redirect:/error.htm";

		}

	}

}