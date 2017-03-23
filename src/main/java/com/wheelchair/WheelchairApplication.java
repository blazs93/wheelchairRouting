package com.wheelchair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wheelchair.db.model.DijkstraAlgorithm;
import com.wheelchair.db.model.Edge;
import com.wheelchair.db.model.Graph;
import com.wheelchair.db.model.Vertex;

@SpringBootApplication
public class WheelchairApplication {

	static List<Vertex> nodes;
	static List<Edge> edges;

	public static void main(String[] args) {
		SpringApplication.run(WheelchairApplication.class, args);
		
		//testExcute();

	}

	public static void testExcute() {
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		for (long i = 0; i < 6; i++) {
			Vertex location = new Vertex(i, "Node_" + i);
			nodes.add(location);
		}

		addLane("Edge_0", 0, 1, 5);
		addLane("Edge_1", 1, 3, 20);
		addLane("Edge_2", 3, 4, 5);
		addLane("Edge_3", 4, 2, 5);
		addLane("Edge_3", 2, 5, 5);
		addLane("Edge_5", 1, 2, 1);
		

		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(nodes.get(0));
		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(5));

		// assertNotNull(path);
		// assertTrue(path.size() > 0);

		for (Vertex vertex : path) {
			System.out.println(vertex);
		}

	}

	static void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);
	}
}
