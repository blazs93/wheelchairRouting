package com.wheelchair.db.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "route")
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String acc;
	private double distance;
	@ElementCollection
	private List<Waypoint> waypoints;

	public String getAccessible() {
		return acc;
	}

	public void setAccessible(String accessible) {
		this.acc = accessible;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public Edge toEdge(){
		Edge edge = new Edge(this.getId()+"", waypoints.get(0).toVertex(), waypoints.get(1).toVertex(), distance);
		return edge;
	}

	private long getId() {
		return id;
	}
	
	public Waypoint getSource(){
		return waypoints.get(0);
	}
	
	public Waypoint getDestination(){
		return waypoints.get(1);
	}
}