package com.wheelchair.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.geo.Point;

@Entity
@Table(name = "poi")
public class Poi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long poiId;
	
	private Point latLong;

	public Point getLatLong() {
		return latLong;
	}

	public void setLatLong(Point latLong) {
		this.latLong = latLong;
	}

}