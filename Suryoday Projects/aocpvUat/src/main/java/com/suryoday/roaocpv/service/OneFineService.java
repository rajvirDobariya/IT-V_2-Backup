package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface OneFineService {

//	JSONObject checkOneFineStatus(JSONObject jsonObject, JSONObject header);

	JSONObject checkOneFineStatus(String leadId, JSONObject header);

}
