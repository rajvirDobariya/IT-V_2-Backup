package com.suryoday.Counterfeit.Controller;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.Counterfeit.Service.CounterfeitService;

@RestController
@RequestMapping("/testing/counterfeit")
public class CounterfeitController {

	Logger LOG = LoggerFactory.getLogger(CounterfeitController.class);

	@Autowired
	private CounterfeitService counterfeitService;

	@PostMapping("/welcome")
	public ResponseEntity<String> welcome() {
		LOG.info("CounterfeitController :: This is an info log - should appear in logs");
		LOG.error("CounterfeitController :: This is an error log - should definitely appear in logs");
		LOG.debug("CounterfeitController :: welcome :: ");
		return ResponseEntity.status(HttpStatus.OK).body("Welcome in Counterfeit Project!");
	}

	// ADD
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
				X_encode_ID, request, false);

		LOG.debug("COUNTERFEIT_CONTROLLER :: createCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(responseJson.toString());
	}

	// GET
	@PostMapping("/get")
	public ResponseEntity<Object> getCounterfeit(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: getCounterfeit :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject responseJson = counterfeitService.getCounterfeit(requestString, channel, X_Session_ID, X_encode_ID,
				request, false);

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
				request, false);

		LOG.debug("COUNTERFEIT_CONTROLLER :: getCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}

	// CHECKER 
	@PostMapping("/update/statusAndBmRemarks")
	public ResponseEntity<Object> updateCounterfeitStatusAndBoRemarks(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {
		LOG.debug(
				"COUNTERFEIT_CONTROLLER :: updateCounterfeitStatusAndBoRemarks :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject responseJson = counterfeitService.updateCounterfeitStatusAndBoRemarks(requestString, channel,
				X_Session_ID, X_encode_ID, request, false);

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
				"COUNTERFEIT_CONTROLLER :: getMonthlyCount :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject responseJson = counterfeitService.getMonthlyCount(requestString, channel, X_Session_ID, X_encode_ID,
				request, false);

		LOG.debug("COUNTERFEIT_CONTROLLER :: getCounterfeit :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}

	@PostMapping("/delete")
	public ResponseEntity<Object> delete(@RequestBody String request) {
		counterfeitService.delete(request);
		return ResponseEntity.status(HttpStatus.OK).body("");
	}

	@PostMapping("/addMonthlyCounterfeits")
	public ResponseEntity<Object> addMonthlyCounterfeitsTesting(@RequestParam String month) {
		counterfeitService.addMonthlyCounterfeitsTesting(month);
		return ResponseEntity.status(HttpStatus.OK).body("");

	}

}