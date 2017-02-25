package com.wheelchair.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wheelchair.db.model.Route;


public interface RouteRepository extends JpaRepository<Route, Long> {
	
}
