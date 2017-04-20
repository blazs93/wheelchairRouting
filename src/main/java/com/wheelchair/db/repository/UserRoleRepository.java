package com.wheelchair.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wheelchair.db.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
