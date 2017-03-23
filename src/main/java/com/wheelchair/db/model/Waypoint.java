package com.wheelchair.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "waypoint")
public class Waypoint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private Double latitude;
	private Double longitude;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;

	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Vertex toVertex() {
		 Vertex v = new Vertex(this.id, "Vertex of: " +this.id);
		 v.setLatitude(latitude);
		 v.setLongitude(longitude);
		 return v;
	}

}