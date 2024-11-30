package com.suryoday.LoanTracking.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.LoanTracking.Pojo.LoanTrackingDisbursement;
import com.suryoday.LoanTracking.Pojo.LoanTrackingUserLog;
import com.suryoday.LoanTracking.Repository.LoanTrackingUserLogRepo;
import com.suryoday.LoanTracking.Service.LoanTrackingDisbursementService;
import com.suryoday.LoanTracking.Service.LoanTrackingUserLogService;
import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.pojo.User;

@Component
@RestController
@RequestMapping(value = "/loanTracking")
public class LoanTrackingDisbursementController {
	private static Logger logger = LoggerFactory.getLogger(LoanTrackingDisbursementController.class);
	@Autowired
	LoanTrackingDisbursementService disbursementservice;
	@Autowired
	LoanTrackingUserLogService userlogservice;
	@Autowired
	LoanTrackingUserLogRepo userlogrepo;

	@RequestMapping(value = "/saveData", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> saveData(@RequestBody String bm,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-Flag", required = true) String X_Request_Flag) {
		if (X_Request_Flag.equalsIgnoreCase("Add")) {
			long id = 1;
			Optional<Long> optional = userlogrepo.fetchLastId();
			if (optional.isPresent()) {
				id = optional.get();
				id++;
			}
			String applicationNo = disbursementservice.createApplicationNo();
			JSONObject jsonObject = new JSONObject(bm);
			String productType = jsonObject.getJSONObject("Data").getString("ProductType");
			String panNo = jsonObject.getJSONObject("Data").getString("PanNo");
			String name = jsonObject.getJSONObject("Data").getString("Name");
			String isVerify = jsonObject.getJSONObject("Data").getString("IsVerify");
			String loanAmount = jsonObject.getJSONObject("Data").getString("LoanAmount");
			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String assignTo = jsonObject.getJSONObject("Data").getString("AssignTo");
			String assignRole = jsonObject.getJSONObject("Data").getString("AssignRole");
			String createdBy = jsonObject.getJSONObject("Data").getString("CreatedBy");
			String currenStage = jsonObject.getJSONObject("Data").getString("CurrentStage");
			String remarks = jsonObject.getJSONObject("Data").getString("Remarks");
			String city = jsonObject.getJSONObject("Data").getString("City");
			String state = jsonObject.getJSONObject("Data").getString("State");
			LocalDateTime now = LocalDateTime.now();
			String appNoWithProductCode = productType + applicationNo;
			logger.debug(appNoWithProductCode);
			LoanTrackingDisbursement disbursement = new LoanTrackingDisbursement();
			disbursement.setApplicationId(Long.parseLong(applicationNo));
			disbursement.setName(name);
			disbursement.setPanNo(panNo);
			disbursement.setIsVerify(isVerify);
			disbursement.setLoanAmount(loanAmount);
			disbursement.setBranchId(branchId);
			disbursement.setAssignTo(assignTo);
			disbursement.setAssignRole(assignRole);
			disbursement.setCreatedBy(createdBy);
			disbursement.setAppNoWithProductCode(appNoWithProductCode);
			disbursement.setProductType(productType);
			disbursement.setCity(city);
			disbursement.setState(state);
			disbursement.setRemarks(remarks);
			disbursement.setStartDate(now);
			disbursement.setIsLocked("N");
			disbursement.setEndDate(now);
			if (assignTo.equalsIgnoreCase("ALL") && assignRole.equalsIgnoreCase("CREDIT")) {
				disbursement.setCurrentStage("CREDIT_ADMIN");
				disbursementservice.save(disbursement, panNo, productType);
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage("CREDIT_ADMIN");
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(assignTo);
				userlog.setFromStatus(currenStage);
				userlog.setToStatus("CREDIT_ADMIN");
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				userlogservice.save(userlog);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("ApplicationNo", applicationNo);
				response.put("Success", "Save data successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			} else if (currenStage.equalsIgnoreCase("RO") || currenStage.equalsIgnoreCase("SALES_DISCREPANCY")
					|| currenStage.equalsIgnoreCase("ADMIN")) {
				disbursement.setCurrentStage("CREDIT");
			} else {
				disbursement.setCurrentStage(currenStage);
			}

			disbursementservice.save(disbursement, panNo, productType);

			LoanTrackingUserLog userlog = new LoanTrackingUserLog();
			userlog.setId(id);
			userlog.setApplicationNo(Long.parseLong(applicationNo));
			userlog.setCreatedBy(createdBy);
			userlog.setCurrentStage(assignRole);
			userlog.setStartDate(now);
			userlog.setEndDate(now);
			userlog.setRemarks(remarks);
			userlog.setAssignTo(assignTo);
			userlog.setFromStatus(currenStage);
			userlog.setToStatus(assignRole);
			userlog.setAppNoWithProductCode(appNoWithProductCode);
			userlog.setProductType(productType);
			userlogservice.save(userlog);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("ApplicationNo", applicationNo);
			response.put("Success", "Save data successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		} else if (X_Request_Flag.equalsIgnoreCase("Edit")) {
			long id = 1;
			Optional<Long> optional = userlogrepo.fetchLastId();
			if (optional.isPresent()) {
				id = optional.get();
				id++;
			}
			JSONObject jsonObject = new JSONObject(bm);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String productType = jsonObject.getJSONObject("Data").getString("ProductType");
			String name = jsonObject.getJSONObject("Data").getString("Name");
			String panNo = jsonObject.getJSONObject("Data").getString("PanNo");
			String isVerify = jsonObject.getJSONObject("Data").getString("IsVerify");
			String loanAmount = jsonObject.getJSONObject("Data").getString("LoanAmount");
			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String assignTo = jsonObject.getJSONObject("Data").getString("AssignTo");
			String assignRole = jsonObject.getJSONObject("Data").getString("AssignRole");
			String createdBy = jsonObject.getJSONObject("Data").getString("CreatedBy");
			String currenStage = jsonObject.getJSONObject("Data").getString("CurrentStage");
			String remarks = jsonObject.getJSONObject("Data").getString("Remarks");
			LocalDateTime now = LocalDateTime.now();
			String appNoWithProductCode = productType + applicationNo;
			LoanTrackingDisbursement byApplicationId = disbursementservice.getByApplicationId(Long.parseLong(applicationNo));
			LoanTrackingDisbursement disbursement = new LoanTrackingDisbursement();
			disbursement.setApplicationId(Long.parseLong(applicationNo));
			disbursement.setName(name);
			disbursement.setPanNo(panNo);
			disbursement.setIsVerify(isVerify);
			disbursement.setLoanAmount(loanAmount);
			disbursement.setBranchId(branchId);
			disbursement.setAssignTo(assignTo);
			disbursement.setAssignRole(assignRole);
			disbursement.setAppNoWithProductCode(appNoWithProductCode);
			disbursement.setProductType(productType);
			disbursement.setCreatedBy(createdBy);
			disbursement.setRemarks(remarks);
			disbursement.setStartDate(byApplicationId.getStartDate());
			disbursement.setEndDate(now);
			if (currenStage.equalsIgnoreCase("RO") || currenStage.equalsIgnoreCase("SALES_DISCREPANCY")
					|| currenStage.equalsIgnoreCase("CREDIT_ADMIN") || currenStage.equalsIgnoreCase("Reject")) {
				disbursement.setCurrentStage("CREDIT");
			} else if (currenStage.equalsIgnoreCase("CREDIT") && assignRole.equalsIgnoreCase("Reject")) {
				disbursement.setCurrentStage("Reject");
			} else if (currenStage.equalsIgnoreCase("CREDIT") && assignRole.equalsIgnoreCase("SALES_DISCREPANCY")) {
				disbursement.setCurrentStage("SALES_DISCREPANCY");
			} else if (assignRole.equalsIgnoreCase("CREDIT_OPS") && assignTo.equalsIgnoreCase("ALL")) {
				disbursement.setCurrentStage("CREDITOPS_ADMIN");
				disbursementservice.save(disbursement);
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage("CREDITOPS_ADMIN");
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(assignTo);
				userlog.setFromStatus(currenStage);
				userlog.setToStatus("CREDITOPS_ADMIN");
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				userlogservice.save(userlog);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Success", "Data updated successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			} else if (assignRole.equalsIgnoreCase("OPS") && assignTo.equalsIgnoreCase("ALL")) {
				disbursement.setCurrentStage("OPS_ADMIN");
				disbursementservice.save(disbursement);
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage("OPS_ADMIN");
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(assignTo);
				userlog.setFromStatus(currenStage);
				userlog.setToStatus("OPS_ADMIN");
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				userlogservice.save(userlog);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Success", "Data updated successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			} else if (currenStage.equalsIgnoreCase("CREDIT_OPS") && assignRole.equalsIgnoreCase("SALES_DISCREPANCY")) {
				disbursement.setCurrentStage("SALES_DISCREPANCY");
			} else if (currenStage.equalsIgnoreCase("CREDIT_OPS")
					&& assignRole.equalsIgnoreCase("SALESANDCREDIT_DISCREPANCY")) {
				disbursement.setCurrentStage("SALESANDCREDIT_DISCREPANCY");
			} else if (currenStage.equalsIgnoreCase("OPS") && assignRole.equalsIgnoreCase("CREDITOPS_DISCREPANCY")) {
				disbursement.setCurrentStage("CREDITOPS_DISCREPANCY");
			} else if (currenStage.equalsIgnoreCase("OPS") && assignRole.equalsIgnoreCase("SALES_DISCREPANCY")) {
				disbursement.setCurrentStage("SALES_DISCREPANCY");
			} else if (currenStage.equalsIgnoreCase("CREDIT") || currenStage.equalsIgnoreCase("CREDIT_DISCREPANCY")
					|| currenStage.equalsIgnoreCase("CREDITOPS_ADMIN")) {
				disbursement.setCurrentStage("CREDIT_OPS");
			} else if (currenStage.equalsIgnoreCase("CREDIT_OPS")
					|| currenStage.equalsIgnoreCase("CREDITOPS_DISCREPANCY")
					|| currenStage.equalsIgnoreCase("OPS_ADMIN")) {
				disbursement.setCurrentStage("OPS");
			} else if (currenStage.equalsIgnoreCase("SALESANDCREDIT_DISCREPANCY")) {
				disbursement.setCurrentStage("PARTIAL_DISCREPANCY");
			} else if (currenStage.equalsIgnoreCase("PARTIAL_DISCREPANCY")) {
				disbursement.setCurrentStage("CREDIT_OPS");
			} else if (currenStage.equalsIgnoreCase("OPS")) {
				disbursement.setCurrentStage("END");
				disbursement.setEndDate(now);
			} else {
				disbursement.setCurrentStage(currenStage);
			}
			disbursementservice.save(disbursement);

			if (assignRole.equalsIgnoreCase("SALESANDCREDIT_DISCREPANCY")
					&& currenStage.equalsIgnoreCase("CREDIT_OPS")) {
				String role1 = "CREDIT";
				String role2 = "CREDIT_OPS";
				List<LoanTrackingUserLog> list = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1,
						role2);
				String createdBy2 = list.get(0).getCreatedBy();
				logger.debug(createdBy2);
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id++);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage(assignRole);
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(createdBy2);
				userlog.setFromStatus("CREDIT_OPS");
				userlog.setToStatus("SALESANDCREDIT_DISCREPANCY");
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				String save = userlogservice.saveuserlog(userlog);
				if (save.equalsIgnoreCase("Success")) {
					String role3 = "RO";
					String role4 = "CREDIT";
					List<LoanTrackingUserLog> list2 = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo),
							role3, role4);
					String createdBy3 = list2.get(0).getCreatedBy();
					logger.debug(createdBy3);
					LoanTrackingUserLog userlog2 = new LoanTrackingUserLog();
					userlog2.setId(id++);
					userlog2.setApplicationNo(Long.parseLong(applicationNo));
					userlog2.setCreatedBy(createdBy);
					userlog2.setCurrentStage(assignRole);
					userlog2.setStartDate(now);
					userlog2.setEndDate(now);
					userlog2.setRemarks(remarks);
					userlog2.setAssignTo(createdBy3);
					userlog2.setFromStatus("CREDIT_OPS");
					userlog2.setToStatus("SALESANDCREDIT_DISCREPANCY");
					userlog.setAppNoWithProductCode(appNoWithProductCode);
					userlog.setProductType(productType);
					userlogservice.save(userlog2);
				}
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Success", "Data updated successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			} else if (currenStage.equals("SALESANDCREDIT_DISCREPANCY")) {
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage("PARTIAL_DISCREPANCY");
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(assignTo);
				userlog.setFromStatus(currenStage);
				userlog.setToStatus("PARTIAL_DISCREPANCY");
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				userlogservice.save(userlog);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Success", "Data updated successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			} else if (currenStage.equals("PARTIAL_DISCREPANCY")) {
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage("CREDIT");
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(assignTo);
				userlog.setFromStatus(currenStage);
				userlog.setToStatus("CREDIT_OPS");
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				userlogservice.save(userlog);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Success", "Data updated successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			} else {
				LoanTrackingUserLog userlog = new LoanTrackingUserLog();
				userlog.setId(id);
				userlog.setApplicationNo(Long.parseLong(applicationNo));
				userlog.setCreatedBy(createdBy);
				userlog.setCurrentStage(assignRole);
				userlog.setStartDate(now);
				userlog.setEndDate(now);
				userlog.setRemarks(remarks);
				userlog.setAssignTo(assignTo);
				userlog.setFromStatus(currenStage);
				userlog.setToStatus(assignRole);
				userlog.setAppNoWithProductCode(appNoWithProductCode);
				userlog.setProductType(productType);
				userlogservice.save(userlog);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Success", "Data updated successfully");
				return new ResponseEntity(response, HttpStatus.OK);
			}
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Error", "BAD REQUEST");
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/fetchByApplicationId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationId(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchByApplicationId start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String applicationId = jsonObject.getJSONObject("Data").getString("ApplicationId");
		List<LoanTrackingDisbursement> list = disbursementservice.fetchByApplicationId(applicationId);
		List<LoanTrackingUserLog> list1 = userlogservice.fetchByApplicationNo(applicationId);
		logger.debug("List" + list);
		logger.debug("db Call end" + list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list.get(0));
		response.put("userLog", list1);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchByDate start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
		String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
		String createdBy = jsonObject.getJSONObject("Data").getString("CreatedBy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter2);
		String userRole = jsonObject.getJSONObject("Data").getString("UserRole");
		if (createdBy.equalsIgnoreCase("ALL") && userRole.equalsIgnoreCase("ALL")) {
			List<LoanTrackingDisbursement> list = disbursementservice.fetchByDateAll(startdate, enddate);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", list);

			logger.debug("final response" + list);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else if (createdBy.equalsIgnoreCase("ALL")) {
			List<LoanTrackingDisbursement> list = disbursementservice.fetchByDateAndUserRole(startdate, enddate,
					userRole);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", list);

			logger.debug("final response" + list);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		List<LoanTrackingDisbursement> list = disbursementservice.fetchByDate(startdate, enddate, createdBy);
		logger.debug("List" + list);
		logger.debug("db Call end" + list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);

		logger.debug("final response" + list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByRole", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByRole(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchByRole start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String assignrole = jsonObject.getJSONObject("Data").getString("AssignRole");
		if (assignrole.equalsIgnoreCase("SYSTEM_USER")) {
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("EmpId", "SYSTEM_USER");
			json.put("EmpName", "");
			array.put(json);
			JSONObject response = new JSONObject();
			response.put("Data", array);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
		}
		List<User> list = disbursementservice.fetchByCredit(assignrole);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			JSONObject json = new JSONObject();
			json.put("EmpId", user.getUserId());
			json.put("EmpName", user.getUserName());
			array.put(json);
		}
		JSONObject json = new JSONObject();
		json.put("EmpId", "ALL");
		json.put("EmpName", "");
		array.put(json);
		logger.debug("db Call end" + list);
		JSONObject response = new JSONObject();
		response.put("Data", array);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByUserRole", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByUserRole(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchByUserRole start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
		String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter2);
		String userRole = jsonObject.getJSONObject("Data").getString("UserRole");
		String userId = jsonObject.getJSONObject("Data").getString("UserId");
		List<LoanTrackingDisbursement> list = disbursementservice.fetchByUserRoleAndUserId(userRole, userId, startdate,
				enddate);
		logger.debug("List" + list);
		logger.debug("db Call end" + list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchByDiscrepancy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByDiscrepancy(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchByDiscrepancy start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String assignRole = jsonObject.getJSONObject("Data").getString("AssignRole");
		String role1 = "";
		String role2 = "";
		if (assignRole.equalsIgnoreCase("SALES_DISCREPANCY")) {
			role1 = "RO";
			role2 = "CREDIT";
			List<LoanTrackingUserLog> list = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1,
					role2);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			String userId = list.get(0).getCreatedBy();
			if(userId.contains("-")==true)
			{
			String[] split2 = userId.split("-");
			String SPlit = Arrays.toString(split2);
			List<String> splitlist=Arrays.asList(split2);
			userId=splitlist.get(0);
			}
			String userName = disbursementservice.getUserName(userId);
			logger.debug("UserName"+userName);
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("EmpId", userId);
			json.put("EmpName", userName);
			array.put(json);
			JSONObject response = new JSONObject();
			response.put("Data", array);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
		} else if (assignRole.equalsIgnoreCase("Reject")) {
			role1 = "RO";
			role2 = "CREDIT";
			List<LoanTrackingUserLog> list = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1,
					role2);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			String userId = list.get(0).getCreatedBy();
			if(userId.contains("-")==true)
			{
			String[] split2 = userId.split("-");
			String SPlit = Arrays.toString(split2);
			List<String> splitlist=Arrays.asList(split2);
			userId=splitlist.get(0);
			}
			String userName = disbursementservice.getUserName(userId);
			logger.debug(userName);
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("EmpId", userId);
			json.put("EmpName", userName);
			array.put(json);
			JSONObject response = new JSONObject();
			response.put("Data", array);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
		} else if (assignRole.equalsIgnoreCase("SALESANDCREDIT_DISCREPANCY")) {
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			role1 = "RO";
			role2 = "CREDIT";
			List<LoanTrackingUserLog> list = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1, role2);
			String userId = list.get(0).getCreatedBy();
			if(userId.contains("-")==true)
			{
			String[] split2 = userId.split("-");
			String SPlit = Arrays.toString(split2);
			List<String> splitlist=Arrays.asList(split2);
			userId=splitlist.get(0);
			}
			String userName = disbursementservice.getUserName(userId);
			role1 = "CREDIT";
			role2 = "CREDIT_OPS";
			List<LoanTrackingUserLog> list2 = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1, role2);
			String userId2 = list2.get(0).getCreatedBy();
			if(userId2.contains("-")==true)
			{
			String[] split2 = userId2.split("-");
			String SPlit = Arrays.toString(split2);
			List<String> splitlist=Arrays.asList(split2);
			userId2=splitlist.get(0);
			}
			String userName2 = disbursementservice.getUserName(userId2);
			json.put("EmpId", userId +" - " +userName);
			json.put("EmpName", userId2 +" - " +userName2);
			array.put(json);
			JSONObject response = new JSONObject();
			response.put("Data", array);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
		} else if (assignRole.equalsIgnoreCase("CREDIT_DISCREPANCY")) {
			role1 = "CREDIT";
			role2 = "CREDIT_OPS";
			List<LoanTrackingUserLog> list = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1,
					role2);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			String userId = list.get(0).getCreatedBy();
			if(userId.contains("-")==true)
			{
			String[] split2 = userId.split("-");
			String SPlit = Arrays.toString(split2);
			List<String> splitlist=Arrays.asList(split2);
			userId=splitlist.get(0);
			}
			String userName = disbursementservice.getUserName(userId);
			logger.debug(userName);
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("EmpId", userId);
			json.put("EmpName", userName);
			array.put(json);
			JSONObject response = new JSONObject();
			response.put("Data", array);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
		} else if (assignRole.equalsIgnoreCase("CREDITOPS_DISCREPANCY")) {
			role1 = "CREDIT_OPS";
			role2 = "OPS";
			List<LoanTrackingUserLog> list = userlogservice.fetchByDiscrepancy(Long.parseLong(applicationNo), role1,
					role2);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			String userId = list.get(0).getCreatedBy();
			if(userId.contains("-")==true)
			{
			String[] split2 = userId.split("-");
			String SPlit = Arrays.toString(split2);
			List<String> splitlist=Arrays.asList(split2);
			userId=splitlist.get(0);
			}
			String userName = disbursementservice.getUserName(userId);
			logger.debug(userName);
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put("EmpId", userId);
			json.put("EmpName", userName);
			array.put(json);
			JSONObject response = new JSONObject();
			response.put("Data", array);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Error", "Success");
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/myTasks", method = RequestMethod.POST)
	public ResponseEntity<Object> myTasks(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchByUserRole start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
		String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter2);
		String userRole = jsonObject.getJSONObject("Data").getString("UserRole");
		String userId = jsonObject.getJSONObject("Data").getString("UserId");
		if (userRole.equalsIgnoreCase("CREDIT_ADMIN") || userRole.equalsIgnoreCase("CREDITOPS_ADMIN")
				|| userRole.equalsIgnoreCase("OPS_ADMIN")) {
			userId = "ALL";
			List<LoanTrackingDisbursement> list = disbursementservice.fetchMyTasks(userRole, userId, startdate,
					enddate);
			logger.debug("db Call end" + list);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", list);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			List<LoanTrackingDisbursement> list = disbursementservice.fetchMyTasks(userRole, userId, startdate,
					enddate);
			logger.debug("List" + list);
			logger.debug("db Call end" + list);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", list);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/appLocking", method = RequestMethod.POST)
	public ResponseEntity<Object> appLocking(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("appLocking start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String startOrEnd = jsonObject.getJSONObject("Data").getString("StartOrEnd");
		String userRole = jsonObject.getJSONObject("Data").getString("UserRole");
		LoanTrackingDisbursement loanTracking=disbursementservice.getByApplicationId(Long.parseLong(applicationNo));
		if (startOrEnd.equalsIgnoreCase("Start")) {
			loanTracking.setIsLocked("Y");
			disbursementservice.save(loanTracking);
			LocalDateTime startDate = LocalDateTime.now();
			List<LoanTrackingUserLog> list = userlogservice.fetchByApplicationNo(applicationNo);
			LoanTrackingUserLog userLog = list.get(0);
			userLog.setStartDate(startDate);
			userlogservice.save(userLog);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			org.json.simple.JSONObject success = new org.json.simple.JSONObject();
			success.put("Status", "Sucess");
			success.put("Description", "Data updated successfully");
			response.put("Data", success);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else if (startOrEnd.equalsIgnoreCase("End")) {
			LocalDateTime endDate = LocalDateTime.now();
			loanTracking.setIsLocked("N");
			disbursementservice.save(loanTracking);
			List<LoanTrackingUserLog> list = userlogservice.fetchByApplicationNo(applicationNo);
			LoanTrackingUserLog userLog = list.get(0);
			userLog.setEndDate(endDate);
			userlogservice.save(userLog);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			org.json.simple.JSONObject success = new org.json.simple.JSONObject();
			success.put("Status", "Sucess");
			success.put("Description", "Data updated successfully");
			response.put("Data", success);
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		logger.debug("db Call end");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		org.json.simple.JSONObject error = new org.json.simple.JSONObject();
		error.put("Status", "Error");
		error.put("Description", "Error while updating");
		response.put("Data", error);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/fetchDashboard", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchDashboard(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("fetchDashboard start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		List<String> productList = disbursementservice.getAllProducts();
		List list = new ArrayList<>();
		for (int i = 0; i < productList.size(); i++) {
			String product = productList.get(i);
			System.out.println(product);
			int countRO = disbursementservice.countPendingRecords2("RO", product);
			int countCredit = disbursementservice.countPendingRecords2("CREDIT", product);
			int countCreditOps = disbursementservice.countPendingRecords2("CREDIT_OPS", product);
			int countOps = disbursementservice.countPendingRecords2("OPS", product);
			int countEnd = disbursementservice.countPendingRecords2("END", product);
			int mainCount = countRO + countCredit + countCreditOps + countOps;
			if (product == null) {
				JSONObject list1 = new JSONObject();
				list1.put("Product", "NA");
				list1.put("Sales", countRO);
				list1.put("Credit", countCredit);
				list1.put("Credit_Ops", countCreditOps);
				list1.put("Ops", countOps);
				list1.put("End", countEnd);
				list1.put("Total", mainCount);
				list.add(list1);
			} else {
				JSONObject list1 = new JSONObject();
				list1.put("Product", product);
				list1.put("Sales", countRO);
				list1.put("Credit", countCredit);
				list1.put("Credit_Ops", countCreditOps);
				list1.put("Ops", countOps);
				list1.put("End", countEnd);
				list1.put("Total", mainCount);
				list.add(list1);
			}
		}
		JSONArray array = new JSONArray(list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", array);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/download/excel", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadCsv(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObject = new JSONObject(bm);
		String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
		String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
		String status = jsonObject.getJSONObject("Data").getString("Status");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);
		List<LoanTrackingDisbursement> list = new ArrayList<>();
		if (status.equalsIgnoreCase("All")) {
			list = disbursementservice.fetchByDateAll(startdate, enddate);
			if (list.isEmpty()) {
				throw new NoSuchElementException("List is empty");
			} else {
				JSONArray j = new JSONArray(list);
				System.out.println(j);
				JSONObject object1 = disbursementservice.writeExcel(j);
				return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
			}
		}
		list = disbursementservice.findTopTenByDate(startdate, enddate);
		if (list.isEmpty()) {
			throw new NoSuchElementException("List is empty");
		} else {
			JSONArray j = new JSONArray(list);
			System.out.println(j);
			JSONObject object1 = disbursementservice.writeExcel(j);
			return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/searchByNameOrApplication", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> searchByNameOrApplication(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("searchByNameOrApplication start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String name = jsonObject.getJSONObject("Data").getString("Name");
		String applicationId = jsonObject.getJSONObject("Data").getString("ApplicationId");
		if (name.length() == 0 && applicationId.length() == 0) {
			throw new EmptyInputException("Please enter name or application id");
		} else if (applicationId.length() == 0) {
			applicationId = "0";
			name = name + "%";
		}
		List<LoanTrackingDisbursement> list = disbursementservice.searchByNameOrApplication(name,
				Long.parseLong(applicationId));
		JSONArray array = new JSONArray(list);
		logger.debug("List" + list);
		logger.debug("db Call end" + list);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", array);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
	}

}
