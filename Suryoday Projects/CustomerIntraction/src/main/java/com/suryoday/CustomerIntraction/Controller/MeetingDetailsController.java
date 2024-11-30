package com.suryoday.CustomerIntraction.Controller;

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

import com.suryoday.CustomerIntraction.Service.MeetingDetailsService;

@RestController
@RequestMapping("/CustomerIntraction")
public class MeetingDetailsController {

	Logger logger = LoggerFactory.getLogger(MeetingDetailsController.class);

	@Autowired
	private MeetingDetailsService meetingDetailsService;

	@PostMapping("/getMeetingData")
	public ResponseEntity<Object> getData(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request)
			throws Exception {

		logger.debug(
				"MEETING_DETAILS_CONTROLLER :: get checker data :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel, X_encode_ID);

		JSONObject responseJson = meetingDetailsService.getMeetingDetails(requestString, channel, X_Session_ID,
				X_encode_ID, request);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/downloadPDF")
	public ResponseEntity<Object> downloadPDFFile(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) throws Exception {

		logger.debug(
				"MEETING_DETAILS_CONTROLLER :: Download PDF :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel, X_encode_ID);
		// 1 PROCESS
		JSONObject responseJson = meetingDetailsService.downloadPDF(requestString, channel, X_Session_ID, X_encode_ID,
				null);

		logger.debug("DENOMINATION_CONTROLLER :: getDenominations :: responseJson : {}", responseJson.toString());
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
}
