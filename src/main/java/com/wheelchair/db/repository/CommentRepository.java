package com.wheelchair.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wheelchair.db.model.Comment;
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	@Query(value = "SELECT * FROM Comment c WHERE c.poi_id = ?1", 
			nativeQuery = true
	)
    public List<Comment> findComments(String poiId);
	
}
