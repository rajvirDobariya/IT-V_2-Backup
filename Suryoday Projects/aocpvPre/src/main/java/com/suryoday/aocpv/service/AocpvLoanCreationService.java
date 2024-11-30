package com.suryoday.aocpv.service;

import java.util.List;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;

public interface AocpvLoanCreationService {

	void save(AocpCustomer aocpCustomer, String x_User_ID, String finalsanctionAmount);

	AocpvLoanCreation findByApplicationNo(long applicationNo);

	List<AocpvLoanCreation> fetchByBranchId(String branchId, String status);

	String update(AocpvLoanCreation aocpvLoanCreation);

	AocpvLoanCreation getByApplicationNo(long applicationNoInLong, String status);

	List<AocpvLoanCreation> getLoanPassbookPDFDetails(String customer);

	void saveData(AocpvLoanCreation findByApplicationNo);

}
