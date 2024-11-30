package com.suryoday.loantrackingphase2.controller.Ency;

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

import com.suryoday.loantrackingphase2.serviceImpl.ProductMasterServiceImpl;
import com.suryoday.loantrackingphase2.utils.EncryptDecryptHelper;

@RestController
@RequestMapping("/product-masters/ency")
public class ProductMasterControllerEncy {

	Logger LOG = LoggerFactory.getLogger(ProductMasterControllerEncy.class);

	@Autowired
	private ProductMasterServiceImpl productMasterService;
	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	// GET ALL
	@PostMapping("/get/All")
	public ResponseEntity<Object> getAllProductMasters(
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getAllProductMasters :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = productMasterService.getAllProductMasters(channel, X_Session_ID, X_User_ID, true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// GET BY ID
	@PostMapping("/get/byId")
	public ResponseEntity<Object> getProductMasterById(@RequestBody String request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel) {
		LOG.debug(
				"ProductMasterController :: getAllProductMasters :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				null, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = productMasterService.getProductMasterById(request, channel, X_Session_ID, X_User_ID,
				true);
		responseJson = encryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	// SERVER SIDE ONLY : Create a new ProductMaster
	@PostMapping("/checkSessingId")
	public ResponseEntity<Object> checkSessingId(@RequestParam String sesisonId) {
		return ResponseEntity.ok(productMasterService.checkSessingId(sesisonId));
	}

}
