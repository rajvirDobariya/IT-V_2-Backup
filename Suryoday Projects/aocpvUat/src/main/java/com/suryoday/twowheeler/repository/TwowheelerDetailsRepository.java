package com.suryoday.twowheeler.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;

@Repository
public interface TwowheelerDetailsRepository extends JpaRepository<TwowheelerDetailesTable, String> {

	@Query(value = "SELECT a from TwowheelerDetailesTable a where a.applicationNo =:applicationNo")
	Optional<TwowheelerDetailesTable> getByApplicationNo(String applicationNo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.status =:Status and l.assignTo =:assignTo and l.assignTo =:assignTo and l.salesBranchId =:BranchId and l.updatedTimestamp between :s and :e order by l.updatedTimestamp desc")
	List<TwowheelerResponse> fetchByDate(@Param("s") LocalDateTime startdate, @Param("e") LocalDateTime enddate,
			String Status, String BranchId, String assignTo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.name like :customSearch+'%'")
	List<TwowheelerResponse> getByName(String customSearch);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.applicationNo like :customSearch+'%'")
	List<TwowheelerResponse> getByAppID(String customSearch);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.customerId like :customSearch+'%'")
	List<TwowheelerResponse> getByCustomerId(String customSearch);

	@Query(value = "SELECT a.address from TwowheelerDetailesTable a where a.applicationNo =:applicationNo")
	Optional<String> fetchAddress(String applicationNo);

	@Query(value = "SELECT top 1 a.[application_no] from [two_wheeler_details_table] a order by a.[application_no] desc", nativeQuery = true)
	Optional<String> fetchLastApplicationNo();

	@Query(value = "SELECT a.appNoWithProductCode from TwowheelerDetailesTable a where a.applicationNo =:applicationNo")
	Optional<String> fetchByapplicationId(String applicationNo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.salesBranchId =:BranchId and l.assignTo =:assignTo and l.updatedTimestamp between :s and :e order by l.updatedTimestamp desc")
	List<TwowheelerResponse> fetchByDateWithBranchId(@Param("s") LocalDateTime startdate,
			@Param("e") LocalDateTime enddate, String BranchId, String assignTo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.status =:Status and l.assignTo =:assignTo and l.updatedTimestamp between :s and :e order by l.updatedTimestamp desc")
	List<TwowheelerResponse> fetchByDateWithStatus(@Param("s") LocalDateTime startdate,
			@Param("e") LocalDateTime enddate, String Status, String assignTo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.assignTo =:assignTo and l.updatedTimestamp between :s and :e order by l.updatedTimestamp desc")
	List<TwowheelerResponse> fetchByDate(@Param("s") LocalDateTime startdate, @Param("e") LocalDateTime enddate,
			String assignTo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.applicationNo =:applicationNo")
	List<TwowheelerResponse> fetchbyApplicationNo(String applicationNo);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.customerId =:customerId")
	List<TwowheelerResponse> fetchbyCustomerId(String customerId);

	@Modifying
	@Transactional
	@Query(value = "update TwowheelerDetailesTable a set a.customerId =:cifNumber where a.applicationNo =:applicationNo")
	void updateCustomerId(String applicationNo, String cifNumber);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.status =:Status and l.listType =:listType and l.salesBranchId =:BranchId and l.updatedTimestamp between :s and :e order by l.updatedTimestamp desc")
	List<TwowheelerResponse> fetchByDateAndbranch(@Param("s") LocalDateTime startdate,
			@Param("e") LocalDateTime enddate, String Status, String BranchId, String listType);

	@Modifying
	@Transactional
	@Query(value = "update TwowheelerDetailesTable a set a.flowStatus =:flowStatus where a.applicationNo =:applicationNo")
	void updateFlowStatus(String applicationNo, String flowStatus);

	@Modifying
	@Transactional
	@Query(value = "update TwowheelerDetailesTable a set a.assignTo =:assignTo,a.updatedTimestamp =:now where a.applicationNo IN (:list)")
	void updateAssignTo(List<String> list, String assignTo, LocalDateTime now);

	Optional<TwowheelerDetailesTable> getByLeadId(String lead_ID);

	@Query(value = "SELECT new com.suryoday.twowheeler.pojo.TwowheelerResponse(l.applicationNo, l.appNoWithProductCode, l.createdTimestamp, l.updatedTimestamp, l.status, l.salesBranchId, l.name, l.mobileNo, l.customerId, l.stage,l.flowStatus,l.salesCreatedBy, l.listType,l.preApprovalAmount,l.assignTo,l.level,l.rmBranchName) from TwowheelerDetailesTable l where l.status in ('CREDIT','CREDITOPS') and l.listType =:listType and l.salesBranchId =:BranchId and l.updatedTimestamp between :s and :e order by l.updatedTimestamp desc")
	List<TwowheelerResponse> fetchCompleted(@Param("s") LocalDateTime startdate, @Param("e") LocalDateTime enddate,
			String BranchId, String listType);

}
