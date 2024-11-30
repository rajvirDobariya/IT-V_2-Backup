package com.suryoday.roaocpv.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvIncomeDetails;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.roaocpv.others.PDFCIF;
import com.suryoday.roaocpv.pojo.ApplicationDetailList;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ApplicationDetailsResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;

@RestController
@RequestMapping("/roaocpv")
public class ApplicationDetailsController {

	@Autowired
	ApplicationDetailsService applicationDetailsService;

	@Autowired
	AocpvImageService aocpvImageService;
	
	
	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	@Autowired
	AocpvIncomeService aocpvIncomeService;
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationDetailsController.class);

	
	@RequestMapping(value="/fetchByStatusAndBranchId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByStatusAndBranchId(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String status = jsonObject.getJSONObject("Data").getString("status");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");

			List<ApplicationDetailList> list=applicationDetailsService.fetchByStatusAndBranchId(status,branchId);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", list);
		logger.debug("final response"+response.size());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/passorfail", method = RequestMethod.POST)
	public ResponseEntity<Object> passorfail(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
		String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		String status = jsonObject.getJSONObject("Data").getString("status");
			String appNoWithProductCode=applicationDetailsService.passorfail(applicationId,flowStatus,status);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message", "Done");
		response.put("appNoWithProductCode", appNoWithProductCode);
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/fetchByApplicationId", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationId(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");

		ApplicationDetailsResponse applicationDetails=applicationDetailsService.fetchAllByApplicationId(applicationId);
			
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", applicationDetails);
		logger.debug("final response"+response);
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/fetchByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String status = jsonObject.getJSONObject("Data").getString("status");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String startDate = jsonObject.getJSONObject("Data").getString("startDate");
		String endDate = jsonObject.getJSONObject("Data").getString("endDate");
		
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		 LocalDate startdate = LocalDate.parse(startDate, formatter);
		 LocalDate enddate = LocalDate.parse(endDate, formatter);

			List<ApplicationDetails> list=applicationDetailsService.fetchByDate(status,branchId,startdate,enddate);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", list);
		logger.debug("final response"+response.size());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/addpersonalDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> personalDetails(@RequestParam("file") MultipartFile[] files,@RequestParam("Data") String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
		String personalDetails = jsonObject.getJSONObject("Data").getJSONObject("personalDetails").toString();
		long applicationNoInLong= Long.parseLong(applicationId);
		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		String flowStatus="PD";
			String message=applicationDetailsService.addpersonalDetails(applicationId,personalDetails);
			aocpvImageService.saveCustomerPhoto(files,applicationNoInLong,document,flowStatus);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/createLead", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		logger.debug("save data start");
		logger.debug("request" + jsonRequest);

		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		String applicationNo = applicationDetailsService.createLead(jsonRequest,X_User_ID);

		if (applicationNo.isEmpty()) {
			return new ResponseEntity<>("Kindly Enter Valid Details", HttpStatus.BAD_REQUEST);
		}
        JSONObject reponse = new JSONObject();
        reponse.put("value", "Lead was created Sucessfully");
        reponse.put("applicationNo", applicationNo);
        org.json.JSONObject mainReposne= new org.json.JSONObject();
        mainReposne.put("Data", reponse);
		return new ResponseEntity<>(mainReposne.toString(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/saveMultipleImages", method = RequestMethod.POST)
	public ResponseEntity<Object> saveMultipleImages(@RequestParam("file") MultipartFile files[],@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		System.out.println("JSON STRING --->"+jsonObject);
		
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		 org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		  String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		  String member="SELF";
		  if(jsonObject.getJSONObject("Data").has("member")) {
			   member = jsonObject.getJSONObject("Data").getString("member");
		  }
		  long applicationno = Long.parseLong(applicationNo);
		  aocpCustomerDataService.flowStatusChange(applicationno);
		logger.debug("image save to db start");
	String saveImage =aocpvImageService.saveMultipleImages(files,document,applicationno,member);
		logger.debug("image save to db sucessfully");
		org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		response.put("message", saveImage);
		response.put("status", HttpStatus.OK.toString());
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	
	}
	
	@RequestMapping(value="/saveCIf", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCIf(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
		JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
		String message=applicationDetailsService.saveCIf(applicationId,jsonObject2);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message", message);
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	@RequestMapping(value="/fetchByApplicationPDF", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByApplicationPDF(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req, HttpServletResponse res) throws Exception {
		 res.setContentType("application/pdf");
		 res.setHeader("Content-Disposition", "attachment; filename=CIF.pdf");
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String application = jsonObject.getJSONObject("Data").getString("AppliationID");
		Long appln = Long.parseLong(application)
;

		//	List<ApplicationDetails> list=applicationDetailsService.fetchByApplicationPDF(application)
;
		List<AocpCustomer>  list = aocpCustomerDataService.fetchByApplicationNumberPDF(appln);
		List<AocpvIncomeDetails> list2 = aocpvIncomeService.getByApplicationNo(appln);
		AocpvImages aocpvImages = aocpvImageService.getByApplicationNo(appln);
		
		 		byte[] images2 = aocpvImages.getImages();
		 		String base64ImageCustomer = Base64.getEncoder().encodeToString(images2);	
		PDFCIF exportCIF = new PDFCIF(list,list2,images2);
		
		byte[] pdfcif =  exportCIF.exportPDFCIF(res);
		  String base64 = Base64.getEncoder().encodeToString(pdfcif);
	      String result = new String(base64); 
	      System.out.println("Conversion "+result);
	      org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	      response.put("Data", result);	  
		response.put("Data1", list);
		logger.debug("final response"+response.size());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/saveEmiResponse", method = RequestMethod.POST)
	public ResponseEntity<Object> saveEmiResponse(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
		 String requestedLoanResponse = jsonObject.getJSONObject("Data").getJSONObject("requestedLoanResponse").toString();
	
	ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationId);
	applicationDetails.setResponseEmi(requestedLoanResponse);
	applicationDetailsService.save(applicationDetails);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message", "update successfully");
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
}
	
	@RequestMapping(value="/bestEmi", method = RequestMethod.POST)
	public ResponseEntity<Object> bestEmi(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
		 
	ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationId);
	
	org.json.simple.JSONObject   bestOffer= new org.json.simple.JSONObject();
	bestOffer.put("maxEmiEligibility", applicationDetails.getMaxEmiEligibility());
	bestOffer.put("bestEligibleEmi", applicationDetails.getBestEligibleEmi());
	bestOffer.put("requiredAmount", applicationDetails.getRequiredAmount());
	bestOffer.put("applicationId", applicationId);
	
//	applicationDetailsService.save(applicationDetails);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", bestOffer);
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
}
	@RequestMapping(value="/saveBestOffer", method = RequestMethod.POST)
	public ResponseEntity<Object> saveBestOffer(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		if(jsonRequest.isEmpty()) {
			logger.debug("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
		String loanAmount = jsonObject.getJSONObject("Data").getString("loanAmount");
		String tenure = jsonObject.getJSONObject("Data").getString("tenure");
		String emi = jsonObject.getJSONObject("Data").getString("emi");
		String rateOfInterest = jsonObject.getJSONObject("Data").getString("rateOfInterest");
		
	ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationId);
	applicationDetails.setEmi(emi);
	applicationDetails.setTenure(tenure);
	applicationDetails.setRateOfInterest(rateOfInterest);
	applicationDetails.setRequiredAmount(loanAmount);
	
	applicationDetailsService.save(applicationDetails);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message", "update successfully");
		logger.debug("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
}
}
