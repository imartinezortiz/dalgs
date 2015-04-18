package es.ucm.fdi.dalgs.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;

@RestController

public class WebhookController {
	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private LearningGoalService serviceLearningGoal;


	/*@ResponseBody annotation is used to map the response object in the response body. 
	 * Once the response object is returned by the handler method, 
	 * MappingJackson2HttpMessageConverter kicks in and convert it to JSON response.*/
	
	  @RequestMapping(value=RESTUriConstants.GET_ACTIVITY_DUMMY ,headers = {"Accept=text/xml, application/json"}, produces="application/json" ,method = RequestMethod.GET)
	  @ResponseBody 
	  public ActivityREST getDummy() {
	 
		  ActivityREST activity = new ActivityREST();
	      
	      activity.setId_course(1L);
	      activity.setName("Activity_name");
	      activity.setCreatedDate(new Date());
	      return activity;
	  }
	 
	  @RequestMapping(value=RESTUriConstants.GET_ACTIVITY ,headers = {"Accept=text/xml, application/json"}, produces="application/json" ,method = RequestMethod.GET)
	  @ResponseBody 
	  public ActivityREST get() {
	 
		  ActivityREST activity = new ActivityREST();
	      
	      activity.setId_course(1L);
	      activity.setName("Activity_name");
	      activity.setCreatedDate(new Date());
	      return activity;
	  }
	  
	 
	  @RequestMapping(value=RESTUriConstants.POST_ACTIVITY, headers="Accept=application/json", produces="application/json", method = RequestMethod.POST)
	  @ResponseBody 
	  public ActivityREST activity( @RequestBody  ActivityREST activity) {    
	      System.out.println( activity.getName());
	      return activity;
	  }

	
}