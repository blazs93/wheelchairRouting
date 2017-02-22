package com.wheelchair.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wheelchair.db.model.Poi;


public interface PoiRepository extends JpaRepository<Poi, Long> {
	
}
