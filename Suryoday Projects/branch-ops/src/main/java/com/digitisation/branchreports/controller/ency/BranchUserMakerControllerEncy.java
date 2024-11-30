package com.digitisation.branchreports.controller.ency;

import javax.servlet.http.HttpServletRequest;

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

import com.digitisation.branchreports.service.BranchUserMakerService;
import com.digitisation.branchreports.utils.EncryptDecryptHelper;

@RestController
@RequestMapping({ "/branchdigi" })
public class BranchUserMakerControllerEncy {

	Logger LOG = LoggerFactory.getLogger(BranchUserMakerControllerEncy.class);

	@Autowired
	private BranchUserMakerService branchuserservice;

	@PostMapping("/report/get")
	public ResponseEntity<Object> getReports(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) {
		LOG.debug(
				"BranchUserMakerController :: getReports :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = branchuserservice.getReports(requestString, channel, X_Session_ID, X_encode_ID, null,
				false, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/report/get/documentById")
	public ResponseEntity<Object> getReportDocumentById(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) {
		LOG.debug(
				"BranchUserMakerController :: getReportDocumentById :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = branchuserservice.getReportDocumentById(requestString, channel, X_Session_ID, X_encode_ID, null, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	/**
	 * MAKER, CHECKER, HO, AUDITOR
	 **/
	@PostMapping("/report/update")
	public ResponseEntity<Object> updateReport(@RequestParam("Data") String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request,
			@RequestParam(required = false) MultipartFile document) {
		LOG.debug(
				"BranchUserMakerController :: updateReport :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		// 1
		JSONObject responseJson = branchuserservice.updateReport(requestString, channel, X_User_ID, X_Session_ID,
				X_encode_ID, document, request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/report/download/excel")
	public ResponseEntity<Object> downloadExel(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID, HttpServletRequest request) {
		LOG.debug(
				"BranchUserMakerController :: download/exel :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		// 1
		JSONObject responseJson = branchuserservice.downloadExel(requestString, channel, X_Session_ID, X_encode_ID,
				request, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/report/getPendingReportsExcel")
	public ResponseEntity<Object> getPendingReportsExcel(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) {
		LOG.debug(
				"BranchUserMakerController :: getPendingReportsExcel :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = branchuserservice.getPendingReportsExcel(requestString, channel, X_Session_ID,
				X_encode_ID, null, true);
		// Encrypt Response
		responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, X_Session_ID, X_encode_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

}
