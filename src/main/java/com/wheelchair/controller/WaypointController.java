package com.wheelchair.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.Waypoint;
import com.wheelchair.db.repository.WaypointRepository;

@Controller
public class WaypointController {

	@Autowired
	private WaypointRepository waypointRepository;
	
	
	@RequestMapping("/addWaypoint")
	public String addNewPoi(@RequestParam Double waypointLatitude, @RequestParam Double waypointLongitude) {
		Waypoint wp = new Waypoint();
		wp.setLatitude(waypointLatitude);
		wp.setLongitude(waypointLongitude);
		waypointRepository.save(wp);
		
		return "redirect:/index.html";  
	}

	@GetMapping(path = "/allWaypoint")
	public @ResponseBody Iterable<Waypoint> getAllWaypoint() {
		return waypointRepository.findAll();
	}
	
	@GetMapping(path = "/getClosestWaypoint")
	public @ResponseBody Iterable<Waypoint> getClosestWp(@RequestParam Double latitude, @RequestParam Double longitude) {
		return waypointRepository.findClosest(latitude, longitude);
	}
}
