package com.suryoday.collections.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.collections.others.PDFReceipt;
import com.suryoday.collections.pojo.CSVModel;
import com.suryoday.collections.pojo.CSVModelResponse;
import com.suryoday.collections.pojo.ReceiptImage;
import com.suryoday.collections.pojo.ReceiptImageResponse;
import com.suryoday.collections.pojo.ReceiptPojo;
import com.suryoday.collections.pojo.ReceiptResponse;
import com.suryoday.collections.pojo.WebReceiptResponse;
import com.suryoday.collections.service.CSVService;
import com.suryoday.collections.service.ReceiptImageService;
import com.suryoday.collections.service.ReceiptService;
import com.suryoday.mhl.controller.ExcelController;


@Controller
@RequestMapping("/collections")
public class CollectionDetailsController {
	
	@Autowired
	CSVService csvService;
	
	@Autowired
    AocpCustomerDataService aocpCustomerDataService;
    
    @Autowired
    AocpvImageService aocpvImageService;
    
    @Autowired
	ReceiptService receiptService;
	
	@Autowired
	ReceiptImageService imageService;
	
	Logger logger = LoggerFactory.getLogger(CollectionDetailsController.class);
	
	@RequestMapping(value="/fetchAllRecords", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getCIFData(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject Header= new JSONObject();
		 Header.put("X-Request-ID",X_REQUEST_ID );
		 Header.put("X-Transaction-ID",X_TRANSACTION_ID );
		 Header.put("X-To-ID",X_TO_ID );
		 Header.put("X-From-ID",X_FORM_ID );
		 Header.put("Content-TypeD",CONTEND_TYPE );
		 
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String status = jsonObject.getJSONObject("Data").getString("Status");
            String branchID = jsonObject.getJSONObject("Data").getString("BRANCH_CODE");
            logger.debug("db Call start");
            List<CSVModelResponse> csvModel = csvService.findByStatusAll(status,branchID);
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
           // response.put("status", HttpStatus.OK);
            response.put("Data", csvModel);            
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/fetchAllLoan", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getLoanData(@RequestBody String jsonRequest,
	@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
	@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
	@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
	@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
	@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req) throws Exception{

	JSONObject Header= new JSONObject();
	Header.put("X-Request-ID",X_REQUEST_ID );
	Header.put("X-Transaction-ID",X_TRANSACTION_ID );
	Header.put("X-To-ID",X_TO_ID );
	Header.put("X-From-ID",X_FORM_ID );
	Header.put("Content-TypeD",CONTEND_TYPE );

	JSONObject jsonObject=new JSONObject(jsonRequest);
	logger.debug("Fetch All Loan Details");
	logger.debug("Request"+jsonRequest);

	if(jsonRequest.isEmpty()) {
	logger.debug("Request is empty"+jsonRequest);
	throw new EmptyInputException("Input cannot be empty");
	}

	String customerNO = jsonObject.getJSONObject("Data").getString("CUSTOMERID");
	Long customerNOLong=Long.parseLong(customerNO);
	logger.debug("db Call start");
	List<CSVModel> csvModel = csvService.findByAllLoan(customerNO);
	Long application = aocpCustomerDataService.getByCustomerIDApplication(customerNOLong);
	System.out.println(application);
	String address = aocpvImageService.getGeoLoactionByApplication(application);
	JSONObject jsonObjectImage=new JSONObject(address);
	
		String pimage = jsonObjectImage.getString("image");
		String pLat = jsonObjectImage.getString("Lat");
		String pLong = jsonObjectImage.getString("Long");
		String pAddress = jsonObjectImage.getString("Address");
		String ptimestamp = jsonObjectImage.getString("timestamp");
	org.json.simple.JSONObject response= new org.json.simple.JSONObject();
		GeoLcation geolocation =new GeoLcation(pimage,pLat,pLong,pAddress,ptimestamp);

	//response.put("status", HttpStatus.OK);
	response.put("Data", csvModel);
	//response.put("Customer Number", customerNO);
	response.put("GeoLocation", geolocation);
	System.out.println("Final Response"+response.toString());
	logger.debug("Final Response"+response.toString());
	return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/searchByNumber", method = RequestMethod.POST)
    public ResponseEntity<Object> fetchByAccount(@RequestBody String jsonRequest ,
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
        
        String accountNo = jsonObject.getJSONObject("Data").getString("aggrementID");
            
        List<CSVModel> csvModel  = csvService.findByAccountNo(accountNo);
            
        org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
        response.put("Data", csvModel);
        System.out.println("Final Response"+response.toString());
        
     return new ResponseEntity<Object>(response,HttpStatus.OK);
    }
	
	@RequestMapping(value="/fetchAllReceiptTop", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getTopReceipt(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			
            logger.debug("db Call start");
            List<ReceiptResponse> receipts = receiptService.findTopReceiptList(branchId);
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
           // response.put("status", HttpStatus.OK);
            response.put("Data", receipts);            
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getReceiptDetails", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getReceiptDetails(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			
            logger.debug("db Call start");
           //
            List<ReceiptPojo> receipt = receiptService.findAllDetails(branchId);
            String receiptNo = receiptService.getReceiptNumber(branchId);
          //  System.out.println("Receipt Number "+receiptNo);
            List<ReceiptImage> imagereceipt = imageService.findAllDetails2(receiptNo);
            List<ReceiptImageResponse> list =new ArrayList<>();
            for(ReceiptImage image :imagereceipt) {
            	ReceiptImageResponse imageResponse=new ReceiptImageResponse();
            	imageResponse.setAddress(image.getAddress());
            	imageResponse.setCustomerID(image.getCustomerID());
            	imageResponse.setCustomerImage(image.getCustomerID());
            	imageResponse.setLatitude(image.getLatitude());
            	imageResponse.setLongitude(image.getLongitude());
            	imageResponse.setReceiptNo(image.getReceiptNo());
            	imageResponse.setTimestamp(image.getTimestamp());
            	byte[] images2 = image.getImages();
            	String encoded = Base64.getEncoder().encodeToString(images2);
            	imageResponse.setImages(encoded);
            	list.add(imageResponse);
            }
         
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
           // response.put("status", HttpStatus.OK);
//            List webReeipt = new ArrayList(receipt);
//            webReeipt.addAll(imagereceipt);
            WebReceiptResponse webReceipt = new WebReceiptResponse();
            webReceipt.setReceiptPojo(receipt);
            webReceipt.setReceiptImage(list);
            
            response.put("Data", webReceipt); 
           // response.put("Data2", imagereceipt); 
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getReceiptByDate", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getReceiptByDate(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
			String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
			
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate startdate = LocalDate.parse(startDate, formatter);
			 DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate enddate = LocalDate.parse(endDate, formatter2);
            
            List<ReceiptResponse> receipt = receiptService.findAllDetailsByDate(branchId,startdate,enddate);
            
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            response.put("Data", receipt);  
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getCustomerByDate", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getCustomerByDate(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
			String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
			String status = jsonObject.getJSONObject("Data").getString("Status");
			
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate startdate = LocalDate.parse(startDate, formatter);
			 DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate enddate = LocalDate.parse(endDate, formatter2);
            
            List<CSVModelResponse> csv = csvService.findAllCustomerByDate(branchId,startdate,enddate,status);
            
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            response.put("Data", csv);  
            logger.debug("Final Response"+response.toString());
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getReceiptPDF", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getReceiptPDF(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req,HttpServletResponse res)  throws Exception{
		 res.setContentType("application/pdf");
		 res.setHeader("Content-Disposition", "attachment; filename=Receipt.pdf");
		 
		 JSONObject jsonObject=new JSONObject(jsonRequest);
			logger.debug("Fetch All Details");
			logger.debug("Request"+jsonRequest);
			
			if(jsonRequest.isEmpty()) {
				logger.debug("Request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String receiptNo = jsonObject.getJSONObject("Data").getString("ReceiptNO");
			List<ReceiptPojo> receiptPDF = receiptService.getReceiptPDFDetails(receiptNo);
			
        
        String sConfigFile = "props/Letters/receipt.properties";
        InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
		System.out.println("Blank File");
		}
		Properties prop = new Properties();
		prop.load(in);
        
		String title = prop.getProperty("header");
		String lefttable1 = prop.getProperty("left1");
		String lefttable2 = prop.getProperty("left2");
		String lefttable3 = prop.getProperty("left3");
		String lefttable4 = prop.getProperty("left4");
		String lefttable5 = prop.getProperty("left5");
		String lefttable6 = prop.getProperty("left6");
		String lefttable7 = prop.getProperty("left7");
		String lefttable8 = prop.getProperty("left8");
		String lefttable9 = prop.getProperty("left9");
		String lefttable10 = prop.getProperty("left10");
		
		
		List<String> list = new ArrayList<String>();
		  list.add(title);
		  list.add(lefttable1);
		  list.add(lefttable2);
		  list.add(lefttable3);
		  list.add(lefttable4);
		  list.add(lefttable5);
		  list.add(lefttable6);
		  list.add(lefttable7);
		  list.add(lefttable8);
		  list.add(lefttable9);
		  list.add(lefttable10);
		  		  
		  PDFReceipt receipt = new PDFReceipt(list,receiptPDF);
		  byte[] pdfr =  receipt.exportReceipt(res);
		  System.out.println(pdfr.length);
		  String base64 = Base64.getEncoder().encodeToString(pdfr);
	      String result = new String(base64); 
	      System.out.println("Conversion "+result);
	      org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	      response.put("Data", result);	  
		 return new ResponseEntity<Object>(response,HttpStatus.OK);
	}
}
