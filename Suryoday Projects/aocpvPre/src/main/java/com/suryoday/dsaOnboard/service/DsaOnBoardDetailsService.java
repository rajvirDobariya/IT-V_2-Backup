package com.suryoday.dsaOnboard.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.JSONObject;

import com.suryoday.dsaOnboard.pojo.BankDetailsResponse;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardResponse;
import com.suryoday.dsaOnboard.pojo.EmpanellementCriteriaResponse;
import com.suryoday.dsaOnboard.pojo.LoginResponse;

public interface DsaOnBoardDetailsService {

	String saveData(DsaOnboardDetails dsaOnboardDetails);

	DsaOnboardDetails getByApplicationNo(String applicationNo);

	DsaOnboardDetails getByApplicationno(String applicationNo);

	JSONObject getReviewData(String applicationNo, String request);

	void updateFlowStatus(long applicationNo, String flowStatus);

	String createLead(String mobileNo);

	LoginResponse getRegistrationDetails(String applicationNo);

	EmpanellementCriteriaResponse getEmpanellementCriteria(String applicationNo);

	List<DsaOnboardResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId);

	List<DsaOnboardResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate);

	List<DsaOnboardResponse> fetchByDateAndStatus(LocalDateTime startdate, LocalDateTime enddate, String status);

	List<DsaOnboardResponse> fetchByDateAndBranch(LocalDateTime startdate, LocalDateTime enddate, String branchId);

	List<DsaOnboardResponse> getByApplication(String applicationNo, String mobileNo, String emailId);

	BankDetailsResponse getBankDetails(String applicationNo);

	void validateMobileNo(String mobile);

	String createlead(org.json.JSONObject jsonObject);

	DsaOnboardDetails getByregiCode(String applicationNo);

	String getlastDsaCode(String string);

	List<DsaOnboardResponse> customSearch(String customSearch, String branchId);

	List<String> getSchemeCode(List<String> list);

}
