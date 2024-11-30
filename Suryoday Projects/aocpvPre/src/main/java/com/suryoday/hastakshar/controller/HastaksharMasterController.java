package com.suryoday.hastakshar.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.hastakshar.pojo.HastMaster;

@RestController
@RequestMapping("/hast-master")
public class HastaksharMasterController {

	Logger logger = LoggerFactory.getLogger(HastaksharMasterController.class);
	
	@Autowired
	private com.suryoday.hastakshar.service.HastaksharMasterService HastaksharMasterService;
	
	
	@PostMapping("/testing")
	public String create() {
		logger.debug("Add new entry in hastakshar master!");		
		return "Testing";
	}
	@PostMapping("/add")
	public ResponseEntity<Object> create(@RequestBody List<HastMaster> hastMasters) {
		logger.debug("Add new entry in hastakshar master!");		
		return new ResponseEntity<Object>(HastaksharMasterService.addHastMaster(hastMasters), HttpStatus.CREATED);
	}
}
