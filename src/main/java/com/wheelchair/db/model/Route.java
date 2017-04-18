package com.wheelchair.db.model;

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
	private Integer distance;
	/*@ElementCollection
	private List<Waypoint> waypoints;*/
	private Long sourceId;
	private Long destinationId;
	private Boolean active;

	public String getAccessible() {
		return acc;
	}

	public void setAccessible(String accessible) {
		this.acc = accessible;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	public Long getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(long destinationId) {
		this.destinationId = destinationId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}