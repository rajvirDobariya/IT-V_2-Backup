package com.suryoday.roaocpv.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

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
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.LoanAgreementService;

@RestController
@RequestMapping(value="/roacpv")
public class LoanAgreementController {
	
	
	
	
	@Autowired
	AocpvImageService aocpvImageService;
	
	@Autowired
	LoanAgreementService loanAgreementService;
	
	Logger logger = LoggerFactory.getLogger(LoanAgreementController.class);
	@RequestMapping(value = "/downloadvltcPdf", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> downloadPdf(@RequestBody String jsonRequest,
    		@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws IOException {
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	     long applicationNoInLong = Long.parseLong(applicationNo);
	     
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		String OutputFileName ="";
		String pdfresponse="";
		 ROAOCPVGenerateProperty x=ROAOCPVGenerateProperty.getInstance();
         x.getappprop();
		OutputFileName = x.onePager +applicationNoInLong+"_vltc.pdf";
		StringBuilder htmlString = new StringBuilder();
		
		pdfresponse	=loanAgreementService.reportPdf(htmlString,applicationNoInLong);
	
		HtmlConverter.convertToPdf(pdfresponse,new FileOutputStream(OutputFileName));
		document.close();
		
		byte[] inFileBytes = Files.readAllBytes(Paths.get(OutputFileName));
		
		String lat = "70";
		String geoLong = "70";
	JSONObject jsonObject2 = new JSONObject();
		JSONObject vltc = new JSONObject();
		vltc.put("Lat", lat);
		vltc.put("image", applicationNoInLong+"_vltc.pdf");
		vltc.put("Long", geoLong);
		vltc.put("Address", "");
		vltc.put("timestamp", "");
		jsonObject2.put("agreementpdf", vltc);

		String message =aocpvImageService.savePdf(inFileBytes,jsonObject2,applicationNoInLong);
		String base64 = Base64.getEncoder().encodeToString(inFileBytes);
		JSONObject response=new JSONObject();
		response.put("Success","PDF Successfully Downloaded");
		response.put("pdf",base64);
		return new ResponseEntity<>(response.toString(), HttpStatus.OK);    	
    }

}
