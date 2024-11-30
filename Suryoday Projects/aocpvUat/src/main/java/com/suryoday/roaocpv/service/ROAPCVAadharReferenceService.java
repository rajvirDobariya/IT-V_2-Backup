package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface ROAPCVAadharReferenceService {

	public JSONObject createAadharReference(JSONObject jSONObject, JSONObject header);

	public JSONObject fetchAadharByReferenceNo(String referenceNo, JSONObject header);

}
