package com.suryoday.collections.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.collections.pojo.ReceiptPojo;

@Repository
public interface RecieptRepository extends JpaRepository<ReceiptPojo, Integer> {

	@Query(value = "SELECT a from ReceiptPojo a where a.customerID =:customer")
	Optional<ReceiptPojo> getByCustomer(String customer);

	@Query(value = "SELECT TOP 10 * from iissue_receipt a where a.branch_id =:b order by a.id desc", nativeQuery = true)
	List<ReceiptPojo> findTopReceipt(@Param("b") String branchId);

	@Query(value = "SELECT a from ReceiptPojo a where a.branchId =:branchId")
	List<ReceiptPojo> findDetailsReceipt(String branchId);

	@Query(value = "SELECT a.receiptNo from ReceiptPojo a where a.branchId =:branchId")
	String getByReceiptNumber(String branchId);

	@Query(value = "SELECT * from iissue_receipt where branch_id =:branchId and [current_date] between :s and :l", nativeQuery = true)
	List<ReceiptPojo> findDetailsByDate(String branchId, @Param("s") LocalDate startdate,
			@Param("l") LocalDate enddate);

	@Query(value = "SELECT a from ReceiptPojo a where a.receiptNo =:receiptNo")
	List<ReceiptPojo> findPDFDetailsReceipt(String receiptNo);
}
