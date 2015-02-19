package com.example.tfg.web;

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

import com.example.tfg.domain.LearningGoal;
import com.example.tfg.service.LearningGoalService;


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

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewLearningGoal2(
			@ModelAttribute("addLearningGoal") LearningGoal newLearningGoal,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("degreeId") Long id_degree) {

		boolean created = serviceLearningGoal.addLearningGoal(newLearningGoal,
				id_competence);
		if (created)
			return "redirect:/degree/" + id_degree + "/competence/"+ id_competence +".htm";
		else
			return "redirect:/error.htm";
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

		if (serviceLearningGoal.deleteLearningGoal(id_learningGoal)) {
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
			@ModelAttribute("modifyLearningGoal") LearningGoal modify)

	{

		
		serviceLearningGoal.modifyLearningGoal(modify, id_learningGoal);
		return "redirect:/degree/" + id_degree +"/competence"+ id_competence +".htm";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/modify.htm", method = RequestMethod.GET)
	protected String formModifyLearningGoal(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, 
			@PathVariable("learninggoalId") Long id_learningGoal,
			Model model)
			throws ServletException {

		LearningGoal p = serviceLearningGoal.getLearningGoal(id_learningGoal);
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

		LearningGoal p = serviceLearningGoal.getLearningGoal(id_learningGoal);

		// List<Subject> subjects =
		// serviceSubject.getSubjectsForCompetence(id_competence);

		myModel.put("learningGoal", p);
	

		return new ModelAndView("learningGoal/view", "model", myModel);
	}

}
