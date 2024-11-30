package com.suryoday.travelvisit.controller.Ency;

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
@RequestMapping("/ency/visit-to")
public class VisitToControllerEncy {

	Logger LOG = LoggerFactory.getLogger(VisitToControllerEncy.class);

	@Autowired
	private VisitToService visitToService;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@PostMapping("/welcome")
	public String welcome() {
		return "Welcome in Loan tracking project.";
	}

	// ADD
	@PostMapping("/add")
	public ResponseEntity<Object> addVisitTo(@RequestParam(name = "Data") String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestParam MultipartFile image) {
		LOG.debug(
				"ProductMasterController :: addVisitTo :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = visitToService.addVisitTo(request, channel, X_Session_ID, X_User_ID, true, image);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET (FILTER DATE_RANGE)
	@PostMapping("/get")
	public ResponseEntity<Object> getVisitTos(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getVisitTos :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = visitToService.getVisitTos(request, channel, X_Session_ID, X_User_ID, true);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET EXCEL
	@PostMapping("/get/excel")
	public ResponseEntity<Object> getVisitToExcel(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getVisitToExcel :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = visitToService.getVisitToExcel(request, channel, X_Session_ID, X_User_ID, true);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
