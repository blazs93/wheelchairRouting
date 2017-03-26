package com.wheelchair.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.Edge;
import com.wheelchair.db.model.Route;
import com.wheelchair.db.model.RouteBean;
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
		/*
		 * List<Route> routes = routeRepository.findAll(); List<Waypoint>
		 * waypoints = waypointRepository.findAll();
		 * 
		 * Waypoint wp1 = waypointRepository.getOne(waypoint1); Waypoint wp2 =
		 * waypointRepository.getOne(waypoint2);
		 * 
		 * // Lets check from location Loc_1 to Loc_10 Graph graph = new
		 * Graph(waypoints, routes); DijkstraAlgorithm dijkstra = new
		 * DijkstraAlgorithm(graph);
		 * 
		 * //from //dijkstra.execute(waypoints.get(0)); dijkstra.execute(wp1);
		 * //to //LinkedList<Waypoint> path =
		 * dijkstra.getPath(waypoints.get(1)); LinkedList<Waypoint> path =
		 * dijkstra.getPath(wp2); for (Waypoint vertex : path) {
		 * System.out.println(vertex.getId()); }
		 * 
		 * return path;
		 */

		//// OLD
		/*
		 * Waypoint source = waypointRepository.getOne(waypoint1); Waypoint
		 * destination = waypointRepository.getOne(waypoint2); List<Waypoint>
		 * waypoints = waypointRepository.findAll(); List<Route> routes =
		 * routeRepository.findAll(); nodes = new ArrayList<Vertex>(); edges =
		 * new ArrayList<Edge>();
		 * 
		 * for (Waypoint wp : waypoints) { nodes.add(wp.toVertex()); }
		 * 
		 * for (Route r : routes) { Edge e = r.toEdge(); addLane(e.getId(),
		 * e.getSource(), e.getDestination(), e.getWeight()); }
		 * 
		 * // Graph graph = new Graph(nodes, edges); // DijkstraAlgorithm
		 * dijkstra = new DijkstraAlgorithm(graph); // TODO: //
		 * dijkstra.execute(source.toVertex()); // LinkedList<Vertex> path =
		 * dijkstra.getPath(destination.toVertex()); List<Waypoint> waypointPath
		 * = new ArrayList<Waypoint>();
		 * 
		 * // for (Vertex vertex : path) { // System.out.println(vertex); //
		 * waypointPath.add(vertex.toWaypoint()); // }
		 * 
		 * return waypointPath;
		 */
		vertexes.clear();
		List<Route> routes = routeRepository.findAll();
		Graph.Edge[]test = new Graph.Edge[routes.size()];

		//Fontos az élekben a csúcsok sorrendje!! Az adatbázis növekvően tárolja, azért megfordulhatnak a csúcsok sorrendjei -> elcsesz mindent!!!
		//Köszi 2 sör!!!
		
		
		//test
		/*Graph.Edge[] GRAPH = { 
				new Graph.Edge("1", "2", 631), 
				new Graph.Edge("1", "3", 6), 
				new Graph.Edge("3", "4", 6),
				new Graph.Edge("4", "2", 6), 
		};*/

		for (int i = 0; i<routes.size(); i++) {
			Route r = routes.get(i);
			test[i] = (new Graph.Edge(r.getSourceId().toString(), r.getDestinationId().toString(),
					r.getDistance()));
		}

		Waypoint source = waypointRepository.getOne(waypoint1);
		Waypoint destination = waypointRepository.getOne(waypoint2);

		String START = source.getId().toString();
		String END = destination.getId().toString();

		//String START = "2";
		//String END = "1";

		Graph g = new Graph(test);
		g.dijkstra(START);
		g.printPath(END);
		// g.printAllPaths();
		List<Waypoint> wps = new ArrayList<Waypoint>();
		for(String v : vertexes) {
			wps.add(waypointRepository.getOne(Long.parseLong(v)));
		}

		return wps;

	}

	static List<String> vertexes = new ArrayList<String>();
	static List<Vertex> nodes;
	static List<Edge> edges;

	private void addLane(String laneId, Vertex sourceLocNo, Vertex destLocNo, double duration) {
		Edge lane = new Edge(laneId, sourceLocNo, destLocNo, duration);
		edges.add(lane);
	}

	static class Graph {
		private final Map<String, Vertex> graph; // mapping of vertex names to
													// Vertex objects, built
													// from a set of Edges

		/** One edge of the graph (only used by Graph constructor) */
		public static class Edge {
			public final String v1, v2;
			public final int dist;

			public Edge(String v1, String v2, int dist) {
				this.v1 = v1;
				this.v2 = v2;
				this.dist = dist;
			}
		}

		/**
		 * One vertex of the graph, complete with mappings to neighbouring
		 * vertices
		 */
		public static class Vertex implements Comparable<Vertex> {
			public final String name;
			public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be
													// infinity
			public Vertex previous = null;
			public final Map<Vertex, Integer> neighbours = new HashMap<>();

			public Vertex(String name) {
				this.name = name;
			}

			private void printPath() {
				if (this == this.previous) {
					System.out.printf("%s", this.name);
					vertexes.add(this.name);
				} else if (this.previous == null) {
					System.out.printf("%s(unreached)", this.name);
				} else {
					this.previous.printPath();
					System.out.printf(" -> %s(%d)", this.name, this.dist);
					vertexes.add(this.name);
				}
			}

			public int compareTo(Vertex other) {
				if (dist == other.dist)
					return name.compareTo(other.name);

				return Integer.compare(dist, other.dist);
			}

			@Override
			public String toString() {
				return "(" + name + ", " + dist + ")";
			}
		}

		/** Builds a graph from a set of edges */
		public Graph(Edge[] edges) {
			graph = new HashMap<>(edges.length);

			// one pass to find all vertices
			for (Edge e : edges) {
				if (!graph.containsKey(e.v1))
					graph.put(e.v1, new Vertex(e.v1));
				if (!graph.containsKey(e.v2))
					graph.put(e.v2, new Vertex(e.v2));
			}

			// another pass to set neighbouring vertices
			for (Edge e : edges) {
				graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
				// graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); //
				// also do this for an undirected graph
			}
		}

		public Graph(List<Edge> edges) {
			graph = new HashMap<>(edges.size());

			// one pass to find all vertices
			for (Edge e : edges) {
				if (!graph.containsKey(e.v1))
					graph.put(e.v1, new Vertex(e.v1));
				if (!graph.containsKey(e.v2))
					graph.put(e.v2, new Vertex(e.v2));
			}

			// another pass to set neighbouring vertices
			for (Edge e : edges) {
				graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
				// graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); //
				// also do this for an undirected graph
			}
		}

		/** Runs dijkstra using a specified source vertex */
		public void dijkstra(String startName) {
			if (!graph.containsKey(startName)) {
				System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
				return;
			}
			final Vertex source = graph.get(startName);
			NavigableSet<Vertex> q = new TreeSet<>();

			// set-up vertices
			for (Vertex v : graph.values()) {
				v.previous = v == source ? source : null;
				v.dist = v == source ? 0 : Integer.MAX_VALUE;
				q.add(v);
			}

			dijkstra(q);
		}

		/** Implementation of dijkstra's algorithm using a binary heap. */
		private void dijkstra(final NavigableSet<Vertex> q) {
			Vertex u, v;
			while (!q.isEmpty()) {

				u = q.pollFirst(); // vertex with shortest distance (first
									// iteration will return source)
				if (u.dist == Integer.MAX_VALUE)
					break; // we can ignore u (and any other remaining vertices)
							// since they are unreachable

				// look at distances to each neighbour
				for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
					v = a.getKey(); // the neighbour in this iteration

					final int alternateDist = u.dist + a.getValue();
					if (alternateDist < v.dist) { // shorter path to neighbour
													// found
						q.remove(v);
						v.dist = alternateDist;
						v.previous = u;
						q.add(v);
					}
				}
			}
		}

		/** Prints a path from the source to the specified vertex */
		public void printPath(String endName) {
			if (!graph.containsKey(endName)) {
				System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
				return;
			}

			graph.get(endName).printPath();
			System.out.println();
		}

		/**
		 * Prints the path from the source to every vertex (output order is not
		 * guaranteed)
		 */
		public void printAllPaths() {
			for (Vertex v : graph.values()) {
				v.printPath();
				System.out.println();
			}
		}
	}

}
