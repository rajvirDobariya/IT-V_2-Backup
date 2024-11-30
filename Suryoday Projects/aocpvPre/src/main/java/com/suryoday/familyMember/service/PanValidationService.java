package com.suryoday.familyMember.service;

import org.json.JSONObject;

public interface PanValidationService {

	JSONObject panCardValidation(String panCardNo, JSONObject header);

}
