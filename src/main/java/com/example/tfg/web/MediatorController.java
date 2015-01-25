package com.example.tfg.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller

public class MediatorController {
 
	
	
	@RequestMapping(value="/user.htm")
	public String getUserPage() {
		return "user";
	}
	
	@RequestMapping(value="/admin.htm")
	public String getAdminPage() {
		return "admin";
	}
}