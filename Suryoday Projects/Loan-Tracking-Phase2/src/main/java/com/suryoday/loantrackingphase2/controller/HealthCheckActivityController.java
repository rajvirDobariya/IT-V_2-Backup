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

import com.suryoday.loantrackingphase2.service.HealthCheckActivityService;

@RestController
@RequestMapping("/health-check")
public class HealthCheckActivityController {

	Logger LOG = LoggerFactory.getLogger(HealthCheckActivityController.class);
	
    @Autowired
    private HealthCheckActivityService healthCheckActivityService;
    
	@PostMapping("/welcome")
	public String welcome() {		
		return "Welcome in Loan tracking project.";
	}

	// ADD ACTIVITY
	@PostMapping("/add")
	public ResponseEntity<Object> addHealthCheckActivity(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: addHealthCheckActivity :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = healthCheckActivityService.addHealthCheckActivity(request, channel, X_Session_ID, X_User_ID,
				false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	// GET (BY DATE_RANGE, PAN-NO) 
	@PostMapping("get/all")
	public ResponseEntity<Object> getAllHealthCheckActivity(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getAllHealthCheckActivity :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = healthCheckActivityService.getAllHealthCheckActivity(request, channel, X_Session_ID, X_User_ID,
				false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	// DOWNLOAD EXCEL
	@PostMapping("/get/excel")
	public ResponseEntity<Object> getExcelHealthCheckActivities(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"NOIUpdationController :: getExcelHealthCheckActivities :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = healthCheckActivityService.getExcelHealthCheckActivities(request, channel, X_Session_ID,
				X_User_ID, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}


	// GET BY ID API 
	@PostMapping("/get/byId")
	public ResponseEntity<Object> getHealthCheckActivityById(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getHealthCheckActivityById :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = healthCheckActivityService.getHealthCheckActivityById(request, channel, X_Session_ID, X_User_ID,
				false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	//UPDATE API
	@PostMapping("/update/productChecksAndQueries")
	public ResponseEntity<Object> updateProductChecksAndQueries(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: updateProductChecksAndQueries :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = healthCheckActivityService.updateProductChecksAndQueries(request, channel, X_Session_ID, X_User_ID,
				false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	// GET CUST DET BY CUST_NO
	@PostMapping("/get/custDetail/customerNo")
	public ResponseEntity<Object> getCustomerDataByCustomerNo(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: addHealthCheckActivity :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = healthCheckActivityService.getCustomerDataByCustomerNo(request, channel, X_Session_ID,
				X_User_ID, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	
	// GET EMP_NAME
	@PostMapping("/get/EmpName/byEmpId")
	public ResponseEntity<Object> getEmployeeNameByEmployeeId(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getEmployeeNameByEmployeeId :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = healthCheckActivityService.getEmployeeNameByEmployeeId(request, channel, X_Session_ID,
				X_User_ID, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}


}
