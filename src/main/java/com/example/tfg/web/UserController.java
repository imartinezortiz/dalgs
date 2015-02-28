package com.example.tfg.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.example.tfg.classes.ValidatorUtil;
import com.example.tfg.domain.User;
import com.example.tfg.service.GroupService;
import com.example.tfg.service.UserService;

@Controller
public class UserController {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService serviceUser;

	@Autowired
	private GroupService serviceGroup;

	/**
	 * Methods for list academic terms of a term
	 */
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
	
	@RequestMapping(value = "/user/{userId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewAUser(@PathVariable("userId") Long id_user)
			throws ServletException {
		
		Map<String, Object> myModel = new HashMap<String, Object>();

		User user = serviceUser.getUser(id_user);
		if (user != null){
		myModel.put("user", user);

		if(serviceUser.hasRole(user,"ROLE_PROFESSOR"))
			myModel.put("groups", serviceGroup.getGroupsForProfessor(id_user));
		else if(serviceUser.hasRole(user,"ROLE_STUDENT"))
			myModel.put("groups", serviceGroup.getGroupsForStudent(id_user));

	
		return new ModelAndView("user/view", "model", myModel); //Admin view
		}
		return new ModelAndView("error", "model", myModel);
	}
	
	/**
	 * Methods for add a single user
	 */
	@RequestMapping(value = "/user/add.htm", method = RequestMethod.GET)
	protected String getAddNewUserForm(Model model) {

		User user = new User();
		
		// 			Test
		
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_STUDENT");
		roles.add("ROLE_PROFESSOR");

		model.addAttribute("roles",roles);
		

		// ----------------------
		
		model.addAttribute("addUser", user);
		return "user/add";

	}

	@RequestMapping(value = "/user/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewUser(
			@ModelAttribute("addUser") @Valid User user,
			BindingResult result, Model model) {

	     ValidatorUtil validator = new ValidatorUtil();
		if (!validator.validateEmail(user.getEmail()))
			return "redirect://user/add.htm";

		if (!result.hasErrors()) {

			boolean created = serviceUser.addUser(user);
			if (created)
				return "redirect:/user/page/0.htm";
			else
				return "redirect:/user/add.htm";
		}
		return "redirect:/error.htm";
	}
	
	/** Method method to insert users massively*/
	
	@RequestMapping(value = "/upload/User.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString() ;

		model.addAttribute("className", "User");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("User"));
		return "upload";
	}
	
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

	
 	@RequestMapping(value = "/user/{userId}/status.htm", method = RequestMethod.GET)
	protected String disabledUser(@PathVariable("userId") Long id_user)
			throws ServletException {
 		
 		User user = serviceUser.getUser(id_user);
 		if(user.isEnabled())user.setEnabled(false);
 		else user.setEnabled(true);
 		
 		if (serviceUser.saveUser(user)) {
			return "redirect:/user/page/0.htm";
		} else
			return "redirect:/error.htm";
	}
 	
 
	

}
