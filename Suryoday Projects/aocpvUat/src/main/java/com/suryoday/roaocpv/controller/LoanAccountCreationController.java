package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.roaocpv.service.LoanAccountCreationService;

@RestController
@RequestMapping("/roaocpv")
public class LoanAccountCreationController {

	@Autowired
	LoanAccountCreationService loanCreationService;

	@RequestMapping(value = "/cifCreation", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> cifCreation(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject cifCreation = loanCreationService.cifCreation();
		if (cifCreation.isEmpty()) {
			return new ResponseEntity<>("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(cifCreation.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/accountCreation", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> accountCreation(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject accountCreation = loanCreationService.accountCreation();
		if (accountCreation.isEmpty()) {
			return new ResponseEntity<>("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(accountCreation.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/loanAccountCreation", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> loanCreation(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject loanCreation = loanCreationService.loanCreation();
		if (loanCreation.isEmpty()) {
			return new ResponseEntity<>("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(loanCreation.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/disbursement", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> disbursement(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req) {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject disbursement = loanCreationService.disbursement();
		if (disbursement.isEmpty()) {
			return new ResponseEntity<>("Please Enter Vaild Details", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(disbursement.toString(), HttpStatus.OK);
	}

}