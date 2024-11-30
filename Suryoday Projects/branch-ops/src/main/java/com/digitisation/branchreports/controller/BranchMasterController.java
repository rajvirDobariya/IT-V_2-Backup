package com.digitisation.branchreports.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitisation.branchreports.service.BranchMasterService;

@RestController
@RequestMapping({ "/branchdigi" })
public class BranchMasterController {

	Logger LOG = LoggerFactory.getLogger(BranchUserMakerController.class);

	@Autowired
	private BranchMasterService branchMasterService;

	@PostMapping("/branch/get/all")
	public ResponseEntity<Object> getAllBranches() {
		return new ResponseEntity<Object>(branchMasterService.getAll(), HttpStatus.OK);
	}

	@PostMapping("/branch/get/byId")
	public ResponseEntity<Object> getBranchById(@RequestBody String requestString) {
		return new ResponseEntity<Object>(branchMasterService.getBranchById(requestString), HttpStatus.OK);
	}

	@PostMapping("/branch/get/testingBranches")
	public ResponseEntity<Object> getTestingBranches() {
		return new ResponseEntity<Object>(branchMasterService.getTestingBranches(), HttpStatus.OK);
	}

	@PostMapping("/branch/update/testingBranches")
	public ResponseEntity<Object> updateTestingBranches(@RequestBody String requestString) {
		branchMasterService.updateTestingBranches(requestString);
		return new ResponseEntity<Object>("", HttpStatus.OK);
	}

	@PostMapping("/branch/add/remainingBranches")
	public ResponseEntity<Object> addRemainingBranches() {
		branchMasterService.addRemainingBranches();
		return new ResponseEntity<Object>("DONE", HttpStatus.OK);
	}

	@PostMapping("/branch/get/testingBranchesCount")
	public ResponseEntity<Object> getTestingBranchesCount() {		
		return new ResponseEntity<Object>(branchMasterService.getTestingBranchesCount(), HttpStatus.OK);
	}

}
