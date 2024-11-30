package com.suryoday.hastakshar.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.hastakshar.pojo.HastUserName;

public interface UserNameRepo extends JpaRepository<HastUserName, Integer> {

	@Query("SELECT a FROM HastUserName a WHERE a.empId =:empId")
	List<HastUserName> fetchNameByEmpid(String empId);

	// User Session Patch
	@Query(value = "select SESSION_PRIMARY_ID from SPRING_SESSION_ATTRIBUTES where ATTRIBUTE_NAME =:id", nativeQuery = true)
	List<String> fetchUser(String id);

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	String getAllSessionIds(String sessionid);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM SPRING_SESSION  where PRIMARY_ID=:primaryID", nativeQuery = true)
	int deleteSession(String primaryID);
}
