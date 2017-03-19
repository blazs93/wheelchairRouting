package com.wheelchair.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.Route;
import com.wheelchair.db.model.Waypoint;
import com.wheelchair.db.repository.RouteRepository;
import com.wheelchair.db.repository.WaypointRepository;

@Controller
public class RouteController {

	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private WaypointRepository waypointRepository;
	
	
	@RequestMapping("/addRoute")
	public String addNewRoute(@RequestParam String accessible, @RequestParam Long waypoint1, @RequestParam Long waypoint2) {
		Route route = new Route();
		route.setAccessible(accessible);
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(waypointRepository.getOne(waypoint1));
		waypoints.add(waypointRepository.getOne(waypoint2));
		route.setWaypoints(waypoints);
		
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
