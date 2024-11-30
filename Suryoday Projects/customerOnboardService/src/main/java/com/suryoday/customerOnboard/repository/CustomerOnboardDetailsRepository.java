package com.suryoday.customerOnboard.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.customerOnboard.entity.CustomerOnboardDetails;
@Repository
public interface CustomerOnboardDetailsRepository extends JpaRepository<CustomerOnboardDetails, Long> {

	@Query(value = "SELECT a from CustomerOnboardDetails a where a.applicationNo =:applicationNo")
	Optional<CustomerOnboardDetails> getByAapplicationNo(long applicationNo);

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	String getAllSessionIds(String sessionid);

	@Query(value = "select SESSION_PRIMARY_ID from SPRING_SESSION_ATTRIBUTES where ATTRIBUTE_NAME =:id", nativeQuery = true)	
	List<String> fetchUser(String id);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM SPRING_SESSION  where PRIMARY_ID=:primaryID", nativeQuery = true)
	int deleteSession(String primaryID);

}
