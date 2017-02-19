package com.wheelchair.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.wheelchair.db.model.User;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT username, active FROM User u")
    public List<User> findUsers();
	
	@Modifying
	@Query("UPDATE User u set u.active = ?1 WHERE u.username = ?2")
	@Transactional
	void updateUserActive(Boolean active, String username);
}
