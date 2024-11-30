package com.suryoday.loantrackingphase2.controller.Ency;

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

import com.suryoday.loantrackingphase2.service.NOIUpdationService;
import com.suryoday.loantrackingphase2.utils.EncryptDecryptHelper;

@RestController
@RequestMapping("/noi-update/ency")
public class NOIUpdationControllerEncy {

	Logger LOG = LoggerFactory.getLogger(NOIUpdationControllerEncy.class);

	@Autowired
	private NOIUpdationService noiUpdationService;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	// ADD
	@PostMapping("/add")
	public ResponseEntity<Object> addNOIUpdationActivity(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"NOIUpdationController :: addNOIUpdationActivity :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = noiUpdationService.addNOIUpdationActivity(request, channel, X_Session_ID, X_User_ID,
				true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET
	@PostMapping("/get")
	public ResponseEntity<Object> getNOIUpdationActivities(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"NOIUpdationController :: getNOIUpdationActivities :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = noiUpdationService.getNOIUpdationActivities(request, channel, X_Session_ID, X_User_ID,
				true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	// GET BY ID API 
	@PostMapping("/get/byId")
	public ResponseEntity<Object> getNOIUpdateActivityById(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getNOIUpdateActivityById :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = noiUpdationService.getNOIUpdateActivityById(request, channel, X_Session_ID, X_User_ID,
				true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	
	// DOWNLOAD EXCEL
	@PostMapping("/get/excel")
	public ResponseEntity<Object> getExcelNOIUpdationActivities(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"NOIUpdationController :: downloadExcelNOIUpdationActivities :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = noiUpdationService.getExcelNOIUpdationActivities(request, channel, X_Session_ID,
				X_User_ID, true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET LOAN ACCOUNT DET BY ACC NO
	@PostMapping("/get/loan/account/details/byAccNo")
	public ResponseEntity<Object> getLoanAccountDetailsByAccNo(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"NOIUpdationController :: getLoanAccountDetailsByAccNo :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = noiUpdationService.getLoanAccountDetailsByAccNo(request, channel, X_Session_ID,
				X_User_ID, true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
