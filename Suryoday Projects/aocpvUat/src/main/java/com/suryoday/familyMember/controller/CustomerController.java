package com.suryoday.familyMember.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.familyMember.pojo.CustomerData;
import com.suryoday.familyMember.pojo.FamilyMember;
import com.suryoday.familyMember.pojo.FamilyMemberResponse;
import com.suryoday.familyMember.service.CustomerDataService;
import com.suryoday.familyMember.service.MemberImageService;
import com.suryoday.roaocpv.controller.ApplicationDetailsController;

@RestController
@RequestMapping("/familyMember/customer")
public class CustomerController {

	@Autowired
	CustomerDataService customerDataService;

	@Autowired
	MemberImageService imageService;

	private static Logger logger = LoggerFactory.getLogger(ApplicationDetailsController.class);

	@RequestMapping(value = "/fetchByCustomerId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByStatusAndBranchId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		CustomerData customerData = customerDataService.fetchBycustomerId(customerId);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", customerData);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/addFamilyMember", method = RequestMethod.POST)
	public ResponseEntity<Object> addFamilyMember(@RequestParam("file") MultipartFile[] files,
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		String member = jsonObject.getJSONObject("Data").getString("member");
		String title = jsonObject.getJSONObject("Data").getString("title");
		String firstName = jsonObject.getJSONObject("Data").getString("firstName");
		String lastName = jsonObject.getJSONObject("Data").getString("lastName");
		String gender = jsonObject.getJSONObject("Data").getString("gender");
		String age = jsonObject.getJSONObject("Data").getString("age");
		String dob = jsonObject.getJSONObject("Data").getString("dob");
		String mobile = jsonObject.getJSONObject("Data").getString("mobile");
		String mobileVerify = jsonObject.getJSONObject("Data").getString("mobileVerify");
		String married = jsonObject.getJSONObject("Data").getString("married");
		String earning = jsonObject.getJSONObject("Data").getString("earning");
		String occupation = jsonObject.getJSONObject("Data").getString("occupation");
		String occCode = jsonObject.getJSONObject("Data").getString("occCode");
		String primarySourceOfIncome = jsonObject.getJSONObject("Data").getString("primarySourceOfIncome");
		String monthlyIncome = jsonObject.getJSONObject("Data").getString("monthlyIncome");
		String monthlyLoanEmi = jsonObject.getJSONObject("Data").getString("monthlyLoanEmi");
		String aadharNoVerify = jsonObject.getJSONObject("Data").getString("aadharNoVerify");
		String aadharCardNo = jsonObject.getJSONObject("Data").getString("aadharCard");
		String voterId = jsonObject.getJSONObject("Data").getString("voterId");
		String voterIdVerify = jsonObject.getJSONObject("Data").getString("voterIdVerify");
		String pancardno = jsonObject.getJSONObject("Data").getString("panCard");
		String pancardNoVerify = jsonObject.getJSONObject("Data").getString("pancardNoVerify");
		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		FamilyMember familyMember = new FamilyMember();
		familyMember.setCustomerId(customerId);
		familyMember.setMember(member);
		familyMember.setEarning(earning);
		familyMember.setOccupation(occupation);
		familyMember.setOccCode(occCode);
		familyMember.setPrimarySourceOfIncome(primarySourceOfIncome);
		familyMember.setMonthlyIncome(monthlyIncome);
		familyMember.setMonthlyLoanEmi(monthlyLoanEmi);
		familyMember.setTitle(title);
		familyMember.setFirstName(firstName);
		familyMember.setLastName(lastName);
		familyMember.setGender(gender);
		familyMember.setAge(age);
		familyMember.setDob(dob);
		familyMember.setMobile(mobile);
		familyMember.setMobileVerify(mobileVerify);
		familyMember.setMarried(married);
		familyMember.setAadharCard(aadharCardNo);
		familyMember.setAadharNoVerify(aadharNoVerify);
		familyMember.setPanCard(pancardno);
		familyMember.setPancardNoVerify(pancardNoVerify);
		familyMember.setVoterId(voterId);
		familyMember.setVoterIdVerify(voterIdVerify);

		String message = customerDataService.saveFamilyMember(familyMember);
		if (!document.isEmpty()) {
			imageService.saveImage(files, customerId, document, member);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/fetchFamilyMember", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchFamilyMember(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		List<FamilyMemberResponse> list = customerDataService.fetchFamilyMember(customerId);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteMember", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteMember(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		String member = jsonObject.getJSONObject("Data").getString("member");

		String message = customerDataService.deleteMember(customerId, member);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/statusChange", method = RequestMethod.POST)
	public ResponseEntity<Object> statusChange(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("request" + jsonRequest);
		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		String status = jsonObject.getJSONObject("Data").getString("status");

		String message = customerDataService.statuschange(customerId, status);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
