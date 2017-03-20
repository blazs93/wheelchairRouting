package com.wheelchair.dijkstra;

import java.util.List;

import com.wheelchair.db.model.Route;
import com.wheelchair.db.model.Waypoint;

public class Graph {
    private final List<Waypoint> vertexes;
    private final List<Route> edges;

    public Graph(List<Waypoint> vertexes, List<Route> edges) {
            this.vertexes = vertexes;
            this.edges = edges;
    }

    public List<Waypoint> getVertexes() {
            return vertexes;
    }

    public List<Route> getEdges() {
            return edges;
    }



}
