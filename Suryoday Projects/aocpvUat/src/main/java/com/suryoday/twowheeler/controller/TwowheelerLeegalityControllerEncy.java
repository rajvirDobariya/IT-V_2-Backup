package com.suryoday.twowheeler.controller;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.LeegalityInfo;
import com.suryoday.twowheeler.service.TWPDFService;
import com.suryoday.twowheeler.service.TwowheelerImageService;
import com.suryoday.twowheeler.service.TwowheelerLeegalityService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerLeegalityControllerEncy {

	Logger logger = LoggerFactory.getLogger(TwowheelerLeegalityControllerEncy.class);

	@Autowired
	TwowheelerLeegalityService leegalityservice;

	@Autowired
	TWPDFService twpdfservice;

	@Autowired
	TwowheelerImageService twowheelerImageService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/sendLeegalityEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendLeegality(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
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

//		OutputFileName = applicationNo+"loanAgreement.pdf";

			if (documentType.equals("loanAgreement")) {
				OutputFileName = x.temp + applicationNo + "loanAgreement.pdf";
				StringBuilder htmlString = new StringBuilder();
				pdfresponse = twpdfservice.loanAgreementPdf(htmlString, applicationNo);
			} else if (documentType.equals("sanctionLetter")) {
				OutputFileName = x.temp + applicationNo + "sanctionLetter.pdf";
				StringBuilder htmlString = new StringBuilder();
				pdfresponse = twpdfservice.sanctionPdf(htmlString, applicationNo);
			}

			HtmlConverter.convertToPdf(pdfresponse, new FileOutputStream(OutputFileName));
			document.close();
			byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));

			String base64pdf = Base64.getEncoder().encodeToString(inFileBytes);
//		String base64pdf = jsonObject.getJSONObject("Data").getString("Base64pdf");
			JSONObject sendLeegality = leegalityservice.sendLeegality(applicationNo, base64pdf, documentType);
