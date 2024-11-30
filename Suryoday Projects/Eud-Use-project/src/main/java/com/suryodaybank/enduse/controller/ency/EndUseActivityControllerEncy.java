package com.suryodaybank.enduse.controller.ency;

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

import com.suryodaybank.enduse.service.EndUseActivityService;
import com.suryodaybank.enduse.utils.EncryptDecryptHelper;

@RestController
@RequestMapping("ency/activity")
public class EndUseActivityControllerEncy {

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	private EndUseActivityService endUseActivityService;
	
	Logger LOG = LoggerFactory.getLogger(EndUseActivityControllerEncy.class);


	@PostMapping("/welcome")
	public String welcome() {
		return "Welcome in Loan tracking project.";
	}
	
	// ADD
	@PostMapping("/add")
	public ResponseEntity<Object> addBranchHygiene(@RequestParam MultipartFile file) {
		// 1
		JSONObject responseJson = endUseActivityService.add(file);
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
		JSONObject responseJson = endUseActivityService.getActivities(request, channel, X_Session_ID, X_User_ID, true);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}



}
