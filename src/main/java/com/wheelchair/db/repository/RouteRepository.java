package com.wheelchair.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wheelchair.db.model.Route;


public interface RouteRepository extends JpaRepository<Route, Long> {
	
	/*@Query("SELECT points FROM Route r WHERE r.acc = 'Not accessible'")
	public List<Route> findNotAccessibleRoutes();*/
	
}
