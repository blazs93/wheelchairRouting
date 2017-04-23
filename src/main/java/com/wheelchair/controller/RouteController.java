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
import com.wheelchair.db.model.RouteBean;
import com.wheelchair.db.model.Waypoint;
import com.wheelchair.db.repository.RouteRepository;
import com.wheelchair.db.repository.WaypointRepository;
import com.wheelchair.dijkstra.Graph;

@Controller
public class RouteController {
	
	private static String ACCESSIBLE = "bejárható";
	private static String NOT_ACCESSIBLE = "nem bejárható";
	private static String NOT_DEFINED = "nincs meghatározva";

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private WaypointRepository waypointRepository;

	@RequestMapping("/addRoute")
	public String addNewRoute(@RequestParam String accessible, @RequestParam Long waypoint1,
			@RequestParam Long waypoint2) {
		Route route = new Route();
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(waypointRepository.getOne(waypoint1));
		waypoints.add(waypointRepository.getOne(waypoint2));

		route.setSourceId(waypoint1);
		route.setDestinationId(waypoint2);
		route.setAccessible(accessible);
		route.setActive(false);

		//Route route2 = new Route();
		//route2.setAccessible(accessible);
		//route2.setSourceId(waypoint2);
		//route2.setDestinationId(waypoint1);

		if (accessible.equalsIgnoreCase("nem bejárható")) {

			route.setDistance(getDistance(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude(),
					waypoints.get(1).getLatitude(), waypoints.get(1).getLongitude()) * 1000);

			//route2.setDistance(getDistance(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude(),
			//		waypoints.get(1).getLatitude(), waypoints.get(1).getLongitude()) * 1000);
		} else {

			route.setDistance(getDistance(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude(),
					waypoints.get(1).getLatitude(), waypoints.get(1).getLongitude()));
			//route2.setDistance(getDistance(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude(),
			//		waypoints.get(1).getLatitude(), waypoints.get(1).getLongitude()));
		}

		routeRepository.save(route);
		//routeRepository.save(route2);

		return "redirect:/index.html";
	}

	private static Integer getDistance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344 * 1000;

		return (int) dist;
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	@GetMapping(path = "/allRoutes")
	public @ResponseBody Iterable<RouteBean> getAllRoutes() {
		List<RouteBean> routeBeans = new ArrayList<RouteBean>();
		List<Route> routes = routeRepository.findAll();
		for (Route r : routes) {
			List<Waypoint> wps = new ArrayList<Waypoint>();
			wps.add(waypointRepository.getOne(r.getSourceId()));
			wps.add(waypointRepository.getOne(r.getDestinationId()));
			RouteBean rb = new RouteBean(r);
			rb.setWaypoints(wps);
			routeBeans.add(rb);
		}
		return routeBeans;
	}
	
	@GetMapping(path = "/allActiveRoutes")
	public @ResponseBody Iterable<RouteBean> getAllActiveRoutes() {
		List<RouteBean> routeBeans = new ArrayList<RouteBean>();
		List<Route> routes = routeRepository.findActiveRoutes();
		for (Route r : routes) {
			List<Waypoint> wps = new ArrayList<Waypoint>();
			wps.add(waypointRepository.getOne(r.getSourceId()));
			wps.add(waypointRepository.getOne(r.getDestinationId()));
			RouteBean rb = new RouteBean(r);
			rb.setWaypoints(wps);
			routeBeans.add(rb);
		}
		return routeBeans;
	}
	
	@RequestMapping("/activateRoute")
	public String activateRoute(@RequestParam Boolean active, @RequestParam Long id) {
		routeRepository.updateRouteActive(active, id);
		return "redirect:/admin.html";  
	}
	
	@RequestMapping("/deleteRoute")
	public String deleteRoute(@RequestParam Long id) {
		routeRepository.delete(id);
		return "redirect:/admin.html";  
	}
	
	@RequestMapping("/updateRoute")
	public String updateRoute(@RequestParam Long routeId, @RequestParam String routeAccessible) {
		Route r = routeRepository.findOne(routeId);
		if ((routeAccessible.equalsIgnoreCase(ACCESSIBLE) || routeAccessible.equalsIgnoreCase(NOT_DEFINED)) && r.getAccessible().equalsIgnoreCase(NOT_ACCESSIBLE)) {
			r.setDistance(r.getDistance()/1000);
		}
		
		if (routeAccessible.equalsIgnoreCase(NOT_ACCESSIBLE)  && (routeAccessible.equalsIgnoreCase(NOT_DEFINED) || r.getAccessible().equalsIgnoreCase(ACCESSIBLE))) {
			r.setDistance(r.getDistance()*1000);
		}
		r.setAccessible(routeAccessible);
		routeRepository.save(r);
		return "redirect:/admin.html";  
	}
	
	@RequestMapping("/updateRouteAdmin")
	public String updateRouteAdmin(@RequestParam Long routeId, @RequestParam String routeAccessible) {
		Route r = routeRepository.findOne(routeId);
		if ((routeAccessible.equalsIgnoreCase(ACCESSIBLE) || routeAccessible.equalsIgnoreCase(NOT_DEFINED)) && r.getAccessible().equalsIgnoreCase(NOT_ACCESSIBLE)) {
			r.setDistance(r.getDistance()/1000);
		}
		
		if (routeAccessible.equalsIgnoreCase(NOT_ACCESSIBLE)  && (routeAccessible.equalsIgnoreCase(NOT_DEFINED) || r.getAccessible().equalsIgnoreCase(ACCESSIBLE))) {
			r.setDistance(r.getDistance()*1000);
		}
		r.setAccessible(routeAccessible);
		routeRepository.save(r);
		return "redirect:/";  
	}

	@GetMapping(path = "/notAccessibleRoutes")
	public @ResponseBody Iterable<Route> getNotAccessibleRoutes() {
		return routeRepository.findNotAccessibleRoutes();
	}

	@RequestMapping("/routing")
	public @ResponseBody Iterable<Waypoint> routing(@RequestParam Long waypoint1, @RequestParam Long waypoint2) {
		Graph.vertexes.clear();
		List<Route> routes = routeRepository.findActiveRoutes();
		List<Graph.Edge> edges = new ArrayList<Graph.Edge>();

		for (int i = 0; i < routes.size(); i++) {
			Route r = routes.get(i);
			edges.add(new Graph.Edge(r.getSourceId().toString(), r.getDestinationId().toString(), r.getDistance()));
			//edges.add(new Graph.Edge(r.getDestinationId().toString(), r.getSourceId().toString(), r.getDistance()));
		}

		Waypoint source = waypointRepository.getOne(waypoint1);
		Waypoint destination = waypointRepository.getOne(waypoint2);

		String START = source.getId().toString();
		String END = destination.getId().toString();

		Graph g = new Graph(edges);
		g.dijkstra(START);
		g.printPath(END);
		// g.printAllPaths();
		List<Waypoint> wps = new ArrayList<Waypoint>();
		for (String v : Graph.vertexes) {
			wps.add(waypointRepository.getOne(Long.parseLong(v)));
		}

		return wps;
	}
}
