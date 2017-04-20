package com.wheelchair.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	/*@RequestMapping({ "/index.html", "/" })
	public String redirect() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().toString();
		if(role.equalsIgnoreCase("[ROLE_ADMIN]")){
			role = "admin.html";
		}
		else if(role.equalsIgnoreCase("[ROLE_USER]")){
			role = "user.html";
		}else{
			role = "anonym.html";
		}

		return "redirect:" + role;
	}*/
	
	@GetMapping(path = "/getAuth")
	public @ResponseBody String getAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().toString();
		return role;
	}
	
	@GetMapping(path = "/getLogin")
	public @ResponseBody String getLogin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		return login;
	}
}
