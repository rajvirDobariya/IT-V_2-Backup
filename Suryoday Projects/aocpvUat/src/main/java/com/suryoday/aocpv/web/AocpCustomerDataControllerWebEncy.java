package com.suryoday.aocpv.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.pojo.CustomerResponseForDisbursed;
import com.suryoday.aocpv.pojo.CustomerResponseWeb;
import com.suryoday.aocpv.pojo.CustomerWebResponse;
import com.suryoday.aocpv.pojo.FetchAllWebResponse;
import com.suryoday.aocpv.pojo.FetchByCustResponse;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.pojo.PreApprovedList;
import com.suryoday.aocpv.pojo.PreApprovedListVikasLoan;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerDataWeb;
import com.suryoday.aocpv.pojo.UserLog;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.aocpv.service.PreApprovalService;
import com.suryoday.aocpv.service.RetrieveReportService;
import com.suryoday.aocpv.service.UserLogService;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ApplicationDetailsResponseWeb;
import com.suryoday.roaocpv.service.ApplicationDetailsService;

@CrossOrigin(origins = { "http://localhost:4200", "http://10.20.25.37:443", "https://sarathi.suryodaybank.com/",
		"http://10.20.18.66:443", "https://sarathi.suryodaybank.co.in/" })
@RestController
@RequestMapping("/aocp/web/customer")
public class AocpCustomerDataControllerWebEncy {
	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	@Autowired
	LoanInputService loanInputService;

	@Autowired
	UserLogService userLogService;

	@Autowired
	UserService userService;

	@Autowired
	RetrieveReportService retrieveReportService;

	@Autowired
	PreApprovalService approvalService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	ApplicationDetailsService applicationDetailsService;

	Logger logger = LoggerFactory.getLogger(AocpCustomerDataControllerWebEncy.class);

