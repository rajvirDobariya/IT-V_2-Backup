package com.suryoday.twowheeler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.JSONObject;

import com.suryoday.twowheeler.pojo.AdditionalInfoResponse;
import com.suryoday.twowheeler.pojo.BsrDetailsResponse;
import com.suryoday.twowheeler.pojo.CatCompanies;
import com.suryoday.twowheeler.pojo.DisbustmentDetailsResponse;
import com.suryoday.twowheeler.pojo.LoanDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerAssetDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerPDResponse;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;
import com.suryoday.twowheeler.pojo.UtilityBillResponse;

public interface TwowheelerDetailsService {

	String saveData(TwowheelerDetailesTable twowheelerDetails);

	TwowheelerDetailsResponse getByApplicationNo(String applicationNo, String api);

	List<TwowheelerResponse> fetchByDateAndbranch(LocalDateTime startdate, LocalDateTime enddate, String status,String branchId, String listType, String userId);

	TwowheelerDetailesTable getByApplication(String applicationNo);

	List<TwowheelerResponse> customSearch(String customSearch);

	String fetchAddress(String applicationNo);

	String createApplicationNo(String mobileNo, String product, String branchId, String listType, String customerId, String x_User_ID);

	void saveResponse(String proof, String proofId, String applicationNo, String response);

	TwowheelerPDResponse fetchPdData(String applicationNo, String flowStatus);

	TwowheelerAssetDetailsResponse fetchASSETData(String applicationNo, String flowStatus);

	LoanDetailsResponse fetchLoanDetails(String applicationNo, String flowStatus);

	TwowheelerDetailesTable getCustomerDetails(TwowheelerDetailesTable twowheelerDetails);

	TwowheelerDetailesTable getByAppNo(String applicationNo);

	DisbustmentDetailsResponse fetchDisbustmentDetails(String applicationNo, String flowStatus);

	List<TwowheelerResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate);

	List<TwowheelerResponse> fetchByDateWithStatus(LocalDateTime startdate, LocalDateTime enddate, String status);

	List<TwowheelerResponse> fetchByDateWithBranchId(LocalDateTime startdate, LocalDateTime enddate, String branchId);

	List<TwowheelerResponse> fetchbyApplicationNo(String applicationNo);

	List<TwowheelerResponse> fetchbyCustomerId(String customerId);

	JSONObject getreviewData(String applicationNo, String string);

	void updateCustomerId(String applicationNo, String cifNumber);

	List<TwowheelerResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId);

	void updateFlowStatus(String applicationNo, String flowStatus);

	AdditionalInfoResponse fetchAdditionalInfo(String applicationNo, String flowStatus);

	BsrDetailsResponse fetchBsrDetails(String applicationNo, String flowStatus);

	List<CatCompanies> getCatCompanyList();

	UtilityBillResponse fetchUtilityBillDetails(String applicationNo, String flowStatus);
	
}
