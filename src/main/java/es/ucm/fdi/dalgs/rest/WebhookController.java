package es.ucm.fdi.dalgs.rest;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;






import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WebhookController {

	/*@ResponseBody annotation is used to map the response object in the response body. 
	 * Once the response object is returned by the handler method, 
	 * MappingJackson2HttpMessageConverter kicks in and convert it to JSON response.*/
	
	  @RequestMapping(value=RESTUriConstants.GET_ACTIVITY_DUMMY ,headers = {"Accept=text/xml, application/json"}, produces="application/json" ,method = RequestMethod.GET)
	  @ResponseBody 
	  public ActivityREST get() {
	 
		  ActivityREST activity = new ActivityREST();
	      
	      activity.setId_course(1L);
	      activity.setName("Activity_name");
	      activity.setCreatedDate(new Date());
	      return activity;
	  }
	 
	  //this method response to POST request http://localhost/spring-mvc-json/rest/cont/person
	  // receives json data sent by client --> map it to Person object
	  // return Person object as json
//	  @RequestMapping(value=RESTUriConstants.POST_ACTIVITY,   headers ="Accept:*/*", method = RequestMethod.POST)
	  @RequestMapping(value=RESTUriConstants.POST_ACTIVITY, headers="Accept=application/json", method = RequestMethod.POST)
	  @ResponseBody
	  public ActivityREST activity( ActivityREST activity) {    
	 
	      System.out.println( activity.getName());
	      return activity;
	  }

	
}