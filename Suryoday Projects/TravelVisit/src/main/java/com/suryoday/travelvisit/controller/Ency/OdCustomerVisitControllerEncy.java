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

import com.suryoday.travelvisit.service.OdCustomerVisitService;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;

@RestController
@RequestMapping("/ency/od-customer-visit")
public class OdCustomerVisitControllerEncy {

	Logger LOG = LoggerFactory.getLogger(OdCustomerVisitControllerEncy.class);

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	private OdCustomerVisitService odCustomerVisitService;

	// ADD
	@PostMapping("/add")
	public ResponseEntity<Object> addOdCustomerVisit(@RequestParam(name = "Data") String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel, @RequestParam MultipartFile image) {
		LOG.debug(
				"OdCustomerVisitController :: addOdCustomerVisit :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = odCustomerVisitService.addOdCustomerVisit(request, channel, X_Session_ID, X_User_ID,
				true, image);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET (FILTER DATE_RANGE)
	@PostMapping("/get")
	public ResponseEntity<Object> getOdCustomerVisits(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"OdCustomerVisitController :: getOdCustomerVisits :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = odCustomerVisitService.getOdCustomerVisits(request, channel, X_Session_ID, X_User_ID,
				true);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET EXCEL
	@PostMapping("/get/excel")
	public ResponseEntity<Object> getOdCustomerVisitExcel(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"OdCustomerVisitController :: getOdCustomerVisitExcel :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = odCustomerVisitService.getOdCustomerVisitExcel(request, channel, X_Session_ID,
				X_User_ID, true);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
