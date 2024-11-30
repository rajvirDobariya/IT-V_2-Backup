package com.suryoday.aocpv.controller;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.DisbursementService;

@Component
@RestController
@RequestMapping("aocpv")
public class DisbursementController {

	private static Logger logger = LoggerFactory.getLogger(DisbursementController.class);

	@Autowired
	DisbursementService disbursementservice;

	@RequestMapping(value = "/fetchDisbursementStatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchDisbursementStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		System.out.println("fetchDisbursementStatus start");
		System.out.println("request" + jsonRequest);

		if (jsonRequest.isEmpty()) {
			System.out.println("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		System.out.println("db Call start");
		AocpvLoanCreation aocpvLoanCreation = disbursementservice
				.fetchDisbursementStatus(Long.parseLong(applicationNo));

		org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
		resp.put("CIFNo", aocpvLoanCreation.getCifNumber());
		resp.put("AccountNo", aocpvLoanCreation.getAccountNumber());
		resp.put("DMSUpload", "Y");
		resp.put("ClientCreation", aocpvLoanCreation.getClientId());
		resp.put("LoanCreation", aocpvLoanCreation.getLoanAccoutNumber());
		resp.put("Disbursemnt", aocpvLoanCreation.getDisbrustTranId());
		resp.put("Status", "Booking");

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", resp);
		System.out.println("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
}
