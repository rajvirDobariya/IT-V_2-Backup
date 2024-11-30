package com.digitisation.branchreports.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.service.ReportMasterService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/branchdigi")

public class ReportMasterController {

	Logger LOG = LoggerFactory.getLogger(ReportMasterController.class);

	@Autowired
	private ReportMasterService respservice;

	@PostMapping("/reportMaster/get")
	public ResponseEntity<Object> getallreports(@RequestBody String requestString) {
		JSONObject responseJson = respservice.getAllReports(requestString, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/reportMaster/update")
	public ResponseEntity<Object> updateReports(@RequestBody String requestString) {
		JSONObject responseJson = respservice.updateReports(requestString, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/reportMaster/update/byExcel")
	public ResponseEntity<Object> updateReportsByExcel(@RequestParam MultipartFile file) {
		JSONObject responseJson = respservice.updateReportsByExcel(file, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
