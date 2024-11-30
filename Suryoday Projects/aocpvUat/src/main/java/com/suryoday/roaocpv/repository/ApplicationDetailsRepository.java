package com.suryoday.roaocpv.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.roaocpv.pojo.ApplicationDetailList;
import com.suryoday.roaocpv.pojo.ApplicationDetails;

@Repository
public interface ApplicationDetailsRepository extends JpaRepository<ApplicationDetails, Long> {

	@Query(value = "SELECT a.appNoWithProductCode from ApplicationDetails a where a.applicationId =:applicationId")
	Optional<String> fetchByapplicationId(String applicationId);

	@Modifying
	@Transactional
	@Query(value = "update ApplicationDetails set flowStatus =:flowStatus , status =:status1 where applicationId =:applicationId")
	void updateflowStatus(String applicationId, String flowStatus, String status1);

	@Query(value = "SELECT new com.suryoday.roaocpv.pojo.ApplicationDetailList(a.applicationId, a.name, a.mobileNo ,a.status ,a.requiredAmount ,a.appNoWithProductCode) from ApplicationDetails a where a.status =:status and a.branchCode =:branchId order by a.updateDatets desc")
	List<ApplicationDetailList> fetchByStatusAndBranchId(String status, String branchId);

	Optional<ApplicationDetails> getByApplicationId(String applicationId);

	@Query(value = "SELECT a from ApplicationDetails a where a.status =:status and a.branchCode =:branchId and a.updatedDate between :startdate and :enddate order by a.updateDatets desc")
	List<ApplicationDetails> fetchByDate(String status, String branchId, LocalDate startdate, LocalDate enddate);

	@Query(value = "SELECT top 1 a.application_id from tbl_application_details a order by a.application_id desc", nativeQuery = true)
	Optional<String> fetchLastApplicationNo();

	public Optional<ApplicationDetails> findByApplicationId(String applicationId);

	@Query(value = "SELECT a from ApplicationDetails a where a.mobileNo =:mobile")
	Optional<ApplicationDetails> fetchByMobileNo(String mobile);

	Optional<ApplicationDetails> getByAadharNumber(String referenceNo);

	Optional<ApplicationDetails> getByPanCard(String panCardNo);

	Optional<ApplicationDetails> getByLeadId(String lead_ID);

	@Query(value = "SELECT a from ApplicationDetails a where a.breStatus =:status and a.updatedDate between :startdate and :enddate order by a.updateDatets desc")
	List<ApplicationDetails> getByBreStatus(String status, LocalDate startdate, LocalDate enddate);

	@Query(value = "SELECT a from ApplicationDetails a where a.breStatus =:status and a.branchCode =:branchId and a.updatedDate between :startdate and :enddate order by a.updateDatets desc")
	List<ApplicationDetails> getByBreStatus(String status, String branchId, LocalDate startdate, LocalDate enddate);

}
