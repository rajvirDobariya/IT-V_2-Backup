package com.suryoday.loantrackingphase2.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suryoday.loantrackingphase2.exception.LoanException;
import com.suryoday.loantrackingphase2.mapper.HealthCheckActivityMapper;
import com.suryoday.loantrackingphase2.mapper.ProductMasterMaapper;
import com.suryoday.loantrackingphase2.model.HealthCheckActivity;
import com.suryoday.loantrackingphase2.model.NOIUpdation;
import com.suryoday.loantrackingphase2.model.ProductMaster;
import com.suryoday.loantrackingphase2.repository.NOIUpdationRepository;
import com.suryoday.loantrackingphase2.service.ApiClient;
import com.suryoday.loantrackingphase2.service.NOIUpdationService;
import com.suryoday.loantrackingphase2.utils.DateUtils;
import com.suryoday.loantrackingphase2.utils.EXCELService;
import com.suryoday.loantrackingphase2.utils.EncryptDecryptHelper;

@Service
public class NOIUpdationServiceImpl implements NOIUpdationService {

	Logger LOG = LoggerFactory.getLogger(NOIUpdationServiceImpl.class);

	@Autowired
	private NOIUpdationRepository noiUpdationRepository;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	private ApiClient apiClient;

	@Override
	public JSONObject addNOIUpdationActivity(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncy) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}

		// 4. PROCESS
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new LoanException("Data is null :" + data);
		}

		// Extracting fields using opt methods
		String loanId = data.optString("loanId");
		LocalDate disbursementDate = LocalDate.parse(data.optString("disbursementDate"));
		String mortgageType = data.optString("mortgageType");
		Boolean sanctionLetter = data.optBoolean("sanctionLetter");
		Boolean index2 = data.optBoolean("index2");
		Boolean selfiePhoto = data.optBoolean("selfiePhoto");
		Boolean kyc = data.optBoolean("KYC");
		Boolean noiComformationFromVendor = data.optBoolean("noiComformationFromVendor");
		Boolean moeChallan = data.optBoolean("moeChallan");
		LocalDate noiInitiatedDate = LocalDate.parse(data.optString("noiInitiatedDate"));
		String noiPendingWith = data.optString("noiPendingWith");
		String tokenNumber = data.optString("tokenNumber");
		LocalDate noiReceiveDate = LocalDate.parse(data.optString("noiReceiveDate"));
		String finalStatus = data.optString("finalStatus");
		String remarks = data.optString("remarks");

		// Create and populate the NOIUpdation object
		NOIUpdation noiUpdation = new NOIUpdation();

		noiUpdation.setLoanId(loanId);
		noiUpdation.setDisbursementDate(disbursementDate);
		noiUpdation.setMortgageType(mortgageType);
		noiUpdation.setSanctionLetter(sanctionLetter);
		noiUpdation.setIndex2(index2);
		noiUpdation.setSelfiePhoto(selfiePhoto);
		noiUpdation.setKYC(kyc);
		noiUpdation.setNoiComformationFromVendor(noiComformationFromVendor);
		noiUpdation.setMoeChallan(moeChallan);
		noiUpdation.setNoiInitiatedDate(noiInitiatedDate);
		noiUpdation.setNoiPendingWith(noiPendingWith);
		noiUpdation.setTokenNumber(tokenNumber);
		noiUpdation.setNoiReceiveDate(noiReceiveDate);
		noiUpdation.setFinalStatus(finalStatus);
		noiUpdation.setRemarks(remarks);
		noiUpdation.setCreatedBy(Long.parseLong(X_User_ID));
		noiUpdation.setCreatedDate(new Date());
		// SAVE
		noiUpdation = noiUpdationRepository.save(noiUpdation);
		// RESPONSE
		responseData.put("message", noiUpdation.getId() + " Activity created Successfully.");
		response.put("Data", responseData);
		return response;

	}

	@Override
	public JSONObject getNOIUpdationActivities(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncy) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new LoanException("Data is null :" + data);
		}

		// Extract fields using opt methods
		String fromDateStr = data.optString("fromDate");
		String toDateStr = data.optString("toDate");
		String loanId = data.optString("loanId");

		Date fromDate = null;
		Date toDate = null;
		boolean hasFromDate = !fromDateStr.isEmpty();
		boolean hasToDate = !toDateStr.isEmpty();
		boolean hasLoanId = !loanId.isEmpty();

		// Validate and parse dates
		if (hasFromDate) {
			fromDate = DateUtils.getStartOfDay(fromDateStr);
		}
		if (hasToDate) {
			toDate = DateUtils.getEndOfDay(toDateStr);
		}

		// Initialize result list
		List<NOIUpdation> activities = new ArrayList<>();
		System.out.println("=======" + fromDate);
		System.out.println("=======" + toDate);
		// Fetch data based on available parameters
		if (hasFromDate && hasToDate && hasLoanId) {
			// Find by PanNo and date range
			activities = noiUpdationRepository.findByLoanIdAndCreatedDateBetween(loanId, fromDate, toDate);
		} else if (hasFromDate && hasToDate) {
			// Find by date range only
			activities = noiUpdationRepository.findByCreatedDateBetween(fromDate, toDate);
		} else if (hasLoanId) {
			// Find by PanNo only
			activities = noiUpdationRepository.findByLoanId(loanId);
		} else {
			// Find all if no filters are applied
			activities = noiUpdationRepository.findAll();
		}

		// Return the result list
		responseData.put("activities", activities);
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getExcelNOIUpdationActivities(String request, String channel, String X_Session_ID,
			String X_User_ID, Boolean isEncy) {
		JSONObject result = getNOIUpdationActivities(request, channel, X_Session_ID, X_User_ID, isEncy);
		String excelBase64 = EXCELService.generateNOIUpdateExcelBase64(result);
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("excelBase64", excelBase64);
		response.put("Data", data);
		return response;
	}

	@Override
	public JSONObject getLoanAccountDetailsByAccNo(String request, String channel, String X_Session_ID,
			String X_User_ID, Boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new LoanException("Data is null :" + data);
		}

		String loanAccNo = data.getString("loanAccNo");
		JSONObject result = apiClient.getLoanAccountDetails(loanAccNo);
		JSONObject resultData = result.getJSONObject("Data");
		String disbursementDateStr = resultData.optString("DisbursementDate");
		System.out.println("disbursementDateStr ::" + disbursementDateStr);
		// Convert into dd-mm-yyyy
		LocalDate disbursementDate = DateUtils.getLoanDisbursementDate(disbursementDateStr);
		responseData.put("loanAccNo", loanAccNo);
		responseData.put("disbursementDate", disbursementDate);

		// Return the result list
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getNOIUpdateActivityById(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new LoanException("Data is null :" + data);
		}

		// Extract fields
		Long activityId = data.optLong("activityId");

		// Get Activity
		NOIUpdation activity = noiUpdationRepository.findById(activityId)
				.orElseThrow(() -> new LoanException("Activity not found with id " + activityId + "!"));
	     JSONObject acttivityJSON =new JSONObject(new GsonBuilder().create().toJson(activity)); 
	     
		// RESPONSE
		responseData.put("activity", acttivityJSON);
		response.put("Data", responseData);
		return response;

	}

}
