package com.suryoday.twowheeler.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.PreApprovedListTwoWheeler;
@Repository
public interface PreApprovalListRepository extends JpaRepository<PreApprovedListTwoWheeler, Long>{

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from PreApprovedListTwoWheeler l where l.isActive='1' and l.updatedate between :s and :e and l.branchId =:branch")
	Optional<List<LoanDetail>> getByDate(@Param ("s") LocalDate startdate ,@Param ("e") LocalDate enddate,long branch);

	Optional<PreApprovedListTwoWheeler> getByCustomerID(long custid);

	@Query(value = "SELECT new com.suryoday.aocpv.pojo.LoanDetail(l.mobilePhone, l.referenceNo, l.customerID, l.memberName, l.state, l.branchId, l.amount, l.status, l.createDate, l.productCode) from PreApprovedListTwoWheeler l where l.branchId =:b and l.isActive='1' and l.status =:s and l.productCode =:p order by l.updatedate desc")
	Optional<List<LoanDetail>> getByIdAndStatus(@Param("b") long branchId ,@Param("s") String status ,@Param("p") String productCode);

}
