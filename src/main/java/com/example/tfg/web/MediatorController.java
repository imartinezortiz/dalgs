package com.example.tfg.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.User;
import com.example.tfg.service.UserService;

@Controller
public class MediatorController {

	private static final Logger logger = LoggerFactory
			.getLogger(MediatorController.class);

	@Autowired
	private UserService serviceUser;
	
	@RequestMapping(value = "/user.htm")
	public ModelAndView getUserPage(Model model) {
		Map<String, Object> myModel = new HashMap<String, Object>();
		
		String username =  SecurityContextHolder.getContext().getAuthentication().getName();
		User u = serviceUser.findByUsername(username);
		logger.info(username + "  " + u.getEmail());
		myModel.put("userDetails", u);

		return new ModelAndView("user/user", "model", myModel);
	}

	@RequestMapping(value = "/admin.htm")
	public ModelAndView getAdminPage(Model model) {
		Map<String, Object> myModel = new HashMap<String, Object>();

		String username =  SecurityContextHolder.getContext().getAuthentication().getName();
		User u = serviceUser.findByUsername(username);
		logger.info(username + "  " + u.getEmail());
		model.addAttribute("userDetails", u);
		return new ModelAndView("user/admin", "model", myModel);
	}
}