package com.suryoday.mhl.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.CreateLead;

@Repository
public interface CreateLeadRepository extends JpaRepository<CreateLead, String> {

	Optional<CreateLead> getByApplicationNo(String applicationNo);

	@Query(value = "SELECT a from CreateLead a where a.branchId =:branchId and a.status =:status")
	List<CreateLead> findByBranchIdAndStatus(String branchId, String status);

	@Query(value = "SELECT a from CreateLead a where a.updatedDate between :startdate and :enddate")
	List<CreateLead> findByDate(LocalDateTime startdate, LocalDateTime enddate);

	@Query(value = "SELECT top 1 a.application_no from tbl_lead a order by a.application_no desc",nativeQuery = true)
	Optional<String> fetchLastApplicationNo();

	
	@Modifying
	@Transactional
	@Query(value = "update CreateLead a set a.status =:status where a.applicationNo =:applicationNo")
	void updateStatus(String applicationNo, String status);
	

}
