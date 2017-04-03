package com.wheelchair.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "poi")
public class Poi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long poiId;
	private Double latitude;
	private Double longitude;
	private String description;
	private String acc;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccessible() {
		return acc;
	}

	public void setAccessible(String accessible) {
		this.acc = accessible;
	}

	public long getPoiId() {
		return poiId;
	}

}