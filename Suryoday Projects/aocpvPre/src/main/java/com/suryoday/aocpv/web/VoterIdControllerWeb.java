package com.suryoday.aocpv.web;

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

import com.suryoday.connector.service.VoterIDService;

@RestController
@RequestMapping("/aocpv/web")
public class VoterIdControllerWeb {
	
	@Autowired
	VoterIDService voterIDService;
	
	private static Logger logger = LoggerFactory.getLogger(VoterIdControllerWeb.class);

	@RequestMapping(value = { "/voterId" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public ResponseEntity<Object> voterId(@RequestBody String bm,
			@RequestHeader(name = "X-karza-key", required = true) String X_KARZA_KEY) throws Exception {
		JSONObject Header = new JSONObject();
		Header.put("X-karza-key", X_KARZA_KEY);
		logger.debug("POST Request : " + bm);

				JSONObject jsonObject = new JSONObject(bm);
				 String voterId = jsonObject.getString("epic_no");
				
				JSONObject voterID = voterIDService.voterID(jsonObject, Header);
				logger.debug("response from voterId API : " + voterID);

		//System.out.println(jsonObject.toString());
		
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (voterID != null) {
			String Data2 = voterID.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);
			logger.debug("JSON Object : " + Data2);
			if (Data1.has("status-code"))
				if (Data1.getString("status-code").equals("103")) {
					h = HttpStatus.BAD_REQUEST;
				} else {
					h = HttpStatus.OK;
				}
			logger.debug("Main Response : " + Data1.toString());
			return new ResponseEntity(Data1.toString(), h);
		}
		logger.debug("GATEWAY_TIMEOUT");
		return new ResponseEntity("timeout", HttpStatus.GATEWAY_TIMEOUT);
	}
}
