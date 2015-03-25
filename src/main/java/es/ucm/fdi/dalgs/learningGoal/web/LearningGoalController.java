package es.ucm.fdi.dalgs.learningGoal.web;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
																				
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/add.htm", method = RequestMethod.GET)
	public String addLearningGoalGET(Model model,
			@PathVariable("degreeId") Long id) {
		if(!model.containsAttribute("learningGoal"))
			model.addAttribute("learningGoal", new LearningGoal());
		model.addAttribute("valueButton", "Add");
		model.addAttribute("typeform", "form.add");

		
		return "learningGoal/form";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String addLearningGoalPOST(
			@ModelAttribute("learningGoal") LearningGoal newLearningGoal,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("degreeId") Long id_degree,
			BindingResult resultBinding, RedirectAttributes attr) {

		if (!resultBinding.hasErrors()){

			ResultClass<LearningGoal> result = serviceLearningGoal.addLearningGoal(newLearningGoal, id_competence);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + "/competence/"+ id_competence +".htm";	
			else{

				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("learningGoal", result.getSingleElement());
				}else attr.addFlashAttribute("learningGoal", newLearningGoal);


			}
		}else{
			attr.addFlashAttribute("learningGoal", newLearningGoal);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.learningGoal",
					resultBinding);			
		}
		return "redirect:/degree/"+ id_degree+"/competence/"+ id_competence+"/learninggoal/add.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteLearningGoalPOST(
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("learningGoal") LearningGoal learningGoal,
			BindingResult resultBinding, RedirectAttributes attr) {


		if (!resultBinding.hasErrors()){

			ResultClass<LearningGoal> result = serviceLearningGoal.unDeleteLearningGoal(learningGoal);

			if (!result.hasErrors()){
				attr.addFlashAttribute("learningGoal", result.getSingleElement());

				return "redirect:/degree/" + id_degree + "/competence/" + id_competence + "/learninggoal/"+result.getSingleElement().getId()+"/modify.htm";
			}else{

				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());


			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.learningGoal",
					resultBinding);
		}


		attr.addFlashAttribute("learningGoal", learningGoal);
		return "reidrect:/degree/"+ id_degree+"/competence/"+ id_competence+"/learninggoal/add.htm";
	}

	/**
	 * Methods for delete LearningGoals
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/delete.htm", method = RequestMethod.GET)
	public String deleteLearningGoalGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal)
					throws ServletException {

		if (serviceLearningGoal.deleteLearningGoal(serviceLearningGoal.getLearningGoal(id_learningGoal).getSingleElement()).getSingleElement()) {
			return "redirect:/degree/" + id_degree +"/competence/" +id_competence+ ".htm";
		} else
			return "redirect:/error.htm";
	}





	/**
	 * Methods for modify LearningGoals
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/modify.htm", method = RequestMethod.POST)
	public String modifyLearningGoalPOST(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal,
			@ModelAttribute("learningGoal") LearningGoal modify,
			BindingResult resultBinding, RedirectAttributes attr)

	{
		if (!resultBinding.hasErrors()){

			ResultClass<Boolean> result = serviceLearningGoal.modifyLearningGoal(modify, id_learningGoal);
			if (!result.hasErrors())

				return "redirect:/degree/" + id_degree +"/competence/"+ id_competence +".htm";
			else{
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.learningGoal",
					resultBinding);
		}
		attr.addFlashAttribute("learningGoal", modify);
		return "redirect:/degree/"+id_degree+"/competence/"+id_competence+"/learninggoal/"+id_learningGoal+"/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/modify.htm", method = RequestMethod.GET)
	protected String modifyLearningGoalGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, 
			@PathVariable("learninggoalId") Long id_learningGoal,
			Model model)
					throws ServletException {

		if (!model.containsAttribute("learningGoal")){

			LearningGoal p = serviceLearningGoal.getLearningGoal(id_learningGoal).getSingleElement();
			model.addAttribute("learningGoal", p);
		}
		model.addAttribute("valueButton", "Modify");
		model.addAttribute("typeform", "form.modify");

		return "learningGoal/form";

	}



	/**
	 * Methods for view LearningGoal
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}.htm", method = RequestMethod.GET)
	protected ModelAndView getLearningGoalGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		LearningGoal p = serviceLearningGoal.getLearningGoal(id_learningGoal).getSingleElement();
		myModel.put("learningGoal", p);

		return new ModelAndView("learningGoal/view", "model", myModel);
	}
	
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/learninggoal/{learninggoalId}/restore.htm")
	// Every Post have to return redirect
	public String restoreLearningGoal(@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@PathVariable("learninggoalId") Long id_learningGoal ) {
		
		ResultClass<LearningGoal> result = serviceLearningGoal.unDeleteLearningGoal(serviceLearningGoal.getLearningGoal(id_learningGoal).getSingleElement());
		if (!result.hasErrors())
			return "redirect:/degree/"+id_degree+"/competence/"+id_competence+".htm";
		else{
			return "redirect:/error.htm";

		}

	}

}
