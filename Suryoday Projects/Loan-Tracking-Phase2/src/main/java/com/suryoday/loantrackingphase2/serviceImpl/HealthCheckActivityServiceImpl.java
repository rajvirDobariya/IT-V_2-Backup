package com.suryoday.loantrackingphase2.serviceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.loantrackingphase2.exception.LoanException;
import com.suryoday.loantrackingphase2.exception.ValidationException;
import com.suryoday.loantrackingphase2.mapper.HealthCheckActivityMapper;
import com.suryoday.loantrackingphase2.mapper.ProductMasterMaapper;
import com.suryoday.loantrackingphase2.model.HealthCheckActivity;
import com.suryoday.loantrackingphase2.model.ProductMaster;
import com.suryoday.loantrackingphase2.repository.HealthCheckActivityRepository;
import com.suryoday.loantrackingphase2.service.ApiClient;
import com.suryoday.loantrackingphase2.service.HealthCheckActivityService;
import com.suryoday.loantrackingphase2.service.ProductMasterService;
import com.suryoday.loantrackingphase2.utils.DateUtils;
import com.suryoday.loantrackingphase2.utils.EXCELService;
import com.suryoday.loantrackingphase2.utils.EncryptDecryptHelper;

@Service
public class HealthCheckActivityServiceImpl implements HealthCheckActivityService {

	Logger LOG = LoggerFactory.getLogger(HealthCheckActivityServiceImpl.class);

	@Autowired
	private HealthCheckActivityRepository healthCheckActivityRepository;

	@Autowired
	private ProductMasterService productMasterService;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	private ApiClient apiClient;

	@Override
	public JSONObject addHealthCheckActivity(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
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
		String panNo = data.optString("panNo");
		String custName = data.optString("custName");
		String fileReceiveDateStr = data.optString("fileReceiveDate");
		Long BDMEmpId = data.optLong("BDMEmpId");
		Long SMEmpId = data.optLong("SMEmpId");
		Long ASMEmpId = data.optLong("ASMEmpId");
		String custSourcingBranchName = data.optString("custSourcingBranchName");
		Long productCode = data.optLong("productCode");
		String custId = data.optString("custId");
		String BDMEmpName = data.optString("BDMEmpName");
		String SMEmpName = data.optString("SMEmpName");
		String ASMEmpName = data.optString("ASMEmpName");
		String loanAmount = data.optString("loanAmount");
		String applicationId = data.optString("applicationId");

		// VALIDATE
		// Check if any required fields are null or empty
		if (panNo.isEmpty() || custName.isEmpty() || fileReceiveDateStr.isEmpty() || BDMEmpId == 0 || SMEmpId == 0
				|| ASMEmpId == 0 || custSourcingBranchName.isEmpty() || productCode == 0) {
			throw new ValidationException("One or more required fields are missing or empty.");
		}

		// Check Already exist by applicationId
		if (healthCheckActivityRepository.existsByApplicationId(applicationId)) {
			throw new ValidationException(
					"Health check activity with application Id :" + applicationId + " already exists!");
		}

		LocalDate fileReceiveDate;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fileReceiveDate = LocalDate.parse(fileReceiveDateStr, formatter);
		} catch (Exception e) {
			throw new ValidationException("Invalid date format for fileReceiveDate.");
		}

