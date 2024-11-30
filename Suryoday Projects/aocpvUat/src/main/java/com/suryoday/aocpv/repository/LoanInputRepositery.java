package com.suryoday.aocpv.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.LoanDetails;
import com.suryoday.aocpv.pojo.PreApprovedList;

@Repository
public interface LoanInputRepositery extends JpaRepository<LoanDetails, String> {

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from LoanDetails l where l.branchId =:b and l.isActive='1' and l.status =:s and l.productCode =:p order by l.updatedate desc")
	Optional<List<LoanDetail>> getByIdAndStatus(@Param("b") long branchId, @Param("s") String status,
			@Param("p") String productCode);

	public Optional<LoanDetails> getByCustomerID(long customerID);

	@Query(value = "SELECT a from LoanDetails a where a.status =:s")
	Optional<List<LoanDetails>> findByStatus(@Param("s") String status);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from LoanDetails l where l.customerID =:customerId")
	Optional<LoanDetail> getByCustomerId(@Param("customerId") long customerId);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from LoanDetails l where l.mobilePhone =:mobile and l.branchId =:branchid")
	Optional<List<LoanDetail>> getByMobilePhone(long mobile, long branchid);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from LoanDetails l where l.isActive='1' and l.updatedate between :s and :e and l.branchId =:branch")
	Optional<List<LoanDetail>> getByDate(@Param("s") LocalDate startdate, @Param("e") LocalDate enddate, long branch);

	@Query(value = "SELECT a from LoanDetails a where a.customerID =:custid and a.productCode =:product")
	List<LoanDetails> getByCustomer(long custid, String product);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from LoanDetails l where l.customerID =:customerId")
	List<LoanDetail> getByCustId(long customerId);

	@Query(value = "SELECT a from LoanDetails a where a.customerID =:customerId order by a.updatedate desc")
	List<LoanDetails> fetchByCustomerId(long customerId);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.PreApprovedList(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.branchId, l.amount, l.status, l.createDate ,l.updatedate ,l.CITY ,l.state ,l.Branch_Name) from LoanDetails l where l.isActive='1' and l.branchId =:branchId1 and l.updatedate between :s and :e order by l.updatedate desc")
	Optional<List<PreApprovedList>> fetchByDate(@Param("s") LocalDate startdate, @Param("e") LocalDate enddate,
			long branchId1);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.PreApprovedList(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.branchId, l.amount, l.status, l.createDate ,l.updatedate ,l.CITY ,l.state ,l.Branch_Name) from LoanDetails l where l.customerID =:customerId")
	Optional<PreApprovedList> getLoanDetails(long customerId);

	@Modifying
	@Transactional
	@Query(value = "update LoanDetails set status =:status where customerID =:customerId")
	void updateStatus(long customerId, String status);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.PreApprovedList(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.branchId, l.amount, l.status, l.createDate ,l.updatedate ,l.CITY ,l.state ,l.Branch_Name) from LoanDetails l where l.isActive='1' and l.customerID =:customerId order by l.updatedate desc")
	List<PreApprovedList> getLoanDetailsList(long customerId);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.PreApprovedList(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.branchId, l.amount, l.status, l.createDate ,l.updatedate ,l.CITY ,l.state ,l.Branch_Name) from LoanDetails l where l.isActive='1' order by l.updatedate desc")
	List<PreApprovedList> topTenRecord(Pageable pageable);
}
