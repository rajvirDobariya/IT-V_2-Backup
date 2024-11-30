package com.suryoday.familyMember.service;

import org.json.JSONObject;

public interface AadharValidationService {

	JSONObject createAadharReference(JSONObject jsonObject, JSONObject header);

	JSONObject fetchAadharByReferenceNo(String referenceNo, JSONObject header);

}
