package com.suryoday.Counterfeit.Controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.Counterfeit.Service.CounterfeitService;
import com.suryoday.Counterfeit.Utils.EncryptDecryptHelper;

@RestController
@RequestMapping("/counterfeit")
public class CounterfeitControllerEncy {

	Logger LOG = LoggerFactory.getLogger(CounterfeitControllerEncy.class);

	@Autowired
	private CounterfeitService counterfeitService;

	@PostMapping("/add")
	public ResponseEntity<Object> createCounterfeit(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: createCounterfeit :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson = counterfeitService.createCounterfeit(requestString, channel, X_Session_ID,
				X_encode_ID, request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);

		LOG.debug("COUNTERFEIT_CONTROLLER :: createCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(responseJson.toString());

	}

	@PostMapping("/get")
	public ResponseEntity<Object> getCounterfeit(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: createCounterfeit :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		// 1
		JSONObject responseJson = counterfeitService.getCounterfeit(requestString, channel, X_Session_ID, X_encode_ID,
				request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);

		LOG.debug("COUNTERFEIT_CONTROLLER :: getCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}
	
	// EXCEL
	@PostMapping("/get/excel")
	public ResponseEntity<Object> getCounterfeitExcel(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: getCounterfeitExcel :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject responseJson = counterfeitService.getCounterfeitExcel(requestString, channel, X_Session_ID, X_encode_ID,
				request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);

		LOG.debug("COUNTERFEIT_CONTROLLER :: getCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}



	// CHECKER : BM
	@PostMapping("/update/statusAndBmRemarks")
	public ResponseEntity<Object> updateCounterfeitStatusAndBoRemarks(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: createCounterfeit :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		// 1
		JSONObject responseJson = counterfeitService.updateCounterfeitStatusAndBoRemarks(requestString, channel,
				X_Session_ID, X_encode_ID, request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);

		LOG.debug("COUNTERFEIT_CONTROLLER :: getCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}

	@PostMapping("/get/MonthlyCount")
	public ResponseEntity<Object> getMonthlyCount(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: createCounterfeit :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = counterfeitService.getMonthlyCount(requestString, channel, X_Session_ID, X_encode_ID,
				request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);

		LOG.debug("COUNTERFEIT_CONTROLLER :: getCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}

	// Update Status Pending Scheduler
	@Scheduled(cron = "0 0 15 * * *")
	public void runAddMonthlyCounterfeits() {
	}
}