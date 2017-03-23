package com.wheelchair.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wheelchair.db.model.Route;
import com.wheelchair.db.model.Waypoint;

public class DijkstraAlgorithm {

    private final List<Waypoint> nodes;
    private final List<Route> edges;
    private Set<Waypoint> settledNodes;
    private Set<Waypoint> unSettledNodes;
    private Map<Waypoint, Waypoint> predecessors;
    private Map<Waypoint, Double> distance;

    public DijkstraAlgorithm(Graph graph) {
            // create a copy of the array so that we can operate on this array
            this.nodes = new ArrayList<Waypoint>(graph.getVertexes());
            this.edges = new ArrayList<Route>(graph.getEdges());
    }

    public void execute(Waypoint source) {
            settledNodes = new HashSet<Waypoint>();
            unSettledNodes = new HashSet<Waypoint>();
            distance = new HashMap<Waypoint, Double>();
            predecessors = new HashMap<Waypoint, Waypoint>();
            distance.put(source, 0d);
            unSettledNodes.add(source);
            while (unSettledNodes.size() > 0) {
                    Waypoint node = getMinimum(unSettledNodes);
                    settledNodes.add(node);
                    unSettledNodes.remove(node);
                    findMinimalDistances(node);
            }
    }

    private void findMinimalDistances(Waypoint node) {
            List<Waypoint> adjacentNodes = getNeighbors(node);
            for (Waypoint target : adjacentNodes) {
                    if (getShortestDistance(target) > getShortestDistance(node)
                                    + getDistance(node, target)) {
                            distance.put(target, getShortestDistance(node)
                                            + getDistance(node, target));
                            predecessors.put(target, node);
                            unSettledNodes.add(target);
                    }
            }

    }

    private double getDistance(Waypoint node, Waypoint target) {
           /* for (Route edge : edges) {
                    if (edge.getSource().equals(node)
                                    && edge.getDestination().equals(target)) {
                            return edge.getDistance();
                    }
            }
            throw new RuntimeException("Should not happen");*/
    	return 0;
    }

    private List<Waypoint> getNeighbors(Waypoint node) {
          /*  List<Waypoint> neighbors = new ArrayList<Waypoint>();
            for (Route edge : edges) {
                    if (edge.getSource().equals(node)
                                    && !isSettled(edge.getDestination())) {
                            neighbors.add(edge.getDestination());
                    }
            }
            return neighbors;*/
            return null;
    }

    private Waypoint getMinimum(Set<Waypoint> vertexes) {
            Waypoint minimum = null;
            for (Waypoint vertex : vertexes) {
                    if (minimum == null) {
                            minimum = vertex;
                    } else {
                            if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                                    minimum = vertex;
                            }
                    }
            }
            return minimum;
    }

    private boolean isSettled(Waypoint vertex) {
            return settledNodes.contains(vertex);
    }

    private double getShortestDistance(Waypoint destination) {
            Double d = distance.get(destination);
            if (d == null) {
                    return Integer.MAX_VALUE;
            } else {
                    return d;
            }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Waypoint> getPath(Waypoint target) {
            LinkedList<Waypoint> path = new LinkedList<Waypoint>();
            Waypoint step = target;
            // check if a path exists
            if (predecessors.get(step) == null) {
                    return null;
            }
            path.add(step);
            while (predecessors.get(step) != null) {
                    step = predecessors.get(step);
                    path.add(step);
            }
            // Put it into the correct order
            Collections.reverse(path);
            return path;
    }

}
