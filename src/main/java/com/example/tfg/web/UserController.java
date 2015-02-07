package com.example.tfg.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.classes.CharsetString;
import com.example.tfg.classes.UploadForm;

import com.example.tfg.domain.User;
import com.example.tfg.service.UserService;

@Controller
public class UserController {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService serviceUser;


	/**
	 * Methods for list academic terms of a term
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/user/page/{pageIndex}.htm")
	protected ModelAndView formViewAUsers(
			@PathVariable("pageIndex") Integer pageIndex)
			throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		List<User> p = serviceUser.getAll(pageIndex);

		myModel.put("users", p);
		myModel.put("numberOfPages", serviceUser.numberOfPages());
		myModel.put("currentPage", pageIndex);

		return new ModelAndView("user/list", "model", myModel);
	}
	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@RequestMapping(value = "/user/{userId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewAUser(@PathVariable("userId") Long id_user)
			throws ServletException {
		
		Map<String, Object> myModel = new HashMap<String, Object>();

		User user = serviceUser.getUser(id_user);
		myModel.put("user", user);

		myModel.put("courses", user.getCourses());

		return new ModelAndView("user/view", "model", myModel); //Admin view
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/upload/User.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString() ;

		model.addAttribute("className", "User");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("User"));
		return "upload";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/upload/User.htm", method = RequestMethod.POST)
	public String uploadPost(@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult result, Model model) {

		if (result.hasErrors() || upload.getCharset().isEmpty()) {
			for (ObjectError error : result.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}

		 if (serviceUser.uploadCVS(upload)) return "home";
		 else return "upload";
	}

	

}
