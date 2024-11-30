package com.suryoday.connector.service;

import org.json.JSONObject;

public interface VoterIDService {

	public JSONObject voterID(JSONObject jSONObject, JSONObject header);

	public JSONObject getHardCodeValue();

}
