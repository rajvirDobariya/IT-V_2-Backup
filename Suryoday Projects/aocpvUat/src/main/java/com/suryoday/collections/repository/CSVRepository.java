package com.suryoday.collections.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.collections.pojo.CSVModel;
import com.suryoday.collections.pojo.CSVModelResponse;

@Repository
public interface CSVRepository extends JpaRepository<CSVModel, Long> {

	@Query(value = "SELECT new com.suryoday.collections.pojo.CSVModelResponse(a.customerName, a.city, a.currentPOS, a.customerID, a.aggrementID, a.chasisnum, a.disbursalDate) from CSVModel a where a.status =:status and a.branchCode =:branchId")
	List<CSVModelResponse> findByStatus(String status, String branchId);

	@Query(value = "SELECT a from CSVModel a where a.customerID like :customerNO+'%'")
	List<CSVModel> findByCustomerNO(String customerNO);

	@Query(value = "SELECT a from CSVModel a where a.aggrementID like :accountNo+'%'")
	List<CSVModel> findByAccountNO(String accountNo);

	@Query(value = "SELECT a from CSVModel a where a.mailingMobile like :mobileNO+'%'")
	List<CSVModel> findByMobileNo(String mobileNO);

	@Query(value = "SELECT * from collection_input a where a.branch_code =:branchId and a.status =:status and a.updated_date between :s and :l", nativeQuery = true)
	List<CSVModel> findByDateCustomer(String status, String branchId, @Param("s") LocalDate startdate,
			@Param("l") LocalDate enddate);
}
