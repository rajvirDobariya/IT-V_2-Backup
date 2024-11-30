package com.suryoday.customerOnboard.service;

import org.json.JSONObject;

public interface PanValidationService {

	JSONObject panCardValidation(JSONObject jsonRequest, JSONObject header);

}
