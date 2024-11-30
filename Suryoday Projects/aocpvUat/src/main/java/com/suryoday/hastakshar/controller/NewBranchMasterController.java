package com.suryoday.hastakshar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.service.NewBranchMasterService;

@RestController
@RequestMapping("/new-branch-master")
public class NewBranchMasterController {

	@Autowired
	private NewBranchMasterService newBranchMasterService;

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(CustomerDetailController.class);

	@PostMapping("/update/excel")
	public ResponseEntity<Object> updateBranchExcel(@RequestParam MultipartFile file) {
		newBranchMasterService.updateBranchExcel(file);
		return new ResponseEntity<Object>("Testing", HttpStatus.OK);
	}
}
