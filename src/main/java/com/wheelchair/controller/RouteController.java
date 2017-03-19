package com.wheelchair.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.DijkstraAlgorithm;
import com.wheelchair.db.model.Edge;
import com.wheelchair.db.model.Graph;
import com.wheelchair.db.model.Route;
import com.wheelchair.db.model.Vertex;
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
	
	@RequestMapping("/routing")
	public String routing(@RequestParam Long waypoint1, @RequestParam Long waypoint2) {
		testExcute();
		return "ok";
	}
	
	static List<Vertex> nodes;
	static List<Edge> edges;
	
	private void testExcute() {
		List<Route> routes = routeRepository.findAll();
		List<Waypoint> waypoints = waypointRepository.findAll();
		
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		for(Waypoint wp : waypoints) {
			nodes.add(wp.toVertex());
		}
		for(Route r : routes){
			edges.add(r.toEdge());
		}

		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		
		//from
		dijkstra.execute(nodes.get(0));
		//to
		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(1));

		for (Vertex vertex : path) {
			System.out.println(vertex);
		}

	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);
	}
	
}
