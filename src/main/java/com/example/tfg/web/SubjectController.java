package com.example.tfg.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
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

	
	@RequestMapping(value="/degree/{degreeId}/subject/{subjectId}/delete.htm",method=RequestMethod.GET)
	public String formDeleteSubjectFromDegree(@PathVariable("degreeId") Long id_degree,@PathVariable("subjectId") Long id_subject)
			throws ServletException {

		if (serviceSubject.deleteSubject(id_subject)){
			return "redirect:/degree/"+id_degree+".htm";
		}
		else return "redirect:/error.htm";
	}

	/**
	 * Methods for adding subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/subject/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(Model model,
			@PathVariable("degreeId") Long id) {
		Subject newSubject = new Subject();
		newSubject.setCode(serviceSubject.getNextCode());

		Degree d = serviceDegree.getDegree(id);

		newSubject.setDegree(d);
		model.addAttribute("addsubject", newSubject);
		return "subject/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/subject/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewActivity(
			@ModelAttribute("addsubject") Subject newSubject,
			@PathVariable("degreeId") Long id) {
		Degree degree = serviceDegree.getDegree(id);

		newSubject.setDegree(degree);
		boolean created = serviceSubject.addSubject(newSubject);
		if (created)
			return "redirect:/degree/" + id + ".htm";
		else
			return "redirect:/error.htm";
	}
	
	/**
	 * Methods for modify subjects
	 */
	@RequestMapping(value= "/degree/{degreeId}/subject/{subjectId}/modify.htm",method=RequestMethod.POST)	
	public String formModifySubjectFromDegree(@PathVariable("degreeId") Long id_degree,@PathVariable("subjectId") Long id_subject, @ModelAttribute("modifySubject")Subject modify)

    {
		modify.setDegree(serviceDegree.getDegree(id_degree));
		modify.setId(id_subject);
		modify.setCompetences(serviceCompetence.getCompetencesForSubject(id_subject));
        serviceSubject.modifySubject(modify);
        return "redirect:/degree/"+id_degree+".htm";
    }
	
	
	@RequestMapping(value="/degree/{degreeId}/subject/{subjectId}/modify.htm",method=RequestMethod.GET)
    protected ModelAndView formModifySubjectFromDegree(@PathVariable("degreeId") Long id_degree, @PathVariable("subjectId") Long id_subject)
            throws ServletException {
	  	ModelAndView model = new ModelAndView();
    	Subject p= serviceSubject.getSubject(id_subject);
    	model.addObject("modifySubject",p);
    	model.setViewName("/subject/modify");
    	
    	return model;
    }



	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewSubject(@PathVariable("degreeId") Long id_degree, @PathVariable("subjectId") Long id_subject)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Subject p = serviceSubject.getSubject(id_subject);
		Degree d = serviceDegree.getDegree(id_degree);
		p.setDegree(d);
		myModel.put("subject", p);


		List<Competence> competences = serviceCompetence
				.getCompetencesForSubject(id_subject);


		if (competences != null)
			myModel.put("competences", competences);

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
			return "redirect:/degree/"+ id_degree + "/subject/" + id_subject + ".htm";
		} else
			return "redirect:/error.htm";
	}



	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}/addCompetences.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm(
			@PathVariable("subjectId") Long id_subject, Model model) {

		Subject s = serviceSubject.getSubject(id_subject);
		Collection<Competence> competences = serviceCompetence
				.getCompetencesForDegree(s.getDegree().getId());

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

		Subject aux = serviceSubject.getSubject(id_subject);
		
		subject.setId(id_subject);
		subject.setDegree(aux.getDegree());

		try {
			serviceSubject.modifySubject(subject);
			return "redirect:/degree/"+ id_degree + "/subject/" + id_subject + ".htm";
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
					logger.info("Loking up {} to {}", element,competence);


					return competence;
				}
				System.out.println("Don't know what to do with: "
						+ element);
				return null;
			}
		});
	}

}
