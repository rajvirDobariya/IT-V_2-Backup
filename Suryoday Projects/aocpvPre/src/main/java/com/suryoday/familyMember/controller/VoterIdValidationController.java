package com.suryoday.familyMember.controller;

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

import com.suryoday.familyMember.service.VoterIdValidationService;

@RestController
@RequestMapping("/familyMember")
public class VoterIdValidationController {

	@Autowired
	VoterIdValidationService voterIDService;
	
	private static Logger logger = LoggerFactory.getLogger(VoterIdValidationController.class);
	
	
	@RequestMapping(value = "/voterId", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> voterId(@RequestBody String bm,
			@RequestHeader(name = "X-karza-key", required = true) String X_KARZA_KEY)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-karza-key", X_KARZA_KEY);
		
		logger.debug("POST Request : " + bm);

				JSONObject jsonObject = new JSONObject(bm);
				 String voterId = jsonObject.getString("epic_no");
				//	incomeDetailsService.fetchByVoterId(voterId);
				JSONObject voterID = voterIDService.voterID(jsonObject, Header);
				logger.debug("response from voterId API : " + voterID);

				HttpStatus h = HttpStatus.OK;
				if (voterID != null) {
					String Data2 = voterID.getString("data");
					System.out.println("data2");
					JSONObject Data1 = new JSONObject(Data2);
					logger.debug("JSON Object : " + Data2);

					if (Data1.has("status")) {
						h = HttpStatus.OK;

					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					logger.debug("Main Response : " + Data1.toString());
					return new ResponseEntity<Object>(Data1.toString(), h);
				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
//				JSONObject hardCodeValue = voterIDService.getHardCodeValue();
				
//				logger.debug("HardCoded Response:" + hardCodeValue.toString());
//				return new ResponseEntity<Object>(hardCodeValue.toString(), HttpStatus.OK);

	}
}
