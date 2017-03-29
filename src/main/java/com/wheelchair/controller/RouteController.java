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
		
		String accessibleValue = "";
		if(accessible.equalsIgnoreCase("bejárható")){
			accessibleValue = "accessible";
			route.setDistance(getDistance(waypoints.get(0), waypoints.get(1)));
		} else if (accessible.equalsIgnoreCase("nem bejárható")) {
			accessibleValue ="not accessible";
			route.setDistance(getDistance(waypoints.get(0), waypoints.get(1)) * 1000);
		} else {
			accessibleValue= "not defined";
			route.setDistance(getDistance(waypoints.get(0), waypoints.get(1)));
		}
		
		route.setAccessible(accessibleValue);

		routeRepository.save(route);
		
		Route route2 = new Route();
		route2.setAccessible(accessible);
		route2.setSourceId(waypoint2);
		route2.setDestinationId(waypoint1);
		if (accessible.equalsIgnoreCase("not accessible")) {
			route2.setDistance(getDistance(waypoints.get(1), waypoints.get(0)) * 1000);
		} else {
			route2.setDistance(getDistance(waypoints.get(1), waypoints.get(0)));
		}

		routeRepository.save(route2);

		return "redirect:/index.html";
	}

	//TODO nem jó integer!
	private Integer getDistance(Waypoint wp1, Waypoint wp2) {
		String unit = "K";
		double theta = wp1.getLongitude() - wp2.getLongitude();
		Integer dist = (int) (Math.sin(deg2rad(wp1.getLatitude())) * Math.sin(deg2rad(wp2.getLatitude()))
				+ Math.cos(deg2rad(wp1.getLatitude())) * Math.cos(deg2rad(wp2.getLatitude()))
						* Math.cos(deg2rad(theta)));
		dist = (int) Math.acos(dist);
		dist = (int) rad2deg(dist);
		dist = (int) (dist * 60 * 1.1515);
		if (unit == "K") {
			dist = (int) (dist * 1.609344);
		} else if (unit == "N") {
			dist = (int) (dist * 0.8684);
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

	@GetMapping(path = "/notAccessibleRoutes")
	public @ResponseBody Iterable<Route> getNotAccessibleRoutes() {
		return routeRepository.findNotAccessibleRoutes();
	}

	@RequestMapping("/routing")
	public @ResponseBody Iterable<Waypoint> routing(@RequestParam Long waypoint1, @RequestParam Long waypoint2) {
		Graph.vertexes.clear();
		List<Route> routes = routeRepository.findAll();
		Graph.Edge[]edges = new Graph.Edge[routes.size()];

		//Fontos az élekben a csúcsok sorrendje!! Az adatbázis növekvően tárolja, azért megfordulhatnak a csúcsok sorrendjei -> elcsesz mindent!!!
		
		for (int i = 0; i<routes.size(); i++) {
			Route r = routes.get(i);
			edges[i] = (new Graph.Edge(r.getSourceId().toString(), r.getDestinationId().toString(),
					r.getDistance()));
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
		for(String v : Graph.vertexes) {
			wps.add(waypointRepository.getOne(Long.parseLong(v)));
		}

		return wps;
	}
}
