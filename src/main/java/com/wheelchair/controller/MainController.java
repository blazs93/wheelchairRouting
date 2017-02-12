package com.wheelchair.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping({ "/index.html", "/" })
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
	}

}
