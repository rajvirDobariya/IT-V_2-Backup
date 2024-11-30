package com.suryoday.mhl.service;

import org.json.JSONObject;

public interface MHLPanCardConsumingService {

	public JSONObject panCardValidation(String panCardNo, JSONObject header);

}
