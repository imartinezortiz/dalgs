package com.example.tfg.web;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.CompetenceStatus;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Subject;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.SubjectService;

@Controller
public class ActivityController {

	// @Autowired
	// private ConversionService conversionService;
	// Autowiring the ConversionService we declared in the context file above.
	/*
	 * @InitBinder public void registerConversionServices(WebDataBinder
	 * dataBinder) { dataBinder.setConversionService(conversionService);
	 * 
	 * }
	 */

	@Autowired
	private ActivityService serviceActivity;

	//@Autowired
	//private SubjectService serviceSubject;

	@Autowired
	private CourseService serviceCourse;
	
	@Autowired
	private CompetenceService serviceCompetence; 
	
	private static final Logger logger = LoggerFactory
			.getLogger(ActivityController.class);

	

	@ModelAttribute("competences")
	public List<Competence> competences() {
		return serviceCompetence.getAll();
	}
	
	@ModelAttribute("courses")
	public List<Course> courses() {
		return serviceCourse.getAll();
	}
	
	@ModelAttribute("competenceStatus")
	public List<CompetenceStatus> competencestatus() {
		return (List<CompetenceStatus>) col;
	}
	
	
	
	private List<CompetenceStatus> col = new ArrayList<CompetenceStatus>();
	/**
	 * Methods for adding activities
	 */
	@RequestMapping(value = "/activity/add.htm", method = RequestMethod.GET)
	protected String getAddNewActivityForm(Model model) {
		Activity newActivity = new Activity();
		newActivity.setCompetenceStatus(new ArrayList<CompetenceStatus>());
		newActivity.setCode(serviceActivity.getNextCode());
		newActivity.setCourse(new Course());
		model.addAttribute("addactivity", newActivity);	

		CompetenceStatus cs = new CompetenceStatus();
		model.addAttribute("addcompetencestatus", cs);
	
	
		return "activity/addChoose";

	}

	@RequestMapping(value = "/activity/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewCompetence(
			@ModelAttribute("addactivity") @Valid Activity newactivity, BindingResult result, 
			@ModelAttribute("addcompetencestatus") @Valid CompetenceStatus competencestatus, BindingResult result2,
			@ModelAttribute("course") @Valid Course course, BindingResult result3, 
			Model model) {
		
		if(!result2.hasErrors() && competencestatus.getId_competence() != 0){
			if( competencestatus.getPercentage() <= 0.0 || competencestatus.getPercentage() > 100.0)		
				return "redirect:/activity/add.htm";
				
			for(CompetenceStatus cs: col){
					if (cs.getId_competence() == competencestatus.getId_competence()){
						return "redirect:/activity/add.htm";
					}
			}			
			
			col.add(competencestatus);
			return "redirect:/activity/add.htm";
			
			// return "redirect:/activity/add.htm";

			//newactivity.getCompetenceStatus().add(competencestatus);  //CASCA AQUI!!!
			//model.addAttribute("addactivity", newactivity);
			//return "redirect:/activity/add.htm";
		}
		
		if(course == null)
			return "redirect:/activity/add.htm";
		
		if (!result.hasErrors()) {
			newactivity.setCourse(course);
			newactivity.setCompetenceStatus(col);
			boolean created = serviceActivity.addActivity(newactivity);
			if (created){
				col = new ArrayList<CompetenceStatus>();
				return "redirect:/activity/list.htm";
			}
			else
				return "redirect:/activity/add.htm";
		}
		return "redirect:/error.htm";
	}



	/**
	 * Methods for listing activities
	 */

	@RequestMapping(value = "/activity/list.htm")
	public ModelAndView handleRequestActivityList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String now = (new Date()).toString();
		logger.info("Returning hello view with " + now);

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<Activity> result = serviceActivity.getAll();
		myModel.put("activities", result);

