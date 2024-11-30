package com.suryoday.roaocpv.service;

import org.json.JSONObject;


public interface ROAOCPVPanCardConsumingService {
	
	public JSONObject panCardValidation(String panCardNo, JSONObject header);

}
