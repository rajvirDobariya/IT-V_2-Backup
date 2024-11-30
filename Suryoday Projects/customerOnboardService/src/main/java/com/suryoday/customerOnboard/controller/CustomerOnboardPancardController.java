package com.suryoday.customerOnboard.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.suryoday.customerOnboard.service.CustomerOnboardDetailsService;
import com.suryoday.customerOnboard.service.PanValidationService;



@RestController
public class CustomerOnboardPancardController {

	@Autowired
	private PanValidationService panCardConsumingService;
	
	@Autowired
	CustomerOnboardDetailsService onboardDetailsService;
	
	private static Logger logger = LoggerFactory.getLogger(CustomerOnboardPancardController.class);
	
	@RequestMapping(value = "/panCardValidate", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> panCardValidation(@RequestBody String jsonReq,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

			if (X_Request_ID.equals("IEXCEED")) {
				JSONObject jsonObject = new JSONObject(jsonReq);
				JSONObject jsonRequest = jsonObject.getJSONArray("Data").getJSONObject(0);
				String panCardNo = jsonRequest.getString("Pan");
				JSONObject panCardValidate = panCardConsumingService.panCardValidation(jsonObject, Header);			
				HttpStatus h = HttpStatus.OK;
				if (panCardValidate != null) {
					
					String Data2 = panCardValidate.getString("data");
					JSONObject Data1 = new JSONObject(Data2);
					
					if (Data1.has("Data")) {
						Boolean isVerify=false;
						JSONObject responseResponse = Data1.getJSONArray("Data").getJSONObject(0).getJSONArray("OutputData").getJSONObject(0);
						 if(responseResponse.getString("PanStatus").equals("E")) {
							 isVerify=true;
						 }
							
							responseResponse.put("NameOnCard", jsonRequest.getString("Name"));
							responseResponse.put("dobOnCard", jsonRequest.getString("DateOfBirth"));
							responseResponse.put("isVerify", isVerify);
							onboardDetailsService.saveResponse("panCard", panCardNo, applicationNo,responseResponse.toString());
							
						
					}
					logger.debug("Main Response from API : " + Data1.toString());
					
					return new ResponseEntity<Object>(Data1.toString(), h);

				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("INVALID REQUEST");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

			}
		
	}
}
