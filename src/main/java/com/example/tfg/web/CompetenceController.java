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
	
	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm2(Model model,
			@PathVariable("degreeId") Long id) {
		Competence newCompetence = new Competence();
		newCompetence.setCode(serviceCompetence.getNextCode());

		Degree d = serviceDegree.getDegree(id);

		newCompetence.setDegree(d);
		model.addAttribute("addcompetence", newCompetence);
		return "competence/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence2(
			@ModelAttribute("addcompetence") Competence newCompetence,
			@PathVariable("degreeId") Long id) {
		Degree degree = serviceDegree.getDegree(id);

		newCompetence.setDegree(degree);
		boolean created = serviceCompetence.addCompetence(newCompetence);
		if (created)
			return "redirect:/degree/" + id + ".htm";
		else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for delete competences
	 */
	@RequestMapping(value="/degree/{degreeId}/competence/{competenceId}/delete.htm",method=RequestMethod.GET)
	public String formDeleteCompetenceFromDegree(@PathVariable("degreeId") Long id_degree,@PathVariable("competenceId") Long id_competence)
            throws ServletException {

		if (serviceCompetence.deleteCompetence(id_competence)){
			return "redirect:/degree/"+id_degree+".htm";
		}
		else return "redirect:/error.htm";
	}
	
	/**
	 * Methods for modify competences
	 */
	@RequestMapping(value= "/degree/{degreeId}/competence/{competenceId}/modify.htm",method=RequestMethod.POST)	
	public String formModifyCompetenceFromDegree(@PathVariable("degreeId") Long id_degree, @PathVariable("competenceId") Long id_competence, 
			@ModelAttribute("modifyCompetence")Competence modify)

    {     
        modify.setDegree(serviceDegree.getDegree(id_degree));
        modify.setId(id_competence);
        modify.setSubjects(serviceSubject.getSubjectsForCompetence(id_competence));
		serviceCompetence.modifyCompetence(modify);
        return "redirect:/degree/"+id_degree+".htm";
    }
	
	@RequestMapping(value="/degree/{degreeId}/competence/{competenceId}/modify.htm",method=RequestMethod.GET)
    protected String formModifyCompetenceFromDegree(@PathVariable("degreeId") Long id_degree,
    		@PathVariable("competenceId") Long id_competence, Model model)
            throws ServletException {
	  
    	Competence p= serviceCompetence.getCompetence(id_competence);
    	model.addAttribute("modifyCompetence",p);
    	return "/competence/modify";
    	
	}
	
	
//
//	/**
//	 * Methods for listing competences
//	 */
//
//	@RequestMapping(value = "/competence/list.htm")
//	public ModelAndView handleRequestCompetenceList(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		Map<String, Object> myModel = new HashMap<String, Object>();
//
//		List<Competence> result = serviceCompetence.getAll();
//		myModel.put("competences", result);
//
//		return new ModelAndView("competence/list", "model", myModel);
//	}
//
//	/**
//	 * Methods for modifying competences
//	 */
//	@RequestMapping(value = "/competence/modifyChoose/{competenceId}.htm", method = RequestMethod.POST)
//	public String formModifyCompetence(@PathVariable("competenceId") Long id,
//			@ModelAttribute("modifyCompetence") Competence modify,
//			BindingResult result, Model model)
//
//	{
//		if(modify.getDegree() == null)
//			return "redirect://competence/modifyChoose/"+id+".htm";
//		
//		if (!result.hasErrors()) {
//			modify.setId(id);
//			boolean success = serviceCompetence.modifyCompetence(modify);
//			if (success)
//				return "redirect:/competence/list.htm";
//		}
//
//		return "redirect:/error.htm";
//
//	}
//
//	@RequestMapping(value = "/competence/modifyChoose/{competenceId}.htm", method = RequestMethod.GET)
//	protected String formModifyCompetences(
//			@PathVariable("competenceId") Long id, Model model)
//			throws ServletException {
//
//		Competence p = serviceCompetence.getCompetence(id);
//		model.addAttribute("idDegree",p.getDegree().getId());
//		//p.setDegree(null);
//
//		model.addAttribute("modifyCompetence", p);
//		return "competence/modifyChoose";
//	}
//
//
//	/**
//	 * Delete Competence
//	 */
//	@RequestMapping(value = "/competence/delete/{competenceId}.htm", method = RequestMethod.GET)
//	public String formDeleteCompetences(@PathVariable("competenceId") Long id)
//			throws ServletException {
//
//		if (serviceCompetence.deleteCompetence(id)) {
//			return "redirect:/competence/list.htm";
//		} else
//			return "redirect:/error.htm";
//	}
	/**
	 * Delete Subject of a Competence
	 */
//	/degree/${model.competence.degree.id}/competence/${competenceId}/subject/${subject.id}/delete.htm
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/subject/{subjectId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteSubjectForCompetence(@PathVariable("degreeId") Long id_degree, @PathVariable("competenceId") Long id_competence,@PathVariable("subjectId") Long id_subject)
			throws ServletException {

		if (serviceCompetence.deleteCompetenceFromSubject(id_competence, id_subject)) {
			return "redirect:/degree/"+ id_degree +"/competence/"+id_competence+".htm";
		} else
			return "redirect:/error.htm";
	}


	/**
	 * Methods for view competence
	 */
	@RequestMapping(value="/degree/{degreeId}/competence/{competenceId}.htm",method=RequestMethod.GET)
	protected ModelAndView formViewSubject(@PathVariable("degreeId") Long id_degree, @PathVariable("competenceId") Long id_competence)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

	
		Competence p= serviceCompetence.getCompetence(id_competence);
		myModel.put("competence",p);



		List<Subject> subjects = serviceSubject.getSubjectsForCompetence(id_competence);

		
		if(subjects != null) myModel.put("subjects",subjects);

	


		return new ModelAndView("competence/view", "model", myModel);
	}
	

}