		// Create and populate HealthCheckActivity entity
		HealthCheckActivity activity = new HealthCheckActivity();
		activity.setPanNo(panNo);
		activity.setCustName(custName);
		activity.setFileReceiveDate(fileReceiveDate);
		activity.setBDMEmpId(BDMEmpId);
		activity.setSMEmpId(SMEmpId);
		activity.setASMEmpId(ASMEmpId);
		activity.setCustSourcingBranchName(custSourcingBranchName);
		activity.setProductCode(productCode);
		activity.setCustId(custId);
		activity.setBDMEmpName(BDMEmpName);
		activity.setSMEmpName(SMEmpName);
		activity.setASMEmpName(ASMEmpName);
		activity.setLoanAmount(loanAmount);
		activity.setApplicationId(applicationId);
		activity.setRemarks("");
		activity.setFirstTimeRight("");
		activity.setBilledOrDispatched("");
		activity.setStatus("Created");
		activity.setCreatedDate(new Date());
		activity.setCreatedBy(Long.parseLong(X_User_ID));
		// SAVE
		activity = healthCheckActivityRepository.save(activity);
		// RESPONSE
		responseData.put("message", activity.getId() + " Activity created Successfully.");
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getAllHealthCheckActivity(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
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

		// Extract fields using opt methods
		String fromDateStr = data.optString("fromDate");
		String toDateStr = data.optString("toDate");
		String panNo = data.optString("panNo");

		Date fromDate = null;
		Date toDate = null;
		boolean hasFromDate = !fromDateStr.isEmpty();
		boolean hasToDate = !toDateStr.isEmpty();
		boolean hasPanNo = !panNo.isEmpty();

		// Validate and parse dates
		if (hasFromDate) {
			fromDate = DateUtils.getStartOfDay(fromDateStr);
		}
		if (hasToDate) {
			toDate = DateUtils.getEndOfDay(toDateStr);
		}

		// Initialize result list
		List<HealthCheckActivity> activities = new ArrayList<>();

		// Fetch data based on available parameters
		if (hasFromDate && hasToDate && hasPanNo) {
			// Find by PanNo and date range
			activities = healthCheckActivityRepository.findByPanNoAndCreatedDateBetween(panNo, fromDate, toDate);
		} else if (hasFromDate && hasToDate) {
			// Find by date range only
			activities = healthCheckActivityRepository.findByCreatedDateBetween(fromDate, toDate);
		} else if (hasPanNo) {
			// Find by PanNo only
			activities = healthCheckActivityRepository.findByPanNo(panNo);
		} else {
			// Find all if no filters are applied
			activities = healthCheckActivityRepository.findAll();
		}

		activities.forEach(activity -> {
			activity.setProductData("");
			activity.setQueries(null);
		});
		// RESPONSE
		responseData.put("activities", activities);
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getHealthCheckActivityById(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
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
		HealthCheckActivity activity = healthCheckActivityRepository.findById(activityId)
				.orElseThrow(() -> new LoanException("Activity not found with id " + activityId + "!"));

		// Get Product
		Long productCode = activity.getProductCode();
		JSONObject productJSON = new JSONObject();
		if (activity.getStatus().equals("Created")) {
			ProductMaster product = productMasterService.findById(productCode);
			productJSON = ProductMasterMaapper.convertToJSON(product);
		} else {
			productJSON = new JSONObject(activity.getProductData());
		}

		JSONObject activityJson = HealthCheckActivityMapper.toJSONObject(activity, productJSON);

		// RESPONSE
		responseData.put("activity", activityJson);
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject updateProductChecksAndQueries(String request, String channel, String X_Session_ID,
			String X_User_ID, boolean isEncrypt) {
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

		// Extract fields
		Long activityId = data.optLong("activityId");
		JSONObject product = data.optJSONObject("product");
		JSONArray queriesArray = data.optJSONArray("queries");
		String remarks = data.optString("remarks");
		String firstTimeRight = data.optString("firstTimeRight");
		String billedOrDispatched = data.optString("billedOrDispatched");
		String status = data.optString("status");
		List<String> queries = IntStream.range(0, queriesArray.length()).mapToObj(queriesArray::optString)
				.collect(Collectors.toList());

		// Get Activity
		HealthCheckActivity activity = healthCheckActivityRepository.findById(activityId)
				.orElseThrow(() -> new LoanException("Activity not found with id " + activityId + "!"));

		activity.setProductData(product.toString());
		activity.setQueries(queries);
		activity.setRemarks(remarks);
		activity.setStatus(status);
		activity.setFirstTimeRight(firstTimeRight);
		activity.setBilledOrDispatched(billedOrDispatched);
		healthCheckActivityRepository.save(activity);

		// SAVE
		responseData.put("message", "Activity with ID " + activity.getId()
				+ " has been successfully updated with product data and queries.");
		response.put("Data", responseData);
		return response;

	}

	@Override
	public JSONObject getCustomerDataByCustomerNo(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
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

		String customerNo = data.getString("customerNo");
		JSONObject result = apiClient.getCustomerDetails(customerNo);
		String custName = validateReultAndGetCustomerName(result, customerNo);
		JSONObject custDetails = new JSONObject();
		custDetails.put("custNo", customerNo);
		custDetails.put("custName", custName);
		response.put("Data", custDetails);
		return response;
	}

	private String validateReultAndGetCustomerName(JSONObject result, String customerNo) {
		// 1
		JSONObject data1 = result.optJSONObject("Data");
		if (data1 == null) {
			throw new ValidationException("Customer details not found with this customer no :" + customerNo + "!");
		}
		// 2
		JSONArray data2 = data1.optJSONArray("CustomerDetails");
		if (data2 == null) {
			throw new ValidationException("Customer details not found with this customer no :" + customerNo + "!");
		}
		// 3
		Object data3 = data2.get(0);
		if (data3 == null) {
			throw new ValidationException("Customer details not found with this customer no :" + customerNo + "!");
		}
		JSONObject dataMap = (JSONObject) data3;

		String firstName = dataMap.optString("FirstName");
		String middleName = dataMap.optString("MiddleName");
		String lastName = dataMap.optString("LastName");
		String custName = firstName + " " + middleName + " " + lastName;
		return custName;
	}

	@Override
	public JSONObject getEmployeeNameByEmployeeId(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
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

		Long empId = data.getLong("empId");
		String empName = getEmpNameByEmpId(empId);
		
		//RESPONSE
		responseData.put("empId", empId);
		responseData.put("empName", empName);
		response.put("Data", responseData);
		return response;
	}

	private String getEmpNameByEmpId(Long empId) {
	    // Create a HashMap to store ID and Name pairs
	    Map<Long, String> empMap = new HashMap<>();

	    // Add entries to the HashMap
	    empMap.put(92952L, "Ravi");
	    empMap.put(98765L, "Leeza");
	    empMap.put(87654L, "Harsh");

	    // Retrieve the name by empId
	    return empMap.getOrDefault(empId, "Employee not found");
	}

	@Override
	public JSONObject getExcelHealthCheckActivities(String request, String channel, String X_Session_ID,
			String X_User_ID, boolean isEncrypt) {
		JSONObject result = getAllHealthCheckActivity(request, channel, X_Session_ID, X_User_ID, isEncrypt);
		String excelBase64 = EXCELService.generateHealthCheckExcelBase64(result);
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("excelBase64", excelBase64);
		response.put("Data", data);
		return response;

	}
}
