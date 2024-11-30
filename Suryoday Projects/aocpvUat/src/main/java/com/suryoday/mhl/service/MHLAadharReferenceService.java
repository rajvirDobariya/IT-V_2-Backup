package com.suryoday.mhl.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface MHLAadharReferenceService {

	public JSONObject createAadharReference(JSONObject jSONObject, JSONObject header);

	public JSONObject fetchAadharByReferenceNo(String referenceNo);

}
