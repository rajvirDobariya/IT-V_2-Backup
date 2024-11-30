package com.suryoday.roaocpv.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ErrorResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.BREService;

@Controller
@RequestMapping("/roaocpv")
public class BREController extends OncePerRequestFilter {
	
	@Autowired
	BREService bresService;
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	
	@Autowired
	UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(BREController.class);
	
	@RequestMapping(value = "/createlead", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> createLead(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		JSONObject createLead = new JSONObject();
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		if(fetchByApplicationId.getLeadId() != null) {
			
			JSONObject data = new JSONObject();
			JSONObject res = new JSONObject();
			data.put("is_success", true);
			data.put("data", res);
			res.put("loan_account_number", "D100026");
			res.put("lead_id",fetchByApplicationId.getLeadId());
			createLead.put("data", data.toString());
		}
		else {
			 createLead = bresService.createLead(applicationNo, Header);	
		}
//		JSONObject createLead = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();

//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("loan_account_number", "D100026");
//		res.put("lead_id", "b7ea25d0-f4e1-4ce6-b848-d68f626b9117");
//		createLead.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (createLead != null) {
			String Data2 = createLead.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;
				String leadID = Data1.getJSONObject("data").getString("lead_id");
				if(fetchByApplicationId.getLeadId() == null) {
					fetchByApplicationId.setLeadId(leadID);
					fetchByApplicationId.setIsLeadCreated("Yes");
					fetchByApplicationId.setIsBreRuring("No");
					fetchByApplicationId.setBreStatus("Initiated");
					applicationDetailsService.save(fetchByApplicationId);
				}
				
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@RequestMapping(value = "/fetchBre", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchBre(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		JSONObject fetchBre = bresService.fetchBre(applicationNo, Header,fetchByApplicationId.getLeadId());
//		JSONObject fetchBre = new JSONObject();
//		JSONObject data = new JSONObject();
//		JSONObject res = new JSONObject();

//		data.put("is_success", true);
//		data.put("data", res);
//		res.put("status", "Approved");
	
//		fetchBre.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
	
	@RequestMapping(value = "/webhookcallback", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> webhookcallback(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		logger.debug("webhookcallback start");
		logger.debug("request"+bm);
		
		if(X_From_ID.equals("CB") && X_Request_ID.equals("MB") && X_To_ID.equals("MB")
				&& X_Transaction_ID.equals("EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4") && authorization.equals("Basic d2ViaG9va2NhbGxiYWNrOm9uZWZpbg=")) {
//			User fetchById = userService.fetchById(X_User_ID);
//			if (fetchById == null) {
//			
//				throw new NoSuchElementException("You are Not authorized");
//			}
			
			JSONObject jsonObject = new JSONObject(bm);
			String Lead_ID=jsonObject.getString("lead_id");
			if(Lead_ID.equals("")) {
				throw new NoSuchElementException("Lead_Id cannot be Empty");
			}
			
			int respons = applicationDetailsService.fetchByLeadId(Lead_ID);
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			if(respons == 1) {
				logger.debug("message : Success");
				response.put("message", "Success");
			}
			else {
				response.put("message", "failed");
				logger.debug("message : failed");
			}
			return new ResponseEntity<Object>(response,HttpStatus.OK);
		}
		 else
			{ 
			 logger.debug("Invalid Request");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
				
			}
		
	}
	
	
	@RequestMapping(value = "/equifaxReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxReport(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject1 = new JSONObject(bm);
		String applicationNo=jsonObject1.getJSONObject("Data").getString("ApplicationNo");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		JSONObject	fetchBre=bresService.equifaxReport(applicationNo, Header, fetchByApplicationId.getLeadId());
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}


	@RequestMapping(value = "/equifaxreport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxreport(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject1 = new JSONObject(bm);
		String applicationNo=jsonObject1.getJSONObject("Data").getString("ApplicationNo");	
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		JSONObject	fetchBre=bresService.equifaxReport(applicationNo, Header, fetchByApplicationId.getLeadId());
		
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			System.out.println(Data1.toString());
			return new ResponseEntity<Object>(Data1.toString(), h);
		}
		else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
			}
	
	
	
	@RequestMapping(value = "/brerules", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> brerules(
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String applicationNo,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject brerules = bresService.brerules(applicationNo);
		String Data = brerules.getString("Data");
		JSONObject Data1 = new JSONObject(Data);
		org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
 		//response.put("status", HttpStatus.OK);
 		response.put("Data", Data1);
 		return new ResponseEntity<Object>(Data1.toString(), HttpStatus.OK);
 
	}
	
	@RequestMapping(value = "/checkstatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkstatus(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		long applicationNoInLong = Long.parseLong(applicationNo);
		ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationNo);
		if(applicationDetails.getCustomerId() != null){
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			response.put("Message", "YOU ARE EXITING CUSTOMER");
			logger.debug("final response"+response.size());
			return new ResponseEntity<Object>(response,HttpStatus.OK);
		}
		String breStatus="Approved";//INITIATED , APPROVED, REJECT
		if(applicationDetails.getLeadId() == null) {
			 JSONObject createLead = bresService.createLead(applicationNo, Header);
			 HttpStatus h = HttpStatus.OK;
				if (createLead != null) {
					String Data2 = createLead.getString("data");
					System.out.println("data2"+Data2);
					JSONObject Data1 = new JSONObject(Data2);

					System.out.println(Data1);

					if (Data1.has("data")) {
						h = HttpStatus.OK;
						String leadID = Data1.getJSONObject("data").getString("lead_id");
						if(applicationDetails.getLeadId() == null) {
							applicationDetails.setLeadId(leadID);
							applicationDetails.setIsLeadCreated("YES");
							applicationDetails.setIsBreRuring("NO");
							applicationDetails.setBreStatus("INITIATED");
							applicationDetailsService.save(applicationDetails);
						}
					} 
					else if (Data1.has("error")) {
						String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
						applicationDetails.setLeadId(leadId);
						applicationDetails.setIsLeadCreated("YES");
						applicationDetails.setIsBreRuring("NO");
						applicationDetails.setBreStatus("INITIATED");
						applicationDetailsService.save(applicationDetails);
						

					}
				}
				else {

						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
		
		}
		else if(applicationDetails.getIsBreRuring().equalsIgnoreCase("YES") && applicationDetails.getBreResponse()==null)
		{
			JSONObject fetchBre = bresService.fetchBre(applicationNo, Header,applicationDetails.getLeadId());
			HttpStatus h = HttpStatus.OK;
			if (fetchBre != null) {
				String Data2 = fetchBre.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String status = Data1.getJSONObject("data").getString("status");
					JSONObject breResp = Data1.getJSONObject("data").getJSONObject("bre_response");
					if(status.equalsIgnoreCase("Approved"))
					{
						applicationDetails.setBreResponse(breResp.toString());
						 Object eligibilityAmount = breResp.getJSONObject("LOAN_ELIGIBILITY_AMOUNT_DERIVED").getJSONObject("derived_values").get("LOAN_ELIGIBILITY_AMOUNT");
						applicationDetails.setMaxEmiEligibility(eligibilityAmount.toString());
						applicationDetails.setBreStatus(status);
						applicationDetails.setFlowStatus("DD");
						applicationDetails.setBreResponse(breResp.toString());
						applicationDetailsService.save(applicationDetails);
					}
					else if(status.equalsIgnoreCase("Rejected"))
					{
						applicationDetails.setBreStatus(status);
						applicationDetails.setBreResponse(breResp.toString());
						applicationDetails.setStatus(status.toUpperCase());
						applicationDetailsService.save(applicationDetails);
					}

				} else if (Data1.has("error")) {
					h = HttpStatus.BAD_REQUEST;
					String erromsg=Data1.getJSONObject("error").getString("message").toString();
					
					
					JSONObject errr = new JSONObject();
					errr.put("Description",  erromsg);
					errr.put("Code",  h);

					JSONObject j = new JSONObject();
					j.put("Error", errr);
					
					
					
					return new ResponseEntity<Object>(j, h);
				}
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("LeadCreated", applicationDetails.getIsLeadCreated());
		response.put("isBreRuring", applicationDetails.getIsBreRuring());
		response.put("BreStatus", applicationDetails.getBreStatus());
		
		logger.debug("final response"+response.size());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
		
	}

	@Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException { 
        if (request.getMethod().equals("OPTIONS")) {
 //         response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
 //           throw new NoSuchElementException("You are Not authorized");
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
        	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Strict-Transport-Security", "max-age=16070400");
            httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
            httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
            httpServletResponse.setHeader("Content-Security-Policy", "default-src");
            httpServletResponse.setHeader("X-XSS-Protection", "0");
            filterChain.doFilter(request, response); 
        } 
    }
	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
        return serialized.getBytes();
    }
	
	
}