//		String s="{\r\n"
//				+ "    \"Data\": {\r\n"
//				+ "        \"TransactionCode\": \"00\",\r\n"
//				+ "        \"DocumentId\": \"3nsPEPc\",\r\n"
//				+ "        \"Irn\": \"88989235647\",\r\n"
//				+ "        \"Invitees\": [\r\n"
//				+ "            {\r\n"
//				+ "                \"Name\": \"Ratnesh\",\r\n"
//				+ "                \"Email\": \"ratnesh.shinde@suryodaybank.com\",\r\n"
//				+ "                \"Phone\": \"9372807903\",\r\n"
//				+ "                \"SignUrl\": \"https://sandbox.leegality.com/sign/13614284-56e2-43f3-9a55-ede01755cca5\",\r\n"
//				+ "                \"Active\": \"true\",\r\n"
//				+ "                \"ExpiryDate\": \"2023-08-17T18:29:59Z\"\r\n"
//				+ "            },\r\n"
//				+ "            {\r\n"
//				+ "                \"Name\": \"Mohit Arya\",\r\n"
//				+ "                \"Email\": \"mohit.arya@suryodaybank.com\",\r\n"
//				+ "                \"Phone\": null,\r\n"
//				+ "                \"SignUrl\": \"https://sandbox.leegality.com/sign/130bd54d-7a25-4707-a189-33d2a43173ff\",\r\n"
//				+ "                \"Active\": \"false\",\r\n"
//				+ "                \"ExpiryDate\": \"2023-08-17T18:29:59Z\"\r\n"
//				+ "            }\r\n"
//				+ "        ],\r\n"
//				+ "        \"Messages\": [\r\n"
//				+ "            {\r\n"
//				+ "                \"Code\": \"simpleWorkFlow.success\",\r\n"
//				+ "                \"Message\": \"Invitations sent successfully.\"\r\n"
//				+ "            }\r\n"
//				+ "        ]\r\n"
//				+ "    }\r\n"
//				+ "}";
//		
//		JSONObject data5 = new JSONObject(s);
//		JSONObject sendLeegality = new JSONObject();
//		sendLeegality.put("data", data5.toString());	

			String documentId = "";
			JSONArray invitees = null;
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (sendLeegality != null) {
				JSONObject response = new JSONObject();
				String Data2 = sendLeegality.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("Data")) {
					documentId = Data1.getJSONObject("Data").getString("DocumentId");
					invitees = Data1.getJSONObject("Data").getJSONArray("Invitees");
					leegalityservice.updateDocumentId(Data1, applicationNo, documentType);
					h = HttpStatus.OK;
					response.put("DocumentId", documentId);
					response.put("Invitees", invitees);
				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;
					String description = Data1.getJSONObject("Error").getString("Description");
					response.put("Description", description);

				}
				response.put("DocumentId", documentId);
				response.put("Invitees", invitees);
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
//			logger.debug("response" + Data1);
//			return new ResponseEntity<Object>(response.toString(), h);

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
	public ResponseEntity<Object> checkLeegality(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Accept", required = true) String Accept,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		JSONObject Header = new JSONObject();
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			Header.put("Accept", Accept);

			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String documentType = jsonObject.getJSONObject("Data").getString("documentType");
			JSONObject response = new JSONObject();
			List<LeegalityInfo> listDB = leegalityservice.getByApplicationNoAndDocument(applicationNo, documentType);
			int count = 0;
			for (LeegalityInfo leegalityInfo : listDB) {
				if (leegalityInfo.getSigned().equalsIgnoreCase("true")) {
					count++;
				}
			}
			if (count == 2) {
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
			JSONObject checkLeegality = leegalityservice.checkLeegality(applicationNo, Header, documentType);
//		String s="{\r\n"
//				+ "    \"Data\": {\r\n"
//				+ "        \"DocumentId\": \"3nsPEPc\",\r\n"
//				+ "        \"Requests\": [\r\n"
//				+ "            {\r\n"
//				+ "                \"Signed\": \"true\",\r\n"
//				+ "                \"Name\": \"Ratnesh\"\r\n"
//				+ "            },\r\n"
//				+ "            {\r\n"
//				+ "                \"Signed\": \"true\",\r\n"
//				+ "                \"Name\": \"Mohit Arya\"\r\n"
//				+ "            }\r\n"
//				+ "        ]\r\n"
//				+ "    }\r\n"
//				+ "}";
//		JSONObject data5 = new JSONObject(s);
//		JSONObject checkLeegality = new JSONObject();
//		checkLeegality.put("data", data5.toString());

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (checkLeegality != null) {
				String Data2 = checkLeegality.getString("data");
				System.out.println("data2" + Data2);
				JSONObject Data1 = new JSONObject(Data2);

				if (Data1.has("Data")) {
					String signed1 = Data1.getJSONObject("Data").getJSONArray("Requests").getJSONObject(0)
							.getString("Signed");
					String signed2 = Data1.getJSONObject("Data").getJSONArray("Requests").getJSONObject(1)
							.getString("Signed");
					String pdf = Data1.getJSONObject("Data").getJSONArray("Files").getString(0);
//			String pdf="";
					List<LeegalityInfo> list = leegalityservice.updateLeegalityVerify(applicationNo, Data1,
							documentType);
//			String flag = "No";
//			if (signed1.equals("true") && signed2.equals("true")) {
//				 flag = "Yes";
//				 response.put("Pdf", pdf);
					org.json.JSONObject loanAgreement = new org.json.JSONObject();
					loanAgreement.put("image", documentType + ".pdf");
					loanAgreement.put("Lat", "00");
					loanAgreement.put("Long", "00");
					loanAgreement.put("Address", "");
					LocalDateTime localDateTime = LocalDateTime.now();
					loanAgreement.put("timestamp", localDateTime);
					org.json.JSONObject loanAgreement1 = new org.json.JSONObject();
					if (documentType.equalsIgnoreCase("sanctionLetter")) {
						loanAgreement1.put("sanctionLetter", loanAgreement);
					} else {
						loanAgreement1.put("loanAgreementPdf", loanAgreement);
					}
					byte[] image = Base64.getDecoder().decode(pdf);
					twowheelerImageService.savepdf(image, loanAgreement1, applicationNo);

//			} else {
//				 flag = "No";
//			}
					JSONArray j = new JSONArray(list);
					response.put("Data", j);

					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				logger.debug("response : " + Data1);
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
