package com.suryoday.audioComplaintController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.suryoday.audioComplaint.entity.AudioComplaintDetails;
import com.suryoday.audioComplaint.entity.AudioComplaintResponse;
import com.suryoday.audioComplaint.service.AudioComplaintService;

@RestController
public class AudioComplaintController {

	Logger logger = LoggerFactory.getLogger(AudioComplaintController.class);
	
	@Autowired
	private AudioComplaintService audioComplaintService;
	
	@RequestMapping(value = "/saveAudioComplaint", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestParam("file") MultipartFile[] files,
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("complaintNo");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		
		if (!document.isEmpty()) {
			audioComplaintService.saveAudioComplaint(files, applicationNo, document,X_User_ID,branchId);
		}

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", "Image Saved");
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	
	@RequestMapping(value = "/fetchByBranchId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByBranchId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);

		String userId = jsonObject.getJSONObject("Data").getString("userId");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		
		List<AudioComplaintResponse> fetchByApplicationNo = audioComplaintService.fetchByBranchId(userId,branchId);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public ResponseEntity<Object> changeStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);

		String complaintNo = jsonObject.getJSONObject("Data").getString("complaintNo");
		String status = jsonObject.getJSONObject("Data").getString("status");
		
		AudioComplaintDetails audioComplaintDetails = audioComplaintService.fetchByComplaintNo(complaintNo);
		audioComplaintDetails.setStatus(status);
		audioComplaintDetails.setUpdatedDate(LocalDateTime.now());
		audioComplaintService.save(audioComplaintDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", "status Change");
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	
	@RequestMapping(value = "/fetchByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String startDate = jsonObject.getJSONObject("Data").getString("startDate");
		String endDate = jsonObject.getJSONObject("Data").getString("endDate");
		
		if (startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("status", HttpStatus.BAD_REQUEST.toString());
			response.put("message", "enter a proper Dates");
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);

	
		List<AudioComplaintResponse> fetchByApplicationNo = audioComplaintService.fetchByDate(startdate,enddate);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
}