		return new ModelAndView("activity/list", "model", myModel);
	}

	/**
	 * Methods for modifying activities
	 */
	
	@RequestMapping(value = "/activity/modifyChoose/{activityId}.htm", method = RequestMethod.GET)
	protected String formModifyActivities(@PathVariable("activityId") long id,
			Model model) throws ServletException {
		boolean contains = false;
		Activity p = serviceActivity.getActivity(id);
		if(p.getCourse()!= null)
			model.addAttribute("idCourse",p.getCourse().getId());
		p.setCourse(null);

		model.addAttribute("modifyactivity", p);
	
		CompetenceStatus cs = new CompetenceStatus();
		model.addAttribute("addcompetencestatus", cs);
	
		
		for (CompetenceStatus coms : p.getCompetenceStatus())
			for (CompetenceStatus coms2 : col)	
				if (coms.getId_competence() == coms2.getId_competence() && coms.getPercentage() == coms2.getPercentage()){
					contains = true;
					break;
				}
		if (!contains)		
				col.addAll(p.getCompetenceStatus());
		//model.addAttribute("col", p.getCompetenceStatus());
		return "activity/modifyChoose";
	}
	
	@RequestMapping(value = "/activity/modifyChoose/{activityId}.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("activityId") long id,
			@ModelAttribute("modifyactivity") @Valid Activity modify,BindingResult result, 
			@ModelAttribute("addcompetencestatus") @Valid CompetenceStatus competencestatus, BindingResult result2,
			@ModelAttribute("course") @Valid Course course, BindingResult result3,
			Model model)

	{
		if(!result2.hasErrors() && competencestatus.getId_competence() != 0){
			if( competencestatus.getPercentage() <= 0.0 || competencestatus.getPercentage() > 100.0)		
				return "redirect:/activity/modifyChoose/"+id+".htm";
				
			for(CompetenceStatus cs: col){
					if (cs.getId_competence() == competencestatus.getId_competence()){
						return "redirect:/activity/modifyChoose/"+id+".htm";
					}
			}
					
			col.add(competencestatus);
			return "redirect:/activity/modifyChoose/"+id+".htm";
			

		}
		

		//modify.setSubject(serviceSubject.getSubject(modify.getSubject().getId()));
		if(modify.getCourse() == null)
			return "redirect:/activity/modifyChoose/"+id+".htm";
		
		if (!result.hasErrors()) {
			modify.setId(id);
			modify.setCompetenceStatus(col);
			boolean success = serviceActivity.modifyActivity(modify);
			if (success){
				col = new ArrayList<CompetenceStatus>();
				return "redirect:/activity/view/"+id+".htm";
			}
		}
		return "redirect:/error.htm";

	}


	/**
	 * Method for delete an activities
	 */
	
	@RequestMapping(value = "/activity/delete/{activityId}.htm", method = RequestMethod.GET)
	public String formDeleteActivity(@PathVariable("activityId") long id)
			throws ServletException {

		if (serviceActivity.deleteActivity(id)) {
			return "redirect:/activity/list.htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Method for delete an competence status of anactivities
	 */
	
	@RequestMapping(value = "/activity/competenceStatus/delete/{activityId}/{compStatusId}.htm", method = RequestMethod.GET)
	public String formDeleteCompetenceStatusActivity(@PathVariable("activityId") long id_Activity,@PathVariable("compStatusId") long id_competenceStatus)
			throws ServletException {

		if (serviceActivity.deleteCompetenceActivity(id_competenceStatus, id_Activity)) {
			return "redirect:/activity/view/"+ id_Activity+".htm";
		} else
			return "redirect:/error.htm";
	}



	/**
	 * Methods for view subjects
	 */
	@RequestMapping(value = "/activity/view/{activityId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewActivity(@PathVariable("activityId") long id)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();


		Activity a = serviceActivity.getActivity(id);

		model.put("activity", a);
		model.put("activityId", id);
		
		model.put("competenceStatus", a.getCompetenceStatus());
		
		List<CompetenceStatus> comps = (List<CompetenceStatus>) a.getCompetenceStatus(); //La competencia de competenceStatus llega null
	
		//model.put("competences", a.)
		
		return new ModelAndView("activity/view", "model", model);
	}
	

	public Collection<CompetenceStatus> getCol() {
		return col;
	}

	public void setCol(List<CompetenceStatus> col) {
		this.col = col;
	}
	
	/**
	 * For binding the courses of the activity
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Set.class, "courses",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						if (element instanceof Course) {
							logger.info("Converting...{}", element);
							return element;
						}
						if (element instanceof String) {
							Course course = serviceCourse.getCourseByName(element.toString());
								
							logger.info("Loking up {} to {}", element,course);

						
							return course;
						}
						System.out.println("Don't know what to do with: "
								+ element);
						return null;
					}
				});
	}
}