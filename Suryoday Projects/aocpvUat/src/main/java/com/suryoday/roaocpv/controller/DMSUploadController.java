package com.suryoday.roaocpv.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.service.DMSUploadService;

@RestController
@RequestMapping(value = "/roaocpv")
public class DMSUploadController {
	@Autowired
	DMSUploadService dmsuplaodservice;
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	private static Logger logger = LoggerFactory.getLogger(DMSUploadController.class);

	@SuppressWarnings("unchecked")
//	@Scheduled(cron="0 0/30 * * * ?")  //Every 30 Minute
	@RequestMapping(value = "/dmsupload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> dmsuplaod() throws IOException {
		List<AocpvLoanCreation> list = aocpvLoanCreationService.fetchByIsdms("NO");
		JSONObject sendAuthenticateResponse = new JSONObject();
		for (int i = 0; i < list.size(); i++) {
			long appNoformDB = list.get(i).getApplicationNo();
			sendAuthenticateResponse = dmsuplaodservice.dmsupload(appNoformDB);
		}
		JSONArray jsonArray = sendAuthenticateResponse.getJSONArray("Response");
		HttpStatus h = HttpStatus.OK;
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Message", "DOCUMENT UPLOAD SUCCESSFULLY");
		for (int n = 0; n < jsonArray.length(); n++) {
			JSONObject jsonResponse = jsonArray.getJSONObject(n);
			if (jsonResponse.has("Errors")) {
				h = HttpStatus.BAD_REQUEST;
				response.put("Message", "FAILED!! PLEASE TRY AGAIN");
			}
		}
		logger.debug("Response in controller: " + response);
		return new ResponseEntity<>(response.toString(), h);
	}
}