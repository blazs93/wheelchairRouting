package com.wheelchair.db.model;

import java.util.List;

public class RouteBean extends Route {
	
	
	private List<Waypoint> waypoints = null;
	
	
	public RouteBean(Route r){
		setAccessible(r.getAccessible());
		setDestinationId(r.getDestinationId());
		setDistance(r.getDistance());
		setSourceId(r.getSourceId());
		setId(r.getId());
		setActive(r.getActive());
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

}
