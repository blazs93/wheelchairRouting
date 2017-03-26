package com.wheelchair.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.Poi;
import com.wheelchair.db.repository.PoiRepository;

@Controller
public class PoiController {

	@Autowired
	private PoiRepository poiRepository;
	
	
	@RequestMapping("/addPoi")
	public String addNewPoi(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam String description, @RequestParam String accessible) {
		Poi poi = new Poi();
		poi.setLatitude(latitude);
		poi.setLongitude(longitude);
		poi.setDescription(description);
		String accessibleValue = "";
		if(accessible.equalsIgnoreCase("bejárható")){
			accessibleValue = "accessible";
		} else if (accessible.equalsIgnoreCase("nem bejárható")) {
			accessibleValue ="not accessible";
		} else {
			accessibleValue= "not defined";
		}
		poi.setAccessible(accessibleValue);
		poiRepository.save(poi);
		
		return "redirect:/index.html";  
	}

	@GetMapping(path = "/allPoi")
	public @ResponseBody Iterable<Poi> getAllPois() {
		return poiRepository.findAll();
	}
	
	@GetMapping(path = "/getClosestPoi")
	public @ResponseBody Iterable<Poi> getClosestPoi(@RequestParam Double latitude, @RequestParam Double longitude) {
		return poiRepository.findClosest(latitude, longitude);
	}
}
