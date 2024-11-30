package com.suryoday.hastakshar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.hastakshar.enums.EmailTemplate;
import com.suryoday.hastakshar.service.EmailService;
import com.suryoday.hastakshar.service.ReqStatusService;

@RestController
@RequestMapping("/hastakshar")
public class SaveHastRequestData {

	@Autowired
	private ReqStatusService reqstatusservice;

	@Autowired
	private EmailService emailService;

	private static Logger logger = LoggerFactory.getLogger(SaveHastRequestData.class);

	@PostMapping(value = "/saveNewRequest", produces = "application/json")
	public ResponseEntity<Object> saveNewRequest(@RequestBody String requestBody,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject getResponse = reqstatusservice.saveNewRequestData(jsonRequest, X_User_ID);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchTopten", produces = "application/json")
	public ResponseEntity<Object> fetchTopten(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = reqstatusservice.fetchList(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchBySearch", produces = "application/json")
	public ResponseEntity<Object> fetchBySearch(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = reqstatusservice.fetchBySearch(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchByDate", produces = "application/json")
	public ResponseEntity<Object> fetchByDate(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = reqstatusservice.fetchByDate(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchByStatus", produces = "application/json")
	public ResponseEntity<Object> fetchByStatus(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = reqstatusservice.fetchByStatus(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/updateStatus", produces = "application/json")
	public ResponseEntity<Object> updateStatus(@RequestBody String requestBody,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject getResponse = reqstatusservice.updateStatus(jsonRequest, X_User_ID);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchUserLog", produces = "application/json")
	public ResponseEntity<Object> fetchUserLog(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = reqstatusservice.fetchUserLog(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/saveAttachment", produces = "application/json")
	public ResponseEntity<Object> saveAttachment(@RequestParam List<MultipartFile> files,
			@RequestParam("Data") String requestBody, HttpServletRequest req) {

		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject getResponse = reqstatusservice.saveAttachment(jsonRequest, files);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
//		return null;
	}

//	@PostMapping(value = "/saveAttachment", produces = "application/json")
//	public ResponseEntity<Object> saveAttachment(@RequestParam List<MultipartFile> files,
//			@RequestParam("Data") String requestBody) {
//		System.out.println(files.size());
//		System.out.println(requestBody);
//		return null;
//	}

	@PostMapping(value = "/fetchAttachment", produces = "application/json")
	public ResponseEntity<Object> fetchAttachment(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject response = reqstatusservice.fetchAttachment(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", response);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchStatusByEmpID", produces = "application/json")
	public ResponseEntity<Object> fetchStatusByEmpID(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONArray getResponse = reqstatusservice.fetchStatusByEmpID(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/reAssignApprover", produces = "application/json")
	public ResponseEntity<Object> reAssignApprover(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject getResponse = reqstatusservice.reAssignApprover(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/getApprovalOfProxy", produces = "application/json")
	public ResponseEntity<Object> getApprovalOfProxy(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);
		JSONObject getResponse = reqstatusservice.getApprovalOfProxy(jsonRequest);
		JSONObject outerData = new JSONObject();
		outerData.put("Data", getResponse);
		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	// TESTING

	@PostMapping("/testingGetAccountDetails")
	public String testingGetAccountDetails() {
		JSONObject request = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("accountNumber", "233000000629");
		request.put("Data", data);
		// STEP :: 1 GET CIF
		JSONObject response = reqstatusservice.getAccountDetails(null, request);
		return response.toString();
	}

	@PostMapping("/testingUpdateMobileNumberLimit")
	public String testingUpdateMobileNumberLimit() {
		JSONObject response = reqstatusservice.updateMobileNumberLimit("220160800", "2500000");
		return response.toString();
	}

	@PostMapping("/testingEnhanceLimit")
	public String testingEnhanceLimit() {
		JSONObject response = reqstatusservice.enhanceLimit("233000000629", "2500000");
		return response.toString();
	}

	@PostMapping("/testingSentEmail")
	public String testingSentEmail() {
		JSONObject response = emailService.sendEmail("it.developer@suryodaybank.com", "subject",
				EmailTemplate.HASTAKSHAR_REMINDER.getTemplateName(), "16AD312BB51E89A570D9A8903436A16A",
				"Suryoday Small Finance Bank Limited", "alerts@suryodaybank.com", "T", "", "");
		System.out.println(response);
		return response.toString();
	}

	@PostMapping("/testing/linkMobileToMultipleCIF")
	public String testingLinkMobileToMultipleCIF() {
		// moblieNo, existingCIF, linkToCIF
		JSONObject response = reqstatusservice.linkMobileToMultipleCIF("8826584155", "220160800", "220160800");
		return response.toString();
	}

	@PostMapping("/linkMobileToMultipleCIF")
	public String testingLinkMobileToMultipleCIF(@RequestParam String moblieNo, @RequestParam String existingCIF,
			@RequestParam String linkToCIF) {
		JSONObject response = reqstatusservice.linkMobileToMultipleCIF(moblieNo, existingCIF, linkToCIF);
		return response.toString();
	}

	@PostMapping("/getOriginalBalanceAmount")
	public String getOriginalBalanceAmountByCustomerNo(@RequestParam String accountNo) {
		String response = reqstatusservice.getOriginalBalanceAmountByCustomerNo(accountNo);
		return response;
	}

}