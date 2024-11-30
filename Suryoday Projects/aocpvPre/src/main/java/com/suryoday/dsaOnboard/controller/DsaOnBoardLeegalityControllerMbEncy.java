package com.suryoday.dsaOnboard.controller;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.HtmlConverter;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.dsaOnboard.pojo.DsaOnBoardLeegalityInfo;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.service.DmsUploadService;
import com.suryoday.dsaOnboard.service.DsaCodeCreationService;
import com.suryoday.dsaOnboard.service.DsaImageService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnBoardLeegalityService;
import com.suryoday.dsaOnboard.service.DsaOnBoardPdfService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@RestController
@RequestMapping("/dsaOnBoard/mb")
public class DsaOnBoardLeegalityControllerMbEncy {

	@Autowired
	DsaOnBoardLeegalityService leegalityservice;
	
	@Autowired
	DsaOnBoardPdfService dsaOnBoardPdfService;
	
	@Autowired
	DsaImageService imageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;
	
	@Autowired
	DsaCodeCreationService dsaCodeCreationService;
	
	@Autowired 
	DmsUploadService dmsUploadService;
	
	Logger logger = LoggerFactory.getLogger(DsaOnBoardLeegalityController.class);
			
	@RequestMapping(value = "/sendLeegalityEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendLeegality(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)
			throws Exception {
	
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
//			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		String OutputFileName = "";
		String pdfresponse = "";
		ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
		x.getappprop();

		if(documentType.equals("connectorServiceAgreement")) {
			OutputFileName = x.temp+applicationNo+"connectorServiceAgreement.pdf";
			StringBuilder htmlString = new StringBuilder();
			pdfresponse = dsaOnBoardPdfService.connectorServiceAgreementPdf(htmlString,applicationNo);
		}else if(documentType.equals("dsaAgreement")) {
			OutputFileName = x.temp+applicationNo+"dsaAgreement.pdf";
			StringBuilder htmlString = new StringBuilder();
			pdfresponse = dsaOnBoardPdfService.dsaAgreementPdf(htmlString,applicationNo);	
		}

		HtmlConverter.convertToPdf(pdfresponse, new FileOutputStream(OutputFileName));
		document.close();
		byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));
		
		String base64pdf = Base64.getEncoder().encodeToString(inFileBytes);

		JSONObject response = new JSONObject();
		DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		
		if(dsaOnboardDetails.getSendLeegalityVerify() !=null && dsaOnboardDetails.getSendLeegalityVerify().equals("YES")) {
			response.put("DocumentId", "");
			response.put("Invitees", "");
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		}
		
		JSONObject sendLeegality = leegalityservice.sendLeegality(dsaOnboardDetails, base64pdf,documentType);
		String documentId="";
		JSONArray invitees=null;
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (sendLeegality != null) {
			JSONObject requestToSave = sendLeegality.getJSONObject("request");
			requestToSave.getJSONObject("Data").remove("File");

			String Data2 = sendLeegality.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
			 documentId = Data1.getJSONObject("Data").getString("DocumentId");
			 invitees = Data1.getJSONObject("Data").getJSONArray("Invitees");
			leegalityservice.updateDocumentId(Data1, applicationNo,documentType);
			h = HttpStatus.OK;
			response.put("DocumentId", documentId);
			response.put("Invitees", invitees);
			
			dsaOnboardDetails.setSendLeegalityVerify("YES");
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
			dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;
				String description = Data1.getJSONObject("Error").getString("Description");
				response.put("Description", description);
				
			}
			
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);

		} else {
			logger.debug("timeout");
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
	
	@RequestMapping(value = "/checkLeegalityEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkLeegality(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "Accept", required = true) String Accept,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)
			throws Exception {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
//			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			
		JSONObject Header = new JSONObject();
		Header.put("Accept", Accept);
		
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		JSONObject response = new JSONObject();
		List<DsaOnBoardLeegalityInfo> listDB = leegalityservice.getByApplicationNoAndDocument(applicationNo, documentType);
		int count=0;
				for(DsaOnBoardLeegalityInfo leegalityInfo:listDB) {
					if(leegalityInfo.getSigned().equalsIgnoreCase("true")) {
						count++;
					}
				}
				if(count==2) {
					JSONArray j = new JSONArray(listDB);	
					response.put("Data", j);
					String data = response.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.OK);
				}
				if(listDB.size() == 0) {
					throw new NoSuchElementException("No recoed Present");
				}
				String documentId = listDB.get(0).getDocumentId();
		JSONObject checkLeegality = leegalityservice.checkLeegality(applicationNo, Header,documentType,documentId);
		logger.debug("checkLeegalityResponse"+checkLeegality);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (checkLeegality != null) {
			String Data2 = checkLeegality.getString("data");
			System.out.println("data2" + Data2);
			JSONObject Data1 = new JSONObject(Data2);
			
			if (Data1.has("Data")) {
			String signed1 = Data1.getJSONObject("Data").getJSONArray("Requests").getJSONObject(0).getString("Signed");
			String signed2 = Data1.getJSONObject("Data").getJSONArray("Requests").getJSONObject(1).getString("Signed");
			String pdf = Data1.getJSONObject("Data").getJSONArray("Files").getString(0);
			logger.debug("signed1"+signed1);
			logger.debug("signed2"+signed2);
			List<DsaOnBoardLeegalityInfo> list = leegalityservice.updateLeegalityVerify(applicationNo, Data1,documentType);

					org.json.JSONObject loanAgreement = new org.json.JSONObject();
					loanAgreement.put("image", documentType+".pdf");
					loanAgreement.put("Lat", "00");
					loanAgreement.put("Long", "00");
					loanAgreement.put("Address", "");
					LocalDateTime localDateTime = LocalDateTime.now();
					loanAgreement.put("timestamp", localDateTime);
					org.json.JSONObject loanAgreement1 = new org.json.JSONObject();
					if(documentType.equalsIgnoreCase("connectorServiceAgreement")) {
						loanAgreement1.put("connectorServiceAgreement", loanAgreement);	
					}else {
						loanAgreement1.put("dsaAgreement", loanAgreement);	
					}
					 byte[] image = Base64.getDecoder().decode(pdf);
					 imageService.savepdf(image,loanAgreement1,applicationNo);
					 JSONArray j = new JSONArray(list);	
					 response.put("Data", j);
					 h = HttpStatus.OK;
					 logger.debug("signed1"+signed1);
						logger.debug("signed2"+signed2);
					 if(signed1.equals("true") || signed2.equals("true")) {
						 
						 DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
						
							dsaOnboardDetails.setCheckLeegalityVerify("YES");
							dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
							dsaOnboardDetails.setLeegalityDate(LocalDate.now());
							dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
							
					 }
					
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);

		} else {
			logger.debug("timeout");
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

}
