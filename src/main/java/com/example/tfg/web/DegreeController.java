package com.example.tfg.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.SubjectService;

@Controller
public class DegreeController {

	@Autowired
	private DegreeService serviceDegree;
	
	@Autowired
	private SubjectService serviceSubject;
	
	@Autowired
	private CompetenceService serviceCompetence;
	
	@Autowired 
	private AcademicTermService serviceAcademicTerm;
	
	private static final Logger logger = LoggerFactory.getLogger(DegreeController.class);
	
	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.GET)
	protected String getAddNewDegreeForm(Model model) {
		Degree newDegree = new Degree();
		newDegree.setCode(serviceDegree.getNextCode());

		model.addAttribute("addDegree", newDegree);
		return "degree/add";
	}
	
	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewDegree(
			@ModelAttribute("addDegree") Degree newDegree) {
		boolean created = serviceDegree.addDegree(newDegree);
		if (created)
			return "redirect:/degree/list.htm";
		else
			return "redirect:/degree/add.htm";
	}
	
	/**
	 * Methods for listing degrees
	 */

	@RequestMapping(value = "/degree/list.htm")
	public ModelAndView handleRequestDegreeList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String now = (new Date()).toString();
		logger.info("Returning hello view with " + now);

		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("now", now);
		
		List<Degree> result = serviceDegree.getAll();
		myModel.put("degrees", result);


		return new ModelAndView("degree/list", "model", myModel);
	}
	
	
	/**
	 * Methods for modify degrees
	 */
	@RequestMapping(value= "/degree/{degreeId}/modify.htm",method=RequestMethod.POST)	
	public String formModifyDegree(@PathVariable("degreeId") long id, @ModelAttribute("modifyDegree")Degree modify)

    {
        
        modify.setId(id);
        serviceDegree.modifyDegree(modify);
        return "redirect:/degree/list.htm";
    }
	
	@RequestMapping(value="/degree/{degreeId}/modify.htm",method=RequestMethod.GET)
    protected ModelAndView formModifyDegrees(@PathVariable("degreeId") long id)
            throws ServletException {
	  	ModelAndView model = new ModelAndView();
    	Degree p= serviceDegree.getDegree(id);
    	model.addObject("modifyDegree",p);
    	model.setViewName("degree/modify");
    	
    	
    	return model;
    }
	
	/**
	 * Methods for delete degrees
	 */

	@RequestMapping(value="/degree/delete/{degreeId}.htm",method=RequestMethod.GET)
	public String formDeleteDegrees(@PathVariable("degreeId") long id)
            throws ServletException {

	
		if (serviceDegree.deleteDegree(id)){
			return "redirect:/degree/list.htm";
		}
		else return "redirect:/error.htm";
	}

	/**
	 * Methods for view degrees
	 */
	@RequestMapping(value="/degree/{degreeId}.htm",method=RequestMethod.GET)
    protected ModelAndView formViewDegree(@PathVariable("degreeId") long id)
            throws ServletException {
		
		Map<String, Object> myModel = new HashMap<String, Object>();

    	Degree p= serviceDegree.getDegree(id);
    	myModel.put("degree",p);
    	
    	
    	
    	List<Subject> subjects = serviceSubject.getSubjectsForDegree(id);
    	List<Competence> competences =  serviceCompetence.getCompetencesForDegree(id);
    	
    	
    	if(subjects != null) myModel.put("subjects",subjects);
    	if(competences != null) myModel.put("competences", competences);
    	
    	
    
    	
    	
    	return new ModelAndView("degree/view", "model", myModel);
    }
//	/**
//	 * Method for manage competences of a degree
//	 */
//	
//	@RequestMapping(value="/degree/competence/delete/{degreeId}/{competenceId}.htm",method=RequestMethod.GET)
//	public String formDeleteCompetenceFromDegree(@PathVariable("degreeId") long id_degree,@PathVariable("competenceId") long id_competence)
//            throws ServletException {
//
//		if (serviceCompetence.deleteCompetence(id_competence)){
//			return "redirect:/degree/view/"+id_degree+".htm";
//		}
//		else return "redirect:/error.htm";
//	}
//	
//	@RequestMapping(value= "/degree/competence/modify/{degreeId}/{competenceId}.htm",method=RequestMethod.POST)	
//	public String formModifyCompetenceFromDegree(@PathVariable("degreeId") long id_degree,@PathVariable("competenceId") long id_competence, @ModelAttribute("modifyCompetence")Competence modify)
//
//    {      
//        serviceCompetence.modifyCompetence(modify);
//        return "redirect:/degree/view/"+id_degree+".htm";
//    }
//	
//	
//	@RequestMapping(value = "/degree/competence/add/{degreeId}.htm", method = RequestMethod.GET)
//	protected String getAddNewCompetenceForm2(Model model,
//			@PathVariable("degreeId") long id) {
//		Competence newCompetence = new Competence();
//		newCompetence.setCode(serviceCompetence.getNextCode());
//
//		Degree d = serviceDegree.getDegree(id);
//
//		newCompetence.setDegree(d);
//		model.addAttribute("addcompetence", newCompetence);
//		return "competence/add";
//	}
//
//	@RequestMapping(value = "/degree/competence/add/{degreeId}.htm", method = RequestMethod.POST)
//	// Every Post have to return redirect
//	public String processAddNewCompetence2(
//			@ModelAttribute("addcompetence") Competence newCompetence,
//			@PathVariable("degreeId") long id) {
//		Degree degree = serviceDegree.getDegree(id);
//
//		newCompetence.setDegree(degree);
//		boolean created = serviceCompetence.addCompetence(newCompetence);
//		if (created)
//			return "redirect:/degree/view/" + id + ".htm";
//		else
//			return "redirect:/error.htm";
//	}
	

	
//	@RequestMapping(value= "/degree/subject/modify/{degreeId}/{subjectId}.htm",method=RequestMethod.POST)	
//	public String formModifySubjectFromDegree(@PathVariable("degreeId") long id_degree,@PathVariable("subjectId") long id_subject, @ModelAttribute("modifySubject")Subject modify)
//
//    {
//		modify.setDegree(serviceDegree.getDegree(id_degree));
//        serviceSubject.modifySubject(modify);
//        return "redirect:/degree/view/"+id_degree+".htm";
//    }
//	
//	
//	@RequestMapping(value="/degree/subject/modify/{degreeId}/{subjectId}.htm",method=RequestMethod.GET)
//    protected ModelAndView formModifySubjectFromDegree(@PathVariable("degreeId") long id_degree, @PathVariable("subjectId") long id_subject)
//            throws ServletException {
//	  	ModelAndView model = new ModelAndView();
//    	Subject p= serviceSubject.getSubject(id_subject);
//    	model.addObject("modifySubject",p);
//    	model.setViewName("/subject/modify");
//    	
//    	return model;
//    }
    
	
}
