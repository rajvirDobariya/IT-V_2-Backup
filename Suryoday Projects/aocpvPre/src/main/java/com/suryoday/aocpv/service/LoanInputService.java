package com.suryoday.aocpv.service;

import java.time.LocalDate;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.pojo.LoanDetail;
import com.suryoday.aocpv.pojo.PreApprovedList;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;


public interface LoanInputService {

	void save(MultipartFile file);
	List<PreApprovedListVikasLoan>  getAllProduct();
	List<LoanDetail> getByIdAndstatus(long branchId,String status, String productCode);
	PreApprovedListVikasLoan getByReferenceNo(String referenceNo);
	PreApprovedListVikasLoan saveSingleData(PreApprovedListVikasLoan loanDetails);
	List<PreApprovedListVikasLoan> getByStatus(String status);

	LoanDetail getByCustomerId(long customerId,String status);
	List<LoanDetail> findByMobilePhone(long mobilePhone, long branchIdInLong);
	List<LoanDetail> findByDate(LocalDate startdate, LocalDate enddate, long branchId);
	PreApprovedListVikasLoan fetchByCustomerId(long customerId);
	List<PreApprovedList> fetchByDate(String startDate, String endDate, String branchId);
	PreApprovedList fetchByCustomerId(String customerId);
	void statusChange(long customerId, String status);
	List<PreApprovedList> fetchTopTenPreApprovedList();
	PreApprovedListVikasLoan fetchByCustomerID(long customerNo);
	JSONObject getBRNetLoanDetails(String accountNo, JSONObject header);
	List<PreApprovedListVikasLoan> fetchPreApprovalMfiData(long customerIdInLong);
	PreApprovedListVikasLoan fetchByCustomerIdAndBranch(long customerId, long branchIdInLong);
}
