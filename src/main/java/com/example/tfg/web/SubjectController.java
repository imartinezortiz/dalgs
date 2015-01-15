package com.example.tfg.web;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@ModelAttribute("degrees")
	public List<Degree> degrees() {
		return serviceDegree.getAll();
	}

	@ModelAttribute("aTerms")
	public List<AcademicTerm> academicTerms() {
		return serviceAcademicTerm.getAll();
	}

	private static final Logger logger = LoggerFactory
			.getLogger(SubjectController.class);

	/**
	 * Methods for adding subjects
	 */

	/**
	 * Methods for manage subjects of a degree
	 */

	@RequestMapping(value="/degree/{degreeId}/subject/{subjectId}/delete.htm",method=RequestMethod.GET)
	public String formDeleteSubjectFromDegree(@PathVariable("degreeId") Long id_degree,@PathVariable("subjectId") long id_subject)
			throws ServletException {

		if (serviceSubject.deleteSubject(id_subject)){
			return "redirect:/degree/"+id_degree+".htm";
		}
		else return "redirect:/error.htm";
	}

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
	
	@RequestMapping(value= "/degree/{degreeId}/subject/{subjectId}/modify.htm",method=RequestMethod.POST)	
	public String formModifySubjectFromDegree(@PathVariable("degreeId") Long id_degree,@PathVariable("subjectId") Long id_subject, @ModelAttribute("modifySubject")Subject modify)

    {
		modify.setDegree(serviceDegree.getDegree(id_degree));
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

//	@RequestMapping(value= "/degree/{degreeId}/subject/{subjectId}/modify.htm",method=RequestMethod.POST)	
//	public String formModifySubject(@PathVariable("subjectId") Long id,
//				@ModelAttribute("modifySubject") Subject modify,
//				BindingResult result, Model model)
//	
//		{
//			if(modify.getDegree() == null)
//				return "redirect:/subject/modifyChoose/"+id+".htm";
//			
//			if (!result.hasErrors()) {
//				modify.setId(id);
//				boolean success = serviceSubject.modifySubject(modify);
//				if (success)
//					return "redirect:/subject/list.htm";
//			}
//	
//			return "redirect:/error.htm";
//	
//		}
//
//	@RequestMapping(value="/degree/{degreeId}/subject/{subjectId}/modify.htm",method=RequestMethod.GET)
//	protected String formModifySubject(@PathVariable("degreeId") Long id_degree, @PathVariable("subjectId") Long id_subject,
//				Model model) throws ServletException {
//			Subject p = serviceSubject.getSubject(id_subject);
//			model.addAttribute("idDegree",id_degree);
//	
//	
//			model.addAttribute("modifySubject", p);
//			return "subject/modifyChoose";
//
//	
//	
//	}
//	
	
	//
	//	return "redirect:/error.htm";
	//	@RequestMapping(value = "/subject/add.htm", method = RequestMethod.GET)
	//	protected String getAddNewSubjectForm(Model model) {
	//		Subject newSubject = new Subject();
	//		newSubject.setCode(serviceSubject.getNextCode());
	//		model.addAttribute("addsubject", newSubject);
	//		return "subject/addChoose";
	//	}
	//
	//	@RequestMapping(value = "/subject/add.htm", method = RequestMethod.POST)
	//	// Every Post have to return redirect
	//	public String processAddNewSubject(
	//			@ModelAttribute("addsubject") Subject newSubject,
	//			BindingResult result, Model model) {
	//
	//		if(newSubject.getDegree() == null)
	//			return "redirect://subject/add.htm";
	//		
	//		if (!result.hasErrors()) {
	//			boolean created = serviceSubject.addSubject(newSubject);
	//
	//			if (created)
	//				return "redirect:/subject/list.htm";
	//			else
	//				return "redirect:/subject/add.htm";
	//		}
	//		return "redirect:/error.htm";
	//	}
	//
	//	
	//	/**
	//	 * Methods for listing subjects
	//	 */
	//
	//	@RequestMapping(value = "/subject/list.htm")
	//	public ModelAndView handleRequestSubjectList(HttpServletRequest request,
	//			HttpServletResponse response) throws ServletException, IOException {
	//
	//		String now = (new Date()).toString();
	//		logger.info("Returning hello view with " + now);
	//
	//		Map<String, Object> myModel = new HashMap<String, Object>();
	//		myModel.put("now", now);
	//
	//		List<Subject> result = serviceSubject.getAll();
	//		myModel.put("subjects", result);
	//
	//		return new ModelAndView("subject/list", "model", myModel);
	//	}
	//
	//	/**
	//	 * Methods for modify subjects
	//	 */
	//
	//	@RequestMapping(value = "/subject/modifyChoose/{subjectId}.htm", method = RequestMethod.POST)
	//	public String formModifySubject(@PathVariable("subjectId") long id,
	//			@ModelAttribute("modifySubject") Subject modify,
	//			BindingResult result, Model model)
	//
	//	{
	//		if(modify.getDegree() == null)
	//			return "redirect:/subject/modifyChoose/"+id+".htm";
	//		
	//		if (!result.hasErrors()) {
	//			modify.setId(id);
	//			boolean success = serviceSubject.modifySubject(modify);
	//			if (success)
	//				return "redirect:/subject/list.htm";
	//		}
	//
	//		return "redirect:/error.htm";
	//
	//	}
	//
	//	@RequestMapping(value = "/subject/modifyChoose/{subjectId}.htm", method = RequestMethod.GET)
	//	protected String formModifySubjects(@PathVariable("subjectId") long id,
	//			Model model) throws ServletException {
	//		Subject p = serviceSubject.getSubject(id);
	//		model.addAttribute("idDegree",p.getDegree().getId());
	//		//model.addAttribute("idAcademicTerm",p.getAcademicTerm().getId());
	//		p.setDegree(null);
	//		//p.setAcademicTerm(null);
	//
	//		model.addAttribute("modifySubject", p);
	//		return "subject/modifyChoose";
	//
	//	}
	//
	//	/**
	//	 * Methods for delete subjects
	//	 */
	//
	//	@RequestMapping(value = "/subject/delete/{subjectId}.htm", method = RequestMethod.GET)
	//	public String formDeleteSubjects(@PathVariable("subjectId") long id)
	//			throws ServletException {
	//
	//		if (serviceSubject.deleteSubject(id)) {
	//			return "redirect:/subject/list.htm";
	//		} else
	//			return "redirect:/error.htm";
	//
	//	}

	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/degree/{degreeId}/subject/{subjectId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewSubject(@PathVariable("subjectId") long id)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Subject p = serviceSubject.getSubject(id);
		Degree d = serviceDegree.getDegreeSubject(p);
		p.setDegree(d);
		myModel.put("subject", p);


		List<Competence> competences = serviceCompetence
				.getCompetencesForSubject(id);


		if (competences != null)
			myModel.put("competences", competences);

		return new ModelAndView("subject/view", "model", myModel);
	}

	/**
	 * Method for manage competences of a subject
	 */

	@RequestMapping(value = "/subject/competence/delete/{subjectId}/{competenceId}.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceFromSubject(
			@PathVariable("subjectId") long id_subject,
			@PathVariable("competenceId") long id_competence)
					throws ServletException {

		if (serviceCompetence.deleteCompetenceFromSubject(id_competence,
				id_subject)) {
			return "redirect:/subject/view/" + id_subject + ".htm";
		} else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/subject/competence/modify/{subjectId}/{competenceId}.htm", method = RequestMethod.POST)
	public String formModifyCompetenceFromSubject(
			@PathVariable("subjectId") long id_subject,
			@PathVariable("competenceId") long id_competence,
			@ModelAttribute("modifyCompetence") Competence modify)

	{

		Competence aux = serviceCompetence.getCompetence(id_competence);
		aux.setName(modify.getName());
		aux.setDescription(modify.getDescription());
		serviceCompetence.modifyCompetence(aux);
		return "redirect:/subject/view/" + id_subject + ".htm";
	}

	@RequestMapping(value = "/subject/competence/modify/{subjectId}/{competenceId}.htm", method = RequestMethod.GET)
	protected ModelAndView formModifyCompetenceFromSubject(
			@PathVariable("subjectId") long id_subject,
			@PathVariable("competenceId") long id_competence)
					throws ServletException {
		ModelAndView model = new ModelAndView();
		Competence p = serviceCompetence.getCompetence(id_competence);
		model.addObject("modifyCompetence", p);
		model.setViewName("/competence/modify");

		return model;
	}

	/**
	 * Methods for manage activities of a subject
	 */

	/*
	@RequestMapping(value = "/subject/activity/delete/{subjectId}/{activityId}.htm", method = RequestMethod.GET)
	public String formDeleteActivityFromSubject(
			@PathVariable("subjectId") long id_subject,
			@PathVariable("activityId") long id_activity)
			throws ServletException {

		if (serviceActivity.deleteActivity(id_activity)) {
			return "redirect:/subject/view/" + id_subject + ".htm";
		} else
			return "redirect:/error.htm";
	}
	 */

	/*
	@RequestMapping(value = "/subject/activity/modify/{subjectId}/{activityId}.htm", method = RequestMethod.POST)
	public String formModifyActivityFromSubject(
			@PathVariable("subjectId") long id_subject,
			@PathVariable("activityId") long id_activity,
			@ModelAttribute("modifyActivity") Activity modify)

	{
		modify.setId(id_activity);
		modify.setSubject(serviceSubject.getSubject(id_subject));
		serviceActivity.modifyActivity(modify);
		return "redirect:/subject/view/" + id_subject + ".htm";
	}

	@RequestMapping(value = "/subject/activity/modify/{subjectId}/{activityId}.htm", method = RequestMethod.GET)
	protected ModelAndView formModifyActivityFromSubject(
			@PathVariable("subjectId") long id_subject,
			@PathVariable("activityId") long id_activity)
			throws ServletException {
		ModelAndView model = new ModelAndView();
		Activity p = serviceActivity.getActivity(id_activity);
		model.addObject("modifyActivity", p);
		model.setViewName("/activity/modify");

		return model;
	}
	 */
	@RequestMapping(value = "/subject/addCompetences/{subjectId}.htm", method = RequestMethod.GET)
	protected String getAddNewCompetenceForm(
			@PathVariable("subjectId") long id_subject, Model model) {

		Subject s = serviceSubject.getSubject(id_subject);
		Collection<Competence> competences = serviceCompetence
				.getCompetencesForDegree(s.getDegree().getId());

		model.addAttribute("subject", s);
		model.addAttribute("competences", competences);
		//model.addAttribute("activities", s.getActivities());

		return "subject/addCompetences";
	}

	@RequestMapping(value = "/subject/addCompetences/{subjectId}.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@PathVariable("subjectId") long id_subject,
			@ModelAttribute("subject") Subject subject, BindingResult result,
			Model model) {

		Subject aux = serviceSubject.getSubject(id_subject);
		//subject.setActivities(aux.getActivities());
		subject.setId(id_subject);
		subject.setDegree(aux.getDegree());

		try {
			serviceSubject.modifySubject(subject);// getSubject(id_subject).getCompetences().addAll(competences);
			return "redirect:/subject/view/" + id_subject + ".htm";
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
