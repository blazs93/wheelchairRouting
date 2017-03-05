package com.wheelchair.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.Route;
import com.wheelchair.db.repository.RouteRepository;

@Controller
public class RouteController {

	@Autowired
	private RouteRepository routeRepository;
	
	
	@RequestMapping("/addRoute")
	public String addNewRoute(@RequestParam String description, @RequestParam String accessible, @RequestParam String jsonRoute) {
		Route route = new Route();
		route.setDescription(description);
		route.setAccessible(accessible);
		route.setPoints(jsonRoute);
		
		routeRepository.save(route);
		
		return "redirect:/index.html";  
	}

	@GetMapping(path = "/allRoutes")
	public @ResponseBody Iterable<Route> getAllRoutes() {
		return routeRepository.findAll();
	}
	
	@GetMapping(path = "/notAccessibleRoutes")
	public @ResponseBody Iterable<Route> getNotAccessibleRoutes() {
		return routeRepository.findNotAccessibleRoutes();
	}
}
