package com.wheelchair.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wheelchair.db.model.Route;


public interface RouteRepository extends JpaRepository<Route, Long> {
	
	@Query(value = "SELECT * FROM Route r WHERE r.acc = 'Not accessible'", nativeQuery = true)
	public List<Route> findNotAccessibleRoutes();
	
	@Query(value = "SELECT route_route_id WHERE waypoints_waypoint_id = ?1 " +
			"FROM route_waypoints ",
			nativeQuery = true
	)
	public Long findRoute(Long waypointId);
	
}
