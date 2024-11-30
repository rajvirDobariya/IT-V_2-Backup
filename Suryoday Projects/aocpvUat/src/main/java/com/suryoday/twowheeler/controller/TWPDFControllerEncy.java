package com.suryoday.twowheeler.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.HtmlConverter;
import com.lowagie.text.BadElementException;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.service.TWPDFService;
import com.suryoday.twowheeler.service.TwowheelerImageService;

@Component
@RestController
@RequestMapping(value = "/twowheeler")
public class TWPDFControllerEncy {

	@Autowired
	TWPDFService twpdfservice;

	@Autowired
	TwowheelerImageService twowheelerImageService;

	@Autowired
	UserService userService;

	Logger logger = LoggerFactory.getLogger(TWPDFControllerEncy.class);

	@RequestMapping(value = "/sanctionLetter/downloadPdfEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadTwSanctionLetterPdf(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws IOException, BadElementException {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			String OutputFileName = "";
			String pdfresponse = "";
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();

			OutputFileName = x.temp + applicationNo + "sanctionLetter.pdf";
			StringBuilder htmlString = new StringBuilder();
			pdfresponse = twpdfservice.sanctionPdf(htmlString, applicationNo);

			HtmlConverter.convertToPdf(pdfresponse, new FileOutputStream(OutputFileName));
			document.close();
			byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));
//		org.json.JSONObject sanctionLetter = new org.json.JSONObject();
//		sanctionLetter.put("image", "photo.jpg");
//		sanctionLetter.put("Lat", "00");
//		sanctionLetter.put("Long", "00");
//		sanctionLetter.put("Address", "");
//		LocalDateTime localDateTime = LocalDateTime.now();
//		sanctionLetter.put("timestamp", localDateTime);
//		org.json.JSONObject sanctionLetter1 = new org.json.JSONObject();
//		sanctionLetter1.put("sanctionLetter", sanctionLetter);

//		twowheelerImageService.savepdf(inFileBytes,sanctionLetter1,applicationNo);

			String base64 = Base64.getEncoder().encodeToString(inFileBytes);
			System.out.println(base64);
			JSONObject response = new JSONObject();
			response.put("Success", "PDF Successfully Downloaded");
			response.put("pdf", base64);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/loanApplicationForm/downloadPdfEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadLoanApplicationFormPdf(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws IOException, BadElementException {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			String OutputFileName = "";
			String pdfresponse = "";
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();

			OutputFileName = x.temp + applicationNo + "loanApplicationForm.pdf";
			StringBuilder htmlString = new StringBuilder();
			pdfresponse = twpdfservice.loanApplicationFormPdf(htmlString, applicationNo);

			HtmlConverter.convertToPdf(pdfresponse, new FileOutputStream(OutputFileName));
			document.close();
			byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));

			org.json.JSONObject loanApplicationForm = new org.json.JSONObject();
			loanApplicationForm.put("image", "photo.jpg");
			loanApplicationForm.put("Lat", "00");
			loanApplicationForm.put("Long", "00");
			loanApplicationForm.put("Address", "");
			LocalDateTime localDateTime = LocalDateTime.now();
			loanApplicationForm.put("timestamp", localDateTime);
			org.json.JSONObject loanApplication = new org.json.JSONObject();
			loanApplication.put("loanApplicationForm", loanApplicationForm);

			twowheelerImageService.savepdf(inFileBytes, loanApplication, applicationNo);

			String base64 = Base64.getEncoder().encodeToString(inFileBytes);
			System.out.println(base64);
			JSONObject response = new JSONObject();
			response.put("Success", "PDF Successfully Downloaded");
			response.put("pdf", base64);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/whatsappMediaSendEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> whatsappMediaSend(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			JSONObject jsonObject1 = twpdfservice.sendMedia(jsonObject, Header);
			JSONObject jsonObject2 = null;
			JSONArray jsonObject3 = null;
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonObject1 != null) {
				String Data2 = jsonObject1.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("Data")) {
					jsonObject2 = Data1.getJSONObject("Data");
					h = HttpStatus.OK;
				} else if (Data1.has("Errors")) {
					jsonObject3 = Data1.getJSONArray("Errors");
					h = HttpStatus.BAD_REQUEST;
					String data = jsonObject3.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, h);
				}
				String data = jsonObject2.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, h);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/loanAgreement/downloadPdfEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> downloadloanAgreementPdfEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request)
			throws IOException, BadElementException {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			long applicationNoInLong = Long.parseLong(applicationNo);

			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			String OutputFileName = "";
			String pdfresponse = "";
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();

			OutputFileName = x.temp + applicationNo + "loanAgreement.pdf";
			StringBuilder htmlString = new StringBuilder();
			pdfresponse = twpdfservice.loanAgreementPdf(htmlString, applicationNo);

			HtmlConverter.convertToPdf(pdfresponse, new FileOutputStream(OutputFileName));
			document.close();
			byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));

//		org.json.JSONObject loanAgreement = new org.json.JSONObject();
//		loanAgreement.put("image", "photo.jpg");
//		loanAgreement.put("Lat", "00");
//		loanAgreement.put("Long", "00");
//		loanAgreement.put("Address", "");
//		LocalDateTime localDateTime = LocalDateTime.now();
//		loanAgreement.put("timestamp", localDateTime);
//		org.json.JSONObject loanAgreement1 = new org.json.JSONObject();
//		loanAgreement1.put("loanAgreementPdf", loanAgreement);

//		twowheelerImageService.savepdf(inFileBytes,loanAgreement1,applicationNo);

			String base64 = Base64.getEncoder().encodeToString(inFileBytes);
			System.out.println(base64);
			JSONObject response = new JSONObject();
			response.put("Success", "PDF Successfully Downloaded");
			response.put("pdf", base64);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

}
