package com.suryoday.loantrackingphase2.controller;

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

import com.suryoday.loantrackingphase2.serviceImpl.ProductMasterServiceImpl;

@RestController
@RequestMapping("/product-masters")
public class ProductMasterController {

	Logger LOG = LoggerFactory.getLogger(ProductMasterController.class);

	@Autowired
	private ProductMasterServiceImpl productMasterService;

	// SERVER SIDE ONLY : Create a new ProductMaster
	@PostMapping("/add")
	public ResponseEntity<Object> addExcel(@RequestParam MultipartFile file) {
		return ResponseEntity.ok(productMasterService.addExcel(file));
	}

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
		JSONObject responseJson = productMasterService.getAllProductMasters(channel, X_Session_ID, X_User_ID, false);
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
				false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	@PostMapping("/get/employeeNameById")
	public String getEmployeeNameById(){
		return productMasterService.getEmployeeNameById(34631);
	}

	
}
