package com.digitisation.branchreports.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.service.HolidayMasterService;

@RequestMapping("/branchdigi")
@RestController
public class HolidayMasterController {

	Logger LOG = LoggerFactory.getLogger(HolidayMasterController.class);

	@Autowired
	private HolidayMasterService holidaymasterservice;

	@PostMapping("/holiday/get/all")
	public ResponseEntity<Object> getAll() {
		JSONObject responseJson = holidaymasterservice.getAll(null, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/holiday/get/byDate")
	public ResponseEntity<Object> getByDate(@RequestBody String requestString) {
		JSONObject responseJson = holidaymasterservice.getByDate(requestString, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/holiday/add/national")
	public ResponseEntity<Object> addNationalHoliday(@RequestBody String requestString) {
		JSONObject responseJson = holidaymasterservice.addNationalHoliday(requestString, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/holiday/add/national/byExcel")
	public ResponseEntity<Object> addNationalHolidayByExcel(@RequestParam MultipartFile file) {
		JSONObject responseJson = holidaymasterservice.addNationalHolidayByExcel(file, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/holiday/add/yearly")
	public ResponseEntity<Object> addHolidayYearly(@RequestBody String requestString) {
		JSONObject responseJson = holidaymasterservice.addYearlyHoliday(requestString, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/holiday/delete/all")
	public ResponseEntity<Object> deleteHolidays(@RequestBody String requestString) {
		JSONObject responseJson = holidaymasterservice.deleteHolidays(requestString, null, null, null, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
