package es.ucm.fdi.dalgs.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController {

	private static final Logger logger = LoggerFactory
			.getLogger(MainController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		model.addAttribute("class", "User");
		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/error.htm", method = RequestMethod.GET)
	public String error() {
		return "error";
	}

	@RequestMapping(value = "/pageNotFound.htm")
	public String handlePageNotFound() {
		// do something
		return "exception/notFound";
	}
	
	 @ExceptionHandler
	 @ResponseBody
	 @ResponseStatus(value= HttpStatus.BAD_REQUEST)
	 public ModelAndView handleException(Exception exception){
		ModelAndView model = new ModelAndView("error");
		model.addObject("reason", exception.getMessage());
		return model;
		  
	 }
	

}
