package com.suryoday.CustomerIntraction.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.CustomerIntraction.Service.CustomerIntractionService;

@RestController
@RequestMapping("/CustomerIntraction")
@Validated 
public class CustomerIntractionController{
	//public class CustomerIntractionController extends OncePerRequestFilter{	
	Logger logger = LoggerFactory.getLogger(CustomerIntractionController.class);
	//private static final String HEADER_VALUE = "max-age=31536000";
	
	@Autowired
	CustomerIntractionService customerIntractionService;
	
	@PostMapping("/getMemberName")
	public ResponseEntity<Object> getNameById(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true)@NotBlank String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true)@NotBlank String X_Session_ID,
			@RequestHeader(name = "channel", required = true)@NotBlank String channel,
			@RequestHeader(name = "X-encode-ID", required = true)@NotBlank String X_encode_ID, HttpServletRequest request)
			throws Exception {
		
		logger.debug("CustomerIntractionController_CONTROLLER :: fetchName :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson =  customerIntractionService.fetchCustomerName(requestString, channel, X_Session_ID,
				X_encode_ID, request);
		
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Object> save(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true)@NotBlank String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true)@NotBlank String X_Session_ID,
			@RequestHeader(name = "channel", required = true)@NotBlank String channel,
			@RequestHeader(name = "X-encode-ID", required = true)@NotBlank String X_encode_ID, HttpServletRequest request) throws Exception{
		
		//JSONObject requestJson = new JSONObject(requestString);
		
		logger.debug("CustomerIntractionController_CONTROLLER :: Save Meeting :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		
		JSONObject responseJson =  customerIntractionService.saveMeetingDetails(requestString, channel, X_Session_ID,
				X_encode_ID, request);
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
	@PostMapping("/getById")
	public ResponseEntity<Object> getByMeetingNumber(@RequestBody String requestString,
			@RequestHeader(name = "X-User-ID", required = true)@NotBlank String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true)@NotBlank String X_Session_ID,
			@RequestHeader(name = "channel", required = true)@NotBlank String channel,
			@RequestHeader(name = "X-encode-ID", required = true)@NotBlank String X_encode_ID, HttpServletRequest request)
			throws Exception {
		
		logger.debug("CustomerIntractionController_CONTROLLER :: get by meeting Number :: requestString : {} : X_User_ID : {} : X_Session_ID : {} : channel : {}",
				requestString, X_User_ID, X_Session_ID, channel);
		JSONObject responseJson =  customerIntractionService.getById(requestString, channel, X_Session_ID,
				X_encode_ID, request);
		
		return new ResponseEntity<Object>(responseJson.toString(), HttpStatus.OK);
	}
	
//	@Override 
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
//                    throws ServletException, IOException { 
//        if (request.getMethod().equals("OPTIONS")) {
// //         response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
// //           throw new NoSuchElementException("You are Not authorized");
//        	org.json.JSONObject data2 = new org.json.JSONObject();
//            data2.put("value", "unatharised Access");
//            org.json.JSONObject data3 = new org.json.JSONObject();
//            data3.put("Error", data2);
//            
//            ErrorResponse errorResponse = new ErrorResponse();
//            errorResponse.setCode(401);
//            errorResponse.setMessage("Unauthorized Access");
//   
//            byte[] responseToSend = restResponseBytes(errorResponse);
//            ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
//            ((HttpServletResponse) response).setStatus(401);
//            response.getOutputStream().write(responseToSend);
//            return;
//        } else { 
//        	 HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//             httpServletResponse.setHeader("Strict-Transport-Security",  HEADER_VALUE);
//             httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
//             httpServletResponse.setHeader("X-Content-Type-Options", "no sniff");
//             httpServletResponse.setHeader("Content-Security-Policy", "default-src");
//             httpServletResponse.setHeader("X-XSS-Protection", "0");
//           
//            filterChain.doFilter(request, response); 
//        } 
//    }
//	private byte[] restResponseBytes(ErrorResponse eErrorResponse) throws IOException {
//        String serialized = new ObjectMapper().writeValueAsString(eErrorResponse);
//        return serialized.getBytes();
//    }

}
