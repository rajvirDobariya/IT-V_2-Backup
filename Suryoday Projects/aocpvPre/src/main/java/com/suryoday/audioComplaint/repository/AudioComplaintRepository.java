package com.suryoday.audioComplaint.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.audioComplaint.entity.AudioComplaintDetails;

@Repository
public interface AudioComplaintRepository extends JpaRepository<AudioComplaintDetails, Long>{

	@Query(value = "SELECT a from AudioComplaintDetails a where a.userId =:userId and a.branchId =:branchId")
	List<AudioComplaintDetails> fetchByBranchId(String userId, String branchId);

	Optional<AudioComplaintDetails> getByComplaintNo(String complaintNo);

	@Query(value = "SELECT l from AudioComplaintDetails l where l.updatedDate between :s and :e order by l.updatedDate desc")
	List<AudioComplaintDetails> fetchByDate(@Param ("s") LocalDateTime startdate,@Param ("e") LocalDateTime enddate);

	@Query(value = "select SESSION_ID from SPRING_SESSION where SESSION_ID =:sessionid", nativeQuery = true)
	String getAllSessionIds(String sessionid);

	@Query(value = "select SESSION_PRIMARY_ID from SPRING_SESSION_ATTRIBUTES where ATTRIBUTE_NAME =:id", nativeQuery = true)
	List<String> fetchUser(String id);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM SPRING_SESSION  where PRIMARY_ID=:primaryID", nativeQuery = true)
	int deleteSession(String primaryID);

}
