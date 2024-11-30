package com.suryoday.goNogo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.goNogo.pojo.GoNogoCustomerDetails;
import com.suryoday.goNogo.pojo.GoNogoDetailsResponse;

@Repository
public interface GoNogoCustDetailsRepository extends JpaRepository<GoNogoCustomerDetails, Long> {

	@Query(value = "SELECT a from GoNogoCustomerDetails a where a.applicationNo =:applicationNo")
	Optional<GoNogoCustomerDetails> getByAapplicationNo(long applicationNo);

	@Query(value = "SELECT top 1 a.[application_no] from [go_nogo_customer_details] a order by a.[application_no] desc", nativeQuery = true)
	Optional<String> fetchLastApplicationNo();

	@Query(value = "SELECT new com.suryoday.goNogo.pojo.GoNogoDetailsResponse(l.name, l.dateOfBirth, l.mobileNo, l.status, l.flowStatus, l.branchId) from GoNogoCustomerDetails l where l.status =:Status and l.branchId =:BranchId and l.updatedDate between :startdate and :enddate order by l.updatedDate desc")
	List<GoNogoDetailsResponse> fetchByDateAndbranch(LocalDateTime startdate, LocalDateTime enddate, String Status,
			String BranchId);

}
