package com.suryoday.twowheeler.service;

import java.time.LocalDate;
import java.util.List;

import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.PreApprovedListTwoWheeler;

public interface PreApprovalListService {

	List<LoanDetail> findByDate(LocalDate start, LocalDate end, Long branchid);

	PreApprovedListTwoWheeler getByReferenceNo(String customerId);

	PreApprovedListTwoWheeler saveSingleData(PreApprovedListTwoWheeler loanDetails);

	List<LoanDetail> getByIdAndstatus(long branchId1, String status, String productCode);

}
