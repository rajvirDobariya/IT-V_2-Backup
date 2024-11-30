package com.suryoday.mhl.service;

import org.json.JSONObject;

public interface MHLVoterIDService {

	public JSONObject voterID(JSONObject jSONObject, JSONObject header);

	public JSONObject getHardCodeValue();

}
