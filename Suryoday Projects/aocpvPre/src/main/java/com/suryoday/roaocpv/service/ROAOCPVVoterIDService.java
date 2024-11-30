package com.suryoday.roaocpv.service;

import org.json.JSONObject;

public interface ROAOCPVVoterIDService {

	public JSONObject voterID(JSONObject jSONObject, JSONObject header);

	public JSONObject getHardCodeValue();

}
