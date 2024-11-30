package com.suryoday.loantrackingphase2.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.loantrackingphase2.service.LoanPrductService;

@RestController
@RequestMapping("/loan-product")
public class LoanProductController {

	Logger LOG = LoggerFactory.getLogger(LoanProductController.class);

	@Autowired
	private LoanPrductService loanPrductService;

	// GET
	@PostMapping("/get")
	public ResponseEntity<Object> getLoanProducts(
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"LoanProductController :: getAll :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = loanPrductService.getAll(channel, X_Session_ID, X_User_ID, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// ADD
	@PostMapping("/add")
	public ResponseEntity<Object> addAllLoanProduct() {
		return new ResponseEntity<Object>(loanPrductService.addLoanProducts(), HttpStatus.OK);
	}
}