	@RequestMapping(value = "/fetchByCustomerIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCustomerIdWeb(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId=true;
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			// String substring = key.substring(0,Math.min(key.length(), 16));
//	String encodeKey = EncrtionAngulurTest.encodeKey(substring);
			// String encodedBase64Key = EncrtionAngulurTest.encodeKey(key);
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String versionCode = jsonObject.getJSONObject("Data").getString("versionCode");
			if (applicationNo == null || applicationNo.isEmpty()) {
				throw new EmptyInputException("input field is empty");
			}
			int versioncode = Integer.parseInt(versionCode);
			long applicationNoInLong = Long.parseLong(applicationNo);
			ResponseAocpCustomerDataWeb aocpCustomerData = aocpCustomerDataService
					.fetchByApplicationNo(applicationNoInLong, versioncode);
			// long customerIdInLong = aocpCustomerData.getCustomerId();
			// String customerId = Long.toString(customerIdInLong);
			User fetchById = userService.fetchById(X_User_ID);
			if (fetchById.getUserRole().getUserName().equals("AM")) {

				if (fetchById.getBranchIdArray() != null) {
					org.json.JSONArray branchIdArray = new org.json.JSONArray(fetchById.getBranchIdArray());
					int count = 0;
					for (int n = 0; n < branchIdArray.length(); n++) {
						String branchId = "";

						org.json.JSONObject jsonObject1 = branchIdArray.getJSONObject(n);
						try {
							branchId = jsonObject1.getString("branchId");
						} catch (Exception e) {
							branchId = Long.toString(jsonObject1.getLong("branchId"));
						}
						if (branchId.equals(aocpCustomerData.getBranchId())) {
							count++;
							break;
						}
					}
					if (count == 0) {
						throw new NoSuchElementException("You are Not authorized to View");
					}
				} else {
					if (!Long.toString(fetchById.getBranchId()).equals(aocpCustomerData.getBranchId())) {
						throw new NoSuchElementException("You are Not authorized to View");
					}
				}
			}

			else if (fetchById.getUserRole().getUserName().equals("SH")) {

				HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
				int count = 0;
				for (String branchesofuser : listofbranches) {
					if (aocpCustomerData.getBranchId().equals(branchesofuser)) {
						count++;
						break;
					}
				}
				if (count == 0) {
					throw new NoSuchElementException("You are Not authorized to View");
				}
			}
			User fetchCreatedBy = userService.fetchById(aocpCustomerData.getCreatedBy());
			aocpCustomerData.setAgentName(fetchCreatedBy.getUserName());
			String branchName = "";

			if (fetchCreatedBy.getBranchIdArray() != null) {
				org.json.JSONArray branchIdArray = new org.json.JSONArray(fetchCreatedBy.getBranchIdArray());
				for (int n = 0; n < branchIdArray.length(); n++) {
					String branchId = "";
					org.json.JSONObject jsonObject1 = branchIdArray.getJSONObject(n);
					try {
						branchId = jsonObject1.getString("branchId");
					} catch (Exception e) {
						branchId = Long.toString(jsonObject1.getLong("branchId"));
					}
					if (branchId.equals(aocpCustomerData.getBranchId())) {
						branchName = jsonObject1.getString("branchName");
						break;
					}
				}
			}
			aocpCustomerData.setBranchName(branchName);
			org.json.simple.JSONObject pdresponse = new org.json.simple.JSONObject();
//		if(!customerId.equals("0")) {
//			LoanDetails loanDetails = loanInputService.getByReferenceNo(customerId);
//			JSONObject j=new JSONObject(loanDetails);
//			pdresponse.put("LoanDetails", j);
//		}
			JSONObject k = new JSONObject(aocpCustomerData);
			pdresponse.put("status", HttpStatus.OK.toString());

			pdresponse.put("CustomerDetails", k);
			// if (aocpCustomerData.getStatus().equalsIgnoreCase("COMPLETED")) {
			List<UserLog> list = userLogService.getByApplicationNo(aocpCustomerData.getApplicationNo());
			// pdresponse.put("CreatedBy", userLog.getUserId());
			// pdresponse.put("DateAndTime", userLog.getCreatedDate().toString());
			// }
			if (aocpCustomerData.getUtilityBillIsVerify() != null) {
				if (aocpCustomerData.getUtilityBillIsVerify().equals("YES")) {
					String utilityBillResponse = aocpCustomerData.getUtilityBillResponse();
					pdresponse.put("utilityBillResponse", utilityBillResponse);
				}
			}
			JSONArray j = new JSONArray(list);
			pdresponse.put("userLog", j);

			if (aocpCustomerData.getStatus().equalsIgnoreCase("DISBURSED")
					|| aocpCustomerData.getStatus().equalsIgnoreCase("APPROVED")) {
				AocpvLoanCreation findByApplicationNo = aocpvLoanCreationService
						.findByApplicationNo(aocpCustomerData.getApplicationNo());
				pdresponse.put("SanctionedLoanAmount", findByApplicationNo.getSanctionedLoanAmount());
				pdresponse.put("LoanAccoutNumber", findByApplicationNo.getLoanAccoutNumber());
				pdresponse.put("LoanCreationDate", findByApplicationNo.getLoanCreationDate());
				pdresponse.put("sanctionLetterIsVerify", findByApplicationNo.getSanctionLetterIsVerify());
				pdresponse.put("aggreementLetterIsVerify", findByApplicationNo.getAggreementLetterIsVerify());
			}
			String data = pdresponse.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/updateStatusEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> updateStatusWeb(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			if (applicationNo != null) {
				long applicationNoInLong = Long.parseLong(applicationNo);
				AocpCustomer aocpCustomer = aocpCustomerDataService.getByApplicationNo(applicationNoInLong);
				String customerId = Long.toString(aocpCustomer.getCustomerId());
				PreApprovedListVikasLoan loanDetails = loanInputService.getByReferenceNo(customerId);
				String status = jsonObject.getJSONObject("Data").getString("status");
				aocpCustomer.setStatus(status);
				loanDetails.setStatus(status);
				String remark = jsonObject.getJSONObject("Data").getString("remark");
				if (remark == null || remark.isEmpty()) {
					aocpCustomer.setRemarks(null);
				} else {
					aocpCustomer.setRemarks(remark);
				}
				String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
				PreApprovedListVikasLoan loanDetails2 = loanInputService.saveSingleData(loanDetails);

				org.json.simple.JSONObject pdresponse = new org.json.simple.JSONObject();
				pdresponse.put("status", HttpStatus.OK.toString());
				pdresponse.put("messgae", saveSingleData);
				String data = pdresponse.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);

			} else {
				org.json.simple.JSONObject pdresponse = new org.json.simple.JSONObject();
				pdresponse.put("status", HttpStatus.OK.toString());
				pdresponse.put("messgae", "enter valid applicationNo");
				String data = pdresponse.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchAllByStatusEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getAllByStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String status = jsonObject.getJSONObject("Data").getString("status");
			List<FetchAllWebResponse> list = new ArrayList<>();
			List<FetchAllWebResponse> fetchAllByBranchIdAndStatus = aocpCustomerDataService
					.fetchAllByBranchIdAndStatus(status);
			User fetchById = userService.fetchById(X_User_ID);
			for (FetchAllWebResponse aocpCustomerData : fetchAllByBranchIdAndStatus) {
				if (fetchById.getUserRole().getUserName().equals("AM")) {
					if (fetchById.getBranchIdArray() != null) {
						org.json.JSONArray branchIdArray = new org.json.JSONArray(fetchById.getBranchIdArray());
						int count = 0;
						for (int n = 0; n < branchIdArray.length(); n++) {
							String branchId = "";

							org.json.JSONObject jsonObject1 = branchIdArray.getJSONObject(n);
							try {
								branchId = jsonObject1.getString("branchId");
							} catch (Exception e) {
								branchId = Long.toString(jsonObject1.getLong("branchId"));
							}
							if (branchId.equals(aocpCustomerData.getBranchId())) {
								list.add(aocpCustomerData);
								break;
							}
						}

					} else {
						if (Long.toString(fetchById.getBranchId()).equals(aocpCustomerData.getBranchId())) {
							list.add(aocpCustomerData);
						}
					}
				} else if (fetchById.getUserRole().getUserName().equals("SH")) {
					HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
					for (String branchesofuser : listofbranches) {
						if (aocpCustomerData.getBranchId().equals(branchesofuser)) {
							list.add(aocpCustomerData);
						}
					}

				} else {
					org.json.simple.JSONObject response = new org.json.simple.JSONObject();
					JSONArray j = new JSONArray(fetchAllByBranchIdAndStatus);
					response.put("Data", j);
					String data = response.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.OK);
				}
			}
			if (list.size() == 0) {
				throw new NoSuchElementException("NO Record found");
			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchAllByBranchIdAndStatusWithDateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllByBranchIdAndStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("findByDate start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			String status = jsonObject.getJSONObject("Data").getString("status");
			String startDate = jsonObject.getJSONObject("Data").getString("startDate");
			String endDate = jsonObject.getJSONObject("Data").getString("endDate");
			String allowCreditAccess = jsonObject.getJSONObject("Data").getString("allowCreditAccess");
			if (startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "enter a proper Dates");
				logger.debug("final response" + response.toString());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate startdate = LocalDate.parse(startDate, formatter);
			LocalDate enddate = LocalDate.parse(endDate, formatter);
			List<AocpCustomer> list = null;
			if (branchId.isEmpty() && status.isEmpty()) {
				list = aocpCustomerDataService.findByDateWeb(startdate, enddate);
			} else if (branchId.isEmpty()) {
				list = aocpCustomerDataService.findByDateAndStatus(startdate, enddate, status);
			} else if (status.isEmpty()) {
				list = aocpCustomerDataService.findByDateAndBranchIdWeb(startdate, enddate, branchId);
			} else {
				list = aocpCustomerDataService.findByDateStatusAndBranchId(startdate, enddate, status, branchId);
			}
			List<CustomerResponseWeb> listOfCustomer = new ArrayList<>();
			List<CustomerResponseForDisbursed> listForDisbursed = new ArrayList<>();
//		String address_Line1="";
//				String state="";
//				String postal="";
			CustomerResponseWeb customerResponse = null;
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
			if (status.equalsIgnoreCase("REJECTED")) {
				List<ApplicationDetails> listOfapplication = null;
				if (branchId.isEmpty()) {
					listOfapplication = applicationDetailsService.getByBreStatus(status, startdate, enddate);
				} else {
					listOfapplication = applicationDetailsService.getByBreStatus(status, branchId, startdate, enddate);
				}
				for (ApplicationDetails applicationDetails : listOfapplication) {
					String name = "";
					if (applicationDetails.getEkycResponse() != null) {
						org.json.JSONObject ekyc = new org.json.JSONObject(applicationDetails.getEkycResponse());
						name = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData")
								.getJSONObject("Poi").getString("name");
					}
					LocalDateTime createts = applicationDetails.getCreatets();
					LocalDate updatedts = applicationDetails.getUpdatedDate();
					String createdDate = createts.format(formatter1);
					String updatedDate = updatedts.format(formatter1);
					customerResponse = new CustomerResponseWeb(Long.parseLong(applicationDetails.getMobileNo()), 0,
							name, applicationDetails.getBranchCode(), status, createdDate, updatedDate, "BRERejected",
							applicationDetails.getAppNoWithProductCode(), applicationDetails.getCreatedBy(),
							Long.parseLong(applicationDetails.getApplicationId()));
					listOfCustomer.add(customerResponse);
				}
			}
			for (AocpCustomer data : list) {

				LocalDate creationDate = data.getCreationDate();
				LocalDate updatedate = data.getUpdatedate();
				String creationDateString = creationDate.format(formatter1);
				String updatedateString = updatedate.format(formatter1);
				if (status.equalsIgnoreCase("DISBURSED")) {
					if (data.getStatus().equalsIgnoreCase("DISBURSED")) {
						String address_Line1 = "";
						String state = "";
						String postal = "";
						LocalDate dateOfBirth = data.getDateOfBirth();
						String dob1 = dateOfBirth.format(formatter1);

						CustomerResponseForDisbursed customerResponseDisbursed = new CustomerResponseForDisbursed(
								data.getMobileNo(), data.getCustomerId(), data.getName(), dob1, address_Line1, state,
								postal, data.getBranchid(), data.getEligibleAmount(), data.getStatus(),
								creationDateString, data.getApplicationNo(), updatedateString);
						AocpvLoanCreation findByApplicationNo = aocpvLoanCreationService
								.findByApplicationNo(data.getApplicationNo());
						customerResponseDisbursed
								.setSanctionedLoanAmount(findByApplicationNo.getSanctionedLoanAmount());
						customerResponseDisbursed.setLoanAccoutNumber(findByApplicationNo.getLoanAccoutNumber());
						customerResponseDisbursed.setLoanCreationDate(findByApplicationNo.getLoanCreationDate());
						listForDisbursed.add(customerResponseDisbursed);
					}

				}

//				String address1 = data.getAddress();
//				if(address1 == null || address1.isEmpty()) {
//					
//				}
//				else {
//					org.json.JSONArray addressInJson =new org.json.JSONArray(data.getAddress());
//	  			List<Address> listAddress=new ArrayList<>();
//	  			
//	  			for(int n=0;n<addressInJson.length();n++) {
//	  					JSONObject jsonObject2 = addressInJson.getJSONObject(n);
//	  				 address_Line1 = jsonObject2.getString("ADDRESS_LINE1");	  					
//	  					postal = jsonObject2.getString("PINCODE");
//	  					state = jsonObject2.getString("STATE");
//	  			}	
//				}

				if (data.getAppNoWithProductCode() == null) {
					data.setAppNoWithProductCode("VL-" + data.getApplicationNo());
				}
//			   	String remarks = data.getRemarks();
//			   	List<RemarkResponse> listOfremarks=new ArrayList<>();
//			   	if(remarks != null) {
//			   		org.json.JSONArray remark =new org.json.JSONArray(remarks);
//			   		for(int n=0;n<remark.length();n++) {
//			   			String date="NA";
//			   			String updatedBy ="NA";
//			   				JSONObject remark1 = remark.getJSONObject(n);
//			   				String Decision = remark1.getString("DECISION");
//			   				String RejectReason = remark1.getString("REJECTREASON");
//			   				String Remarks = remark1.getString("REMARKS");
//			   				if(remark1.has("DATE")) {
//			   				date=remark1.getString("DATE");
//			   				}
//			   				if(remark1.has("UPDATEDBY")) {
//			   					updatedBy=remark1.getString("UPDATEDBY");
//			   				}
//			   				RemarkResponse remarkResponse=new RemarkResponse(Decision,RejectReason,Remarks,date,updatedBy);
//			   				listOfremarks.add(remarkResponse);
//			   		}
//			   	}
				customerResponse = new CustomerResponseWeb(data.getMobileNo(), data.getCustomerId(), data.getName(),
						data.getBranchid(), data.getStatus(), creationDateString, updatedateString, data.getListType(),
						data.getAppNoWithProductCode(), data.getCreatedby(), data.getApplicationNo());

				// if(allowCreditAccess.equalsIgnoreCase("YES") &&
				// data.getEligibleAmount()!=null && ) {

				// }
				listOfCustomer.add(customerResponse);
			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			if (status.equalsIgnoreCase("DISBURSED")) {
				if (listForDisbursed.isEmpty()) {
					response.put("status", HttpStatus.BAD_REQUEST);
					response.put("message", "list is empty");
					logger.debug("final response" + response.toString());
					String data = response.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);
				}
				JSONArray j = new JSONArray(listForDisbursed);
				response.put("Data", j);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			}

			User fetchById = userService.fetchById(X_User_ID);
			String eligibleAmount1 = "";
			int eligibleAmount = 0;
			for (AocpCustomer data1 : list) {
				eligibleAmount1 = data1.getEligibleAmount();
				eligibleAmount = Integer.parseInt(eligibleAmount1);
			}
			List<CustomerResponseWeb> list2 = new ArrayList<>();
			for (CustomerResponseWeb customer : listOfCustomer) {
				if (fetchById.getUserRole().getUserName().equals("AM")) {
					if (fetchById.getBranchIdArray() != null) {
						org.json.JSONArray branchIdArray = new org.json.JSONArray(fetchById.getBranchIdArray());
						int count = 0;
						for (int n = 0; n < branchIdArray.length(); n++) {
							String branchId1 = "";

							org.json.JSONObject jsonObject1 = branchIdArray.getJSONObject(n);
							try {
								branchId1 = jsonObject1.getString("branchId");
							} catch (Exception e) {
								branchId1 = Long.toString(jsonObject1.getLong("branchId"));
							}
							if (branchId1.equals(customer.getBranchId())) {
								list2.add(customer);
							}
						}
					} else {
						if (Long.toString(fetchById.getBranchId()).equals(customer.getBranchId())) {
							list2.add(customer);
						}
					}
				} else if (fetchById.getUserRole().getUserName().equals("SH")) {
					HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
					for (String branchesofuser : listofbranches) {
						if (customer.getBranchId().equals(branchesofuser)) {
							list2.add(customer);
						}
					}

				} else if (fetchById.getUserRole().getUserName().equals("AO")
						&& fetchById.getAllowCreditAccess().equalsIgnoreCase("YES") && eligibleAmount < 130000) {

					HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
					for (String branchesofuser : listofbranches) {
						if (customer.getBranchId().equals(branchesofuser)) {
							list2.add(customer);
						}
					}

				}

				else if (fetchById.getUserRole().getUserName().equals("CREDIT") && eligibleAmount > 130000) {

					HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
					for (String branchesofuser : listofbranches) {
						if (customer.getBranchId().equals(branchesofuser)) {
							list2.add(customer);
						}
					}

				}

				else {
					JSONArray j = new JSONArray(listOfCustomer);
					response.put("Data", j);
					String data = response.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.OK);

				}
			}
			if (list2.isEmpty()) {
				response.put("status", HttpStatus.BAD_REQUEST);
				response.put("message", "list is empty");
				logger.debug("final response" + response.toString());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);

			}
			JSONArray j = new JSONArray(list2);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);

		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchByCustomerIdOrAppNoEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCustomerIdOrAppNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String customerId = jsonObject.getJSONObject("Data").getString("customerId");
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			AocpCustomer aocpCustomer = null;

