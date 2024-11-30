package com.suryoday.LoanTracking.Controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.LoanTracking.Service.LoanTrackingPanCardConsumeService;

@Component
@RestController
@RequestMapping(value = "/loanTracking")
public class LoanTrackingPanCardConsumeController {
	@Autowired
	LoanTrackingPanCardConsumeService panCardConsumingService;
	private static Logger logger = LoggerFactory.getLogger(LoanTrackingPanCardConsumeController.class);

	@RequestMapping(value = "/panCardValidate", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> panCardValidation(
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "panCardNo", required = true) String panCardNo,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("GET Request" + panCardNo);

		if (X_Request_ID.equals("IEXCEED")) {

			JSONObject panCardValidate = panCardConsumingService.panCardValidation(panCardNo, Header);
			logger.debug("Response from the API" + panCardValidate);
			String panStatus = null;
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (panCardValidate != null) {
				String Data2 = panCardValidate.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				// JSONArray jsonArray = Data1.getJSONArray("PANDetails");

				logger.debug("JSON Object from Response" + Data2);

				if (Data1.has("Data")) {
					JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("PANDetails");
					for (int n = 0; n < jsonArray.length(); n++) {
						JSONObject jsonArray1 = jsonArray.getJSONObject(n);
						panStatus = jsonArray1.getString("Pan-Status");
						logger.debug(panStatus);
					}

					if (panStatus.equals("Record (PAN) Not Found in ITD Database/Invalid PAN")) {
						h = HttpStatus.BAD_REQUEST;
						JSONObject error = new JSONObject();
						JSONObject error1 = new JSONObject();
						error1.put("Description", panStatus);
						error1.put("status", h);
						error.put("Error", error1);
						return new ResponseEntity<Object>(error.toString(), h);
					}
					h = HttpStatus.OK;

				}
//					 else if(Data1.has("Error"))
//					 {
//						 h= HttpStatus.BAD_REQUEST;
//						 
//					 }
				logger.debug("Main Response from API" + Data1.toString());
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
