package com.wheelchair.db.repository;

import org.springframework.data.repository.CrudRepository;

import com.wheelchair.db.model.UserRole;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

}
