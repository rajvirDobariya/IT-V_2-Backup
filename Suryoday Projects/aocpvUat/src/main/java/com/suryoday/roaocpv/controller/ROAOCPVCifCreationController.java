package com.suryoday.roaocpv.controller;

import java.time.LocalDateTime;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.service.ROAOCPVCifCreationService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVCifCreationController {

	@Autowired
	ROAOCPVCifCreationService roaocpvCifCreationService;

	@Autowired
	ApplicationDetailsController detailsController;

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	@RequestMapping(value = "/CIfCreation", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> dedupeCall(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		JSONObject Header = new JSONObject();

		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);

		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		Long applicationNoInLong = Long.parseLong(applicationNo);
		AocpvLoanCreation findByApplicationNo = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
		if (findByApplicationNo.getCifNumber() != null) {
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Message", "SuccessFully CIFNumber Generated");
			response.put("CifNumber", findByApplicationNo.getCifNumber());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}

		JSONObject dedupeCall = roaocpvCifCreationService.cifCreation(jsonObject, Header);
		System.out.println("Api Response" + dedupeCall);
		String Data2 = dedupeCall.getString("data");
		JSONObject Data1 = new JSONObject(Data2);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (Data1.has("Data")) {
			h = HttpStatus.OK;

		} else if (Data1.has("Error")) {
			h = HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Object>(Data1.toString(), h);
		}
		String cifNumber = Data1.getJSONObject("Data").getString("UCIC");
//		String cifNumber="1234567";
		ResponseEntity<Object> fetchByApplicationPDF = detailsController.fetchByApplicationPDF(bm, headerPersist,
				X_From_ID, X_To_ID, X_Transaction_ID, X_User_ID, X_Request_ID, req, res);

		JSONObject jsonObject1 = new JSONObject(fetchByApplicationPDF);
		String string = jsonObject1.getJSONObject("body").getString("Data");
		System.out.println(string);
		byte[] image = Base64.getDecoder().decode(string);
		String lat = jsonObject.getJSONObject("Data").getString("lat");
		String geoLong = jsonObject.getJSONObject("Data").getString("geoLong");
		JSONObject jsonObject2 = new JSONObject();
		JSONObject cifPdf = new JSONObject();
		cifPdf.put("Lat", lat);
		cifPdf.put("image", "cif_" + cifNumber + ".pdf");
		cifPdf.put("Long", geoLong);
		cifPdf.put("Address", "");
		cifPdf.put("timestamp", "");
		jsonObject2.put("cifPdf", cifPdf);

		String message = aocpvImageService.savePdf(image, jsonObject2, applicationNoInLong);
		findByApplicationNo.setCifNumber(cifNumber);
		long customerId = Long.parseLong(cifNumber);
		findByApplicationNo.setCustomerId(customerId);
		LocalDateTime now = LocalDateTime.now();
		findByApplicationNo.setCifTimeStamp(now);
		aocpvLoanCreationService.saveData(findByApplicationNo);
		if (dedupeCall != null) {
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Message", "SuccessFully CIFNumber Generated");
			response.put("CIFNumber", cifNumber);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

}
