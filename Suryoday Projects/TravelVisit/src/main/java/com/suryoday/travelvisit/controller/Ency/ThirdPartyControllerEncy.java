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
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.travelvisit.service.ThirdPartyAPIService;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;

@RestController
@RequestMapping("/ency/third-party")
public class ThirdPartyControllerEncy {

	Logger LOG = LoggerFactory.getLogger(ThirdPartyControllerEncy.class);

	@Autowired
	private ThirdPartyAPIService thirdPartyAPIService;
	
	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;
	
	// GET CUST_DETAILS
	@PostMapping("/get/custDetailsByCustId")
	public ResponseEntity<Object> getCustDetailsByCustId(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getCustDetailsByCustId :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = thirdPartyAPIService.getCustDetailsByCustId(request, channel, X_Session_ID, X_User_ID, true);
		// Encrypt Response
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);

		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