			if (customerId.isEmpty() && applicationNo.isEmpty()) {
				throw new EmptyInputException("both input field can't be empty");
			}

			else if (customerId.isEmpty()) {
				long applicationNoInLong = Long.parseLong(applicationNo);
				aocpCustomer = aocpCustomerDataService.fetchByApp(applicationNoInLong);

			} else if (applicationNo.isEmpty()) {
				long customerIdInLong = Long.parseLong(customerId);
				aocpCustomer = aocpCustomerDataService.fetchByCustNo(customerIdInLong);
			} else {
				long applicationNoInLong = Long.parseLong(applicationNo);
				aocpCustomer = aocpCustomerDataService.fetchByApp(applicationNoInLong);
			}
			User fetchById = userService.fetchById(X_User_ID);
			String allowCreditAccess = fetchById.getAllowCreditAccess();
			String eligibleAmount1 = aocpCustomer.getEligibleAmount();
			int eligibleAmount = Integer.parseInt(eligibleAmount1);
			int count = 0;
			if (fetchById.getUserRole().getUserName().equals("AM")) {
				if (fetchById.getBranchIdArray() != null) {
					org.json.JSONArray branchIdArray = new org.json.JSONArray(fetchById.getBranchIdArray());
					for (int n = 0; n < branchIdArray.length(); n++) {
						String branchId = "";

						org.json.JSONObject jsonObject1 = branchIdArray.getJSONObject(n);
						try {
							branchId = jsonObject1.getString("branchId");
						} catch (Exception e) {
							branchId = Long.toString(jsonObject1.getLong("branchId"));
						}
						if (branchId.equals(aocpCustomer.getBranchid())) {
							count++;
							break;
						}
					}
					if (count == 0) {
						throw new NoSuchElementException("You are Not authorized to View");
					}
				}
			} else if (fetchById.getUserRole().getUserName().equals("SH")) {
				HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
				for (String branchesofuser : listofbranches) {
					if (aocpCustomer.getBranchid().equals(branchesofuser)) {
						count++;
						break;
					}
				}
				if (count == 0) {
					throw new NoSuchElementException("You are Not authorized to View");
				}
			} else if (fetchById.getUserRole().getUserName().equals("AO")
					&& fetchById.getAllowCreditAccess().equalsIgnoreCase("YES") && eligibleAmount < 130000) {

				HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
				for (String branchesofuser : listofbranches) {
					if (aocpCustomer.getBranchid().equals(branchesofuser)) {
						count++;
						break;
					}
				}
				if (count == 0) {
					throw new NoSuchElementException("You are Not authorized to View");
				}
			}

