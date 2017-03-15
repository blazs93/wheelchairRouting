package com.wheelchair.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wheelchair.db.model.Waypoint;


public interface WaypointRepository extends JpaRepository<Waypoint, Long> {
	@Query(value = "SELECT p.waypoint_id, p.latitude, p.longitude, 111.045 * DEGREES(ACOS(COS(RADIANS(?1)) " +
			"* COS(RADIANS(p.latitude)) " +
			"* COS(RADIANS(p.longitude) - RADIANS(?2)) " +
			"+ SIN(RADIANS(?1)) " +
			"* SIN(RADIANS(p.latitude)))) " +
			"AS distance_in_km " +
			"FROM Waypoint p " +
			"ORDER BY distance_in_km ASC LIMIT 0,1", 
			nativeQuery = true
	)
    public List<Waypoint> findClosest(Double lat, Double lng);
}
