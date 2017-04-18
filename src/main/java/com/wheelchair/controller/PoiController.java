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
	public String addNewPoi(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam String description, @RequestParam String accessible, @RequestParam String name) {
		Poi poi = new Poi();
		poi.setLatitude(latitude);
		poi.setLongitude(longitude);
		poi.setDescription(description);
		poi.setName(name);
		poi.setActive(false);
		poi.setAccessible(accessible);
		poiRepository.save(poi);
		
		return "redirect:/index.html";  
	}

	@GetMapping(path = "/allPoi")
	public @ResponseBody Iterable<Poi> getAllPois() {
		return poiRepository.findAll();
	}
	
	@GetMapping(path = "/allActivePoi")
	public @ResponseBody Iterable<Poi> getAllActivePois() {
		return poiRepository.findActivePois();
	}
	
	@GetMapping(path = "/getClosestPoi")
	public @ResponseBody Iterable<Poi> getClosestPoi(@RequestParam Double latitude, @RequestParam Double longitude) {
		return poiRepository.findClosest(latitude, longitude);
	}
	
	@RequestMapping("/deletePoi")
	public String deletePoi(@RequestParam Long id) {
		poiRepository.delete(poiRepository.getOne(id));
		
		return "redirect:/admin.html";  
	}
	
	@RequestMapping("/activatePoi")
	public String activatePoi(@RequestParam Boolean active, @RequestParam Long poiId) {
		poiRepository.updatePoiActive(active, poiId);
		return "redirect:/admin.html";  
	}
	
	@RequestMapping("/updatePoi")
	public String updatePoi(@RequestParam Long poiId, @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam String description, @RequestParam String accessible, @RequestParam String name) {
		Poi p = poiRepository.getOne(poiId);
		p.setAccessible(accessible);
		p.setDescription(description);
		p.setLatitude(latitude);
		p.setLongitude(longitude);
		p.setName(name);
		poiRepository.save(p);
		return "redirect:/";
	}
	
	@RequestMapping("/updatePoiAdmin")
	public String updatePoiAdmin(@RequestParam Long poiId, @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam String description, @RequestParam String accessible, @RequestParam String name) {
		Poi p = poiRepository.getOne(poiId);
		p.setAccessible(accessible);
		p.setDescription(description);
		p.setLatitude(latitude);
		p.setLongitude(longitude);
		p.setName(name);
		poiRepository.save(p);
		return "redirect:/admin.html";
	}
}
