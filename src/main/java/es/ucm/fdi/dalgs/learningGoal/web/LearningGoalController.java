package es.ucm.fdi.dalgs.learningGoal.web;

import java.util.HashMap;
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
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;


@Controller
public class LearningGoalController {

	@Autowired
	LearningGoalService serviceLearningGoal;
	
	/**
	 * Methods for adding LearningGoals
	 */

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/add.htm", method = RequestMethod.GET)
	public String getAddNewLearningGoalForm2(Model model,
			@PathVariable("degreeId") Long id) {
		LearningGoal newLearningGoal = new LearningGoal();

		model.addAttribute("addLearningGoal", newLearningGoal);
		return "learningGoal/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewLearningGoal2(
			@ModelAttribute("addLearningGoal") LearningGoal newLearningGoal,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("degreeId") Long id_degree,
			Model model) {

		
		ResultClass<Boolean> result = serviceLearningGoal.addLearningGoal(newLearningGoal, id_competence);
		if (!result.hasErrors())
			return "redirect:/degree/" + id_degree + "/competence/"+ id_competence +".htm";	
		else{
			model.addAttribute("addLearningGoal", newLearningGoal);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", result.isElementDeleted()); 
			model.addAttribute("errors", result.getErrorsList());
			return "learningGoal/add";

		}
		
	}
	
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("addLearningGoal") LearningGoal learningGoal, Model model) {
		
		ResultClass<Boolean> result = serviceLearningGoal.unDeleteLearningGoal(learningGoal);
		
		if (!result.hasErrors())

			return "redirect:/degree/" + id_degree + "/competence/"+ id_competence +".htm";	
		else{
			model.addAttribute("addLearningGoal", learningGoal);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "module/add";
		}
	}

	/**
	 * Methods for delete LearningGoals
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteLearningGoalFromDegree(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal)
			throws ServletException {

		if (serviceLearningGoal.deleteLearningGoal(id_learningGoal).getSingleElement()) {
			return "redirect:/degree/" + id_degree +"/competence/" +id_competence+ ".htm";
		} else
			return "redirect:/error.htm";
	}

	
	
	
	
	/**
	 * Methods for modify LearningGoals
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/modify.htm", method = RequestMethod.POST)
	public String formModifyLearningGoalFromCompetence(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal,
			@ModelAttribute("modifyLearningGoal") LearningGoal modify,
			Model model)

	{

		ResultClass<Boolean> result = serviceLearningGoal.modifyLearningGoal(modify, id_learningGoal);
		if (!result.hasErrors())

			return "redirect:/degree/" + id_degree +"/competence"+ id_competence +".htm";
		else{
				model.addAttribute("modifyLearningGoal", modify);
				if (result.isElementDeleted()){
					model.addAttribute("addLearningGoal", modify);
					model.addAttribute("unDelete", true); 
					model.addAttribute("errors", result.getErrorsList());
					return "module/add";
				}	
				model.addAttribute("errors", result.getErrorsList());
				return "module/modify";
			}
	
		
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyLearningGoal(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, 
			@PathVariable("learninggoalId") Long id_learningGoal,
			Model model)
			throws ServletException {

		LearningGoal p = serviceLearningGoal.getLearningGoal(id_learningGoal).getSingleElement();
		model.addAttribute("modifyLearningGoal", p);
		return "learningGoal/modify";

	}



	/**
	 * Methods for view LearningGoal
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewLearningGoal(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		LearningGoal p = serviceLearningGoal.getLearningGoal(id_learningGoal).getSingleElement();

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForCompetence(id_competence);

		myModel.put("learningGoal", p);
	

		return new ModelAndView("learningGoal/view", "model", myModel);
	}

}
