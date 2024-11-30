package com.digitisation.branchreports.controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.exception.ErrorResponse;
import com.digitisation.branchreports.service.BranchUserMakerService;
import com.digitisation.branchreports.utils.EncryptDecryptHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping({ "/testing/branchdigi" })
public class BranchUserMakerController extends OncePerRequestFilter {

	Logger LOG = LoggerFactory.getLogger(BranchUserMakerController.class);

	@Autowired
	private BranchUserMakerService branchuserservice;

	@PostMapping("/welcome")
	public String welcome() {
		LOG.debug("BranchUserMakerService :: WELCOME to Branch Degitisation Project.");
		return "BranchUserMakerService :: WELCOME to Branch Degitisation Project.";
	}

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
				false, false);
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
		JSONObject responseJson = branchuserservice.getReportDocumentById(requestString, channel, X_Session_ID,
				X_encode_ID, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/report/update")
	public ResponseEntity<Object> updateReport(@RequestParam("Data") String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID,
			@RequestParam(required = false) MultipartFile document) {
		LOG.debug(
				"BranchUserMakerController :: updateReport :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject responseJson = branchuserservice.updateReport(requestString, channel, X_User_ID, X_Session_ID,
				X_encode_ID, document, null, false);
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
				request, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/report/getPendingReportsExcel")
	public ResponseEntity<Object> getPendingReportsExcel(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) {
		LOG.debug(
				"BranchUserMakerController :: getReports :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = branchuserservice.getPendingReportsExcel(requestString, null, null, null, null,
				false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/decryption")
	public ResponseEntity<Object> decryption(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) {
		LOG.debug(
				"BranchUserMakerController ::encrypton :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		// 1
		JSONObject responseJson = branchuserservice.encryption(requestString, channel, X_Session_ID, null, false);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	@PostMapping("/encryption")
	public ResponseEntity<Object> encryption(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-encode-ID", required = true) String X_encode_ID) {
		LOG.debug(
				"BranchUserMakerController ::decryption :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);

		JSONObject encryptJson = new JSONObject(requestString);
		// Encrypt Response
		JSONObject responseJson = EncryptDecryptHelper.encryptResponseString(encryptJson, channel, X_Session_ID,
				X_encode_ID);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}

	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException, JsonProcessingException {
		String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
		return serialized.getBytes();
	}

//	Daily
//	Monthly
//	Quarterly
//	Weekly

	/**
	 * TESTING APIs
	 **/
	@PostMapping("/report/add/daily")
	public String testtingAddDailyReports() {
		return branchuserservice.addDailyReports(null, null);
	}

	@PostMapping("/report/add/weekly")
	public String testingAddWeeklyReports() {
		return branchuserservice.addWeeklyReports(null);
	}

	@PostMapping("/report/add/monthly")
	public String testingAddMonthlyReports() {
		return branchuserservice.addMonthlyReports(null);
	}

	@PostMapping("/report/add/qurterly")
	public String testingAddQurterlyReportsV2() {
		return branchuserservice.addQuarterlyReports(null);
	}

	@PostMapping("/report/update/status/pending")
	public String testingUpdateStatusNotsubmittedToPending() {
		return branchuserservice.updateStatusNotsubmittedToPending();
	}

	@PostMapping("/report/getCountByDate")
	public Long getCountByDate(@RequestParam String date) {
		return branchuserservice.getCountByDate(date);
	}

	@PostMapping("report/base64ToExcel")
	public String base64ToExcel(@RequestBody String request) {
		return branchuserservice.base64ToExcel(request);
	}

	@PostMapping("report/getExcelByDate")
	public String getExcelByDate(@RequestBody String request) {
		return branchuserservice.getExcelByDate(request);
	}

	@PostMapping("/report/add/daily/byDate/byBranch")
	public String testtingAddDailyReports(@RequestParam String reportDate, @RequestParam Long branchCode) {
		return branchuserservice.addDailyReports(LocalDate.parse(reportDate), branchCode);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getMethod().equals("OPTIONS")) {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "unatharised Access");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(401);
			errorResponse.setMessage("Unauthorized Access");

			byte[] responseToSend = restResponseBytes(errorResponse);
			((HttpServletResponse) response).setHeader("Content-Type", "application/json");
			((HttpServletResponse) response).setStatus(401);
			response.getOutputStream().write(responseToSend);
			return;
		} else {
//			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://sarathi.suryodaybank.com");
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://sarathi.suryodaybank.co.in");
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST");

			// Set other security headers
			response.setHeader("Strict-Transport-Security", "false");
			response.setHeader("X-Frame-Options", "");
			response.setHeader("X-Content-Type-Options", "nosniff");
			response.setHeader("Content-Security-Policy", "src");
			response.setHeader("X-XSS-Protection", "0");

			filterChain.doFilter(request, response);
		}
	}

	@PostMapping("/report/existingBase64ToFile")
	public ResponseEntity<Object> existingBase64ToFile() {
		// 1
		branchuserservice.existingBase64ToFile();
		return new ResponseEntity<Object>("=-=-=-=-", HttpStatus.OK);
	}

	@PostMapping("/report/today/branch/excel")
	public ResponseEntity<Object> getTodayBranchesList() {
		return new ResponseEntity<Object>(branchuserservice.getTodayBranchesList(), HttpStatus.OK);
	}

	@PostMapping("/report/bytesToExcel")
	public ResponseEntity<Object> getBytesToExcel(@RequestBody String request) {
		branchuserservice.getBytesToExcel(request);
		return new ResponseEntity<Object>("DONE", HttpStatus.OK);
	}

	@PostMapping("/report/getCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId")
	public ResponseEntity<Object> gtCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId(
			@RequestBody String request) {
		return new ResponseEntity<Object>(
				branchuserservice.getCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId(request),
				HttpStatus.OK);
	}

}
