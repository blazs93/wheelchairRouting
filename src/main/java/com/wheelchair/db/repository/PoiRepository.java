package com.wheelchair.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.wheelchair.db.model.Poi;
import com.wheelchair.db.model.Route;


public interface PoiRepository extends JpaRepository<Poi, Long> {
	@Query(value = "SELECT poi_id, acc, description, latitude, longitude, 111.045 * DEGREES(ACOS(COS(RADIANS(?1)) " +
			"* COS(RADIANS(latitude)) " +
			"* COS(RADIANS(longitude) - RADIANS(?2)) " +
			"+ SIN(RADIANS(?1)) " +
			"* SIN(RADIANS(latitude)))) " +
			"AS distance_in_km " +
			"FROM Poi p " +
			"ORDER BY distance_in_km ASC LIMIT 0,1", 
			nativeQuery = true
	)
    public List<Poi> findClosest(Double lat, Double lng);
	
	@Modifying
	@Query("UPDATE Poi p set p.active = ?1 WHERE p.poiId = ?2")
	@Transactional
	void updatePoiActive(Boolean active, Long poiId);
	
	@Query(value = "SELECT * FROM Poi p WHERE p.active = 1", nativeQuery = true)
	public List<Poi> findActivePois();
}
