package com.suryoday.mhl.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.mhl.pojo.CreateLead;
import com.suryoday.mhl.pojo.MHLIncomeDetails;
import com.suryoday.mhl.service.CreateLeadService;
import com.suryoday.mhl.service.MHLIncomeDetailsService;
import com.suryoday.mhl.service.MhlUserLogService;

@RestController
@RequestMapping("/mhl")
public class CreateLeadControllerEncy {

	@Autowired
	CreateLeadService createLeadService;

	@Autowired
	MhlUserLogService mhlUserLogService;

	@Autowired
	MHLIncomeDetailsService incomeDetailsService;

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(CreateLeadControllerEncy.class);

	@RequestMapping(value = "/createLead/saveDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("save data start");
			logger.debug("request" + decryptContainerString);
			System.out.println("JSON STRING " + jsonObject);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			String product = jsonObject.getJSONObject("Data").getString("product");
			String loanPurpose = jsonObject.getJSONObject("Data").getString("loanPurpose");
			String requiredLoanAmount = jsonObject.getJSONObject("Data").getString("requiredLoanAmount");
			String tenure = jsonObject.getJSONObject("Data").getString("tenure");
			String sourceType = jsonObject.getJSONObject("Data").getString("sourceType");
			String sourceName = jsonObject.getJSONObject("Data").getString("sourceName");
			String empId = jsonObject.getJSONObject("Data").getString("empId");
			String createdBy = jsonObject.getJSONObject("Data").getString("createdBy");
			String createdName = jsonObject.getJSONObject("Data").getString("createdName");
			String name = jsonObject.getJSONObject("Data").getString("name");
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			String branchName = jsonObject.getJSONObject("Data").getString("branchName");
			String role = jsonObject.getJSONObject("Data").getString("role");

			CreateLead createLead = new CreateLead();
			createLead.setProduct(product);
			createLead.setLoanPurpose(loanPurpose);
			createLead.setRequiredLoanAmount(requiredLoanAmount);
			createLead.setTenure(tenure);
			createLead.setSourceType(sourceType);
			createLead.setSourceName(sourceName);
			createLead.setEmpId(empId);
			createLead.setCreatedBy(createdBy);
			createLead.setCreatedName(createdName);
			createLead.setBranchName(branchName);
			createLead.setBranchId(branchId);
			createLead.setName(name);
			logger.debug("db Call start" + createLead);
			String applicationNo = createLeadService.save(createLead);
			logger.debug("db Call end response  " + applicationNo);

			String status = "QDE";
			mhlUserLogService.save(createLead, role, applicationNo, status);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "Data Save Successfully");
			response.put("status", HttpStatus.OK.toString());
			response.put("ApplicationNo", applicationNo);
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/createLead/fetchByBranchIdAndstatusEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByBranchIdAndstatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("fetchByBranchIdAndstatus start");
			logger.debug("request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String status = jsonObject.getJSONObject("Data").getString("Status");
			logger.debug("db Call start");
			List<CreateLead> createLeads = createLeadService.findByBranchIdAndStatus(branchId, status);
			logger.debug("db Call end" + createLeads);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(createLeads);
			response.put("status", HttpStatus.OK.toString());
			response.put("Data", j);
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/createLead/fetchByDateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDates(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("fetchByBranchIdAndstatus start");
			logger.debug("request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String startDate = jsonObject.getJSONObject("Data").getString("startDate");
			String endDate = jsonObject.getJSONObject("Data").getString("endDate");
			String status = jsonObject.getJSONObject("Data").getString("Status");
			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");

			if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "enter a proper Dates");
				logger.debug("final response" + response.toString());
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				System.out.println(data3);
				return new ResponseEntity<Object>(data3, HttpStatus.BAD_REQUEST);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
			LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);
			logger.debug("db Call start");
			List<CreateLead> list = createLeadService.findByDate(startdate, enddate);
			logger.debug("db Call end" + list);

			if (status.isEmpty() && branchId.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				JSONArray j = new JSONArray(list);
				response.put("status", HttpStatus.OK.toString());
				response.put("Data", j);
				logger.debug("final response without branchId and status" + response.toString());
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				System.out.println(data3);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			} else if (status == null || status.isEmpty()) {
				List<CreateLead> listWithoutStatus = new ArrayList<>();
				for (CreateLead createLead : list) {
					if (createLead.getBranchId().equalsIgnoreCase(branchId)) {
						listWithoutStatus.add(createLead);
					}
				}
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				JSONArray j = new JSONArray(listWithoutStatus);
				response.put("status", HttpStatus.OK.toString());
				response.put("Data", j);
				logger.debug("final response without status" + response.toString());
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				System.out.println(data3);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			} else if (branchId == null || branchId.isEmpty()) {
				List<CreateLead> listWithoutBranchId = new ArrayList<>();
				for (CreateLead createLead : list) {
					if (createLead.getStatus().equalsIgnoreCase(status)) {
						listWithoutBranchId.add(createLead);
					}
				}
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				JSONArray j = new JSONArray(listWithoutBranchId);
				response.put("status", HttpStatus.OK.toString());
				response.put("Data", j);
				logger.debug("final response without branchId" + response.toString());
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				System.out.println(data3);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			} else {
				List<CreateLead> listWithBranchIdAndStatus = new ArrayList<>();
				for (CreateLead createLead : list) {
					if (createLead.getStatus().equalsIgnoreCase(status)
							&& createLead.getBranchId().equalsIgnoreCase(branchId)) {
						listWithBranchIdAndStatus.add(createLead);
					}
				}
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				JSONArray j = new JSONArray(listWithBranchIdAndStatus);
				response.put("status", HttpStatus.OK.toString());
				response.put("Data", j);
				logger.debug("final response with branchId and Status " + response.toString());
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				System.out.println(data3);
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

	// Save Income Details

	@RequestMapping(value = "/incomeDetails/saveDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveIncomelDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("save data start");
			logger.debug("request" + decryptContainerString);
			System.out.println("JSON STRING " + jsonObject);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = jsonObject.getJSONObject("Data").getString("member");
			String earning = jsonObject.getJSONObject("Data").getString("earning");
			String occupation = jsonObject.getJSONObject("Data").getString("occupation");
			String occCode = jsonObject.getJSONObject("Data").getString("occCode");
			String primarySourceOfIncome = jsonObject.getJSONObject("Data").getString("primarySourceOfIncome");
			String securedLoan = jsonObject.getJSONObject("Data").getString("securedLoan");
			double securedLoanD = Double.parseDouble(securedLoan);
			String unsecuredLoan = jsonObject.getJSONObject("Data").getString("unsecuredLoan");
			double unsecuredLoanD = Double.parseDouble(unsecuredLoan);
			String monthlyIncome = jsonObject.getJSONObject("Data").getString("monthlyIncome");
			double monthlyIncomeD = Double.parseDouble(monthlyIncome);
			String monthlyLoanEmi = jsonObject.getJSONObject("Data").getString("monthlyLoanEmi");
			double monthlyIncomeEMID = Double.parseDouble(monthlyLoanEmi);
			String title = jsonObject.getJSONObject("Data").getString("title");
			String firstName = jsonObject.getJSONObject("Data").getString("firstName");
			String lastName = jsonObject.getJSONObject("Data").getString("lastName");
			String gender = jsonObject.getJSONObject("Data").getString("gender");
			String age = jsonObject.getJSONObject("Data").getString("age");
			int ageInt = Integer.parseInt(age);
			String dob = jsonObject.getJSONObject("Data").getString("dob");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateOfBirth = LocalDate.parse(dob, formatter);
			String mobile = jsonObject.getJSONObject("Data").getString("mobile");
			String mobileVerify = jsonObject.getJSONObject("Data").getString("mobileVerify");
			String married = jsonObject.getJSONObject("Data").getString("married");
			String aadharCard = jsonObject.getJSONObject("Data").getString("aadharCard");
			String aadharNoVerify = jsonObject.getJSONObject("Data").getString("aadharNoVerify");
			String panCard = jsonObject.getJSONObject("Data").getString("panCard");
			String pancardNoVerify = jsonObject.getJSONObject("Data").getString("pancardNoVerify");
			String voterId = jsonObject.getJSONObject("Data").getString("voterId");
			String applicantType = jsonObject.getJSONObject("Data").getString("applicantType");
			String relation = jsonObject.getJSONObject("Data").getString("relation");
			String voterIdVerify = jsonObject.getJSONObject("Data").getString("voterIdVerify");
			String modeOfIncome = jsonObject.getJSONObject("Data").getString("modeOfIncome");
			String modeOfSalary = jsonObject.getJSONObject("Data").getString("modeOfSalary");
			String incomeProof = jsonObject.getJSONObject("Data").getString("incomeProof");
			String employmentProof = jsonObject.getJSONObject("Data").getString("employmentProof");
			String frequencyOfSalary = jsonObject.getJSONObject("Data").getString("frequencyOfSalary");
			String natureOfBussiness = jsonObject.getJSONObject("Data").getString("natureOfBussiness");
			String typeOfBussiness = jsonObject.getJSONObject("Data").getString("typeOfBussiness");
			String totalIncome = jsonObject.getJSONObject("Data").getString("totalIncomeBussiness");
			double totalIncomeBussiness = Double.parseDouble(totalIncome);
			String annualIncome = jsonObject.getJSONObject("Data").getString("annualIncome");
			double annualIncomeProfessional = Double.parseDouble(annualIncome);
			String bussinessAddress = jsonObject.getJSONObject("Data").getString("bussinessAddress");
			String bussinessPremisesOwn = jsonObject.getJSONObject("Data").getString("bussinessPremisesOwn");
			String nameOfOrganization = jsonObject.getJSONObject("Data").getString("nameOfOrganization");
			String totalExperience = jsonObject.getJSONObject("Data").getString("totalExperience");
			String bussinesRole = jsonObject.getJSONObject("Data").getString("bussinesRole");
			String intersetedInLifeInsurance = jsonObject.getJSONObject("Data").getString("intersetedInLifeInsurance");
			String intersetedInPropertyInsurance = jsonObject.getJSONObject("Data")
					.getString("intersetedInPropertyInsurance");
			String nomineeInsurance = jsonObject.getJSONObject("Data").getString("nomineeInsurance");
			String nomineeName = jsonObject.getJSONObject("Data").getString("nomineeName");
			String relationWithInsured = jsonObject.getJSONObject("Data").getString("relationWithInsured");
			String insuranceNo = jsonObject.getJSONObject("Data").getString("insuranceNo");
			String insuranceType = jsonObject.getJSONObject("Data").getString("insuranceType");
			String insuranceAmount = jsonObject.getJSONObject("Data").getString("insuranceAmount");
			double insuranceAmountD = Double.parseDouble(insuranceAmount);

			MHLIncomeDetails income = new MHLIncomeDetails();

			income.setApplicationNo(applicationNo);
			income.setMember(member);
			income.setEarning(earning);
			income.setOccupation(occupation);
			income.setOccCode(occCode);
			income.setPrimarySourceOfIncome(primarySourceOfIncome);
			income.setSecuredLoan(securedLoanD);
			income.setUnsecuredLoan(unsecuredLoanD);
			income.setMonthlyIncome(monthlyIncomeD);
			income.setMonthlyLoanEmi(monthlyIncomeEMID);
			income.setTitle(title);
			income.setFirstName(firstName);
			income.setLastName(lastName);
			income.setAge(ageInt);
			income.setGender(gender);
			income.setDob(dateOfBirth);
			income.setMobile(mobile);
			income.setMobileVerify(mobileVerify);
			income.setMarried(married);
			income.setAadharCard(aadharCard);
			income.setAadharNoVerify(aadharNoVerify);
			income.setPanCard(panCard);
			income.setPancardNoVerify(pancardNoVerify);
			income.setVoterId(voterId);
			income.setVoterIdVerify(voterIdVerify);
			income.setApplicantType(applicantType);
			income.setRelation(relation);
			income.setModeOfIncome(modeOfIncome);
			income.setModeOfSalary(modeOfSalary);
			income.setIncomeProof(incomeProof);
			income.setEmploymentProof(employmentProof);
			income.setFrequencyOfSalary(frequencyOfSalary);
			income.setNatureOfBussiness(natureOfBussiness);
			income.setTypeOfBussiness(typeOfBussiness);
			income.setTotalIncomeBussiness(totalIncomeBussiness);
			income.setAnnualIncome(annualIncomeProfessional);
			income.setBussinesRole(bussinesRole);
			income.setBussinessAddress(bussinessAddress);
			income.setBussinessPremisesOwn(bussinessPremisesOwn);
			income.setNameOfOrganization(nameOfOrganization);
			income.setTotalExperience(totalExperience);
			income.setIntersetedInLifeInsurance(intersetedInLifeInsurance);
			income.setIntersetedInPropertyInsurance(intersetedInPropertyInsurance);
			income.setNomineeInsurance(nomineeInsurance);
			income.setNomineeName(nomineeName);
			income.setRelationWithInsured(relationWithInsured);
			income.setInsuranceAmount(insuranceAmountD);
			income.setInsuranceNo(insuranceNo);
			income.setInsuranceType(insuranceType);

			logger.debug("db Call start" + income);
			System.out.println("Income Details" + income);
			logger.debug("db Call end response  " + applicationNo);
			incomeDetailsService.saveIncomeDetails(income);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "Data Save Successfully");
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
	public ResponseEntity<Object> updateStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("save data start");
			logger.debug("request" + decryptContainerString);
			System.out.println("JSON STRING " + jsonObject);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String status = jsonObject.getJSONObject("Data").getString("status");
			createLeadService.updateStatus(applicationNo, status);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "Data Save Successfully");
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
