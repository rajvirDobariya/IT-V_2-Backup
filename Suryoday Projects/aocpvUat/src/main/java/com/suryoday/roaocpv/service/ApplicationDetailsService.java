package com.suryoday.roaocpv.service;

import java.time.LocalDate;
import java.util.List;

import org.json.JSONObject;

import com.suryoday.roaocpv.pojo.Address;
import com.suryoday.roaocpv.pojo.ApplicationDetailList;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ApplicationDetailsResponse;
import com.suryoday.roaocpv.pojo.PersonalDetailResponse;

public interface ApplicationDetailsService {

	List<ApplicationDetailList> fetchByStatusAndBranchId(String status, String branchId);

	String passorfail(String applicationId, String flowStatus, String status);

	ApplicationDetails fetchByApplicationId(String applicationId);

	List<ApplicationDetails> fetchByDate(String status, String branchId, LocalDate startdate, LocalDate enddate);

	String addpersonalDetails(String applicationId, String personalDetails);

	public String createLead(String jsonRequest, String userId);

	public void blockValidation(String proof, String proofId, String applicationNo);

	public void saveData(String proof, String proofId, String applicationNo, String response);

	String saveCIf(String applicationId, JSONObject jsonObject2);

	PersonalDetailResponse getPersonalDetailsByAppln(String appln);

	String saveAddress(String appln, String address);

	List<Address> getAddress(String appln);

	void validateMobile(String mobile);

	ApplicationDetailsResponse fetchAllByApplicationId(String applicationNo);

	void validateAadharCard(String referenceNo);

	void validatePancard(String panCardNo);

	String updateFlowStatus(String flowStatus, String applicationId, String status);

	void save(ApplicationDetails fetchByApplicationId);

	String createApplicationNo(String mobileNo, String product, String branchId);

	int fetchByLeadId(String lead_ID);

	List<ApplicationDetails> getByBreStatus(String status, LocalDate startdate, LocalDate enddate);

	List<ApplicationDetails> getByBreStatus(String status, String branchId, LocalDate startdate, LocalDate enddate);
}
