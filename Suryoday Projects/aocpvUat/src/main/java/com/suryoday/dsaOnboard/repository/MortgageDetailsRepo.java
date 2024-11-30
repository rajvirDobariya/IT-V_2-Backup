package com.suryoday.dsaOnboard.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.MortgageDetails;

@Repository
public interface MortgageDetailsRepo extends JpaRepository<MortgageDetails, Long> {

//	@Query(value="Select new com.suryoday.dsaOnboard.pojo.MortgageDetailsResp(a.empId,a.empName,a.purposeToVisit,a.visitName,a.dateOfActivity,a.contactNo,a.relationshipType,a.remarks,a.geoLatLong) from MortgageDetails a where a.empId=:empId")
	@Query(value = "Select a from MortgageDetails a where a.empId=:empId")
	List<MortgageDetails> fetchByEmpId(String empId);

//	@Query(value="Select new com.suryoday.dsaOnboard.pojo.MortgageDetailsResp(a.empId,a.empName,a.purposeToVisit,a.visitName,a.dateOfActivity,a.contactNo,a.relationshipType,a.remarks,a.geoLatLong) from MortgageDetails a where a.createdDate between :s and :e order by createdDate desc")
	@Query(value = "Select a from MortgageDetails a where a.createdDate between :s and :e order by createdDate desc ")
	List<MortgageDetails> fetchByDate(@Param("s") LocalDateTime startDate, @Param("e") LocalDateTime endDate);

}
