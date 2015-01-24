package com.example.tfg.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */

	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
    @RequestMapping(value = "/tfg/home", method = RequestMethod.GET)
    public String home2(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
         
        String formattedDate = dateFormat.format(date);
         
        model.addAttribute("serverTime", formattedDate );
         
        return "home";
    }
	@RequestMapping(value = "/error.htm", method = RequestMethod.GET)
	public String error() {
		return "error";
	}
	
	 @RequestMapping(value = "/emp/get/{id}", method = RequestMethod.GET)
	    public String getEmployee(Locale locale, Model model,@PathVariable("id") int id) {
	        logger.info("Welcome user! Requested Emp ID is: "+id);
	        Date date = new Date();
	        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
	         
	        String formattedDate = dateFormat.format(date);
	         
	        model.addAttribute("serverTime", formattedDate );
	        model.addAttribute("id", id);
	        model.addAttribute("name", "Pankaj");
	         
	        return "employee";
	    }
	     
	    @RequestMapping(value="/login")
	    public String login(HttpServletRequest request, Model model){
	        return "login";
	    }
	     
	    @RequestMapping(value="/logout")
	    public String logout(){
	        return "logout";
	    }
	     
	    @RequestMapping(value="/denied")
	    public String denied(){
	        return "denied";
	    }
}
