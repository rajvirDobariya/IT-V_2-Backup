package com.suryoday.travelvisit.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.travelvisit.service.VisitToService;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;

@RestController
@RequestMapping("/visit-to")
public class VisitToController {

	Logger LOG = LoggerFactory.getLogger(VisitToController.class);

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	private VisitToService visitToService;

	@PostMapping("/welcome")
	public String welcome() {
		return "Welcome in Loan tracking project.";
	}

	// BASE 64 TO EXCEL
	@PostMapping("/base64ToExcel")
	public String base64ToExcel(@RequestBody String request) {
		return visitToService.base64ToExcel(request);
	}

	@PostMapping("/decryption")
	public ResponseEntity<Object> decryption(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"BranchUserMakerController ::encrypton :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = encryptDecryptHelper.validateHeadersAndSessionId(requestString, channel,
				X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/encryption")
	public ResponseEntity<Object> encryption(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"BranchUserMakerController ::decryption :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject encryptJson = new JSONObject(requestString);
		// Encrypt Response
		JSONObject responseJson = encryptDecryptHelper.encryptResponseString(encryptJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// ADD
	@PostMapping("/add")
	public ResponseEntity<Object> addVisitTo(@RequestParam(name = "Data") String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel, @RequestParam MultipartFile image) {
		LOG.debug(
				"VisitToController :: addVisitTo :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = visitToService.addVisitTo(request, channel, X_Session_ID, X_User_ID, false, image);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET (FILTER DATE_RANGE)
	@PostMapping("/get")
	public ResponseEntity<Object> getVisitTos(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"VisitToController :: getVisitTos :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = visitToService.getVisitTos(request, channel, X_Session_ID, X_User_ID, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET EXCEL
	@PostMapping("/get/excel")
	public ResponseEntity<Object> getVisitToExcel(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"VisitToController :: getVisitToExcel :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = visitToService.getVisitToExcel(request, channel, X_Session_ID, X_User_ID, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
