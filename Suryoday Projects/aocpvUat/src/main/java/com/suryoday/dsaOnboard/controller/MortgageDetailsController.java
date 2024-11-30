package com.suryoday.dsaOnboard.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.dsaOnboard.pojo.MortgageDetails;
import com.suryoday.dsaOnboard.service.MortgageDetailsService;

@RestController
@RequestMapping(value = "/mortagage")
public class MortgageDetailsController {
	@Autowired
	MortgageDetailsService mortgageDetailsService;

	@RequestMapping(value = "/saveMortgageDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestParam("file") MultipartFile file, @RequestParam("Data") String req)
			throws IOException {

		JSONObject jsonObject = new JSONObject(req);
		mortgageDetailsService.saveMortgageDetails(jsonObject, file);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", "Details Saved Successfully");

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchByEmpId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByEmpId(@RequestBody String jsonRequest) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		List<MortgageDetails> list = mortgageDetailsService.fetchByEmpId(jsonObject);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", list);

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		List<MortgageDetails> list = mortgageDetailsService.fetchByDate(jsonObject);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", list);

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/download/excel", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadCsv(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		JSONObject jsonObject = new JSONObject(bm);
		String startDate = jsonObject.getString("StartDate");
		String endDate = jsonObject.getString("EndDate");

		List<MortgageDetails> list = new ArrayList<>();
		if (startDate.isEmpty() || endDate.isEmpty()) {
			list = mortgageDetailsService.fetchByDate(jsonObject);
			System.out.println("List" + list);
			if (list.isEmpty()) {
				throw new NoSuchElementException("List is empty");
			} else {
				JSONArray j = new JSONArray(list);
				System.out.println(j);
				JSONObject object1 = mortgageDetailsService.writeExcel(j);
				return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
			}
		}
//		list = leadService.findTopTenByDate(startdate, enddate);
		list = mortgageDetailsService.fetchByDate(jsonObject);
		System.out.println("List" + list);
		if (list.isEmpty()) {
			throw new NoSuchElementException("List is empty");
		} else {
			JSONArray j = new JSONArray(list);
			System.out.println(j);
			JSONObject object1 = mortgageDetailsService.writeExcel(j);
			return new ResponseEntity<Object>(object1.toString(), HttpStatus.OK);
		}
	}

}