			else if (fetchById.getUserRole().getUserName().equals("CREDIT") && eligibleAmount > 130000) {

				HashSet<String> listofbranches = userService.getlistofBranch(fetchById.getState());
				for (String branchesofuser : listofbranches) {
					if (aocpCustomer.getBranchid().equals(branchesofuser)) {
						count++;
						break;
					}
				}
				if (count == 0) {
					throw new NoSuchElementException("You are Not authorized to View");
				}
			}
			LocalDate dateOfBirth = aocpCustomer.getDateOfBirth();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
			String dob = dateOfBirth.format(formatter1);
			LocalDate creationDate = aocpCustomer.getCreationDate();
			String creationdate = creationDate.format(formatter1);
			if (aocpCustomer.getAppNoWithProductCode() == null) {
				aocpCustomer.setAppNoWithProductCode("VL-" + aocpCustomer.getApplicationNo());
			}
			LocalDate updatedate = aocpCustomer.getUpdatedate();
			String updatedate1 = updatedate.format(formatter1);
			FetchByCustResponse custResponse = new FetchByCustResponse(aocpCustomer.getApplicationNo(),
					aocpCustomer.getCustomerId(), aocpCustomer.getName(), aocpCustomer.getMobileNo(),
					aocpCustomer.getBranchid(), aocpCustomer.getStatus(), creationdate, updatedate1,
					aocpCustomer.getAppNoWithProductCode());
//FetchAllWebResponse fetchAllWebResponse = new FetchAllWebResponse(aocpCustomer.getApplicationNo(),aocpCustomer.getCustomerId(),aocpCustomer.getName(),aocpCustomer.getMobileNo(),dob,aocpCustomer.getCreatedby(),creationdate,updatedate1);
			List<FetchByCustResponse> list = new ArrayList<>();
			list.add(custResponse);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchAllStateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllByState(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("fetchAllByState start");
		logger.debug("request Data" + jsonRequest);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			// String encryptString =
			// encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			// decryptContainerString = Crypt.decrypt(encryptString,key);
			List<String> listOfState = userService.fetchAllState();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", listOfState);
			JSONArray j = new JSONArray(listOfState);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchAllCityEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllCity(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("fetchAllByState start");
		// logger.debug("request Data" + jsonRequest);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String state = jsonObject.getJSONObject("Data").getString("state");
			if (state.isEmpty()) {
				logger.debug("request is empty" + state);
				throw new EmptyInputException("state cannot be empty");
			}

			List<String> listOfCity = userService.fetchAllCity(state);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", listOfCity);
			JSONArray j = new JSONArray(listOfCity);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchAllAreaEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchAllArea(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("fetchAllByState start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String state = jsonObject.getJSONObject("Data").getString("state");
			String city = jsonObject.getJSONObject("Data").getString("city");
			if (state.isEmpty() || city.isEmpty()) {
				logger.debug("request is empty" + state);
				throw new EmptyInputException("state or city cannot be empty");
			}
			List<String> listOfArea = userService.fetchAllArea(state, city);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", listOfArea);
			JSONArray j = new JSONArray(listOfArea);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchstatemasterency", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchstatemaster(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("fetchAllByState start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject object = jsonObject.getJSONObject("Data");
			String status = object.getString("status");
			String startDate = object.getString("startDate");
			String endDate = object.getString("endDate");
			if (startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "enter a proper Dates");
				logger.debug("final response" + response.toString());
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate startdate = LocalDate.parse(startDate, formatter);
			LocalDate enddate = LocalDate.parse(endDate, formatter);
			List<AocpCustomer> list = aocpCustomerDataService.findByDate(startdate, enddate);
			List<CustomerWebResponse> listOfCustomer = getFilterData(list);

			String state1 = object.getString("state");
			String city = object.getString("city");
			String area = object.getString("area");
			List<Long> list2 = null;
			List<CustomerWebResponse> list3 = new ArrayList<>();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			if (status.equalsIgnoreCase("All")) {
				if (state1.equalsIgnoreCase("All")) {
					if (listOfCustomer.isEmpty()) {

						response.put("status", HttpStatus.BAD_REQUEST.toString());
						response.put("message", "list is empty");
						logger.debug("final response" + response.toString());
						String data = response.toString();
						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);

					}
					int count = listOfCustomer.size();
					logger.debug("final response size" + count);
					JSONArray j = new JSONArray(listOfCustomer);
					response.put("Data", j);
					String data = response.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.OK);
					// return new ResponseEntity<Object>(listOfCustomer, HttpStatus.OK);
				} else {

					if (city.equalsIgnoreCase("All")) {
						list2 = userService.getBranchId(state1);
					} else {
						if (area.equalsIgnoreCase("All")) {
							list2 = userService.getBranchId(state1, city);
						} else {
							list2 = userService.getBranchId(state1, city, area);
						}
					}
				}
				for (long branchid : list2) {
					String branchID = Long.toString(branchid);
					for (CustomerWebResponse aocpCustomerData : listOfCustomer) {
						if (aocpCustomerData.getBranchId().equalsIgnoreCase(branchID)) {
							list3.add(aocpCustomerData);
						}

					}
				}
			} else {
				if (state1.equalsIgnoreCase("All")) {
					List<CustomerWebResponse> list1 = new ArrayList<>();
					for (CustomerWebResponse aocpCustomerData : listOfCustomer) {
						if (aocpCustomerData.getStatus().equalsIgnoreCase(status)) {
							list1.add(aocpCustomerData);
						}
					}
					if (list1.isEmpty()) {
						response.put("status", HttpStatus.BAD_REQUEST.toString());
						response.put("message", "list is empty");
						logger.debug("final response" + response.toString());
						String data = response.toString();
						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);

					}
					// response.put("Data", list1);
					int count = list1.size();
					logger.debug("final response size" + count);
					JSONArray j = new JSONArray(list1);
					response.put("Data", j);
					String data = response.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.OK);
					// return new ResponseEntity<Object>(list1, HttpStatus.OK);
				} else {

					if (city.equalsIgnoreCase("All")) {
						list2 = userService.getBranchId(state1);
					} else {
						if (area.equalsIgnoreCase("All")) {
							list2 = userService.getBranchId(state1, city);
						} else {
							list2 = userService.getBranchId(state1, city, area);
						}
					}
				}

				for (long branchid : list2) {
					String branchID = Long.toString(branchid);
					for (CustomerWebResponse aocpCustomerData : listOfCustomer) {
						if (aocpCustomerData.getStatus().equalsIgnoreCase(status)
								&& aocpCustomerData.getBranchId().equalsIgnoreCase(branchID)) {
							list3.add(aocpCustomerData);
						}

					}
				}
			}
			if (list3.isEmpty()) {
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "list is empty");
				logger.debug("final response" + response.toString());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);
				// return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

			}
			JSONArray j = new JSONArray(list3);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// return new ResponseEntity<Object>(list3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/download/excelEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadCsv(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			// String encryptString =
			// encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			// String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			// JSONObject jsonObject = new JSONObject(decryptContainerString);
			LocalDate enddate = LocalDate.now();
			LocalDate startdate = LocalDate.now().plusMonths(-1);
			List<AocpCustomer> list = aocpCustomerDataService.findTopTenByDate(startdate, enddate);
			List<CustomerWebResponse> listOfCustomer = getFilterData(list);
			if (listOfCustomer.isEmpty()) {
				org.json.simple.JSONObject response1 = new org.json.simple.JSONObject();
				response1.put("status", HttpStatus.BAD_REQUEST.toString());
				response1.put("message", "list is empty");
				// logger.debug("final response" + response.toString());
				String data = response1.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);
				// return new ResponseEntity<Object>(response1, HttpStatus.BAD_REQUEST);
			} else {
				JSONArray j = new JSONArray(listOfCustomer);
				JSONObject object1 = retrieveReportService.writeExcel(j);
				String data = object1.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
				// return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/preApprovedListByDateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("fetchAllByState start");
		logger.debug("request Data" + jsonRequest);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject object = jsonObject.getJSONObject("Data");
			String status = object.getString("status");
			String startDate = object.getString("startDate");
			String endDate = object.getString("endDate");
			String branchId = object.getString("branchId");

			List<PreApprovedList> list = loanInputService.fetchByDate(startDate, endDate, branchId);
			List<PreApprovedList> list1 = new ArrayList<>();
//		if (branchId.isEmpty() && status.isEmpty()) {
//			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
//			response.put("Data", list);
//			logger.debug("final response" + response.toString());
//			return new ResponseEntity<Object>(response, HttpStatus.OK);
//		} 
//		else if (branchId.isEmpty()) {
//			for (PreApprovedList approvedList : list) {
//				if (status.equalsIgnoreCase(approvedList.getStatus())) {
//					list1.add(approvedList);
//				}
//			}
//		} 
			if (status.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				// response.put("Data", list);
				// logger.debug("final response" + response.toString());
				JSONArray j = new JSONArray(list);
				response.put("Data", j);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
				// return new ResponseEntity<Object>(response, HttpStatus.OK);
//			long branchIdInLong = Long.parseLong(branchId);
//			for (PreApprovedList approvedList : list) {
//				if (branchIdInLong == approvedList.getBranchId()) {
//					list1.add(approvedList);
//				}
//			}

			} else {
				for (PreApprovedList approvedList : list) {
					if (status.equalsIgnoreCase(approvedList.getStatus())) {
						list1.add(approvedList);
					}
				}
			}
			if (list1.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "list is empty");
				logger.debug("final response" + response.toString());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);
				// return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", list1);
			JSONArray j = new JSONArray(list1);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// logger.debug("final response" + response.toString());
			// return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/getByCustomerIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByCustomerID(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("fetchAllByState start");
		logger.debug("request Data" + jsonRequest);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String customerId = jsonObject.getJSONObject("Data").getString("customerId");
			if (customerId == null || customerId.isEmpty()) {
				throw new EmptyInputException("input field is empty");
			}
			PreApprovedList preApprovedList = loanInputService.fetchByCustomerId(customerId);
			List<PreApprovedList> list = new ArrayList<>();
			list.add(preApprovedList);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", list);
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// logger.debug("final response" + response.toString());
			// return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchTopTenPreApprovedListEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchTopTenPreApprovedList(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			// String encryptString =
			// encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			// String decryptContainerString = Crypt.decrypt(encryptString,key);
			// JSONObject jsonObject = new JSONObject(decryptContainerString);
			List<PreApprovedList> list = loanInputService.fetchTopTenPreApprovedList();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", list);
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);

			// return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchLastMonthRecordEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchLastMonthRecord(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			// String encryptString =
			// encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			logger.debug("fetchAllByState start");
			LocalDate enddate = LocalDate.now();
			LocalDate startdate = LocalDate.now().plusMonths(-1);
			List<AocpCustomer> list = aocpCustomerDataService.findTopTenByDate(startdate, enddate);
			List<CustomerWebResponse> listOfCustomer = getFilterData(list);

			if (listOfCustomer.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "list is empty");
				logger.debug("final response" + response.toString());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("Data", listOfCustomer);
			JSONArray j = new JSONArray(listOfCustomer);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// int count = listOfCustomer.size();
			// logger.debug("final response count" + count);
			// return new ResponseEntity<Object>(listOfCustomer, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/download/PreApprovedListEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadexcelPreApproved(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=customers.xlsx");
		JSONObject jsonObject = new JSONObject(bm);
		JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("report");
		JSONObject object = approvalService.writeExcel(jsonArray);

		// org.json.simple.JSONObject response1= new org.json.simple.JSONObject();
		// response1.put("message", object.toString());
		// logger.debug("final response"+response1.toString());

		return new ResponseEntity<Object>(object.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/loanDisbursementEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> loanDisbursement(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			if (applicationNo == null || applicationNo.isEmpty()) {
				throw new EmptyInputException("input field is empty");
			}

			long applicationNoInLong = Long.parseLong(applicationNo);
			String loanAccountNumber = jsonObject.getJSONObject("Data").getString("loanAccountNumber");
			String LoanCreatedDate = jsonObject.getJSONObject("Data").getString("LoanCreatedDate");
			String SanctionLoanAmount = jsonObject.getJSONObject("Data").getString("SanctionLoanAmount");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate loancreationDate = LocalDate.parse(LoanCreatedDate, formatter);
			String status = "DISBURSED";
			AocpvLoanCreation aocpvLoanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
			LocalDate now = LocalDate.now();
			aocpvLoanCreation.setLoanCreationDate(loancreationDate);
			aocpvLoanCreation.setUpdetedDate(now);
			aocpvLoanCreation.setLoanAccoutNumber(loanAccountNumber);
			aocpvLoanCreation.setSanctionedLoanAmount(SanctionLoanAmount);
			String massege = aocpvLoanCreationService.update(aocpvLoanCreation);
			userLogService.save(X_User_ID, applicationNo, status, "DISBURSED", 0);
			long customerId = aocpCustomerDataService.statusChange(applicationNo, status);
			loanInputService.statusChange(customerId, status);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "Loan Disbursed");
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			// logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
			// logger.debug("final response" + response.toString());
			// return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/download/fetchstatemasterEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadfetchstatemaster(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=customers.xlsx");
		// JSONObject jsonObject = new JSONObject(jsonRequest);
		// JSONArray jsonArray =
		// jsonObject.getJSONObject("Data").getJSONArray("report");
		logger.debug("fetchAllByState start");
		logger.debug("request Data" + jsonRequest);
		JSONObject jsonObject = new JSONObject(jsonRequest);
		JSONObject object = jsonObject.getJSONObject("Data");
		String status = object.getString("status");
		String startDate = object.getString("startDate");
		String endDate = object.getString("endDate");
		if (startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
			org.json.simple.JSONObject response1 = new org.json.simple.JSONObject();
			response1.put("status", HttpStatus.BAD_REQUEST.toString());
			response1.put("message", "enter a proper Dates");
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response1, HttpStatus.OK);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate startdate = LocalDate.parse(startDate, formatter);
		LocalDate enddate = LocalDate.parse(endDate, formatter);
		List<AocpCustomer> list = aocpCustomerDataService.findByDate(startdate, enddate);
		List<CustomerWebResponse> listOfCustomer = getFilterData(list);

		String state1 = object.getString("state");
		String city = object.getString("city");
		String area = object.getString("area");
		List<Long> list2 = null;
		List<CustomerWebResponse> list3 = new ArrayList<>();

		if (status.equalsIgnoreCase("All")) {
			if (state1.equalsIgnoreCase("All")) {
				if (listOfCustomer.isEmpty()) {
					org.json.simple.JSONObject response1 = new org.json.simple.JSONObject();
					response1.put("status", HttpStatus.BAD_REQUEST.toString());
					response1.put("message", "list is empty");
					logger.debug("final response" + response1.toString());
					return new ResponseEntity<Object>(response1, HttpStatus.BAD_REQUEST);

				}
				int count = listOfCustomer.size();
				logger.debug("final response size" + count);
				JSONArray j = new JSONArray(listOfCustomer);
				JSONObject object1 = retrieveReportService.writeExcel(j);
				return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
			} else {

				if (city.equalsIgnoreCase("All")) {
					list2 = userService.getBranchId(state1);
				} else {
					if (area.equalsIgnoreCase("All")) {
						list2 = userService.getBranchId(state1, city);
					} else {
						list2 = userService.getBranchId(state1, city, area);
					}
				}
			}
			for (long branchid : list2) {
				String branchID = Long.toString(branchid);
				for (CustomerWebResponse aocpCustomerData : listOfCustomer) {
					if (aocpCustomerData.getBranchId().equalsIgnoreCase(branchID)) {
						list3.add(aocpCustomerData);
					}

				}
			}
		} else {
			if (state1.equalsIgnoreCase("All")) {
				List<CustomerWebResponse> list1 = new ArrayList<>();
				for (CustomerWebResponse aocpCustomerData : listOfCustomer) {
					if (aocpCustomerData.getStatus().equalsIgnoreCase(status)) {
						list1.add(aocpCustomerData);
					}
				}
				if (list1.isEmpty()) {
					org.json.simple.JSONObject response1 = new org.json.simple.JSONObject();
					response1.put("status", HttpStatus.BAD_REQUEST.toString());
					response1.put("message", "list is empty");
					logger.debug("final response" + response1.toString());
					return new ResponseEntity<Object>(response1, HttpStatus.BAD_REQUEST);

				}
				int count = list1.size();
				logger.debug("final response size" + count);
				JSONArray j = new JSONArray(list1);
				JSONObject object1 = retrieveReportService.writeExcel(j);
				return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
				// return new ResponseEntity<Object>(list1, HttpStatus.OK);
			} else {

				if (city.equalsIgnoreCase("All")) {
					list2 = userService.getBranchId(state1);
				} else {
					if (area.equalsIgnoreCase("All")) {
						list2 = userService.getBranchId(state1, city);
					} else {
						list2 = userService.getBranchId(state1, city, area);
					}
				}
			}

			for (long branchid : list2) {
				String branchID = Long.toString(branchid);
				for (CustomerWebResponse aocpCustomerData : listOfCustomer) {
					if (aocpCustomerData.getStatus().equalsIgnoreCase(status)
							&& aocpCustomerData.getBranchId().equalsIgnoreCase(branchID)) {
						list3.add(aocpCustomerData);
					}

				}
			}
		}
		if (list3.isEmpty()) {
			org.json.simple.JSONObject response1 = new org.json.simple.JSONObject();
			response1.put("status", HttpStatus.BAD_REQUEST.toString());
			response1.put("message", "list is empty");
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response1, HttpStatus.BAD_REQUEST);
		}

		JSONArray j = new JSONArray(list3);
		JSONObject object1 = retrieveReportService.writeExcel(j);
		return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
	}

	public List<CustomerWebResponse> getFilterData(List<AocpCustomer> list) {
		List<CustomerWebResponse> listOfCustomer = new ArrayList<>();

		for (AocpCustomer data : list) {
			String address_Line1 = "NA";
			String state = "NA";
			String postal = "NA";
			String centerId = "NA";
			String schemeId = "NA";
			String purposeCategoryId = "NA";
			String loanPurposeId = "NA";
			String nomineeName = "NA";
			String savingAccount = "NA";
			String sanctionAmount = "NA";
			String sanctionTENURE = "NA";
			String amount = "NA";
			String existingLoanPurpose = "NA";
			String finalSanctionAmount = "NA";
			String remarks = "NA";
			String buisness = "NA";
			String residenceStability = "NA";
			String roofType = "NA";
			String utilityBill = "NA";
			String vintage = "NA";
			String mobileNoVerify = "NA";
			String updatedBy = "NA";
			String purposeid = "NA";
			String subCategoryPurposeId = "NA";
			String AmountRequested = "NA";
			String interestRate = "NA";
			String OriginationFees = "NA";
			String PaymentAmount = "NA";
			String Term = "NA";
			String TotalAmountFinanced = "NA";
			String InterestAmount = "NA";

			if (data.getPurposeId() != null) {
				purposeid = data.getPurposeId();
			}
			if (data.getSubCategoryPurposeId() != null) {
				subCategoryPurposeId = data.getSubCategoryPurposeId();
			}
			if (data.getUtilityBill() != null) {
				utilityBill = data.getUtilityBill();
			}
			if (data.getMobileNoVerify() != null) {
				mobileNoVerify = data.getMobileNoVerify();
			}
			if (data.getVintage() != null) {
				vintage = data.getVintage();
			}
			if (data.getExistingLoanPurpose() != null) {
				existingLoanPurpose = data.getExistingLoanPurpose();
			}
			if (data.getRemarks() != null) {
				try {
					String remarks2 = data.getRemarks();
					org.json.JSONArray remark = new org.json.JSONArray(remarks2);
					JSONObject jsonObject = remark.getJSONObject(remark.length() - 1);
					if (jsonObject.has("UPDATEDBY")) {
						updatedBy = jsonObject.getString("UPDATEDBY");
					}
					remarks = data.getRemarks();
				} catch (JSONException e) {
					remarks = data.getRemarks();
				}

			}
			if (data.getBuisness() != null) {
				buisness = data.getBuisness();
			}
			if (data.getEligibleAmount() != null) {
				amount = data.getEligibleAmount();
			}
			if (data.getResidenceStability() != null) {
				residenceStability = data.getResidenceStability();
			}
			if (data.getRoofType() != null) {
				roofType = data.getRoofType();
			}
			String address1 = data.getAddress();
			if (address1 == null || address1.isEmpty()) {

			} else {
				try {
					org.json.JSONArray addressInJson = new org.json.JSONArray(data.getAddress());

					for (int n = 0; n < addressInJson.length(); n++) {
						JSONObject jsonObject2 = addressInJson.getJSONObject(n);
						address_Line1 = jsonObject2.getString("ADDRESS_LINE1");
						postal = jsonObject2.getString("PINCODE");
						state = jsonObject2.getString("STATE");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			String finalsanctionAmount = data.getFinalsanctionAmount();

			if (finalsanctionAmount != null) {
				finalSanctionAmount = finalsanctionAmount;
				org.json.JSONArray finalSanctionAmountlist = new org.json.JSONArray(finalsanctionAmount);
				for (int n = 0; n < finalSanctionAmountlist.length(); n++) {
					JSONObject jsonObject2 = finalSanctionAmountlist.getJSONObject(n);
					if (jsonObject2.has("sanctionAmount")) {
						sanctionAmount = jsonObject2.getString("sanctionAmount");
					}
					if (jsonObject2.has("sanctionTENURE")) {
						sanctionTENURE = jsonObject2.getString("sanctionTENURE");
					}

				}
			}
			LocalDate dateOfBirth = data.getDateOfBirth();
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
			String dob1 = dateOfBirth.format(formatter1);
			LocalDate creationDate = data.getCreationDate();
			LocalDate updatedate = data.getUpdatedate();
			String creationDateString = creationDate.format(formatter1);
			String updatedateString = updatedate.format(formatter1);
			String requestedloanResponse = data.getResponseEmi();
			if (requestedloanResponse != null) {
				String OutstandingBalance = null;
				JSONObject jsonObject2 = new JSONObject(requestedloanResponse);
				if (jsonObject2.has("AMOUNTREQUESTED")) {
					AmountRequested = jsonObject2.getString("AMOUNTREQUESTED");
				}
				if (jsonObject2.has("INTERESTRATE")) {
					interestRate = jsonObject2.getString("INTERESTRATE");
				}
				if (jsonObject2.has("ORIGINATIONFEES")) {
					OriginationFees = jsonObject2.getString("ORIGINATIONFEES");
				}
				if (jsonObject2.has("PAYMENTAMOUNT")) {
					PaymentAmount = jsonObject2.getString("PAYMENTAMOUNT");
				}
				if (jsonObject2.has("TERM")) {
					Term = jsonObject2.getString("TERM");
				}
				if (jsonObject2.has("TOTALAMOUNTFINANCED")) {
					TotalAmountFinanced = jsonObject2.getString("TOTALAMOUNTFINANCED");
				}

				if (jsonObject2.has("INTERESTAMOUNT")) {
					InterestAmount = jsonObject2.getString("INTERESTAMOUNT");
				}

			}
			CustomerWebResponse customerResponse = new CustomerWebResponse(data.getMobileNo(), data.getCustomerId(),
					data.getName(), dob1, address_Line1, state, postal, data.getBranchid(), amount, data.getStatus(),
					creationDateString, updatedateString, data.getApplicationNo(), buisness, data.getCreatedby(),
					existingLoanPurpose, mobileNoVerify, data.getMonthlyBalance(), data.getOther(), remarks,
					data.getRent(), residenceStability, roofType, data.getTotal(), data.getTotalMonthlyEmi(),
					data.getTotalMonthlyIncome(), data.getTransportation(), utilityBill, vintage, centerId, schemeId,
					purposeCategoryId, loanPurposeId, nomineeName, savingAccount, sanctionAmount, sanctionTENURE,
					finalSanctionAmount, updatedBy, purposeid, subCategoryPurposeId, AmountRequested, interestRate,
					OriginationFees, PaymentAmount, Term, TotalAmountFinanced, InterestAmount);
			listOfCustomer.add(customerResponse);
		}
		return listOfCustomer;

	}

	@RequestMapping(value = "/updateAppDetailsWebEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> updateAppDetailsWeb(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			long applicationNoInLong = Long.parseLong(applicationNo);
			String houseOwnership = jsonObject.getJSONObject("Data").getString("houseOwnership");
			String relationshipWithOwner = jsonObject.getJSONObject("Data").getString("relationshipWithOwner");
			String purposeOfLoan = jsonObject.getJSONObject("Data").getString("purposeOfLoan");
			String subCategory = jsonObject.getJSONObject("Data").getString("subCategory");
			String purposeId = jsonObject.getJSONObject("Data").getString("purposeId");
			String subCategoryPurposeId = jsonObject.getJSONObject("Data").getString("subCategoryPurposeId");

			if (applicationNo.isEmpty() || houseOwnership.isEmpty() || relationshipWithOwner.isEmpty()
					|| purposeOfLoan.isEmpty() || subCategory.isEmpty() || purposeId.isEmpty()
					|| subCategoryPurposeId.isEmpty()) {
				throw new EmptyInputException("input field is empty");
			}

			AocpCustomer aocpCustomer = aocpCustomerDataService.fetchByApp(applicationNoInLong);
			if (!aocpCustomer.getHouseOwnership().equalsIgnoreCase(houseOwnership)) {
				aocpCustomer.setHouseOwnership(houseOwnership);
			}
			if (!aocpCustomer.getRelationshipWithOwner().equalsIgnoreCase(relationshipWithOwner)) {
				aocpCustomer.setRelationshipWithOwner(relationshipWithOwner);
			}
			if (!aocpCustomer.getPurposeOfLoan().equalsIgnoreCase(purposeOfLoan)) {
				aocpCustomer.setPurposeOfLoan(purposeOfLoan);
			}
			if (!aocpCustomer.getSubCategory().equalsIgnoreCase(subCategory)) {
				aocpCustomer.setSubCategory(subCategory);
			}
			if (!aocpCustomer.getPurposeId().equalsIgnoreCase(purposeId)) {
				aocpCustomer.setPurposeId(purposeId);
			}
			if (!aocpCustomer.getSubCategoryPurposeId().equalsIgnoreCase(subCategoryPurposeId)) {
				aocpCustomer.setSubCategoryPurposeId(subCategoryPurposeId);
			}
			String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", saveSingleData);
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			// logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/getDocumentsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getDocuments(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("getDocumentsEncy Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			List<Image> list = aocpvImageService.getsanctionLetterAndagreement(applicationNo);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			// logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/verifyDocumentsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> verifyDocuments(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("verifyDocuments Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String sanctionLetterIsVerify = jsonObject.getJSONObject("Data").getString("sanctionLetterIsVerify");
			String aggreementLetterIsVerify = jsonObject.getJSONObject("Data").getString("aggreementLetterIsVerify");
			long applicationNoInlong = Long.parseLong(applicationNo);
			AocpvLoanCreation findByApplicationNo = aocpvLoanCreationService.findByApplicationNo(applicationNoInlong);
			findByApplicationNo.setSanctionLetterIsVerify(sanctionLetterIsVerify);
			findByApplicationNo.setAggreementLetterIsVerify(aggreementLetterIsVerify);
			if (sanctionLetterIsVerify.equalsIgnoreCase("YES") && aggreementLetterIsVerify.equalsIgnoreCase("YES")) {
				findByApplicationNo.setIsVerify("YES");
			}
			aocpvLoanCreationService.saveData(findByApplicationNo);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "data saved");
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			// logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchBreRejectDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchBreRejectDataEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("fetchBreRejectData Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			// userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationNo);
			String breResponse = applicationDetails.getBreResponse();
			JSONObject json = new JSONObject(breResponse);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			String city = "";
			String dateofBirth = "";
			String fullname = "";
			String address = "";
			String state = "";
			String gender = "";
			long pincode = 0;
			if (applicationDetails.getEkycResponse() != null) {
				org.json.JSONObject ekyc = new org.json.JSONObject(applicationDetails.getEkycResponse());
				JSONObject PoaResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData")
						.getJSONObject("Poa");
				JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData")
						.getJSONObject("Poi");
				if (PoaResponse.has("street")) {
					address = PoaResponse.getString("street");
				} else if (PoaResponse.has("loc")) {
					city = PoaResponse.getString("loc");
				}
				pincode = PoaResponse.getLong("pc");
				dateofBirth = PoiResponse.getString("dob");
				fullname = PoiResponse.getString("name");
				state = PoaResponse.getString("state");
				gender = PoiResponse.getString("gender");
			}
			ApplicationDetailsResponseWeb appDetails = new ApplicationDetailsResponseWeb();
			appDetails.setName(fullname);
			appDetails.setDob(dateofBirth);
			appDetails.setApplicationNo(applicationNo);
			appDetails.setAppNoWithProductCode(applicationDetails.getAppNoWithProductCode());
			appDetails.setMobileNo(applicationDetails.getMobileNo());
			appDetails.setCreationDate(applicationDetails.getCreatets());
			appDetails.setUpdatedDate(applicationDetails.getUpdateDatets());
			appDetails.setCreatedBy(applicationDetails.getCreatedBy());
			appDetails.setAddress(address);
			appDetails.setState(state);
			appDetails.setGender(gender);
			appDetails.setPincode(Long.toString(pincode));
			List<AocpvImages> listAocpvImage = aocpvImageService
					.getByApplicationNoAnddocument(Long.parseLong(applicationNo), "ekyc_photo");
			List<Image> listOfImages = new ArrayList<>();
			if (listAocpvImage.isEmpty()) {
				// throw new EmptyInputException("Images Lists are Empty");
			} else {
				for (AocpvImages aocpvImages : listAocpvImage) {
					String geoLocation = aocpvImages.getGeoLocation();
					JSONObject jsonObjectImage = new JSONObject(geoLocation);
					String pimage = jsonObjectImage.getString("image");
					String pLat = jsonObjectImage.getString("Lat");
					String pLong = jsonObjectImage.getString("Long");
					String pAddress = jsonObjectImage.getString("Address");
					String ptimestamp = jsonObjectImage.getString("timestamp");

					GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);

					String documenttype = aocpvImages.getDocumenttype();
					String imageName = aocpvImages.getImageName();
					String type = aocpvImages.getType();
					long size = aocpvImages.getSize();
					String member = aocpvImages.getMember();
					byte[] images2 = aocpvImages.getImages();
					String encoded = Base64.getEncoder().encodeToString(images2);

					Image images = new Image(documenttype, imageName, type, size, encoded, member, geolocation);

					listOfImages.add(images);
				}
			}
			appDetails.setImages(listOfImages);
			response.put("Data", json);
			JSONObject j = new JSONObject(appDetails);
			response.put("Data1", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			// logger.debug("final response count " + list.size());
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/uploadDocumentsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> uploadDocuments(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws Exception {

		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = "SELF";
			if (jsonObject.getJSONObject("Data").has("member")) {
				member = jsonObject.getJSONObject("Data").getString("member");
			}
			long applicationno = Long.parseLong(applicationNo);
			String base64Image = jsonObject.getJSONObject("Data").getString("base64Image");
			byte[] image = Base64.getDecoder().decode(base64Image);
			logger.debug("image save to db start");
			aocpvImageService.saveImageWeb(image, document, applicationno, member);
			logger.debug("image save to db sucessfully");
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "saveImage");
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/fetchBreResponseEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchBreResponse(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationNo);
			String breResponse = applicationDetails.getBreResponse();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", breResponse);
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/userlogdetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> userlogdetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {

			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
//logger.debug("start request" + jsonRequest.toString());

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			logger.debug("userlogdetails Start");
			logger.debug("request Data" + decryptContainerString);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			if (applicationNo == null || applicationNo.isEmpty()) {
				throw new EmptyInputException("input field is empty");
			}
			long applicationNoInLong = Long.parseLong(applicationNo);

			List<UserLog> list = userLogService.getByApplicationNo(applicationNoInLong);
			JSONArray j = new JSONArray(list);
			int count = list.size();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("status", HttpStatus.OK);
			response.put("Data", j);
			logger.debug("final response" + count);
			String data = response.toString();

			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			// System.out.println(data3);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchBrNetLoanDetailsEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchBrNetLoanDetailsEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("fetchByCustomerId start");
		logger.debug("request Data " + jsonRequest);
		JSONObject header = new JSONObject();
		header.put("X-Request-ID", "Channel_Name");
		header.put("X-Correlation-ID", "23456789");
		header.put("X-From-ID", "CB");
		header.put("X-To-ID", "Channel_Name");
		header.put("X-User-ID", "Channel_Name");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String CustomerId = jsonObject.getJSONObject("Data").getString("CustomerId");
			long CustomerIdInLong = Long.parseLong(CustomerId);
			PreApprovedListVikasLoan loanDetail = loanInputService.fetchByCustomerId(CustomerIdInLong);
			String accountNo = loanDetail.getAccountNo();
			JSONObject loanDetails = loanInputService.getBRNetLoanDetails(accountNo, header);
			logger.debug("response" + loanDetails.toString());
			String Data3 = loanDetails.getString("data");
			JSONObject Data4 = new JSONObject(Data3);
			if (Data4.has("Data")) {
				JSONObject loanDtls = Data4.getJSONObject("Data").getJSONObject("AccountDetails")
						.getJSONObject("LoanDtls");
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("LoanDetails", loanDtls);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			}
			String data = Data4.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);

		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/fetchPreApprvalMfiDataEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchCustomerDataEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("fetchByCustomerId start");
		logger.debug("request Data " + jsonRequest);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String CustomerId = jsonObject.getJSONObject("Data").getString("CustomerId");
			long CustomerIdInLong = Long.parseLong(CustomerId);
			List<PreApprovedListVikasLoan> list = loanInputService.fetchPreApprovalMfiData(CustomerIdInLong);
			JSONArray arr = new JSONArray(list);
			System.out.println(arr.getJSONObject(0));
			JSONObject Data1 = arr.getJSONObject(0);
			String data = Data1.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/fetchstatemasterEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getRetriveReport(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String startDate = jsonObject.getJSONObject("Data").getString("startDate");
			String endDate = jsonObject.getJSONObject("Data").getString("endDate");
			String status = jsonObject.getJSONObject("Data").getString("status");
			String state = jsonObject.getJSONObject("Data").getString("state");
			String city = jsonObject.getJSONObject("Data").getString("city");
			String area = jsonObject.getJSONObject("Data").getString("area");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate startdate = LocalDate.parse(startDate, formatter);
			LocalDate enddate = LocalDate.parse(endDate, formatter);

			List<CustomerWebResponse> list = aocpCustomerDataService.getRetriveReportData(startdate.toString(),
					enddate.toString(), status, state, city, area);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);

		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
}
