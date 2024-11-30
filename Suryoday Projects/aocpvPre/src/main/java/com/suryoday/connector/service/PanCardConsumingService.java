package com.suryoday.connector.service;

import org.json.JSONObject;


public interface PanCardConsumingService {
	
	public JSONObject panCardValidation(JSONObject panRequest, JSONObject header);

}
