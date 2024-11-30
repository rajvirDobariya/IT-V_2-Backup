package com.suryoday.connector.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.connector.pojo.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
	
	public Optional<UserRole> findByUserName(String userName);

}
