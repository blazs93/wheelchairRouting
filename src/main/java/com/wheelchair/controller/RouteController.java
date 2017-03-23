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
//import com.wheelchair.dijkstra.DijkstraAlgorithm;
//import com.wheelchair.dijkstra.Graph;

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
		if (accessible.equalsIgnoreCase("not accessible")) {
			route.setDistance(getDistance(waypoints.get(0), waypoints.get(1))*1000);
		} else {
			route.setDistance(getDistance(waypoints.get(0), waypoints.get(1)));
		}
		
		routeRepository.save(route);
		
		return "redirect:/index.html";
	}
	
	
	private Double getDistance(Waypoint wp1, Waypoint wp2) {
		String unit = "K";
		double theta = wp1.getLongitude() - wp2.getLongitude();
		double dist = Math.sin(deg2rad(wp1.getLatitude())) * Math.sin(deg2rad(wp2.getLatitude())) + Math.cos(deg2rad(wp1.getLatitude())) * Math.cos(deg2rad(wp2.getLatitude())) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
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
	public @ResponseBody Iterable<Waypoint> routing(@RequestParam Long waypoint1, @RequestParam Long waypoint2) {
		/*List<Route> routes = routeRepository.findAll();
		List<Waypoint> waypoints = waypointRepository.findAll();
		
		Waypoint wp1 = waypointRepository.getOne(waypoint1);
		Waypoint wp2 = waypointRepository.getOne(waypoint2);
		
		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(waypoints, routes);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		
		//from
		//dijkstra.execute(waypoints.get(0));
		dijkstra.execute(wp1);
		//to
		//LinkedList<Waypoint> path = dijkstra.getPath(waypoints.get(1));
		LinkedList<Waypoint> path = dijkstra.getPath(wp2);
		for (Waypoint vertex : path) {
			System.out.println(vertex.getId());
		}
		
		return path;*/
		Waypoint source = waypointRepository.getOne(waypoint1);
		Waypoint destination = waypointRepository.getOne(waypoint2);
		//testExcute(wp1, wp2);
		List<Waypoint> waypoints = waypointRepository.findAll();
		List<Route> routes = routeRepository.findAll();
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	
		for(Waypoint wp: waypoints) {
			nodes.add(wp.toVertex());
		}
		
		for(Route r : routes) {
			Edge e = r.toEdge();
			addLane(e.getId(), e.getSource(), e.getDestination(), e.getWeight());
		}
		
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		//TODO:
		dijkstra.execute(source.toVertex());
		LinkedList<Vertex> path = dijkstra.getPath(destination.toVertex());
		List<Waypoint> waypointPath = new ArrayList<Waypoint>();
		
		for (Vertex vertex : path) {
			System.out.println(vertex);
			waypointPath.add(vertex.toWaypoint());
		}
		
		
		return waypointPath;
	}
	
	static List<Vertex> nodes;
	static List<Edge> edges;
	
	private void testExcute(Waypoint source, Waypoint destination) {
		List<Waypoint> waypoints = waypointRepository.findAll();
		List<Route> routes = routeRepository.findAll();
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	
		for(Waypoint wp: waypoints) {
			nodes.add(wp.toVertex());
		}
		
		for(Route r : routes) {
			Edge e = r.toEdge();
			addLane(e.getId(), e.getSource(), e.getDestination(), e.getWeight());
		}
		
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		//TODO:
		dijkstra.execute(source.toVertex());
		LinkedList<Vertex> path = dijkstra.getPath(destination.toVertex());

		for (Vertex vertex : path) {
			System.out.println(vertex);
		}

	}

	private void addLane(String laneId, Vertex sourceLocNo, Vertex destLocNo, double duration) {
		Edge lane = new Edge(laneId, sourceLocNo, destLocNo, duration);
		edges.add(lane);
	}
}
