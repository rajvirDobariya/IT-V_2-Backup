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
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.Counterfeit.Service.DenominationService;

@RestController
@RequestMapping("/testing/counterfeit/denomination")
public class DenominationController {

	Logger LOG = LoggerFactory.getLogger(DenominationController.class);

	@Autowired
	private DenominationService denominationService;

	@PostMapping("/get")
	public ResponseEntity<Object> getDenominationByCounterfeitId(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request

	) {
		JSONObject responseJson = denominationService.getDenominations(requestString, channel, X_Session_ID,
				X_encode_ID, request, false, false);

		LOG.debug("DENOMINATION_CONTROLLER :: getDenominations :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}

	@PostMapping("/update")
	public ResponseEntity<Object> updateDenominations(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request

	) {
		JSONObject responseJson = denominationService.updateDenominations(requestString, channel, X_Session_ID,
				X_encode_ID, request, false);

		LOG.debug("DENOMINATION_CONTROLLER :: updateDenomination :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}

	@PostMapping("/downloadPDF")
	public ResponseEntity<Object> downloadPDFFile(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request) {

		JSONObject responseJson = denominationService.downloadPDF(requestString, channel, X_Session_ID, X_encode_ID,
				request, false);

		LOG.debug("DENOMINATION_CONTROLLER :: getDenominations :: responseJson : {}", responseJson.toString());
		return ResponseEntity.status(HttpStatus.OK).body(responseJson.toString());
	}
}
