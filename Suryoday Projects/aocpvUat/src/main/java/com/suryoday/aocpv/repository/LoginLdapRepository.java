package com.suryoday.aocpv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.connector.pojo.User;

@Repository
public interface LoginLdapRepository extends JpaRepository<User, String> {

	// Optional<UserRoleMaster> getByUid(String userId);
	// Optional<User> findByUserId(String id);

}
